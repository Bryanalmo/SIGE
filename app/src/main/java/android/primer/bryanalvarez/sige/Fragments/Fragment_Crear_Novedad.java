package android.primer.bryanalvarez.sige.Fragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.primer.bryanalvarez.sige.Models.Object;
import android.primer.bryanalvarez.sige.Util.Util;
import android.provider.MediaStore;
import android.provider.Settings;

import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.primer.bryanalvarez.sige.R;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Crear_Novedad extends Fragment implements  View.OnClickListener {

    private TextView tv_nombre_archivo;

    private Spinner spinnerSerial;
    private Spinner spinnerTipoNovedad;
    private Spinner spinnerEstadoNovedad;
    private Spinner spinnerEstadoMaquina;

    private EditText et_mostrar_fecha_novedad_picker;
    private EditText et_horas_trabajo;
    private EditText et_descripcion_corta;
    private EditText et_descripcion_inicial;

    private ImageButton ib_obtener_fecha_novedad;
    private ImageButton ib_obtener_archivo_novedad;
    private ImageButton ib_buscar_serial;

    private ProgressBar progressBar;

    private ImageView iv_fotoNovedad;

    private Button buttonCrearNovedad;

    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);

    private static final String CERO = "0";
    private static final String GUION = "-";
    private static final String DOS_PUNTOS = ":";
    private final String CARPETA_PRINCIPAL = "misImagenesApp/";
    private final String RUTA_IMAGEN = CARPETA_PRINCIPAL+"SIGE";
    private String path;
    private final int VALOR_RETORNO = 21;
    private final int COD_CAMARA = 1;
    private File fileImagen;
    private Bitmap bitmap;

    private AlertDialog alertDialog;
    private int count;

    String mCurrentPhotoPath;
    RequestQueue request;
    StringRequest stringRequest;

    public Fragment_Crear_Novedad() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment__crear__novedad, container, false);

        if (isOnline(getContext())){
            alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setMessage("Cargando datos...");
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }

        bindUI(view);

        ib_obtener_archivo_novedad.setOnClickListener(this);
        ib_obtener_fecha_novedad.setOnClickListener(this);
        buttonCrearNovedad.setOnClickListener(this);
        ib_buscar_serial.setOnClickListener(this);

        request = Volley.newRequestQueue(getContext());

        AsyncTaskTraerDatosNovedades at = new AsyncTaskTraerDatosNovedades();
        at.execute();
        // Inflate the layout for this fragment
        return view;
    }

    private void crear_novedad_WebService(){

        String url = "https://sige.golfyturf.com/AppWebServices/crearNovedad.php";
        url = url.replace(" ", "%20");

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                //Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                try {
                    jsonObject = new JSONObject(response);
                    if(jsonObject.getString("Success").equals("true")){
                        alertDialog.dismiss();
                        Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_LONG).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Fragment_Novedades())
                                .commit();
                    }else{
                        Toast.makeText(getContext(), "Error al registrar la maquina", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getContext(), "JSON error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Object serialSelect = (Object) spinnerSerial.getSelectedItem();
                Object tipoNovedadSelect = (Object) spinnerTipoNovedad.getSelectedItem();
                Object estadoNovedadSelect = (Object) spinnerEstadoNovedad.getSelectedItem();
                Object estadoMaquinaSelect = (Object) spinnerEstadoMaquina.getSelectedItem();


                String serial = serialSelect.getId();
                String tipo_novedad = tipoNovedadSelect.getId();
                String estado_novedad = estadoNovedadSelect.getId();
                String estado_maquina= estadoMaquinaSelect.getId();
                String fecha = et_mostrar_fecha_novedad_picker.getText().toString();
                String horas = et_horas_trabajo.getText().toString();
                String descripcion_corta = et_descripcion_corta.getText().toString();
                String descripcion_inicial = et_descripcion_inicial.getText().toString();
                String foto = convertirImagenString(bitmap);

                Map<String,String> parametros = new HashMap<>();
                parametros.put("Id_estado_novedad",estado_novedad);
                parametros.put("Fecha",fecha);
                parametros.put("Horas",horas);
                parametros.put("Descripcion_corta",descripcion_corta);
                parametros.put("Descripcion_inicial",descripcion_inicial);
                parametros.put("Id_tipo_novedad",tipo_novedad);
                parametros.put("Id_maquina",serial);
                parametros.put("Id_estado_maquina",estado_maquina);
                parametros.put("Foto_novedad",foto);
                parametros.put("Id_empleado", Util.getId_usuario());

                return parametros;
            }
        };
        request.add(stringRequest);
    }

    private void traer_datos_WebService(final String entidad, final String campo, final String orden){

        String url = "https://sige.golfyturf.com/AppWebServices/getTableData.php";
        url = url.replace(" ", "%20");

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = null;
                Object object = null;
                ArrayList<Object> datos = new ArrayList<>();
                datos.add(new Object("0", "Ninguno"));
                try {
                    jsonArray = new JSONArray(response);
                    for (int i=0; i<jsonArray.length(); i++ ){
                        object = new Object(jsonArray.getJSONObject(i).getString("Id"), jsonArray.getJSONObject(i).getString(campo));
                        datos.add(object);
                    }
                    if(entidad.equals("Maquina")) {
                        spinnerSerial.setAdapter(new ArrayAdapter<Object>(getContext(), android.R.layout.simple_list_item_1, datos));
                    }else
                    if(entidad.equals("Tipo_Novedad")) {
                        spinnerTipoNovedad.setAdapter(new ArrayAdapter<Object>(getContext(), android.R.layout.simple_list_item_1, datos));
                    }else
                    if(entidad.equals("Estado_Novedad")) {
                        spinnerEstadoNovedad.setAdapter(new ArrayAdapter<Object>(getContext(), android.R.layout.simple_list_item_1, datos));
                    }else
                    if(entidad.equals("Estado_Maquina")) {
                        spinnerEstadoMaquina.setAdapter(new ArrayAdapter<Object>(getContext(), android.R.layout.simple_list_item_1, datos));
                    }
                    count++;
                    if (count==4){
                        alertDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> parametros = new HashMap<>();
                parametros.put("table", entidad);
                parametros.put("orden", orden);
                return parametros;
            }
        };
        request.add(stringRequest);
    }

    private String convertirImagenString(Bitmap bitmap) {
        String imagenString = "";
        if (bitmap!=null){
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
            byte[] imageByte = array.toByteArray();
            imagenString = Base64.encodeToString(imageByte,Base64.DEFAULT);
        }else{
            imagenString = "";
        }


        return imagenString;
    }

    public void bindUI(View view){
        spinnerSerial = (Spinner) view.findViewById(R.id.spinnerSerial);
        spinnerTipoNovedad = (Spinner) view.findViewById(R.id.spinnerTipoNovedad);
        spinnerEstadoNovedad = (Spinner) view.findViewById(R.id.spinnerEstadoNovedad);
        spinnerEstadoMaquina = (Spinner) view.findViewById(R.id.spinnerEstadoMaquina);

        et_mostrar_fecha_novedad_picker = (EditText) view.findViewById(R.id.et_mostrar_fecha_novedad_picker);
        et_horas_trabajo = (EditText) view.findViewById(R.id.et_horas_trabajo);
        et_descripcion_corta = (EditText) view.findViewById(R.id.et_descripcion_corta);
        et_descripcion_inicial = (EditText) view.findViewById(R.id.et_descripcion_inicial);

        ib_obtener_fecha_novedad = (ImageButton) view.findViewById(R.id.ib_obtener_fecha_novedad);
        ib_obtener_archivo_novedad = (ImageButton) view.findViewById(R.id.ib_obtener_archivo_novedad);
        ib_buscar_serial= (ImageButton) view.findViewById(R.id.ib_buscar_serial );

        tv_nombre_archivo = (TextView) view.findViewById(R.id.tv_nombre_archivo);

        buttonCrearNovedad = (Button) view.findViewById(R.id.buttonCrearNovedad);
        iv_fotoNovedad= (ImageView) view.findViewById(R.id.iv_foto_novedad);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBarCrearNovedad);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case VALOR_RETORNO:
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                        iv_fotoNovedad.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    tv_nombre_archivo.setText(data.getData().getPath().toString());

                    break;
                case COD_CAMARA:
                        // Show the thumbnail on ImageView
                        Uri imageUri = Uri.parse(path);
                        File file = new File(imageUri.getPath());
                        bitmap = BitmapFactory.decodeFile(path);
                        iv_fotoNovedad.setImageBitmap(bitmap);
                        tv_nombre_archivo.setText("Foto capturada");
                        // ScanFile so it will be appeared on Gallery
                        MediaScannerConnection.scanFile(getContext(),
                                new String[]{imageUri.getPath()}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    public void onScanCompleted(String path, Uri uri) {
                                    }
                                });
                    break;
            }
            bitmap = redimensionarImagen(bitmap, 600,800);
        }

    }

    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {
        int ancho = bitmap.getWidth();
        int alto = bitmap.getHeight();

        if(ancho > anchoNuevo || alto > altoNuevo){
            float escalaAncho = anchoNuevo/ancho;
            float escalaAlto = altoNuevo/alto;
            Matrix matrix = new Matrix();
            matrix.postScale(escalaAncho, escalaAlto);

            return Bitmap.createBitmap(bitmap, 0,0, ancho,alto,matrix,false);
        }else{

            return bitmap;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_obtener_fecha_novedad:
                obtenerFecha();
                break;
            case R.id.ib_obtener_archivo_novedad:
                cargarImagen();
                break;
            case R.id.buttonCrearNovedad:
                if(confirmarForm()){
                    alertDialog.setMessage("Enviando datos...");
                    alertDialog.setCancelable(false);
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();

                    crear_novedad_WebService();
                }else{
                    Toast.makeText(getContext(), "Los campos descripcion y descripcion inicial son necesarios", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.ib_buscar_serial:
                buscar("Buscar serial de maquina");
                break;

        }
    }

    private void buscar(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        if (title != null) builder.setTitle(title);

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_buscar, null);
        builder.setView(viewInflated);

        final EditText et_buscar_filtro = (EditText) viewInflated.findViewById(R.id.et_buscar_filtro);

        builder.setPositiveButton("Filtrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String buscar = et_buscar_filtro.getText().toString();
                traer_datos_WebService("Maquina", "Serial", " WHERE Id <> 0 AND Serial like '"+buscar+"%' OR Serial like '%"+buscar+"%' ORDER BY Serial ASC");
                spinnerSerial.performClick();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void cargarImagen() {
        final CharSequence[] opciones = {"Tomar foto", "Cargar imagen", "Cancelar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(getContext());
        alertOpciones.setTitle("Selecciones una opcion");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(opciones[i].equals("Tomar foto")){
                    abrirCamara();
                }else if(opciones[i].equals("Cargar imagen")){
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    startActivityForResult(Intent.createChooser(intent, "Choose File"), VALOR_RETORNO);
                }else{
                    dialog.dismiss();
                }
            }
        });

        alertOpciones.show();
    }

    private void abrirCamara() {
        File myFile = new File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN);
        boolean isCreated = myFile.exists();
        String nombre = "";
        if(isCreated==false){
            isCreated = myFile.mkdirs();
        }
        if(isCreated == true){
            Long consecutivo = System.currentTimeMillis()/1000;
            nombre = consecutivo.toString()+".jpg";
        }
        path = Environment.getExternalStorageDirectory() + File.separator + RUTA_IMAGEN
                + File.separator + nombre;
        File imagen = new File(path);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoURI = FileProvider.getUriForFile(getContext(),
                        android.primer.bryanalvarez.sige.BuildConfig.APPLICATION_ID + ".provider",
                        imagen);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(intent, COD_CAMARA);
    }

    private void obtenerFecha() {
        DatePickerDialog recogerFecha = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                et_mostrar_fecha_novedad_picker.setText(year + GUION + mesFormateado + GUION+ diaFormateado);

            }
        },anio, mes, dia);
        recogerFecha.show();
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    private class AsyncTaskTraerDatosNovedades extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setMax(100);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if(isOnline(getContext())){

                traer_datos_WebService("Maquina", "Serial", "WHERE Id <> 0 ORDER BY Serial ASC");
                traer_datos_WebService("Tipo_Novedad", "Tipo_novedad", "WHERE Id <> 0 ORDER BY Tipo_novedad ASC");
                traer_datos_WebService("Estado_Novedad", "Estado_novedad", "WHERE Id <> 0 ORDER BY Estado_novedad ASC");
                traer_datos_WebService("Estado_Maquina", "Estado", "WHERE Id <> 0 ORDER BY Estado ASC");
                return true;
            }else{
                for (int i=0; i<=10; i++){
                    publishProgress(i*10);
                    return false;
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            progressBar.setProgress(values[0].intValue());
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            if(aVoid == true){
                progressBar.setVisibility(View.GONE);
            }else{
                Toast.makeText(getContext(), "No tiene acceso a internet, verifique su conexión ", Toast.LENGTH_LONG).show();
            }

        }
    }

    private Boolean confirmarForm(){
        if(et_descripcion_corta.getText().toString().equals("") || et_descripcion_inicial.getText().toString().equals("")){
            return false;
        }else{
        return true;}
    }
}
