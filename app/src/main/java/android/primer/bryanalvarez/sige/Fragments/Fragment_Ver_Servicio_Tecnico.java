package android.primer.bryanalvarez.sige.Fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.primer.bryanalvarez.sige.Activities.CierreServicioTecnicoActivity;
import android.primer.bryanalvarez.sige.Activities.VerNovedadActivity;
import android.primer.bryanalvarez.sige.Activities.VerServicioTecnicoActivity;
import android.primer.bryanalvarez.sige.Adapters.Adapter_Rectificado;
import android.primer.bryanalvarez.sige.Adapters.Adapter_Repuestos;
import android.primer.bryanalvarez.sige.Adapters.Adapter_Servicio_Tecnico;
import android.primer.bryanalvarez.sige.Models.Maquina;
import android.primer.bryanalvarez.sige.Models.Maquina_Rectificado;
import android.primer.bryanalvarez.sige.Models.Object;
import android.primer.bryanalvarez.sige.Models.Servicio_Tecnico;
import android.primer.bryanalvarez.sige.Util.Util;
import android.support.annotation.AnyRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Ver_Servicio_Tecnico extends Fragment{

    private RecyclerView recyclerView;
    private Adapter_Servicio_Tecnico adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Servicio_Tecnico> servicios_tecnicos = new ArrayList<>();
    private DividerItemDecoration itemDecoration;
    private ProgressBar progressBar;
    private Spinner spinnerClienteFiltro;
    private Spinner spinnerEstadoFiltro;
    private Spinner spinnerTecnicoFiltro;
    private ImageButton ib_buscar_cliente;

    private TextWatcher textWatcher;

    private RequestQueue request;
    private StringRequest stringRequest;
    private static ConnectivityManager manager;

    private AlertDialog alertDialog;
    private int count;
    private boolean serviciosCargados = false;
    private boolean datosCargados = false;

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

    String maquinas_seleccionadas = "";

    public Fragment_Ver_Servicio_Tecnico() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment_ver__servicio__tecnico, container, false);

        if (isOnline(getContext())){
            alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setMessage("Cargando datos...");
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }

        request = Volley.newRequestQueue(getContext());

        progressBar = (ProgressBar) view.findViewById(R.id.progressBarServicioTecnico);
        spinnerClienteFiltro = (Spinner) view.findViewById(R.id.spinnerClienteFiltro);
        spinnerEstadoFiltro = (Spinner) view.findViewById(R.id.spinnerEstadoFiltro);
        spinnerTecnicoFiltro = (Spinner) view.findViewById(R.id.spinnerTecnicoFiltro);
        ib_buscar_cliente = (ImageButton) view.findViewById(R.id.ib_buscar_cliente);

        traer_datos_WebService("Cliente", "Nombre_completo",spinnerClienteFiltro, " WHERE Id <> 0 ORDER BY Nombre_completo ASC", false);
        traer_datos_WebService("Estado_Servicio_Tecnico", "Estado_servicio_tecnico", spinnerEstadoFiltro, "WHERE Id <> 0 ORDER BY Estado_servicio_tecnico ASC", false);
        traer_datos_WebService("Empleado", "Nombres",spinnerTecnicoFiltro, "WHERE Id = 29 OR Id = 30 OR Id = 22 ORDER BY Nombres ASC ", false);


        spinnerClienteFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                traer_servicios_tecnicos_WebService();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerEstadoFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                traer_servicios_tecnicos_WebService();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerTecnicoFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                traer_servicios_tecnicos_WebService();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ib_buscar_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar("Buscar cliente");
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewServicioTecnico);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mLayoutManager = new LinearLayoutManager(getContext());
        itemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(itemDecoration);
        // Inflate the layout for this fragment
        return view;
    }

    private void traer_servicios_tecnicos_WebService(){

        String url = "https://sige.golfyturf.com/AppWebServices/getServiciosTecnicos.php";
        url = url.replace(" ", "%20");

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = null;
                Servicio_Tecnico servicio_tecnico= null;
                ArrayList<Servicio_Tecnico> servicios_tecnicosWS= new ArrayList<>();
                JSONArray jsonArrayProductosUsados = null;
                ArrayList<String> productosModeloMaquinaUsados = new ArrayList<>();

                String Id;
                String Id_tipo_falla;
                String Fecha_programacion;
                String Fecha_hora_inicio;
                String Fecha_hora_fin;
                String Horometro;
                String Repuestos_utilizados="";
                String Duracion;
                String Descripcion_corta;
                String Descripcion;
                String Descripcion_cierre;
                String Id_novedad;
                String Id_empleado;
                String Id_estado_servicio_tecnico;
                String Id_tipo_servicio_tecnico;
                String Persona_cargo;
                String Cliente;
                String Serial;
                String Id_cliente_visitar;
                String Id_nota_salida;

                try {
                    jsonArray = new JSONArray(response);

                    for (int i=0; i<jsonArray.length(); i++ ){

                        jsonArrayProductosUsados = jsonArray.getJSONObject(i).getJSONArray("Productos Nota Salida");
                        for (int k=0; k<jsonArrayProductosUsados.length(); k++){
                            Repuestos_utilizados = Repuestos_utilizados +jsonArrayProductosUsados.getJSONObject(k).getString("Referencia") + ", ";
                        }

                        Id = jsonArray.getJSONObject(i).getString("Id");
                        Id_tipo_falla = jsonArray.getJSONObject(i).getString("Tipo_falla");
                        Fecha_programacion = jsonArray.getJSONObject(i).getString("Fecha_programacion");
                        Fecha_hora_inicio = jsonArray.getJSONObject(i).getString("Fecha_hora_inicio");
                        Fecha_hora_fin = jsonArray.getJSONObject(i).getString("Fecha_hora_fin");
                        Horometro = jsonArray.getJSONObject(i).getString("Horometro");
                        //Duracion = jsonArray.getJSONObject(i).getString("Duracion");
                        Descripcion = jsonArray.getJSONObject(i).getString("Descripcion");
                        Descripcion_corta = jsonArray.getJSONObject(i).getString("Descripcion_corta");
                        Descripcion_cierre = jsonArray.getJSONObject(i).getString("Descripcion_cierre");
                        Id_novedad = jsonArray.getJSONObject(i).getString("Id_novedad");
                        Id_empleado = jsonArray.getJSONObject(i).getString("Empleado");
                        Id_estado_servicio_tecnico = jsonArray.getJSONObject(i).getString("Estado_servicio_tecnico");
                        Id_tipo_servicio_tecnico = jsonArray.getJSONObject(i).getString("Tipo_servicio_tecnico");
                        Persona_cargo = jsonArray.getJSONObject(i).getString("Persona_cargo");
                        Id_cliente_visitar = jsonArray.getJSONObject(i).getString("Id_cliente_visitar");
                        Cliente = jsonArray.getJSONObject(i).getString("Nombre_completo");
                        Serial = jsonArray.getJSONObject(i).getString("Serial");
                        Id_nota_salida = jsonArray.getJSONObject(i).getString("Id_nota_salida");

                        servicio_tecnico = new Servicio_Tecnico();
                        servicio_tecnico.setId(Id);
                        servicio_tecnico.setId_tipo_falla(Id_tipo_falla);
                        servicio_tecnico.setFecha_programacion(Fecha_programacion);
                        servicio_tecnico.setFecha_hora_inicio(Fecha_hora_inicio);
                        servicio_tecnico.setFecha_hora_fin(Fecha_hora_fin);
                        servicio_tecnico.setHorometro(Horometro);
                        servicio_tecnico.setDescripcion(Descripcion);
                        servicio_tecnico.setDescripcion_corta(Descripcion_corta);
                        servicio_tecnico.setDescripcion_cierre(Descripcion_cierre);
                        servicio_tecnico.setId_novedad(Id_novedad);
                        servicio_tecnico.setId_empleado(Id_empleado);
                        servicio_tecnico.setId_estado_servicio_tecnico(Id_estado_servicio_tecnico);
                        servicio_tecnico.setId_tipo_servicio_tecnico(Id_tipo_servicio_tecnico);
                        servicio_tecnico.setPersona_cargo(Persona_cargo);
                        servicio_tecnico.setId_cliente_visitar(Id_cliente_visitar);
                        servicio_tecnico.setCliente(Cliente);
                        servicio_tecnico.setSerial(Serial);
                        servicio_tecnico.setRepuestos_utilizados(Repuestos_utilizados);


                        servicios_tecnicosWS.add(servicio_tecnico);
                    }
                    adapter = new Adapter_Servicio_Tecnico(getContext().getApplicationContext(), servicios_tecnicosWS, R.layout.list_view_servicio_tecnico_item, new Adapter_Servicio_Tecnico.OnItemClickListener() {
                        @Override
                        public void onItemClick(Servicio_Tecnico servicio_tecnico, int position) {
                            Intent intent = new Intent(getContext(), VerServicioTecnicoActivity.class);
                            Util.setServicio_tecnico(servicio_tecnico);
                            startActivity(intent);
                        }
                    }, new Adapter_Servicio_Tecnico.OnButtonEditarClickListener() {
                        @Override
                        public void onButtonEditarClick(Servicio_Tecnico servicio_tecnico, int position) {
                            if(servicio_tecnico.getId_estado_servicio_tecnico().equals("Programado")){
                                showAlertForEditingFirstST("Editar", "Ingrese la fecha y hora de inicio", servicio_tecnico, adapter);
                            }else if(servicio_tecnico.getId_estado_servicio_tecnico().equals("En proceso")){
                                showAlertForCloseST("Editar", "Ingrese los datos del cierrre sel servicio",servicio_tecnico, adapter);
                            }else{}
                        }
                    });
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    servicios_tecnicos=servicios_tecnicosWS;
                    serviciosCargados = true;
                    hideAlertDialog();

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
                Object clienteSelected = null;
                Object estadoSelected = null;
                Object tecnicoSelected = null;
                String idCliente= "";
                String idEstado = "";
                String idTecnico ="";
                while(tecnicoSelected == null || clienteSelected ==null || estadoSelected ==null){
                    clienteSelected = (Object) spinnerClienteFiltro.getSelectedItem();
                    idCliente = clienteSelected.getId();
                    estadoSelected = (Object) spinnerEstadoFiltro.getSelectedItem();
                    idEstado= estadoSelected.getId();
                    tecnicoSelected = (Object) spinnerTecnicoFiltro.getSelectedItem();
                    idTecnico = tecnicoSelected.getId();

                }
                Map<String,String> parametros = new HashMap<>();
                parametros.put("idTecnico", idTecnico);
                parametros.put("idCliente", idCliente);
                parametros.put("idEstado", idEstado);


                return parametros;
            }
        };
        request.add(stringRequest);
    }

    private class AsyncTaskTraerServiciosTecnicos extends AsyncTask<Void, Integer, Boolean> {

        private Boolean mostrarMensaje;

        public AsyncTaskTraerServiciosTecnicos(Boolean mostrarMensaje) {
            this.mostrarMensaje = mostrarMensaje;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setMax(100);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if(isOnline(getContext())){

                traer_servicios_tecnicos_WebService();
                return true;
            }else{
                for (int i=0; i<=10; i++){
                    if(servicios_tecnicos == null){
                        publishProgress(i*10);
                    }
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
                if (mostrarMensaje){
                    Toast.makeText(getContext(), "Cargando datos", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getContext(), "No tiene acceso a internet, verifique su conexión ", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showAlertForEditingFirstST(String title, String message, final Servicio_Tecnico servicio_tecnico_edit, final Adapter_Servicio_Tecnico adapter_servicio_tecnico) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        if (title != null) builder.setTitle(title);
        if (message != null) builder.setMessage(message + "\n" + "Esta editando el servicio tecnico con identificador: " + servicio_tecnico_edit.getId());

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_first_servicio_tecnico, null);
        builder.setView(viewInflated);

        final EditText fecha_inicio = (EditText) viewInflated.findViewById(R.id.et_fecha_inicio_dialog);
        final EditText hora_inicio  = (EditText) viewInflated.findViewById(R.id.et_hora_inicio_dialog);
        final ImageButton ib_obtener_fecha_inicio = (ImageButton) viewInflated.findViewById(R.id.ib_obtener_fecha_inicio_dialog);
        final ImageButton ib_obtener_hora_inicio = (ImageButton) viewInflated.findViewById(R.id.ib_obtener_hora_inicio_dialog);

        ib_obtener_fecha_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFecha(fecha_inicio);
            }
        });

        ib_obtener_hora_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerHora(hora_inicio);
            }
        });

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.setMessage("Enviando datos...");
                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
                String fecha = fecha_inicio.getText().toString();
                String hora = hora_inicio.getText().toString();
                servicio_tecnico_edit.setFecha_hora_inicio(fecha + " " + hora);
                servicio_tecnico_edit.setId_estado_servicio_tecnico("En proceso");
                adapter_servicio_tecnico.notifyDataSetChanged();
                actualizar_servicio_tecnico_WebService(fecha,hora, servicio_tecnico_edit.getId(), "2");
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAlertForAddMaquinas(String title,final Servicio_Tecnico servicio_tecnico_edit) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        maquinas_seleccionadas = "";
        if (title != null) builder.setTitle(title);

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_maquinas_rectificadas, null);
        builder.setView(viewInflated);

        final ListView listViewMaquinas = (ListView) viewInflated.findViewById(R.id.listViewMaquina);
        final ArrayList<Maquina_Rectificado> maquinasSelec = new ArrayList<>();
        final ArrayList<Maquina_Rectificado> datos = new ArrayList<>();

        String url = "https://sige.golfyturf.com/AppWebServices/getMaquinaria.php";
        url = url.replace(" ", "%20");

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = null;
                Maquina_Rectificado maquina = null;

                try {
                    jsonArray = new JSONArray(response);
                    for (int i=0; i<jsonArray.length(); i++ ){
                        maquina = new Maquina_Rectificado(jsonArray.getJSONObject(i).getString("Id"), jsonArray.getJSONObject(i).getString("Modelo_maquina"),jsonArray.getJSONObject(i).getString("Serial"));
                        datos.add(maquina);
                        final Adapter_Rectificado adapter_rectificado = new Adapter_Rectificado(getContext().getApplicationContext(), datos, R.layout.list_view_maquina_rectificado);
                        listViewMaquinas.setAdapter(adapter_rectificado);
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
                parametros.put("idCliente", servicio_tecnico_edit.getId_cliente_visitar());
                parametros.put("idModelo", "0");
                parametros.put("idMaquina", "0");
                return parametros;
            }
        };
        request.add(stringRequest);

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for(int j=0; j<datos.size(); j++){
                    if(datos.get(j).isChecked()){
                        maquinasSelec.add(datos.get(j));
                    }
                }
                String maq_selec="";
                for (int k=0; k<maquinasSelec.size(); k++){
                    maq_selec += maquinasSelec.get(k).getId()+ " ";
                }
                Toast.makeText(getContext(),maquinasSelec.size() + " maquinas añadidas a rectificadas",Toast.LENGTH_LONG).show();
                maquinas_seleccionadas = maq_selec;
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void actualizar_servicio_tecnico_WebService(final String fecha,final String hora, final String id, final String id_estado){

        String url = "https://sige.golfyturf.com/AppWebServices/actualizarServicioTecnico.php";
        url = url.replace(" ", "%20");

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if(jsonObject.getString("Success").equals("true")){
                        alertDialog.dismiss();
                        Toast.makeText(getContext(), "Actualización exitosa", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getContext(), "Error al actualizar", Toast.LENGTH_LONG).show();
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
                String fecha_hora= fecha + " " + hora;
                parametros.put("Id_estado_servicio_tecnico", id_estado);
                parametros.put("Id", id);
                parametros.put("Fecha_hora_inicio", fecha_hora);
                parametros.put("Id_empleado", Util.getId_usuario());
                return parametros;
            }
        };
        request.add(stringRequest);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    private void obtenerFecha(final EditText editText) {
        DatePickerDialog recogerFecha = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                editText.setText(year + GUION + mesFormateado + GUION+ diaFormateado);

            }
        },anio, mes, dia);
        recogerFecha.show();
    }

    private void obtenerHora(final EditText editText) {
        TimePickerDialog recogerHora = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                //Muestro la hora con el formato deseado
                editText.setText(horaFormateada + DOS_PUNTOS + minutoFormateado);
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, hora, minuto, false);
        recogerHora.show();
    }

    private void showAlertForCloseST(String title, String message, final Servicio_Tecnico servicio_tecnico_edit, final Adapter_Servicio_Tecnico adapter_servicio_tecnico) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        if (title != null) builder.setTitle(title);
        if (message != null) builder.setMessage(message + "\n" + "Esta editando el servicio tecnico con identificador: " + servicio_tecnico_edit.getId());

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_close_servicio_tecnico, null);
        builder.setView(viewInflated);

        final EditText et_fecha_fin_dialog = (EditText) viewInflated.findViewById(R.id.et_fecha_fin_dialog);
        final EditText et_hora_fin_dialog = (EditText) viewInflated.findViewById(R.id.et_hora_fin_dialog);
        final EditText et_descripcion_cierre_st = (EditText) viewInflated.findViewById(R.id.et_descripcion_cierre_st);
        final EditText et_horometro_actual_st = (EditText) viewInflated.findViewById(R.id.et_horometro_actual_st);
        final ImageButton ib_obtener_fecha_fin_dialog = (ImageButton) viewInflated.findViewById(R.id.ib_obtener_fecha_fin_dialog);
        final ImageButton ib_obtener_hora_fin_dialog = (ImageButton) viewInflated.findViewById(R.id.ib_obtener_hora_fin_dialog);
        final Button btn_guardar_cerrar_st = (Button) viewInflated.findViewById(R.id.btn_guardar_cerrar_st);
        final Spinner spinnerNotaSalida_cerrar_st = (Spinner) viewInflated.findViewById(R.id.spinnerNotaSalida_cerrar_st);
        final Switch switchCerrarNovedad = (Switch) viewInflated.findViewById(R.id.switchCerrarNovedad);
        final Spinner spinnerEstadoMaquina_cerrar_st = (Spinner) viewInflated.findViewById(R.id.spinnerEstadoMaquina_cerrar_st);
        final LinearLayout linearLayout3 = (LinearLayout) viewInflated.findViewById(R.id.linearLayout3);
        final LinearLayout linearLayout7 = (LinearLayout) viewInflated.findViewById(R.id.linearLayout7);
        final LinearLayout linearLayout8 = (LinearLayout) viewInflated.findViewById(R.id.linearLayout8);
        final LinearLayout linearLayout6 = (LinearLayout) viewInflated.findViewById(R.id.linearLayout6);
        final LinearLayout linearLayout10 = (LinearLayout) viewInflated.findViewById(R.id.linearLayout10);
        final ImageButton btn_agregar_maquinas = (ImageButton) viewInflated.findViewById(R.id.btn_agregar_maquinas);

        if(servicio_tecnico_edit.getId_tipo_servicio_tecnico().equals("Visita periodica") || servicio_tecnico_edit.getId_tipo_servicio_tecnico().equals("Rectificado") ){
            linearLayout3.setVisibility(View.GONE);
            linearLayout7.setVisibility(View.GONE);
            linearLayout8.setVisibility(View.GONE);
            linearLayout6.setVisibility(View.GONE);
            if (servicio_tecnico_edit.getId_tipo_servicio_tecnico().equals("Rectificado")){
                linearLayout10.setVisibility(View.VISIBLE);
            }
        }

        btn_agregar_maquinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertForAddMaquinas("Agregar maquinas rectificadas",servicio_tecnico_edit);
            }
        });
        traer_datos_WebService("Estado_Maquina", "Estado",spinnerEstadoMaquina_cerrar_st, "ORDER BY Estado ASC", true);
        traer_datos_WebService("Nota_Salida", "Numero",spinnerNotaSalida_cerrar_st, "ORDER BY Id DESC",true);

        final AlertDialog dialog = builder.create();
        dialog.show();

        ib_obtener_fecha_fin_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFecha(et_fecha_fin_dialog);
            }
        });

        ib_obtener_hora_fin_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerHora(et_hora_fin_dialog);
            }
        });

        btn_guardar_cerrar_st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fecha = et_fecha_fin_dialog.getText().toString();
                String hora = et_hora_fin_dialog.getText().toString();
                String descripcion_final = et_descripcion_cierre_st.getText().toString();
                Object estadoMaquinaSelect = (Object) spinnerEstadoMaquina_cerrar_st.getSelectedItem();
                String idEstadoMaquina = estadoMaquinaSelect.getId();
                Object notaSalidaSelect = (Object) spinnerNotaSalida_cerrar_st.getSelectedItem();
                String notaSalida = notaSalidaSelect.getId();
                String horometro = et_horometro_actual_st.getText().toString();
                String cerrarNovedad="";
                if (switchCerrarNovedad.isChecked()){
                    cerrarNovedad = "true";
                }else{
                    cerrarNovedad="false";
                }

                alertDialog.setMessage("Enviando datos...");
                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
                servicio_tecnico_edit.setFecha_hora_fin(fecha + " " + hora);
                servicio_tecnico_edit.setId_estado_servicio_tecnico("Cerrado");
                servicio_tecnico_edit.setDescripcion_cierre(descripcion_final);
                adapter_servicio_tecnico.notifyDataSetChanged();
                cerrar_servicio_tecnico_WebService(fecha,hora,descripcion_final, notaSalida,idEstadoMaquina,horometro,cerrarNovedad, servicio_tecnico_edit.getId(),"3", maquinas_seleccionadas, servicio_tecnico_edit.getId_tipo_servicio_tecnico());
                dialog.dismiss();

            }
        });
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
                traer_datos_WebService("Cliente", "Nombre_completo",spinnerClienteFiltro, " WHERE Id <> 0 AND Nombre_completo like '"+buscar+"%' OR Nombre_completo like '%"+buscar+"%' ORDER BY Nombre_completo ASC", false);
                spinnerClienteFiltro.performClick();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void traer_datos_WebService(final String entidad, final String campo, final Spinner spinner, final String orden, final Boolean mostrarMensaje){

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
                        if (entidad.equals("Empleado")){
                            if (Util.getCargo_usuario().equals("11")){
                                if (jsonArray.getJSONObject(i).getString("Id").equals(Util.getId_usuario())){
                                    object = new Object(jsonArray.getJSONObject(i).getString("Id"), jsonArray.getJSONObject(i).getString(campo));
                                    datos.add(object);
                                    datos.remove(0);
                                }
                            } else{
                                object = new Object(jsonArray.getJSONObject(i).getString("Id"), jsonArray.getJSONObject(i).getString(campo));
                                datos.add(object);
                            }
                        }else{
                            object = new Object(jsonArray.getJSONObject(i).getString("Id"), jsonArray.getJSONObject(i).getString(campo));
                            datos.add(object);
                        }



                    }
                        spinner.setAdapter(new ArrayAdapter<Object>(getContext(), android.R.layout.simple_list_item_1, datos));
                        AsyncTaskTraerServiciosTecnicos at = new AsyncTaskTraerServiciosTecnicos(mostrarMensaje);
                        at.execute();
                    count++;
                    if (count==3){
                        datosCargados = true;
                        hideAlertDialog();
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

    private void hideAlertDialog() {
        if (datosCargados && serviciosCargados){
            alertDialog.hide();
        }
    }

    private void cerrar_servicio_tecnico_WebService(final String fecha,final String hora, final String descripcion, final String notaSalida, final String idEstadoMaquina, final String horometro, final String cerrarNovedad, final String id, final String id_estado_servicio, final String idMaquinas, final String id_tipo_servicio){

        String url = "https://sige.golfyturf.com/AppWebServices/actualizarServicioTecnico.php";
        url = url.replace(" ", "%20");

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if(jsonObject.getString("Success").equals("true")){
                        alertDialog.dismiss();
                        Toast.makeText(getContext(), "Actualización exitosa", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getContext(), "Error al actualizar", Toast.LENGTH_LONG).show();
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
                Map<String, String> parametros = new HashMap<>();
                String fecha_hora= fecha + " " + hora;
                parametros.put("Id_estado_servicio_tecnico", id_estado_servicio );
                parametros.put("Id", id);
                parametros.put("Fecha_hora_fin", fecha_hora);
                parametros.put("Descripcion_cierre", descripcion);
                parametros.put("Id_estado_maquina", idEstadoMaquina);
                parametros.put("Horometro", horometro);
                parametros.put("cerrarNovedad", cerrarNovedad);
                parametros.put("Id_nota_salida", notaSalida);
                parametros.put("Id_empleado", Util.getId_usuario());
                parametros.put("Id_maquinas", idMaquinas);
                parametros.put("Tipo_servicio", id_tipo_servicio);
                return parametros;
            }
        };
        request.add(stringRequest);
    }
}


