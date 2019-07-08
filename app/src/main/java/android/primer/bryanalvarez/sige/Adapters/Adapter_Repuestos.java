package android.primer.bryanalvarez.sige.Adapters;

import android.content.Context;
import android.primer.bryanalvarez.sige.Models.Repuesto;
import android.primer.bryanalvarez.sige.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by nayar on 27/08/2018.
 */

public class Adapter_Repuestos extends BaseAdapter {

    private Context context;
    private List<Repuesto> list;
    private int layout;

    public Adapter_Repuestos(Context context, List<Repuesto> list, int layout) {
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
        final ViewHolder vh;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout, null);
            vh = new ViewHolder();
            vh.checkBoxRepuesto = (TextView) convertView.findViewById(R.id.checkBoxRepuesto);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        final Repuesto repuesto = list.get(position);
        vh.checkBoxRepuesto.setText(repuesto.getReferencia() + " - " + repuesto.getDescripcion());

        vh.checkBoxRepuesto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                repuesto.setChecked(cb.isChecked());
            }
        });


        return convertView;
    }

    public class ViewHolder {
        TextView checkBoxRepuesto;
    }
}
