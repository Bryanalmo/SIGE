package android.primer.bryanalvarez.sige.Fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.primer.bryanalvarez.sige.Models.Object;
import android.primer.bryanalvarez.sige.Util.Util;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Crear_Servicio_Tecnico extends Fragment implements  View.OnClickListener{

    private TextView textViewTipo;
    private TextView textViewClienteVisitar;
    private TextView textViewNovedad;
    private TextView textViewTipoFalla;
    private TextView textViewHorometro;
    private TextView textViewTipoServicioRelacionado;
    private TextView textViewServicioRelacionado;

    private Spinner spinnerTipoServicio;
    private Spinner spinnerClienteVisitar;
    private Spinner spinnerNovedad;
    private Spinner spinnerTipoFalla;
    private Spinner spinnerTecnico_st;
    private Spinner spinnerTipoServicioRelacionado;
    private Spinner spinnerServicioRelacionado;

    private EditText et_horometro;
    private EditText et_operador_asignado_st;
    private EditText et_desc_corta_st;
    private EditText et_desc_st;
    private EditText et_mostrar_fecha_programacion_picker;
    private EditText et_precio;

    private Button buttonServicioTecnico;

    private ImageButton ib_obtener_fecha_programacion;

    private ProgressBar progressBar;

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

    private int VALOR_RETORNO = 1;
    private AlertDialog alertDialog;
    private int count;

    RequestQueue request;
    StringRequest stringRequest;


    public Fragment_Crear_Servicio_Tecnico() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment__crear__servicio__tecnico, container, false);

        if (isOnline(getContext())){
            alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setMessage("Cargando datos...");
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }

        bindUI(view);

        ArrayList<Object> datosTipoRelacionado = new ArrayList<>();
        datosTipoRelacionado.add(new Object("0","Ninguno"));
        datosTipoRelacionado.add(new Object("6","Overhaul"));
        datosTipoRelacionado.add(new Object("7","Visita periodica"));
        spinnerTipoServicioRelacionado.setAdapter(new ArrayAdapter<Object>(getContext(), android.R.layout.simple_list_item_1, datosTipoRelacionado));

        buttonServicioTecnico.setOnClickListener(this);
        ib_obtener_fecha_programacion.setOnClickListener(this);

        request = Volley.newRequestQueue(getContext());

        AsyncTaskTraerDatosST at = new AsyncTaskTraerDatosST();
        at.execute();

        spinnerTipoServicio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object servicioSelected = (Object) spinnerTipoServicio.getSelectedItem();
                String st_selected = servicioSelected.getId();
                if(st_selected.equals("7") || st_selected.equals("8")){
                    textViewClienteVisitar.setVisibility(View.VISIBLE);
                    spinnerClienteVisitar.setVisibility(View.VISIBLE);
                    spinnerNovedad.setVisibility(View.GONE);
                    spinnerTipoFalla.setVisibility(View.GONE);
                    textViewNovedad.setVisibility(View.GONE);
                    spinnerNovedad.setVisibility(View.GONE);
                    textViewTipoFalla.setVisibility(View.GONE);
                    spinnerTipoFalla.setVisibility(View.GONE);
                    et_horometro.setVisibility(View.GONE);
                    textViewHorometro.setVisibility(View.GONE);
                    textViewTipoServicioRelacionado.setVisibility(View.GONE);
                    spinnerTipoServicioRelacionado.setVisibility(View.GONE);
                    textViewServicioRelacionado.setVisibility(View.GONE);
                    spinnerServicioRelacionado.setVisibility(View.GONE);
                }else {
                    if (st_selected.equals("6")){
                        textViewTipoServicioRelacionado.setVisibility(View.GONE);
                        spinnerTipoServicioRelacionado.setVisibility(View.GONE);
                        textViewServicioRelacionado.setVisibility(View.GONE);
                        spinnerServicioRelacionado.setVisibility(View.GONE);
                    }
                    spinnerNovedad.setVisibility(View.VISIBLE);
                    spinnerTipoFalla.setVisibility(View.VISIBLE);
                    textViewNovedad.setVisibility(View.VISIBLE);
                    spinnerNovedad.setVisibility(View.VISIBLE);
                    textViewTipoFalla.setVisibility(View.VISIBLE);
                    spinnerTipoFalla.setVisibility(View.VISIBLE);
                    et_horometro.setVisibility(View.VISIBLE);
                    textViewHorometro.setVisibility(View.VISIBLE);
                    textViewTipoServicioRelacionado.setVisibility(View.VISIBLE);
                    spinnerTipoServicioRelacionado.setVisibility(View.VISIBLE);
                    textViewClienteVisitar.setVisibility(View.GONE);
                    spinnerClienteVisitar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(),"no sel",Toast.LENGTH_LONG).show();
            }
        });

        spinnerTipoServicioRelacionado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object tipoServicioSelected = (Object) spinnerTipoServicioRelacionado.getSelectedItem();
                String tipo_st_selected = tipoServicioSelected.getId();

                if (tipo_st_selected.equals("6")){

                    textViewServicioRelacionado.setVisibility(View.VISIBLE);
                    textViewServicioRelacionado.setText("Overhaul Relacionado:");
                    spinnerServicioRelacionado.setVisibility(View.VISIBLE);
                    traer_datos_Servicios_Relacionados("6");

                }else if (tipo_st_selected.equals("7")){

                    textViewServicioRelacionado.setVisibility(View.VISIBLE);
                    textViewServicioRelacionado.setText("Visita relacionada");
                    spinnerServicioRelacionado.setVisibility(View.VISIBLE);
                    traer_datos_Servicios_Relacionados("7");

                }else{
                    textViewServicioRelacionado.setVisibility(View.GONE);
                    spinnerServicioRelacionado.setVisibility(View.GONE);
                    traer_datos_Servicios_Relacionados("6");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    private void crear_servicio_tecnico_WebService(){

        String url = "https://sige.golfyturf.com/AppWebServices/crearServicioTecnico.php";
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
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Fragment_Servicio_Tecnico())
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
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Object tipoSelected = (Object) spinnerTipoServicio.getSelectedItem();
                Object tecnicoSelected = (Object) spinnerTecnico_st.getSelectedItem();
                Object clienteSelected = (Object) spinnerClienteVisitar.getSelectedItem();
                Object tipoFallaSelected = (Object) spinnerTipoFalla.getSelectedItem();
                Object novedadSelected = (Object) spinnerNovedad.getSelectedItem();
                Object servicioRelacionadoSelect = (Object) spinnerServicioRelacionado.getSelectedItem();

                String Precio = et_precio.getText().toString();
                String Fecha_programacion = et_mostrar_fecha_programacion_picker.getText().toString();
                String Descripcion_corta = et_desc_corta_st.getText().toString();
                String Descripcion = et_desc_st.getText().toString();
                String Id_empleado = tecnicoSelected.getId();
                String Persona_cargo = et_operador_asignado_st.getText().toString();
                String Id_tipo_servicio_tecnico = tipoSelected.getId();
                String Id_servicio_relacionado = servicioRelacionadoSelect.getId();
                String Id_estado_servicio_tecnico = "1";
                Map<String,String> parametros = new HashMap<>();

                String servicioSelected = spinnerTipoServicio.getSelectedItem().toString();
                if(Id_tipo_servicio_tecnico.equals("7") || Id_tipo_servicio_tecnico.equals("8")) {
                    String Id_cliente_visitar = clienteSelected.getId();
                    parametros.put("Id_cliente_visitar", Id_cliente_visitar);

                    parametros.put("Id_tipo_falla","0");
                    parametros.put("Horometro","0");
                    parametros.put("Id_novedad","0");
                    parametros.put("Id_servicio_relacionado", "0");
                }else{
                    String Id_tipo_falla = tipoFallaSelected.getId();
                    String Horometro = et_horometro.getText().toString();
                    String Id_novedad = novedadSelected.getId();
                    parametros.put("Id_tipo_falla",Id_tipo_falla);
                    parametros.put("Horometro",Horometro);
                    parametros.put("Id_novedad",Id_novedad);
                    if(Id_tipo_servicio_tecnico.equals("6")) {
                        parametros.put("Id_servicio_relacionado", "0");
                    }else{
                        parametros.put("Id_servicio_relacionado", Id_servicio_relacionado);
                    }

                    parametros.put("Id_cliente_visitar","0");

                }
                    parametros.put("Precio",Precio);
                    parametros.put("Fecha_programacion",Fecha_programacion);
                    parametros.put("Descripcion_corta",Descripcion_corta);
                    parametros.put("Descripcion",Descripcion);
                    parametros.put("Id_empleado",Id_empleado);
                    parametros.put("Persona_cargo",Persona_cargo);
                    parametros.put("Id_tipo_servicio_tecnico",Id_tipo_servicio_tecnico);
                    parametros.put("Id_estado_servicio_tecnico",Id_estado_servicio_tecnico);
                    parametros.put("Id_empleado_edit", Util.getId_usuario());

                return parametros;
            }
        };
        request.add(stringRequest);
    }

    public void bindUI(View view){
        spinnerTipoServicio = (Spinner) view.findViewById(R.id.spinnerTipoServicio);
        spinnerClienteVisitar = (Spinner) view.findViewById(R.id.spinnerClienteVisitar);
        spinnerNovedad = (Spinner) view.findViewById(R.id.spinnerNovedad);
        spinnerTipoFalla= (Spinner) view.findViewById(R.id.spinnerTipoFalla);
        spinnerTecnico_st= (Spinner) view.findViewById(R.id.spinnerTecnico_st);
        spinnerTipoServicioRelacionado= (Spinner) view.findViewById(R.id.spinnerTipoServicioRelacionado);
        spinnerServicioRelacionado= (Spinner) view.findViewById(R.id.spinnerServicioRelacionado);

        et_horometro = (EditText) view.findViewById(R.id.et_horometro);
        et_operador_asignado_st = (EditText) view.findViewById(R.id.et_operador_asignado_st);
        et_desc_corta_st = (EditText) view.findViewById(R.id.et_desc_corta_st);
        et_desc_st = (EditText) view.findViewById(R.id.et_desc_st);
        et_mostrar_fecha_programacion_picker = (EditText) view.findViewById(R.id.et_mostrar_fecha_programacion_picker);
        et_precio = (EditText) view.findViewById(R.id.et_precio);

        ib_obtener_fecha_programacion = (ImageButton) view.findViewById(R.id.ib_obtener_fecha_programacion);

        buttonServicioTecnico = (Button) view.findViewById(R.id.buttonServicioTecnico);

        textViewTipo = (TextView) view.findViewById(R.id.textViewTipo);
        textViewClienteVisitar = (TextView) view.findViewById(R.id.textViewClienteVisitar);
        textViewNovedad = (TextView) view.findViewById(R.id.textViewNovedad);
        textViewTipoFalla = (TextView) view.findViewById(R.id.textViewTipoFalla);
        textViewHorometro = (TextView) view.findViewById(R.id.textViewHorometro);
        textViewTipoServicioRelacionado = (TextView) view.findViewById(R.id.textViewTipoServicioRelacionado);
        textViewServicioRelacionado = (TextView) view.findViewById(R.id.textViewServicioRelacionado);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBarCrearST);
    }

    private void traer_datos_Servicios_Relacionados(final String idTipoServicio){

        String url = "https://sige.golfyturf.com/AppWebServices/getServiciosRelacionados.php";
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
                        object = new Object(jsonArray.getJSONObject(i).getString("Id"), jsonArray.getJSONObject(i).getString("Fecha_programacion") + " - " + jsonArray.getJSONObject(i).getString("Nombre_completo"));
                        datos.add(object);
                    }

                    spinnerServicioRelacionado.setAdapter(new ArrayAdapter<Object>(getContext(), android.R.layout.simple_list_item_1, datos));


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
                parametros.put("idTipoServicio", idTipoServicio);
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
                        datos.add(object);
                        datos.get(i);
                    }
                    if(entidad.equals("Tipo_Servicio_Tecnico")) {
                        spinnerTipoServicio.setAdapter(new ArrayAdapter<Object>(getContext(), android.R.layout.simple_list_item_1, datos));

                    }else
                    if(entidad.equals("Cliente")) {
                        spinnerClienteVisitar.setAdapter(new ArrayAdapter<Object>(getContext(), android.R.layout.simple_list_item_1, datos));
                    }else
                    if(entidad.equals("Novedad")) {
                        spinnerNovedad.setAdapter(new ArrayAdapter<Object>(getContext(), android.R.layout.simple_list_item_1, datos));
                    }else
                    if(entidad.equals("Tipo_Falla")) {
                        spinnerTipoFalla.setAdapter(new ArrayAdapter<Object>(getContext(), android.R.layout.simple_list_item_1, datos));
                    }else
                    if(entidad.equals("Empleado")) {
                        spinnerTecnico_st.setAdapter(new ArrayAdapter<Object>(getContext(), android.R.layout.simple_list_item_1, datos));
                    }
                    count++;
                    if (count==5){
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_obtener_fecha_programacion:
                obtenerFecha();
                break;
            case R.id.buttonServicioTecnico:
                if(confirmarForm()){
                    alertDialog.setMessage("Enviando datos...");
                    alertDialog.setCancelable(false);
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
                    crear_servicio_tecnico_WebService();
                }else{
                    Toast.makeText(getContext(), "Los campos descripción, descripción corta y fecha de programación son obligatorios", Toast.LENGTH_LONG).show();
                }
                break;
        }

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

                et_mostrar_fecha_programacion_picker.setText(year + GUION + mesFormateado + GUION+ diaFormateado);

            }
        },anio, mes, dia);
        recogerFecha.show();
    }

    private class AsyncTaskTraerDatosST extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setMax(100);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if(isOnline(getContext())){

                traer_datos_WebService("Tipo_Servicio_Tecnico", "Tipo_servicio_tecnico", "WHERE Id <> 0 ORDER BY Tipo_servicio_tecnico ASC");
                traer_datos_WebService("Cliente", "Nombre_completo", "WHERE Id <> 0 ORDER BY Nombre_completo ASC");
                traer_datos_WebService("Novedad","Descripcion_corta" , "WHERE Id <> 0 ORDER BY Id DESC");
                traer_datos_WebService("Tipo_Falla", "Tipo_falla","WHERE Id <> 0 ORDER BY Tipo_falla ASC");
                traer_datos_WebService("Empleado", "Nombres","WHERE Id = 29 OR Id = 30 OR Id = 22 ORDER BY Nombres ASC");

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
        if(et_desc_corta_st.getText().toString().equals("") || et_desc_st.getText().toString().equals("") ||
                et_mostrar_fecha_programacion_picker.getText().toString().equals("")){
            return false;
        }else{
            return true;}
    }
}
