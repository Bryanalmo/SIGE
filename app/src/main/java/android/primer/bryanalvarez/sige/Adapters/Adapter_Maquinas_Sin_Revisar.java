package android.primer.bryanalvarez.sige.Adapters;

import android.content.Context;
import android.primer.bryanalvarez.sige.Models.Maquina;
import android.primer.bryanalvarez.sige.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nayar on 18/08/2018.
 */

public class Adapter_Maquinas_Sin_Revisar extends BaseAdapter{

    private Context context;
    private List<Maquina> list;
    private int layout;

    public Adapter_Maquinas_Sin_Revisar(Context context, List<Maquina> list, int layout) {
        this.context = context;
        this.list = list;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout, null);
            vh = new ViewHolder();
            vh.serial = (TextView) convertView.findViewById(R.id.tv_list_view_serial_revision);
            vh.estado = (TextView) convertView.findViewById(R.id.tv_list_view_estado_maq_revision);
            vh.marca_modelo= (TextView) convertView.findViewById(R.id.tv_list_view_modelo_maq_revision);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        Maquina maquina = list.get(position);
        vh.serial.setText(maquina.getSerial());
        vh.estado.setText(maquina.getEstado_maquina());
        vh.marca_modelo.setText(maquina.getMarca() + " - " + maquina.getModelo_maquina());


        return convertView;
    }

    public class ViewHolder {
        TextView serial;
        TextView estado;
        TextView marca_modelo;
    }
}
