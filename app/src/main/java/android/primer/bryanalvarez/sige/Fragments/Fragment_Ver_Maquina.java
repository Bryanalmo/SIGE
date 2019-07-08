package android.primer.bryanalvarez.sige.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.primer.bryanalvarez.sige.Activities.VerMaquinaActivity;
import android.primer.bryanalvarez.sige.Adapters.Adapter_Maquina;
import android.primer.bryanalvarez.sige.Models.Maquina;
import android.primer.bryanalvarez.sige.Models.Object;
import android.primer.bryanalvarez.sige.Util.Util;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.primer.bryanalvarez.sige.R;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Ver_Maquina extends Fragment implements AdapterView.OnItemClickListener{

    private ListView listView;
    private Adapter_Maquina adapter;

    private ArrayList<Maquina> maquinaria;
    private ProgressBar progressBar;
    private Spinner spinnerClienteFiltro;
    private Spinner spinnerModeloFiltro;
    private Spinner spinnerSerialFiltro;
    private ImageButton ib_buscar_cliente;
    private ImageButton ib_buscar_modelo;
    private ImageButton ib_buscar_serial;

    private AlertDialog alertDialog;
    private int count;
    private boolean maquinasCargadas = false;
    private boolean datosCargados = false;

    RequestQueue request;
    StringRequest stringRequest;
    String idCliente;

    public Fragment_Ver_Maquina() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment__ver__maquina, container, false);

        request = Volley.newRequestQueue(getContext());
        if (isOnline(getContext())){
            alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setMessage("Cargando datos...");
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }

        progressBar = (ProgressBar) view.findViewById(R.id.progressBarVerMaquina);
        spinnerClienteFiltro = (Spinner) view.findViewById(R.id.spinnerClienteFiltro);
        spinnerModeloFiltro = (Spinner) view.findViewById(R.id.spinnerModeloFiltro);
        spinnerSerialFiltro = (Spinner) view.findViewById(R.id.spinnerSerialFiltro);
        ib_buscar_cliente = (ImageButton) view.findViewById(R.id.ib_buscar_cliente);
        ib_buscar_modelo = (ImageButton) view.findViewById(R.id.ib_buscar_modelo);
        ib_buscar_serial = (ImageButton) view.findViewById(R.id.ib_buscar_serial);


        traer_datos_WebService("Cliente", "Nombre_completo", " WHERE Id <> 0 ORDER BY Nombre_completo ASC", false);
        traer_datos_WebService("Modelo_Maquina", "Nombre_fabrica", "WHERE Id <> 0 ORDER BY Nombre_fabrica ASC", false);
        traer_datos_WebService("Maquina", "Serial", "WHERE Id <> 0 ORDER BY Serial ASC", false);



        listView = (ListView) view.findViewById(R.id.listViewMaquinas);
        listView.setOnItemClickListener(this);
        registerForContextMenu(listView);

        spinnerClienteFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                traer_maquinas_WebService();
                Object clienteSelected = (Object) spinnerClienteFiltro.getSelectedItem();
                idCliente = clienteSelected.getId();

                if (!idCliente.equals("0")){
                    traer_datos_WebService("Modelo_Maquina", "Modelo_maquina", "WHERE Id <> 0 AND Id IN (SELECT Id_modelo FROM Maquina WHERE Id_cliente = '"+idCliente+"' ) ORDER BY Modelo_maquina ASC", true);
                    traer_datos_WebService("Maquina", "Serial", "WHERE Id <> 0 AND Id_cliente = '"+idCliente+"' ORDER BY Serial ASC", true);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerModeloFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                traer_maquinas_WebService();
                Object modeloSelected = (Object) spinnerModeloFiltro.getSelectedItem();
                String idModelo= modeloSelected.getId();

                if (!idModelo.equals("0")){
                    String filtroCliente;
                    if (idCliente.equals("")){
                        filtroCliente = "";
                    }else{
                        filtroCliente = "AND Id_cliente = '"+idCliente+"'";
                    }
                    traer_datos_WebService("Maquina", "Serial", "WHERE Id <> 0 AND Id_modelo = '"+idModelo+"' "+filtroCliente+"  ORDER BY Serial ASC", true);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerSerialFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                traer_maquinas_WebService();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ib_buscar_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar("Buscar cliente", "Cliente");
            }
        });
        ib_buscar_modelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar("Buscar modelo", "Modelo");
            }
        });
        ib_buscar_serial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar("Buscar serial", "Serial");
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), VerMaquinaActivity.class);
        Util.setMaquina(maquinaria.get(position));
        startActivity(intent);
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
                if (nombreSpinner.equals("Cliente")){
                    String buscar = et_buscar_filtro.getText().toString();
                    traer_datos_WebService("Cliente", "Nombre_completo", " WHERE Id <> 0 AND Nombre_completo like '"+buscar+"%' OR Nombre_completo like '%"+buscar+"%' ORDER BY Nombre_completo ASC", false);
                    spinnerClienteFiltro.performClick();
                }else if (nombreSpinner.equals("Modelo")){
                    String buscar = et_buscar_filtro.getText().toString();
                    traer_datos_WebService("Modelo_Maquina", "Nombre_fabrica", "WHERE Id <> 0 AND Nombre_fabrica like'"+buscar+"%' OR Nombre_fabrica like '%"+buscar+"%' ORDER BY Nombre_fabrica ASC", false);
                    spinnerModeloFiltro.performClick();
                }else if (nombreSpinner.equals("Serial")){
                    String buscar = et_buscar_filtro.getText().toString();
                    traer_datos_WebService("Maquina", "Serial", " WHERE Id <> 0 AND Serial like '"+buscar+"%' OR Serial like '%"+buscar+"%' ORDER BY Serial ASC", false);
                    spinnerSerialFiltro.performClick();
                }

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void traer_maquinas_WebService(){

        String url = "https://sige.golfyturf.com/AppWebServices/getMaquinaria.php";
        url = url.replace(" ", "%20");

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = null;
                Maquina maquina= null;
                ArrayList<Maquina> maquinas = new ArrayList<>();


                String Id;
                String Serial;
                String Placa_activo_fijo_club;
                String Numero_maquina_club;
                String Modelo_motor;
                String Serial_motor;
                String Codigo_motor;
                String Serial_unidades_corte;
                String Fecha_compra;
                String Fecha_entrega;
                String Horas;
                String Operador_asignado;
                String Observaciones;
                String Estado_registro_fabrica;
                String Fecha_regisro_fabrica;
                String Año_produccion;
                String Distribuidor_maquina;
                String Ubicacion;
                String Tecnico_asignado;
                String Marca;
                String Modelo_maquina;
                String Funcion;
                String Cliente;
                String Estado_maquina;
                String Foto;
                String Manual_partes;
                try {
                    jsonArray = new JSONArray(response);
                    for (int i=0; i<jsonArray.length(); i++ ){
                        Id = jsonArray.getJSONObject(i).getString("Id");
                        Serial = jsonArray.getJSONObject(i).getString("Serial");
                        Placa_activo_fijo_club = jsonArray.getJSONObject(i).getString("Placa_activo_fijo_club");
                        Numero_maquina_club = jsonArray.getJSONObject(i).getString("Numero_maquina_club");
                        Modelo_motor = jsonArray.getJSONObject(i).getString("Modelo_motor");
                        Serial_motor = jsonArray.getJSONObject(i).getString("Serial_motor");
                        Codigo_motor = jsonArray.getJSONObject(i).getString("Codigo_motor");
                        Serial_unidades_corte = jsonArray.getJSONObject(i).getString("Serial_unidades_corte");
                        Fecha_compra = jsonArray.getJSONObject(i).getString("Fecha_compra");
                        Fecha_entrega = jsonArray.getJSONObject(i).getString("Fecha_entrega");
                        Horas = jsonArray.getJSONObject(i).getString("Horas");
                        Operador_asignado = jsonArray.getJSONObject(i).getString("Operador_asignado");
                        Observaciones = jsonArray.getJSONObject(i).getString("Observaciones");
                        Estado_registro_fabrica = jsonArray.getJSONObject(i).getString("Estado_registro_fabrica");
                        Fecha_regisro_fabrica = jsonArray.getJSONObject(i).getString("Fecha_regisro_fabrica");
                        Año_produccion = jsonArray.getJSONObject(i).getString("Year_produccion");
                        Distribuidor_maquina = jsonArray.getJSONObject(i).getString("Distribuidor_maquina");
                        Ubicacion = jsonArray.getJSONObject(i).getString("Ubicacion");
                        Tecnico_asignado = jsonArray.getJSONObject(i).getString("Empleado");
                        Marca = jsonArray.getJSONObject(i).getString("Marca");
                        Modelo_maquina = jsonArray.getJSONObject(i).getString("Modelo_maquina");
                        Funcion = jsonArray.getJSONObject(i).getString("Funcion");
                        Cliente = jsonArray.getJSONObject(i).getString("Nombre_completo");
                        Estado_maquina = jsonArray.getJSONObject(i).getString("Estado");
                        Foto = jsonArray.getJSONObject(i).getString("Foto_maquina");
                        Manual_partes = jsonArray.getJSONObject(i).getString("Manual_partes");

                        maquina = new Maquina();
                        maquina.setId(Id);
                        maquina.setSerial(Serial);
                        maquina.setPlaca_activo_fijo_club(Placa_activo_fijo_club);
                        maquina.setNumero_maquina_club(Numero_maquina_club);
                        maquina.setModelo_motor(Modelo_motor);
                        maquina.setSerial_motor(Serial_motor);
                        maquina.setCodigo_motor(Codigo_motor);
                        maquina.setSerial_unidades_corte(Serial_unidades_corte);
                        maquina.setFecha_compra(Fecha_compra);
                        maquina.setFecha_entrega(Fecha_entrega);
                        maquina.setHoras(Horas);
                        maquina.setOperador_asignado(Operador_asignado);
                        maquina.setObservaciones(Observaciones);
                        maquina.setEstado_registro_fabrica(Estado_registro_fabrica);
                        maquina.setFecha_regisro_fabrica(Fecha_regisro_fabrica);
                        maquina.setAño_produccion(Año_produccion);
                        maquina.setDistribuidor_maquina(Distribuidor_maquina);
                        maquina.setUbicacion(Ubicacion);
                        maquina.setTecnico_asignado(Tecnico_asignado);
                        maquina.setMarca(Marca);
                        maquina.setModelo_maquina(Modelo_maquina);
                        maquina.setFuncion(Funcion);
                        maquina.setCliente(Cliente);
                        maquina.setEstado_maquina(Estado_maquina);
                        maquina.setFoto(Foto);
                        maquina.setManual_partes(Manual_partes);

                        maquinas.add(maquina);
                    }
                    adapter = new Adapter_Maquina(getContext().getApplicationContext(), maquinas, R.layout.list_view_maquina_item);
                    listView.setAdapter(adapter);
                    maquinaria=maquinas;
                    maquinasCargadas = true;
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
                Object clienteSelected = (Object) spinnerClienteFiltro.getSelectedItem();
                String idCliente = clienteSelected.getId();
                Object modeloSelected = (Object) spinnerModeloFiltro.getSelectedItem();
                String idModelo= modeloSelected.getId();
                Object serialSelected = (Object) spinnerSerialFiltro.getSelectedItem();
                String idMaquina = serialSelected.getId();
                Map<String,String> parametros = new HashMap<>();
                parametros.put("idCliente", idCliente);
                parametros.put("idModelo", idModelo);
                parametros.put("idMaquina", idMaquina);

                return parametros;

            }
        };
        request.add(stringRequest);
    }

    private void traer_datos_WebService(final String entidad, final String campo, final String orden, final Boolean mostrarMensaje){

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
                    if(entidad.equals("Cliente")) {
                        spinnerClienteFiltro.setAdapter(new ArrayAdapter<Object>(getContext(), android.R.layout.simple_list_item_1, datos));
                        AsyncTaskTraerMaquinas at = new AsyncTaskTraerMaquinas(mostrarMensaje);
                        at.execute();
                    }
                    if(entidad.equals("Modelo_Maquina")) {
                        spinnerModeloFiltro.setAdapter(new ArrayAdapter<Object>(getContext(), android.R.layout.simple_list_item_1, datos));
                        AsyncTaskTraerMaquinas at = new AsyncTaskTraerMaquinas(mostrarMensaje);
                        at.execute();
                    }
                    if(entidad.equals("Maquina")) {
                        spinnerSerialFiltro.setAdapter(new ArrayAdapter<Object>(getContext(), android.R.layout.simple_list_item_1, datos));
                        AsyncTaskTraerMaquinas at = new AsyncTaskTraerMaquinas(mostrarMensaje);
                        at.execute();
                    }
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
        if (datosCargados && maquinasCargadas){
            alertDialog.hide();
        }
    }

    private class AsyncTaskTraerMaquinas extends AsyncTask<Void, Integer, Boolean> {

        private Boolean mostrarMensaje;

        public AsyncTaskTraerMaquinas(Boolean mostrarMensaje) {
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

                traer_maquinas_WebService();
                return true;
            }else{
                for (int i=0; i<=10; i++){
                    if(maquinaria == null){
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
                    Toast.makeText(getContext(), "Cargando datos", Toast.LENGTH_SHORT).show();
                }
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




}
