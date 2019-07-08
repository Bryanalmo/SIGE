package android.primer.bryanalvarez.sige.Activities;

import android.content.Intent;
import android.net.Uri;
import android.primer.bryanalvarez.sige.Models.Maquina;
import android.primer.bryanalvarez.sige.Models.Object;
import android.primer.bryanalvarez.sige.Util.Util;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.primer.bryanalvarez.sige.R;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VerMaquinaActivity extends AppCompatActivity  implements Serializable{

    private TextView Serial;
    private TextView Placa_activo_fijo_club;
    private TextView Numero_maquina_club;
    private TextView Modelo_motor;
    private TextView Serial_motor;
    private TextView Codigo_motor;
    private TextView Serial_unidades_corte;
    private TextView Fecha_compra;
    private TextView Fecha_entrega;
    private TextView Horas;
    private TextView Operador_asignado;
    private TextView Observaciones;
    private TextView Estado_registro_fabrica;
    private TextView Estado_maquina;
    private TextView Fecha_regisro_fabrica;
    private TextView Año_produccion;
    private TextView Distribuidor_maquina;
    private TextView Ubicacion;
    private TextView Tecnico_asignado;
    private TextView Marca;
    private TextView Modelo_maquina;
    private TextView Funcion;
    private TextView Cliente;
    private Button verMaquina;
    private Button descargarPDF;
    private ImageView fotoMaquina;
    private Maquina maquina;

    RequestQueue request;
    StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_maquina);

        bindUI();
        maquina = Util.getMaquina();

        request = Volley.newRequestQueue(this);

        if (maquina.getManual_partes().equals("")){
            descargarPDF.setVisibility(View.GONE);
        }

        Serial.setText(maquina.getSerial());
        Placa_activo_fijo_club.setText(maquina.getPlaca_activo_fijo_club());
        Numero_maquina_club.setText(maquina.getNumero_maquina_club());
        Modelo_motor.setText(maquina.getModelo_motor());
        Serial_motor.setText(maquina.getSerial_motor());
        Codigo_motor.setText(maquina.getCodigo_motor());
        Serial_unidades_corte.setText(maquina.getSerial_unidades_corte());
        Fecha_compra.setText(maquina.getFecha_compra());
        Fecha_entrega.setText(maquina.getFecha_entrega());
        Horas.setText(maquina.getHoras());
        Operador_asignado.setText(maquina.getOperador_asignado());
        Observaciones.setText(maquina.getObservaciones());
        Estado_registro_fabrica.setText(maquina.getEstado_registro_fabrica());
        Estado_maquina.setText(maquina.getEstado_maquina());
        Fecha_regisro_fabrica.setText(maquina.getFecha_regisro_fabrica());
        Año_produccion.setText(maquina.getAño_produccion());
        Distribuidor_maquina.setText(maquina.getDistribuidor_maquina());
        Ubicacion.setText(maquina.getUbicacion());
        Tecnico_asignado.setText(maquina.getTecnico_asignado());
        Marca.setText(maquina.getMarca());
        Modelo_maquina.setText(maquina.getModelo_maquina());
        Funcion.setText(maquina.getFuncion());
        Cliente.setText(maquina.getCliente());

        verMaquina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(getApplicationContext()).load(maquina.getFoto()).into(fotoMaquina);
                if(fotoMaquina.getVisibility()==View.VISIBLE){
                    verMaquina.setText("Ver Foto");
                    fotoMaquina.setVisibility(View.GONE);
                }else{
                    verMaquina.setText("Ocultar foto");
                    fotoMaquina.setVisibility(View.VISIBLE);
                }
            }
        });

        descargarPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://sige.golfyturf.com/archivos/"+maquina.getManual_partes());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

    }

    public void bindUI(){
        Serial = (TextView) findViewById(R.id.tv_dialog_serial);
        Placa_activo_fijo_club = (TextView) findViewById(R.id.tv_dialog_placa);
        Numero_maquina_club = (TextView) findViewById(R.id.tv_dialog_numero_maquina_club);
        Modelo_motor = (TextView) findViewById(R.id.tv_dialog_modelo_motor);
        Serial_motor = (TextView) findViewById(R.id.tv_dialog_serial_motor);
        Codigo_motor = (TextView) findViewById(R.id.tv_dialog_codigo_motor);
        Serial_unidades_corte = (TextView) findViewById(R.id.tv_dialog_serial_uni_corte);
        Fecha_compra = (TextView) findViewById(R.id.tv_dialog_fecha_compra);
        Fecha_entrega = (TextView) findViewById(R.id.tv_dialog_fecha_entrega);
        Horas = (TextView) findViewById(R.id.tv_dialog_view_horas);
        Operador_asignado = (TextView) findViewById(R.id.tv_dialog_view_operador_asignado);
        Observaciones = (TextView) findViewById(R.id.tv_dialog_view_observaciones);
        Estado_registro_fabrica = (TextView) findViewById(R.id.tv_dialog_registro_fabrica);
        Estado_maquina = (TextView) findViewById(R.id.tv_dialog_estado_maquina);
        Fecha_regisro_fabrica = (TextView) findViewById(R.id.tv_dialog_fecha_registro);
        Año_produccion = (TextView) findViewById(R.id.tv_dialog_year_produccion);
        Distribuidor_maquina = (TextView) findViewById(R.id.tv_dialog_distribuidor);
        Ubicacion = (TextView) findViewById(R.id.tv_dialog_view_ubicacion);
        Tecnico_asignado = (TextView) findViewById(R.id.tv_dialog_tecnico_asignado);
        Marca = (TextView) findViewById(R.id.tv_dialog_marca);
        Modelo_maquina = (TextView) findViewById(R.id.tv_dialog_modelo_maquina);
        Funcion = (TextView) findViewById(R.id.tv_dialog_funcion);
        Cliente = (TextView) findViewById(R.id.tv_dialog_cliente);
        verMaquina = (Button) findViewById(R.id.btn_imagen);
        descargarPDF = (Button) findViewById(R.id.btn_pdf);
        fotoMaquina = (ImageView) findViewById(R.id.iv_ver_foto_maquina);

    }


}
