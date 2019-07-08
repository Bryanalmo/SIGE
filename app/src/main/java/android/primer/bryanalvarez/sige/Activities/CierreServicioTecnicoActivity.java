package android.primer.bryanalvarez.sige.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.primer.bryanalvarez.sige.R;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

public class CierreServicioTecnicoActivity extends AppCompatActivity {

    private TextView tv_id_servicio_tecnico;
    private EditText et_fecha_fin_dialog;
    private EditText et_hora_fin_dialog;
    private ImageButton ib_obtener_fecha_fin_dialog;
    private ImageButton ib_obtener_hora_fin_dialog;
    private MultiAutoCompleteTextView mtv_referencias;
    private Button button_cierre_st;

    private ArrayList<String> referencias;

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

    private static String[] Brands = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cierre_servicio_tecnico);

        bindUI();

        referencias = new ArrayList<>();
        referencias.add("Ref 1");
        referencias.add("Otr 1");
        referencias.add("Ref 2");
        referencias.add("Et-12");
        referencias.add("Et-123");

        Brands = new String[] {
                "Ref", "Et-1", "Otr"};


        ArrayAdapter subtestAdapter = new ArrayAdapter(this, R.layout.my_layout_file, Brands);

        mtv_referencias.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        mtv_referencias.setAdapter(subtestAdapter);
        mtv_referencias.setThreshold(1);
        mtv_referencias.setOnItemClickListener(onItemClickListener);

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
        button_cierre_st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = mtv_referencias.getText().toString();
                String delimiter = ", ";
                String[] temp;
                temp = s.split(delimiter);
                for (int i=0; i<temp.length; i++){
                    Toast.makeText(CierreServicioTecnicoActivity.this,"Item: " + temp[i] ,Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Toast.makeText(CierreServicioTecnicoActivity.this,
                            "Clicked item from auto completion list "
                                    + adapterView.getItemAtPosition(i)
                            , Toast.LENGTH_SHORT).show();
                }
            };

    private void bindUI() {
        tv_id_servicio_tecnico = (TextView) findViewById(R.id.tv_id_servicio_tecnico);
        et_fecha_fin_dialog = (EditText) findViewById(R.id.et_fecha_fin_dialog);
        et_hora_fin_dialog = (EditText) findViewById(R.id.et_hora_fin_dialog);
        ib_obtener_fecha_fin_dialog = (ImageButton) findViewById(R.id.ib_obtener_fecha_fin_dialog);
        ib_obtener_hora_fin_dialog = (ImageButton) findViewById(R.id.ib_obtener_hora_fin_dialog);
        mtv_referencias = (MultiAutoCompleteTextView) findViewById(R.id.mtv_referencias);
        button_cierre_st = (Button) findViewById(R.id.button_cierre_st);

    }

    private void obtenerFecha(final EditText editText) {
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
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


}
