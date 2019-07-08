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
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.primer.bryanalvarez.sige.R;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Crear_Maquina extends Fragment implements View.OnClickListener{

    private TextView textViewArchivo;

    private Spinner spinnerDistribuidor;
    private Spinner spinnerModelo;
    private Spinner spinnerEstado;
    private Spinner spinnerCliente;
    private Spinner spinnerUbicacion;
    private Spinner spinnerTecnico;
    private Spinner spinnerYear;
    private Spinner spinnerRegistro;

    private EditText et_serial;
    private EditText et_propietario;
    private EditText et_color;
    private EditText et_placa_activo_club;
    private EditText et_maquina_num_club;
    private EditText et_horas;
    private EditText et_modelo_motor;
    private EditText et_serial_motor;
    private EditText et_codigo_motor;
    private EditText et_serial_unidades;
    private EditText et_operador_asignado;
    private EditText et_observaciones;
    private EditText et_mostrar_fecha_compra_picker;
    private EditText et_mostrar_fecha_entrega_picker;
    private EditText et_mostrar_fecha_registro_picker;

    private ImageButton ib_obtener_fecha_compra;
    private ImageButton ib_obtener_fecha_entrega;
    private ImageButton ib_obtener_fecha_registro;
    private ImageButton botonAbrirArchivos;
    private ImageButton ib_buscar_cliente;
    private ImageButton ib_buscar_modelo;

    private LinearLayout linearLayout23;
    private LinearLayout linearLayout24;

    private ProgressBar progressBar;

    private ImageView iv_fotoMaquina;

    private Button buttonCrearMaquina;

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
    private ArrayList<String> anios;

    private int count = 0;

    private AlertDialog alertDialog;

    RequestQueue request;
    StringRequest stringRequest;



    public Fragment_Crear_Maquina() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_fragment__crear__maquina, container, false);

        bindUI(view);

        botonAbrirArchivos.setOnClickListener(this);
        ib_obtener_fecha_compra.setOnClickListener(this);
        ib_obtener_fecha_entrega.setOnClickListener(this);
        ib_obtener_fecha_registro.setOnClickListener(this);
        buttonCrearMaquina.setOnClickListener(this);
        ib_buscar_cliente.setOnClickListener(this);
        ib_buscar_modelo.setOnClickListener(this);

        request = Volley.newRequestQueue(getContext());

        if (isOnline(getContext())){
            alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setMessage("Cargando datos...");
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }

        AsyncTaskTraerDatosMaquina at = new AsyncTaskTraerDatosMaquina();
        at.execute();

        anios = obtenerListaYear();
        spinnerYear.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, anios));

        return view;
    }

    private ArrayList<String> obtenerListaYear() {
        ArrayList<String> years = new ArrayList<>();
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        int formatteDate = Integer.parseInt(df.format(date));
        for (int i=formatteDate; 2000<=i; i--){
            years.add(i+"");
        }
        return years;
    }

   private void crear_maquina_WebService(){

        String url = "https://sige.golfyturf.com/AppWebServices/crearMaquina.php";
        url = url.replace(" ", "%20");

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if(jsonObject.getString("Success").equals("true")){
                        alertDialog.dismiss();
                        Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_LONG).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Fragment_Maquinaria())
                                .commit();
                    }else{
                        Toast.makeText(getContext(), "Error al registrar la maquina", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Object distrbuidorSelect = (Object) spinnerDistribuidor.getSelectedItem();
                Object modeloSelect = (Object) spinnerModelo.getSelectedItem();
                Object estadoSelect = (Object) spinnerEstado.getSelectedItem();
                Object clienteSelect = (Object) spinnerCliente.getSelectedItem();
                Object ubicacionSelect = (Object) spinnerUbicacion.getSelectedItem();
                Object tecnicoSelect = (Object) spinnerTecnico.getSelectedItem();
                Object registroSelect = (Object) spinnerRegistro.getSelectedItem();

                String distribuidor = distrbuidorSelect.getId();
                String serial = et_serial.getText().toString();
                String modelo = modeloSelect.getId();
                String placa_activo_fijo = et_placa_activo_club.getText().toString();
                String maquina_numero_club = et_maquina_num_club.getText().toString();
                String estado = estadoSelect.getId();
                String horas = et_horas.getText().toString();
                String cliente = clienteSelect.getId();
                String modelo_motor = et_modelo_motor.getText().toString();
                String serial_motor = et_serial_motor.getText().toString();
                String codigo_motor = et_codigo_motor.getText().toString();
                String serial_unidades = et_serial_unidades.getText().toString();
                String ubicacion = ubicacionSelect.getId();
                String operador_asignado = et_operador_asignado.getText().toString();
                String tecnico_asignado = tecnicoSelect.getId();
                String year_produccion = spinnerYear.getSelectedItem().toString();
                String observaciones = et_observaciones.getText().toString();
                String fecha_compra = et_mostrar_fecha_compra_picker.getText().toString();
                String fecha_entrega = et_mostrar_fecha_entrega_picker.getText().toString();
                String foto = convertirImagenString(bitmap);
                String registro_fabrica = registroSelect.getId();
                String fecha_registro = et_mostrar_fecha_registro_picker.getText().toString();
                String propietario = et_propietario.getText().toString();
                String color = et_color.getText().toString();

                Map<String,String> parametros = new HashMap<>();
                parametros.put("distribuidor",distribuidor);
                parametros.put("serial",serial);
                parametros.put("modelo",modelo);
                parametros.put("placa_activo_fijo_club",placa_activo_fijo);
                parametros.put("maquina_numero_club",maquina_numero_club);
                parametros.put("estado",estado);
                parametros.put("horas",horas);
                parametros.put("cliente",cliente);
                parametros.put("modelo_motor",modelo_motor);
                parametros.put("serial_motor",serial_motor);
                parametros.put("codigo_motor",codigo_motor);
                parametros.put("serial_unidades_corte",serial_unidades);
                parametros.put("ubicacion",ubicacion);
                parametros.put("operador_asignado",operador_asignado);
                parametros.put("tecnico_asignado",tecnico_asignado);
                parametros.put("year_produccion",year_produccion);
                parametros.put("observaciones",observaciones);
                parametros.put("fecha_compra",fecha_compra);
                parametros.put("fecha_entrega",fecha_entrega);
                parametros.put("estado_registro_fabrica",registro_fabrica);
                parametros.put("fecha_registro_fabrica",fecha_registro);
                parametros.put("foto_maquina",foto);
                parametros.put("propietario",propietario);
                parametros.put("color",color);

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
                        Object ob = object;
                        if (entidad.equals("Modelo_Maquina")){
                            object.setAdditional(jsonArray.getJSONObject(i).getString("Id_marca"));
                        }
                        datos.add(object);
                        datos.get(i);
                    }
                    if(entidad.equals("Cliente")) {
                        spinnerCliente.setAdapter(new ArrayAdapter<Object>(getContext(), android.R.layout.simple_list_item_1, datos));
                    }else
                    if(entidad.equals("Distribuidor_Maquina")) {
                        spinnerDistribuidor.setAdapter(new ArrayAdapter<Object>(getContext(), android.R.layout.simple_list_item_1, datos));
                    }else
                    if(entidad.equals("Ubicacion")) {
                        spinnerUbicacion.setAdapter(new ArrayAdapter<Object>(getContext(), android.R.layout.simple_list_item_1, datos));
                    }else
                    if(entidad.equals("Estado_Maquina")) {
                        spinnerEstado.setAdapter(new ArrayAdapter<Object>(getContext(), android.R.layout.simple_list_item_1, datos));
                    }else
                    if(entidad.equals("Empleado")) {
                        spinnerTecnico.setAdapter(new ArrayAdapter<Object>(getContext(), android.R.layout.simple_list_item_1, datos));
                    }else
                    if(entidad.equals("Estado_Registro_Fabrica")) {
                        spinnerRegistro.setAdapter(new ArrayAdapter<Object>(getContext(), android.R.layout.simple_list_item_1, datos));
                    }else
                    if(entidad.equals("Modelo_Maquina")) {
                        spinnerModelo.setAdapter(new ArrayAdapter<Object>(getContext(), android.R.layout.simple_list_item_1, datos));
                        spinnerModelo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Object modeloSelected = (Object) spinnerModelo.getSelectedItem();
                                String marca_modelo_selected = modeloSelected.getAdditional();
                                if (marca_modelo_selected != null){

                                    if (marca_modelo_selected.equals("65") || marca_modelo_selected.equals("90")){
                                        linearLayout23.setVisibility(View.VISIBLE);
                                        linearLayout24.setVisibility(View.VISIBLE);
                                    }else{
                                        linearLayout23.setVisibility(View.GONE);
                                        linearLayout24.setVisibility(View.GONE);
                                    }
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                    count++;
                    if (count==7){
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

        if (!textViewArchivo.getText().toString().equals("Nombre archivo")){
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
            byte[] imageByte = array.toByteArray();
            imagenString = Base64.encodeToString(imageByte,Base64.DEFAULT);
        }
        return imagenString;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case VALOR_RETORNO:
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                        iv_fotoMaquina.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Uri uri = data.getData();
                    String s = uri.toString();
                    textViewArchivo.setText(uri.getLastPathSegment());
                    break;
                case COD_CAMARA:
                    Uri imageUri = Uri.parse(path);
                    File file = new File(imageUri.getPath());
                        //InputStream ims = new FileInputStream(file);
                        bitmap = BitmapFactory.decodeFile(path);
                        iv_fotoMaquina.setImageBitmap(bitmap);
                        textViewArchivo.setText("Foto capturada");


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
            case R.id.ib_obtener_fecha_entrega:
                obtenerFecha("entrega");
                break;
            case R.id.ib_obtener_fecha_compra:
                obtenerFecha("compra");
                break;
            case R.id.ib_obtener_fecha_registro:
                obtenerFecha("registro");
                break;
            case R.id.b_obtener_foto_maquina:
                cargarImagen();
                break;
            case R.id.buttonCrearMaquina:
                if(confirmarForm()){
                    alertDialog.setMessage("Enviando datos...");
                    alertDialog.setCancelable(false);
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
                    alertDialog.show();
                    crear_maquina_WebService();
                }else{
                    Toast.makeText(getContext(), "El campo serial es necesario", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.ib_buscar_cliente:
                buscar("Buscar cliente", "Cliente");
                break;
            case R.id.ib_buscar_modelo:
                buscar("Buscar modelo", "Modelo");
                break;
        }

    }

    private void buscar(String title, final String nombreSpinner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        if (title != null) builder.setTitle(title);

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_buscar, null);
        builder.setView(viewInflated);

        final EditText et_buscar_filtro = (EditText) viewInflated.findViewById(R.id.et_buscar_filtro);

        builder.setPositiveButton("Filtrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String buscar = et_buscar_filtro.getText().toString();
                if (nombreSpinner.equals("Cliente")){
                    traer_datos_WebService("Cliente", "Nombre_completo", " WHERE Id <> 0 AND Nombre_completo like '"+buscar+"%' OR Nombre_completo like '%"+buscar+"%' ORDER BY Nombre_completo ASC");
                    spinnerCliente.performClick();
                }else if (nombreSpinner.equals("Modelo")){
                    traer_datos_WebService("Modelo_Maquina", "Modelo_maquina", "WHERE Id <> 0 AND Modelo_maquina like '"+buscar+"%' OR Modelo_maquina like '%"+buscar+"%' ORDER BY Modelo_maquina ASC");
                    spinnerModelo.performClick();
                }

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void cargarImagen() {
        final CharSequence[] opciones = {"Tomar foto", "Cargar imagen", "Cancelar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(getContext());
        alertOpciones.setTitle("Seleccione una opcion");
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

    private void obtenerFecha(final String id) {
        DatePickerDialog recogerFecha = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                if(id.equals("entrega")){
                    et_mostrar_fecha_entrega_picker.setText(year + GUION + mesFormateado + GUION+ diaFormateado);
                }else if(id.equals("compra")){
                    et_mostrar_fecha_compra_picker.setText(year + GUION + mesFormateado + GUION+ diaFormateado);
                }else if(id.equals("registro")){
                    et_mostrar_fecha_registro_picker.setText(year + GUION + mesFormateado + GUION+ diaFormateado);
                }
            }
        },anio, mes, dia);
        recogerFecha.show();
    }

    public void bindUI(View view){
        spinnerDistribuidor = (Spinner) view.findViewById(R.id.spinnerDistribuidor);
        spinnerModelo = (Spinner) view.findViewById(R.id.spinnerModelo);
        spinnerEstado = (Spinner) view.findViewById(R.id.spinnerEstado);
        spinnerCliente= (Spinner) view.findViewById(R.id.spinnerCliente);
        spinnerUbicacion= (Spinner) view.findViewById(R.id.spinnerUbicacion);
        spinnerTecnico= (Spinner) view.findViewById(R.id.spinnerTecnico);
        spinnerYear= (Spinner) view.findViewById(R.id.spinnerYear);
        spinnerRegistro= (Spinner) view.findViewById(R.id.spinnerRegistro);

        et_serial = (EditText) view.findViewById(R.id.et_serial);
        et_propietario = (EditText) view.findViewById(R.id.et_propietario);
        et_color = (EditText) view.findViewById(R.id.et_color);
        et_placa_activo_club = (EditText) view.findViewById(R.id.et_placa_activo_club);;
        et_maquina_num_club = (EditText) view.findViewById(R.id.et_maquina_num_club);;
        et_horas = (EditText) view.findViewById(R.id.et_horas);;
        et_modelo_motor = (EditText) view.findViewById(R.id.et_modelo_motor);;
        et_serial_motor = (EditText) view.findViewById(R.id.et_serial_motor);;
        et_codigo_motor = (EditText) view.findViewById(R.id.et_codigo_motor);;
        et_serial_unidades = (EditText) view.findViewById(R.id.et_serial_unidades);;
        et_operador_asignado = (EditText) view.findViewById(R.id.et_operador_asignado);;
        et_observaciones = (EditText) view.findViewById(R.id.et_observaciones);;
        et_mostrar_fecha_compra_picker = (EditText) view.findViewById(R.id.et_mostrar_fecha_compra_picker);;
        et_mostrar_fecha_entrega_picker = (EditText) view.findViewById(R.id.et_mostrar_fecha_entrega_picker);;
        et_mostrar_fecha_registro_picker = (EditText) view.findViewById(R.id.et_mostrar_fecha_registro_picker);;

        botonAbrirArchivos = (ImageButton) view.findViewById(R.id.b_obtener_foto_maquina);
        ib_obtener_fecha_compra = (ImageButton) view.findViewById(R.id.ib_obtener_fecha_compra);
        ib_obtener_fecha_entrega = (ImageButton) view.findViewById(R.id.ib_obtener_fecha_entrega);
        ib_obtener_fecha_registro = (ImageButton) view.findViewById(R.id.ib_obtener_fecha_registro);
        ib_buscar_cliente = (ImageButton) view.findViewById(R.id.ib_buscar_cliente);
        ib_buscar_modelo = (ImageButton) view.findViewById(R.id.ib_buscar_modelo);

        linearLayout23 = (LinearLayout) view.findViewById(R.id.linearLayout23);
        linearLayout24 = (LinearLayout) view.findViewById(R.id.linearLayout24);

        buttonCrearMaquina = (Button) view.findViewById(R.id.buttonCrearMaquina);

        textViewArchivo = (TextView) view.findViewById(R.id.nombre_archivo);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBarCrearMaquina);

        iv_fotoMaquina = (ImageView) view.findViewById(R.id.iv_foto_maquina);

    }

    private class AsyncTaskTraerDatosMaquina extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setMax(100);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if(isOnline(getContext())){

                traer_datos_WebService("Cliente", "Nombre_completo", "WHERE Id <> 0 ORDER BY Nombre_completo ASC");
                traer_datos_WebService("Distribuidor_Maquina", "Distribuidor_maquina", "WHERE Id <> 0 ORDER BY Distribuidor_maquina ASC");
                traer_datos_WebService("Ubicacion","Ubicacion", "WHERE Id <> 0 ORDER BY Ubicacion ASC" );
                traer_datos_WebService("Estado_Maquina", "Estado", "WHERE Id <> 0 ORDER BY Estado ASC");
                traer_datos_WebService("Empleado", "Nombres", "WHERE Id = 29 OR Id = 30 OR Id = 22 ORDER BY Nombres ASC ");
                traer_datos_WebService("Estado_Registro_Fabrica","Estado_registro_fabrica", "WHERE Id <> 0 ORDER BY Estado_registro_fabrica ASC" );
                traer_datos_WebService("Modelo_Maquina", "Modelo_maquina", "WHERE Id <> 0 ORDER BY Modelo_maquina ASC");
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

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    private Boolean confirmarForm(){
        if(    et_serial.getText().toString().equals("")){
            return false;
        }else{
            return true;}
    }



}
