package android.primer.bryanalvarez.sige.Adapters;

import android.content.Context;
import android.primer.bryanalvarez.sige.Models.Maquina_Rectificado;
import android.primer.bryanalvarez.sige.Models.Object;
import android.primer.bryanalvarez.sige.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nayar on 1/10/2018.
 */

public class Adapter_Rectificado extends BaseAdapter {
    private Context context;
    private List<Maquina_Rectificado> list;
    private int layout;

    public Adapter_Rectificado(Context context, List<Maquina_Rectificado> list, int layout) {
        this.context = context;
        this.list = list;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Maquina_Rectificado getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout, null);
            vh = new ViewHolder();
            vh.checkBoxMaquina = (TextView) convertView.findViewById(R.id.checkBoxMaquina);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        final Maquina_Rectificado maquina_rectificado = list.get(position);
        vh.checkBoxMaquina.setText(maquina_rectificado.getModelo() + " - " + maquina_rectificado.getSerial());

        vh.checkBoxMaquina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                maquina_rectificado.setChecked(cb.isChecked());
            }
        });


        return convertView;
    }

    public class ViewHolder {
        TextView checkBoxMaquina;
    }

}

