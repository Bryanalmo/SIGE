package android.primer.bryanalvarez.sige.Activities;

import android.primer.bryanalvarez.sige.Models.Novedad;
import android.primer.bryanalvarez.sige.Util.Util;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.primer.bryanalvarez.sige.R;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class VerNovedadActivity extends AppCompatActivity {

    private TextView id;
    private TextView Cliente;
    private TextView Serial;
    private TextView Fecha;
    private TextView Descripcion_corta;
    private TextView Descripción_novedad_inicial;
    private TextView Descripción_novedad_cierre;
    private TextView Tipo_novedad;
    private TextView Estado_novedad;
    private TextView Estado_maquina;
    private TextView Horas_trabajo_maquina;
    private Button verNovedad;
    private ImageView fotoNovedad;
    private Novedad novedad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_novedad);

        binUI();

        novedad = Util.getNovedad();

        id.setText(novedad.getId());
        Cliente.setText(novedad.getCliente());
        Serial.setText(novedad.getSerial());
        Fecha.setText(novedad.getFecha());
        Descripcion_corta.setText(novedad.getDescripcion_corta());
        Descripción_novedad_inicial.setText(novedad.getDescripción_novedad_inicial());
        Descripción_novedad_cierre.setText(novedad.getDescripción_novedad_cierre());
        Tipo_novedad.setText(novedad.getTipo_novedad());
        Estado_novedad.setText(novedad.getEstado_novedad());
        Estado_maquina.setText(novedad.getEstado_maquina());
        Horas_trabajo_maquina.setText(novedad.getHoras_trabajo_maquina());

        verNovedad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(getApplicationContext()).load(novedad.getFoto_novedad()).into(fotoNovedad);
                if(fotoNovedad.getVisibility()==View.VISIBLE){
                    verNovedad.setText("Ver Foto");
                    fotoNovedad.setVisibility(View.GONE);
                }else{
                    verNovedad.setText("Ocultar foto");
                    fotoNovedad.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void binUI(){
        id = (TextView) findViewById(R.id.tv_dialog_id_novedad);
        Cliente = (TextView) findViewById(R.id.tv_dialog_cliente_novedad);
        Serial = (TextView) findViewById(R.id.tv_dialog_serial_novedad);
        Fecha = (TextView) findViewById(R.id.tv_dialog_fecha_novedad);
        Descripcion_corta = (TextView) findViewById(R.id.tv_dialog_descripcion_corta_novedad);
        Descripción_novedad_inicial = (TextView) findViewById(R.id.tv_dialog_descripción_novedad_inicial);
        Descripción_novedad_cierre = (TextView) findViewById(R.id.tv_dialog_descripción_novedad_cierre);
        Tipo_novedad = (TextView) findViewById(R.id.tv_dialog_tipo_novedad);
        Estado_novedad = (TextView) findViewById(R.id.tv_dialog_estado_novedad);
        Estado_maquina = (TextView) findViewById(R.id.tv_dialog_estado_maquina_novedad);
        Horas_trabajo_maquina = (TextView) findViewById(R.id.tv_dialog_horas_trabajo_maquina_novedad);
        verNovedad = (Button) findViewById(R.id.btn_imagen_novedad);
        fotoNovedad = (ImageView) findViewById(R.id.iv_ver_foto_novedad);
    }
}
