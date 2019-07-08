package android.primer.bryanalvarez.sige.Adapters;

import android.content.Context;
import android.primer.bryanalvarez.sige.Models.Maquina;
import android.primer.bryanalvarez.sige.R;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nayar on 17/07/2018.
 */

public class Adapter_Maquina extends BaseAdapter {

    private Context context;
    private List<Maquina> list;
    private int layout;

    public Adapter_Maquina(Context context, List<Maquina> list, int layout) {
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
            vh.serial = (TextView) convertView.findViewById(R.id.tv_list_view_serial);
            vh.cliente = (TextView) convertView.findViewById(R.id.tv_list_view_cliente);
            vh.marca_modelo= (TextView) convertView.findViewById(R.id.tv_list_view_modelo_marca);
            vh.horometro= (TextView) convertView.findViewById(R.id.tv_list_view_horometro);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        Maquina maquina = list.get(position);
        vh.serial.setText(maquina.getSerial());
        vh.cliente.setText(maquina.getCliente());
        vh.marca_modelo.setText(maquina.getMarca() + " - " + maquina.getModelo_maquina());
        vh.horometro.setText(maquina.getHoras());


        return convertView;
    }

    public class ViewHolder {
        TextView serial;
        TextView cliente;
        TextView marca_modelo;
        TextView horometro;
    }


}
