package android.primer.bryanalvarez.sige.Fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.primer.bryanalvarez.sige.Adapters.Adapter_Maquinas_Sin_Revisar;
import android.primer.bryanalvarez.sige.Adapters.Adapter_Rectificado;
import android.primer.bryanalvarez.sige.Adapters.Adapter_Repuestos;
import android.primer.bryanalvarez.sige.Adapters.Adapter_Servicio_Tecnico;
import android.primer.bryanalvarez.sige.Adapters.TestPagerAdapter;
import android.primer.bryanalvarez.sige.Models.Maquina;
import android.primer.bryanalvarez.sige.Models.Maquina_Rectificado;
import android.primer.bryanalvarez.sige.Models.Novedad;
import android.primer.bryanalvarez.sige.Models.Object;
import android.primer.bryanalvarez.sige.Models.PagerModel;
import android.primer.bryanalvarez.sige.Models.Repuesto;
import android.primer.bryanalvarez.sige.Models.Servicio_Tecnico;
import android.primer.bryanalvarez.sige.Util.Util;
import android.support.annotation.IdRes;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.primer.bryanalvarez.sige.R;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
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
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Revision_Maquina extends Fragment implements AdapterView.OnItemClickListener {

    private ListView listView;
    private Adapter_Maquinas_Sin_Revisar adapter;
    private Adapter_Servicio_Tecnico adapter_servicio_tecnico;

    private ArrayList<Maquina> maquinas_sin_revisar = new ArrayList<>();;
    private ProgressBar progressBar;

    private TextView tv_id_servicio_tecnico_revision;
    private TextView tv_fecha_revision;
    private TextView tv_cliente_revision;
    private ScrollView layoutVisitaExistente;
    private LinearLayout layoutNoVisita;

    private AlertDialog alertDialog;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Servicio_Tecnico> servicios_tecnicos = new ArrayList<>();
    private DividerItemDecoration itemDecoration;

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

    RequestQueue request;
    StringRequest stringRequest;
    String repuestos_seleccionados;
    String Id_cliente="";
    String Id_servicio="";
    int itemHeight;

    public Fragment_Revision_Maquina() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_fragment__revision__maquina, container, false);

        alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setMessage("Enviando datos...");
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        request = Volley.newRequestQueue(getContext());

        layoutVisitaExistente = (ScrollView) view.findViewById(R.id.layoutVisitaExistente);
        layoutNoVisita = (LinearLayout) view.findViewById(R.id.layoutNoVisita);
        tv_id_servicio_tecnico_revision = (TextView) view.findViewById(R.id.tv_id_servicio_tecnico_revision);
        tv_fecha_revision = (TextView) view.findViewById(R.id.tv_fecha_revision);
        tv_cliente_revision = (TextView) view.findViewById(R.id.tv_cliente_revision);

        AsyncTaskTraerMaquinasSinRevisar at = new AsyncTaskTraerMaquinasSinRevisar();
        at.execute();

        listView = (ListView) view.findViewById(R.id.listViewMaquinasSinRevisar);
        listView.setOnItemClickListener(this);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        layoutVisitaExistente.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                view.findViewById(R.id.listViewMaquinasSinRevisar).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                view.findViewById(R.id.recyclerViewServiciosTecnicosRelacionados).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        registerForContextMenu(listView);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewServiciosTecnicosRelacionados);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        mLayoutManager = new LinearLayoutManager(getContext());
        itemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(itemDecoration);


        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.listViewMaquinasSinRevisar:
                //Toast.makeText(getContext(), "si", Toast.LENGTH_LONG).show();
                showAlertForEditingRevision("Revisar maquina","Ingrese los datos",maquinas_sin_revisar.get(position), position);
                break;
            case R.id.listViewRepuestosMaquina:

                break;
        }

    }

    private void showAlertForEditingRevision(String title, String message, final Maquina maquina, final int position){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        if (title != null) builder.setTitle(title);
        if (message != null) builder.setMessage(message + "\n" + "Esta revisando la maquina con serial: " + maquina.getSerial()
                                                + "\n" + "B: Bueno, R: Regular, M: Malo,              N/A: No aplica");
        repuestos_seleccionados = "";

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_revision_maquina, null);
        builder.setView(viewInflated);

        final RadioButton rb_bueno_PL = (RadioButton) viewInflated.findViewById(R.id.rb_bueno_PL);
        final RadioButton rb_regular_PL = (RadioButton) viewInflated.findViewById(R.id.rb_regular_PL);
        final RadioButton rb_malo_PL = (RadioButton) viewInflated.findViewById(R.id.rb_malo_PL);
        final RadioButton rb_na_PL = (RadioButton) viewInflated.findViewById(R.id.rb_na_PL);
        final RadioButton rb_bueno_NA = (RadioButton) viewInflated.findViewById(R.id.rb_bueno_NA);
        final RadioButton rb_regular_NA = (RadioButton) viewInflated.findViewById(R.id.rb_regular_NA);
        final RadioButton rb_malo_NA = (RadioButton) viewInflated.findViewById(R.id.rb_malo_NA);
        final RadioButton rb_na_NA = (RadioButton) viewInflated.findViewById(R.id.rb_na_NA);
        final RadioButton rb_bueno_NH = (RadioButton) viewInflated.findViewById(R.id.rb_bueno_NH);
        final RadioButton rb_regular_NH = (RadioButton) viewInflated.findViewById(R.id.rb_regular_NH);
        final RadioButton rb_malo_NH = (RadioButton) viewInflated.findViewById(R.id.rb_malo_NH);
        final RadioButton rb_na_NH = (RadioButton) viewInflated.findViewById(R.id.rb_na_NH);
        final RadioButton rb_bueno_AC = (RadioButton) viewInflated.findViewById(R.id.rb_bueno_AC);
        final RadioButton rb_regular_AC = (RadioButton) viewInflated.findViewById(R.id.rb_regular_AC);
        final RadioButton rb_malo_AC = (RadioButton) viewInflated.findViewById(R.id.rb_malo_AC);
        final RadioButton rb_na_AC = (RadioButton) viewInflated.findViewById(R.id.rb_na_AC);
        final RadioButton rb_bueno_UC = (RadioButton) viewInflated.findViewById(R.id.rb_bueno_UC);
        final RadioButton rb_regular_UC = (RadioButton) viewInflated.findViewById(R.id.rb_regular_UC);
        final RadioButton rb_malo_UC = (RadioButton) viewInflated.findViewById(R.id.rb_malo_UC);
        final RadioButton rb_na_UC = (RadioButton) viewInflated.findViewById(R.id.rb_na_UC);
        final RadioButton rb_bueno_NR = (RadioButton) viewInflated.findViewById(R.id.rb_bueno_NR);
        final RadioButton rb_regular_NR = (RadioButton) viewInflated.findViewById(R.id.rb_regular_NR);
        final RadioButton rb_malo_NR = (RadioButton) viewInflated.findViewById(R.id.rb_malo_NR);
        final RadioButton rb_na_NR = (RadioButton) viewInflated.findViewById(R.id.rb_na_NR);
        final RadioButton rb_bueno_CO = (RadioButton) viewInflated.findViewById(R.id.rb_bueno_CO);
        final RadioButton rb_regular_CO = (RadioButton) viewInflated.findViewById(R.id.rb_regular_CO);
        final RadioButton rb_malo_CO = (RadioButton) viewInflated.findViewById(R.id.rb_malo_CO);
        final RadioButton rb_na_CO = (RadioButton) viewInflated.findViewById(R.id.rb_na_CO);
        final RadioButton rb_bueno_EC = (RadioButton) viewInflated.findViewById(R.id.rb_bueno_EC);
        final RadioButton rb_regular_EC = (RadioButton) viewInflated.findViewById(R.id.rb_regular_EC);
        final RadioButton rb_malo_EC = (RadioButton) viewInflated.findViewById(R.id.rb_malo_EC);
        final RadioButton rb_na_EC = (RadioButton) viewInflated.findViewById(R.id.rb_na_EC);
        final EditText et_horometro_actual_revision = (EditText) viewInflated.findViewById(R.id.et_horometro_actual_revision);
        final EditText et_comentario_fuga_revision = (EditText) viewInflated.findViewById(R.id.et_comentario_fuga_revision);
        final EditText et_comentario_final_revision = (EditText) viewInflated.findViewById(R.id.et_comentario_final_revision);
        final Switch switchFugas = (Switch) viewInflated.findViewById(R.id.switchFugas);
        final Button btn_guardar_revision_maquina = (Button) viewInflated.findViewById(R.id.btn_guardar_revision_maquina);
        final ImageButton btn_agregar_repuestos = (ImageButton) viewInflated.findViewById(R.id.btn_agregar_repuestos);

        switchFugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchFugas.isChecked()){
                    et_comentario_fuga_revision.setVisibility(View.VISIBLE);
                }else {
                    et_comentario_fuga_revision.setVisibility(View.GONE);
                    et_comentario_fuga_revision.setText("Sin fugas");
                }
            }
        });

        btn_agregar_repuestos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertForAddRepuestos("Agregar repuestos","Seleccione los repuestos que desea recomendar al cliente",maquinas_sin_revisar.get(position), position);
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();

        btn_guardar_revision_maquina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String seleccionPuntosLubricacion = seleccionRadioButton(rb_bueno_PL,rb_regular_PL,rb_malo_PL,rb_na_PL);
                final String seleccionNivelAceite= seleccionRadioButton(rb_bueno_NA,rb_regular_NA,rb_malo_NA,rb_na_NA);
                final String seleccionNivelHidraulico = seleccionRadioButton(rb_bueno_NH,rb_regular_NH,rb_malo_NH, rb_na_NH);
                final String seleccionAceiteCaja = seleccionRadioButton(rb_bueno_AC,rb_regular_AC,rb_malo_AC,rb_na_AC);
                final String seleccionUnidadesCorte = seleccionRadioButton(rb_bueno_UC,rb_regular_UC,rb_malo_UC,rb_na_UC);
                final String seleccionNivelRefrigerante = seleccionRadioButton(rb_bueno_NR,rb_regular_NR,rb_malo_NR,rb_na_NR);
                final String seleccionCorreas = seleccionRadioButton(rb_bueno_CO,rb_regular_CO,rb_malo_CO,rb_na_CO);
                final String seleccionEstructurayChasis = seleccionRadioButton(rb_bueno_EC,rb_regular_EC,rb_malo_EC,rb_na_EC);
                final String horometroRevision = et_horometro_actual_revision.getText().toString();
                final String comentarioFugas = et_comentario_fuga_revision.getText().toString();
                final String comentarioFinal = et_comentario_final_revision.getText().toString();
                final String seleccionFuga = (switchFugas.isChecked()) ? "1":"0";
                final String repuestos_recomendados = repuestos_seleccionados;

                alertDialog.show();
                crear_revision_maquina_WebService(maquina.getId(), seleccionPuntosLubricacion, seleccionNivelAceite, seleccionNivelHidraulico,seleccionAceiteCaja,seleccionUnidadesCorte,horometroRevision,
                        seleccionNivelRefrigerante,seleccionCorreas,seleccionEstructurayChasis, comentarioFugas, seleccionFuga, comentarioFinal, repuestos_recomendados);
                maquinas_sin_revisar.remove(position);
                adapter.notifyDataSetChanged();

                dialog.dismiss();
            }
        });

    }

    private void showAlertForAddRepuestos(String title, String message, final Maquina maquina, final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        if (title != null) builder.setTitle(title);
        if (message != null) builder.setMessage(message);
        repuestos_seleccionados = "";

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_agregar_repuestos, null);
        builder.setView(viewInflated);

        final ArrayList<Repuesto> repuestos = maquina.getRepuestos_modelo_maquina();
        final ArrayList<Repuesto> repuestosSelec = new ArrayList<>();
        final Adapter_Repuestos adapter_repuestos = new Adapter_Repuestos(getContext().getApplicationContext(), repuestos, R.layout.list_view_repuestos_item);
        final ListView listViewRepuestosMaquina = (ListView) viewInflated.findViewById(R.id.listViewRepuestosMaquina);
        listViewRepuestosMaquina.setOnItemClickListener(this);

        registerForContextMenu(listViewRepuestosMaquina);
        listViewRepuestosMaquina.setAdapter(adapter_repuestos);
        builder.setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(int j=0; j<repuestos.size(); j++){
                    if(repuestos.get(j).isChecked()){
                        repuestosSelec.add(repuestos.get(j));
                    }
                }
                String rep_selec="";
                for (int i=0; i<repuestosSelec.size(); i++){
                    rep_selec += repuestosSelec.get(i).getReferencia()+ ",";
                }
                Toast.makeText(getContext(),repuestosSelec.size() + " repuestos añadidos a recomendados",Toast.LENGTH_LONG).show();
                repuestos_seleccionados = rep_selec;

            }
        });

        repuestosSelec.clear();
        final AlertDialog dialog = builder.create();
        dialog.show();
    }



    private String seleccionRadioButton(RadioButton r1, RadioButton r2, RadioButton r3, RadioButton r4){
        if(r1.isChecked()){
            return "1";
        }else if(r2.isChecked()){
            return "2";
        } else if(r3.isChecked()){
            return "3";
        }else if(r4.isChecked()){
            return "4";
        }{
            return "4";
        }
    }

    private void crear_revision_maquina_WebService(final String id_maquina, final String puntos_lubricacion, final String nivel_aceite, final String nivel_hidraulico, final String aceite_caja, final String unidades_corte, final String horometro,
                                                    final String nivel_refrigerante,final String correas,final String estructura_chasis,final String comentario_fuga,final String seleccion_fuga, final String comentario_final, final String repuestos){

        String url = "https://sige.golfyturf.com/AppWebServices/crearRevisionMaquina.php";
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
                        repuestos_seleccionados = "";
                    }else{
                        Toast.makeText(getContext(), "Error al registrar la revision", Toast.LENGTH_LONG).show();
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
                parametros.put("Id_maquina",id_maquina);
                parametros.put("Id_servicio_tecnico",tv_id_servicio_tecnico_revision.getText().toString());
                parametros.put("Punto_lubricacion",puntos_lubricacion);
                parametros.put("Nivel_aceite",nivel_aceite);
                parametros.put("Nivel_hidraulico",nivel_hidraulico);
                parametros.put("Aceite_caja",aceite_caja);
                parametros.put("Unidades_corte",unidades_corte);
                parametros.put("Nivel_tipo_refrigerante",nivel_refrigerante);
                parametros.put("Correas",correas);
                parametros.put("Estructura_chasis",estructura_chasis);
                parametros.put("Existencia_fugas",seleccion_fuga);
                parametros.put("Comentario_fuga",comentario_fuga);
                parametros.put("Horometro",horometro);
                parametros.put("Comentario_final",comentario_final);
                parametros.put("Repuestos_recomendados",repuestos);


                return parametros;
            }
        };
        request.add(stringRequest);
    }

    private void traer_maquinas_sin_revisar_WebService(){

        String url = "https://sige.golfyturf.com/AppWebServices/getMaquinasSinRevisar.php";
        url = url.replace(" ", "%20");

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = null;
                Maquina maquina= null;
                ArrayList<Maquina> maquinas = new ArrayList<>();
                JSONArray jsonArrayMaquinasSinUsar = null;


                String Id_maquina;
                //String Id_cliente;
                String Serial;
                String Marca;
                String Modelo_maquina;
                String Estado_maquina;
                String Id_servicio_tecnico = "";
                String Fecha_programacion= "";
                String Cliente= "";
                String Id_repuesto="";
                String Referencia="";
                String Descripcion="";
                Repuesto repuesto= null;
                try {
                    jsonArray = new JSONArray(response);

                    for (int i=0; i<jsonArray.length(); i++ ){
                        if(jsonArray.getJSONObject(i).getString("Success").equals("true")){
                            layoutNoVisita.setVisibility(View.GONE);
                            layoutVisitaExistente.setVisibility(View.VISIBLE);
                            jsonArrayMaquinasSinUsar = jsonArray.getJSONObject(i).getJSONArray("Maquinas sin revisar");
                            for (int j=0; j<jsonArrayMaquinasSinUsar.length(); j++) {

                                Id_maquina = jsonArrayMaquinasSinUsar.getJSONObject(j).getString("Id");
                                Serial = jsonArrayMaquinasSinUsar.getJSONObject(j).getString("Serial");
                                Marca = jsonArrayMaquinasSinUsar.getJSONObject(j).getString("Marca");
                                Modelo_maquina = jsonArrayMaquinasSinUsar.getJSONObject(j).getString("Modelo_maquina");
                                Estado_maquina = jsonArrayMaquinasSinUsar.getJSONObject(j).getString("Estado");
                                JSONArray jsonArrayRepuestosModelo  = jsonArrayMaquinasSinUsar.getJSONObject(j).getJSONArray("Productos modelo");
                                ArrayList<Repuesto> repuestosModelo = new ArrayList<>();
                                for (int k=0; k<jsonArrayRepuestosModelo.length(); k++){
                                    Id_repuesto = jsonArrayRepuestosModelo.getJSONObject(k).getString("Id");
                                    Referencia = jsonArrayRepuestosModelo.getJSONObject(k).getString("Referencia");
                                    Descripcion = jsonArrayRepuestosModelo.getJSONObject(k).getString("Descripcion");
                                    repuesto = new Repuesto(Id_repuesto,Referencia,Descripcion);
                                    repuestosModelo.add(repuesto);
                                }

                                maquina = new Maquina(Id_maquina, Serial, Marca, Modelo_maquina, Estado_maquina, repuestosModelo);

                                maquinas_sin_revisar.add(maquina);

                            }
                            Id_servicio_tecnico = jsonArray.getJSONObject(i).getString("Id");
                            Id_cliente = jsonArray.getJSONObject(i).getString("Id_cliente");
                            Id_servicio = Id_servicio_tecnico;
                            Fecha_programacion = jsonArray.getJSONObject(i).getString("Fecha_programacion");
                            Cliente = jsonArray.getJSONObject(i).getString("Nombre_completo");
                        } else{
                            layoutNoVisita.setVisibility(View.VISIBLE);
                            layoutVisitaExistente.setVisibility(View.GONE);
                        }


                    }
                    adapter = new Adapter_Maquinas_Sin_Revisar(getContext().getApplicationContext(), maquinas_sin_revisar, R.layout.list_view_revision);
                    listView.setAdapter(adapter);
                    //maquinas_sin_revisar=maquinas;
                    tv_id_servicio_tecnico_revision.setText(Id_servicio_tecnico);
                    tv_fecha_revision.setText(Fecha_programacion);
                    tv_cliente_revision.setText(Cliente);
                    traer_servicios_tecnicos_WebService();
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
                parametros.put("Id_empleado", Util.Id_usuario);
                return parametros;
            }
        };
        request.add(stringRequest);
    }

    private class AsyncTaskTraerMaquinasSinRevisar extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setMax(100);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if(isOnline(getContext())){

                traer_maquinas_sin_revisar_WebService();
                return true;
            }else{
                for (int i=0; i<=10; i++){
                    if(maquinas_sin_revisar == null){
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

            //progressBar.setProgress(values[0].intValue());
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            if(aVoid == true){
                //progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Cargando datos", Toast.LENGTH_LONG).show();
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

    private void traer_servicios_tecnicos_WebService(){

        String url = "https://sige.golfyturf.com/AppWebServices/getServiciosRelacionadosVisita.php";
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
                    adapter_servicio_tecnico = new Adapter_Servicio_Tecnico(getContext().getApplicationContext(), servicios_tecnicosWS, R.layout.list_view_servicio_tecnico_item, new Adapter_Servicio_Tecnico.OnItemClickListener() {
                        @Override
                        public void onItemClick(Servicio_Tecnico servicio_tecnico, int position) {

                        }
                    }, new Adapter_Servicio_Tecnico.OnButtonEditarClickListener() {
                        @Override
                        public void onButtonEditarClick(Servicio_Tecnico servicio_tecnico, int position) {
                            if(servicio_tecnico.getId_estado_servicio_tecnico().equals("Programado")){
                                showAlertForEditingFirstST("Editar", "Ingrese la fecha y hora de inicio", servicio_tecnico, adapter_servicio_tecnico);
                            }else if(servicio_tecnico.getId_estado_servicio_tecnico().equals("En proceso")){
                                showAlertForCloseST("Editar", "Ingrese los datos del cierrre sel servicio",servicio_tecnico, adapter_servicio_tecnico);
                            }else{}
                        }
                    });
                    recyclerView.setAdapter(adapter_servicio_tecnico);
                    adapter_servicio_tecnico.notifyDataSetChanged();
                    recyclerView.getLayoutParams().height = 200 * adapter_servicio_tecnico.getItemCount();
                    //servicios_tecnicos=servicios_tecnicosWS;

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
                parametros.put("idCliente", Id_cliente);
                parametros.put("idServicioPadre", Id_servicio);

                return parametros;
            }
        };
        request.add(stringRequest);
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

        if(servicio_tecnico_edit.getId_tipo_servicio_tecnico().equals("Visita periodica") || servicio_tecnico_edit.getId_tipo_servicio_tecnico().equals("Rectificado") ){
            linearLayout3.setVisibility(View.GONE);
            linearLayout7.setVisibility(View.GONE);
            linearLayout8.setVisibility(View.GONE);
            linearLayout6.setVisibility(View.GONE);
            if (servicio_tecnico_edit.getId_tipo_servicio_tecnico().equals("Rectificado")){
                linearLayout10.setVisibility(View.VISIBLE);
            }
        }
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

                alertDialog.show();
                servicio_tecnico_edit.setFecha_hora_fin(fecha + " " + hora);
                servicio_tecnico_edit.setId_estado_servicio_tecnico("Cerrado");
                servicio_tecnico_edit.setDescripcion_cierre(descripcion_final);
                adapter_servicio_tecnico.notifyDataSetChanged();
                cerrar_servicio_tecnico_WebService(fecha,hora,descripcion_final, notaSalida,idEstadoMaquina,horometro,cerrarNovedad, servicio_tecnico_edit.getId(),"3", "", servicio_tecnico_edit.getId_tipo_servicio_tecnico());
                dialog.dismiss();

            }
        });
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
                        object = new Object(jsonArray.getJSONObject(i).getString("Id"), jsonArray.getJSONObject(i).getString(campo));
                        datos.add(object);
                    }
                    spinner.setAdapter(new ArrayAdapter<Object>(getContext(), android.R.layout.simple_list_item_1, datos));

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
