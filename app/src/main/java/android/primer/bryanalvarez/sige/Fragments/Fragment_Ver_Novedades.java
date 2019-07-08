package android.primer.bryanalvarez.sige.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.primer.bryanalvarez.sige.Activities.VerNovedadActivity;
import android.primer.bryanalvarez.sige.Adapters.Adapter_Novedad;
import android.primer.bryanalvarez.sige.Models.Novedad;
import android.primer.bryanalvarez.sige.Models.Object;
import android.primer.bryanalvarez.sige.R;
import android.primer.bryanalvarez.sige.Util.Util;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Ver_Novedades extends Fragment{

    private RecyclerView recyclerView;
    private Adapter_Novedad adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Novedad> novedades = new ArrayList<>();
    private DividerItemDecoration itemDecoration;
    private ProgressBar progressBar;
    private EditText et_buscar_ClienteFiltro;
    private Spinner spinnerClienteFiltro;

    private TextWatcher textWatcher;

    private RequestQueue request;
    private StringRequest stringRequest;
    private static ConnectivityManager manager;
    private AlertDialog alertDialog;
    private boolean novedadesCargadas = false;
    private boolean datosCargados = false;

    public Fragment_Ver_Novedades() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_fragment__ver__novedades, container, false);

        if (isOnline(getContext())){
            alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setMessage("Cargando datos...");
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }


        request = Volley.newRequestQueue(getContext());

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        et_buscar_ClienteFiltro = (EditText) view.findViewById(R.id.et_buscar_ClienteFiltro);
        spinnerClienteFiltro = (Spinner) view.findViewById(R.id.spinnerClienteFiltro);

        traer_datos_WebService("Cliente", "Nombre_completo", " WHERE Id <> 0 ORDER BY Nombre_completo ASC", false);

        spinnerClienteFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                traer_novedades_WebService();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String buscar = et_buscar_ClienteFiltro.getText().toString();
                traer_datos_WebService("Cliente", "Nombre_completo", " WHERE Id <> 0 AND Nombre_completo like '"+buscar+"%' OR Nombre_completo like '%"+buscar+"%' ORDER BY Nombre_completo ASC", false);
                //spinnerClienteFiltro.performClick();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        et_buscar_ClienteFiltro.addTextChangedListener(textWatcher);


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewNovedades);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mLayoutManager = new LinearLayoutManager(getContext());
        itemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(itemDecoration);
        // Inflate the layout for this fragment
        return view;
    }


    private void traer_novedades_WebService(){

        String url = "https://sige.golfyturf.com/AppWebServices/getNovedades.php";
        url = url.replace(" ", "%20");

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = null;
                Novedad novedad= null;
                ArrayList<Novedad> novedadesWS= new ArrayList<>();

                String id;
                String Cliente;
                String Serial;
                String Fecha;
                String Descripcion_corta;
                String Descripción_novedad_inicial;
                String Descripción_novedad_cierre;
                String Tipo_novedad;
                String Estado_novedad;
                String Estado_maquina;
                String Horas_trabajo_maquina;
                String Foto_novedad;

                try {
                    jsonArray = new JSONArray(response);
                    for (int i=0; i<jsonArray.length(); i++ ){

                        id = jsonArray.getJSONObject(i).getString("Id");
                        Cliente = jsonArray.getJSONObject(i).getString("Nombre_completo");
                        Serial = jsonArray.getJSONObject(i).getString("Serial");
                        Fecha = jsonArray.getJSONObject(i).getString("Fecha");
                        Descripcion_corta = jsonArray.getJSONObject(i).getString("Descripcion_corta");
                        Descripción_novedad_inicial = jsonArray.getJSONObject(i).getString("Descripcion_inicial");
                        Descripción_novedad_cierre = jsonArray.getJSONObject(i).getString("Descripcion_cierre");
                        Tipo_novedad = jsonArray.getJSONObject(i).getString("Tipo_novedad");
                        Estado_novedad = jsonArray.getJSONObject(i).getString("Estado_novedad");
                        Estado_maquina = jsonArray.getJSONObject(i).getString("Estado");
                        Horas_trabajo_maquina = jsonArray.getJSONObject(i).getString("Horas_trabajo_maquina");
                        Foto_novedad = jsonArray.getJSONObject(i).getString("Imagen");

                        novedad = new Novedad();
                        novedad.setId(id);
                        novedad.setCliente(Cliente);
                        novedad.setSerial(Serial);
                        novedad.setFecha(Fecha);
                        novedad.setDescripcion_corta(Descripcion_corta);
                        novedad.setDescripción_novedad_inicial(Descripción_novedad_inicial);
                        novedad.setDescripción_novedad_cierre(Descripción_novedad_cierre);
                        novedad.setTipo_novedad(Tipo_novedad);
                        novedad.setEstado_novedad(Estado_novedad);
                        novedad.setEstado_maquina(Estado_maquina);
                        novedad.setHoras_trabajo_maquina(Horas_trabajo_maquina);
                        novedad.setFoto_novedad(Foto_novedad);


                        novedadesWS.add(novedad);
                    }
                        adapter = new Adapter_Novedad(getContext().getApplicationContext(), novedadesWS, R.layout.list_view_novedad_item, new Adapter_Novedad.OnItemClickListener() {
                        @Override
                        public void onItemClick(Novedad novedad, int position) {
                            Intent intent = new Intent(getContext(), VerNovedadActivity.class);
                            Util.setNovedad(novedad);
                            startActivity(intent);
                        }
                    }, new Adapter_Novedad.OnButtonEditarClickListener() {
                        @Override
                        public void onButtonEditarClick(Novedad novedad, int position) {
                            showAlertForEditingBoard("Actualizar estado","Ingrese la descripcion de cierre de la novedad",novedad);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                    novedades=novedadesWS;
                    novedadesCargadas = true;
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
                Map<String,String> parametros = new HashMap<>();
                parametros.put("idCliente", idCliente);
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
                        AsyncTaskTraerNovedades at = new AsyncTaskTraerNovedades(mostrarMensaje);
                        at.execute();
                    }
                    datosCargados = true;
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

                Map<String,String> parametros = new HashMap<>();
                parametros.put("table", entidad);
                parametros.put("orden", orden);
                return parametros;
            }
        };
        request.add(stringRequest);
    }

    private void hideAlertDialog() {
        if (datosCargados && novedadesCargadas){
            alertDialog.hide();
        }
    }

    private void showAlertForEditingBoard(String title, String message, final Novedad novedad) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        if (title != null) builder.setTitle(title);
        if (message != null) builder.setMessage(message + "\n" + "Esta editando la novedad con identificador: " + novedad.getId());

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_novedad, null);
        builder.setView(viewInflated);

        final EditText descripcion_cierre = (EditText) viewInflated.findViewById(R.id.et_descripcion_cierre);

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.setMessage("Enviando datos...");
                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
                novedad.setDescripción_novedad_cierre(descripcion_cierre.getText().toString());
                novedad.setEstado_novedad("Cerrado");
                adapter.notifyDataSetChanged();
                actualizar_novedad_WebService(descripcion_cierre.getText().toString(), novedad.getId());
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void actualizar_novedad_WebService(final String descripcion, final String id){

        String url = "https://sige.golfyturf.com/AppWebServices/cerrarNovedad.php";
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
                        Toast.makeText(getContext(), "Error al intentar actualizar", Toast.LENGTH_LONG).show();
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
                parametros.put("Id", id);
                parametros.put("Descripcion_cierre", descripcion);
                parametros.put("Id_empleado", Util.getId_usuario());
                return parametros;
            }
        };
        request.add(stringRequest);
    }

    private class AsyncTaskTraerNovedades extends AsyncTask<Void, Integer, Boolean>{

        private Boolean mostrarMensaje;

        public AsyncTaskTraerNovedades(Boolean mostrarMensaje) {
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

                traer_novedades_WebService();
                return true;
            }else{
                for (int i=0; i<=10; i++){
                    if(novedades == null){
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

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }


}
