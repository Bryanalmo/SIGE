package android.primer.bryanalvarez.sige.Activities;

import android.primer.bryanalvarez.sige.Models.Servicio_Tecnico;
import android.primer.bryanalvarez.sige.Util.Util;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.primer.bryanalvarez.sige.R;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VerServicioTecnicoActivity extends AppCompatActivity {

    private TextView tv_dialog_Id_st;
    private TextView tv_dialog_Cliente_st;
    private TextView tv_dialog_Serial_maquina_st;
    private TextView tv_dialog_tipo_falla_st;
    private TextView tv_dialog_Fecha_programacion_st;
    private TextView tv_dialog_Fecha_hora_inicio_st;
    private TextView tv_dialog_Fecha_hora_fin_st;
    private TextView tv_dialog_Horometro_st;
    private TextView tv_dialog_Repuestos_utilizados_st;
    private TextView tv_dialog_Nota_salida;
    private TextView tv_dialog_Descripcion_corta_st;
    private TextView tv_dialog_Descripcion_st;
    private TextView tv_dialog_Descripcion_cierre_st;
    private TextView tv_dialog_Novedad_st;
    private TextView tv_dialog_Empleado_st;
    private TextView tv_dialog_estado_servicio_tecnico;
    private TextView tv_dialog_tipo_servicio_tecnico;
    private TextView tv_dialog_Persona_cargo_st;
    private Servicio_Tecnico servicio_tecnico;
    private LinearLayout linearLayout1;
    private LinearLayout linearLayout2;
    private LinearLayout linearLayout3;
    private LinearLayout linearLayout4;
    private LinearLayout linearLayout5;
    private LinearLayout linearLayout6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_servicio_tecnico);
        
        bindUI();

        servicio_tecnico = Util.getServicio_tecnico();

        if(servicio_tecnico.getId_tipo_servicio_tecnico().equals("Visita periodica") || servicio_tecnico.getId_tipo_servicio_tecnico().equals("Rectificado")){
            linearLayout1.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.GONE);
            linearLayout3.setVisibility(View.GONE);
            linearLayout4.setVisibility(View.GONE);
            linearLayout5.setVisibility(View.GONE);
            linearLayout6.setVisibility(View.GONE);

        }

        tv_dialog_Id_st.setText(servicio_tecnico.getId());
        tv_dialog_Cliente_st.setText(servicio_tecnico.getCliente());
        tv_dialog_Serial_maquina_st.setText(servicio_tecnico.getSerial());
        tv_dialog_tipo_falla_st.setText(servicio_tecnico.getId_tipo_falla());
        tv_dialog_Fecha_programacion_st.setText(servicio_tecnico.getFecha_programacion());
        tv_dialog_Fecha_hora_inicio_st.setText(servicio_tecnico.getFecha_hora_inicio());
        tv_dialog_Fecha_hora_fin_st.setText(servicio_tecnico.getFecha_hora_fin());
        tv_dialog_Horometro_st.setText(servicio_tecnico.getHorometro());
        tv_dialog_Repuestos_utilizados_st.setText(servicio_tecnico.getRepuestos_utilizados());
        tv_dialog_Nota_salida.setText(servicio_tecnico.getId_nota_salida());
        tv_dialog_Descripcion_corta_st.setText(servicio_tecnico.getDescripcion_corta());
        tv_dialog_Descripcion_st.setText(servicio_tecnico.getDescripcion());
        tv_dialog_Descripcion_cierre_st.setText(servicio_tecnico.getDescripcion_cierre());
        tv_dialog_Novedad_st.setText(servicio_tecnico.getId_novedad());
        tv_dialog_Empleado_st.setText(servicio_tecnico.getId_empleado());
        tv_dialog_estado_servicio_tecnico.setText(servicio_tecnico.getId_estado_servicio_tecnico());
        tv_dialog_tipo_servicio_tecnico.setText(servicio_tecnico.getId_tipo_servicio_tecnico());
        tv_dialog_Persona_cargo_st.setText(servicio_tecnico.getPersona_cargo());

    }

    private void bindUI() {

        tv_dialog_Id_st = (TextView) findViewById(R.id.tv_dialog_Id_st);
        tv_dialog_Cliente_st = (TextView) findViewById(R.id.tv_dialog_Cliente_st);
        tv_dialog_Serial_maquina_st = (TextView) findViewById(R.id.tv_dialog_Serial_maquina_st);
        tv_dialog_tipo_falla_st = (TextView) findViewById(R.id.tv_dialog_tipo_falla_st);
        tv_dialog_Fecha_programacion_st = (TextView) findViewById(R.id.tv_dialog_Fecha_programacion_st);
        tv_dialog_Fecha_hora_inicio_st = (TextView) findViewById(R.id.tv_dialog_Fecha_hora_inicio_st);
        tv_dialog_Fecha_hora_fin_st = (TextView) findViewById(R.id.tv_dialog_Fecha_hora_fin_st);
        tv_dialog_Horometro_st = (TextView) findViewById(R.id.tv_dialog_Horometro_st);
        tv_dialog_Repuestos_utilizados_st = (TextView) findViewById(R.id.tv_dialog_Repuestos_utilizados_st);
        tv_dialog_Nota_salida = (TextView) findViewById(R.id.tv_dialog_Nota_salida);
        tv_dialog_Descripcion_corta_st = (TextView) findViewById(R.id.tv_dialog_Descripcion_corta_st);
        tv_dialog_Descripcion_st = (TextView) findViewById(R.id.tv_dialog_Descripcion_st);
        tv_dialog_Descripcion_cierre_st = (TextView) findViewById(R.id.tv_dialog_Descripcion_cierre_st);
        tv_dialog_Novedad_st = (TextView) findViewById(R.id.tv_dialog_Novedad_st);
        tv_dialog_Empleado_st = (TextView) findViewById(R.id.tv_dialog_Empleado_st);
        tv_dialog_estado_servicio_tecnico = (TextView) findViewById(R.id.tv_dialog_estado_servicio_tecnico);
        tv_dialog_tipo_servicio_tecnico = (TextView) findViewById(R.id.tv_dialog_tipo_servicio_tecnico);
        tv_dialog_Persona_cargo_st = (TextView) findViewById(R.id.tv_dialog_Persona_cargo_st);
        linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
        linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout2);
        linearLayout3 = (LinearLayout) findViewById(R.id.linearLayout3);
        linearLayout4 = (LinearLayout) findViewById(R.id.linearLayout4);
        linearLayout5 = (LinearLayout) findViewById(R.id.linearLayout5);
        linearLayout6 = (LinearLayout) findViewById(R.id.linearLayout6);
    }
}
