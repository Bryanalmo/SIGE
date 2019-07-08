package android.primer.bryanalvarez.sige.Adapters;

import android.content.Context;
import android.primer.bryanalvarez.sige.Models.Servicio_Tecnico;
import android.primer.bryanalvarez.sige.R;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nayar on 19/07/2018.
 */

public class Adapter_Servicio_Tecnico extends RecyclerView.Adapter<Adapter_Servicio_Tecnico.ViewHolder>{

    private Context context;
    private List<Servicio_Tecnico> list;
    private int layout;
    private OnItemClickListener itemClickListener;
    private OnButtonEditarClickListener btnEditarClickListener;

    public Adapter_Servicio_Tecnico(Context context, List<Servicio_Tecnico> list, int layout, OnItemClickListener itemClickListener, OnButtonEditarClickListener btnEditarClickListener) {
        this.context = context;
        this.list = list;
        this.layout = layout;
        this.itemClickListener = itemClickListener;
        this.btnEditarClickListener = btnEditarClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        context = parent.getContext();
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(list.get(position),btnEditarClickListener,itemClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_list_view_cliente_st;
        public TextView tv_list_view_serial_st;
        public TextView tv_list_view_fecha_st;
        public TextView tv_list_view_estado_st;
        public ImageButton button_editar_st;
        public LinearLayout linearLayout1;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_list_view_cliente_st = (TextView) itemView.findViewById(R.id.tv_list_view_cliente_st);
            tv_list_view_serial_st = (TextView) itemView.findViewById(R.id.tv_list_view_serial_st);
            tv_list_view_fecha_st = (TextView) itemView.findViewById(R.id.tv_list_view_fecha_st);
            tv_list_view_estado_st= (TextView) itemView.findViewById(R.id.tv_list_view_estado_st);
            button_editar_st = (ImageButton) itemView.findViewById(R.id.button_editar_st);
            linearLayout1 = (LinearLayout) itemView.findViewById(R.id.linearLayout1);
        }

        public void bind(final Servicio_Tecnico servicio_tecnico, final OnButtonEditarClickListener onButtonEditarClickListener, final OnItemClickListener itemClickListener) {

            tv_list_view_cliente_st.setText(servicio_tecnico.getCliente());
            tv_list_view_serial_st.setText(servicio_tecnico.getSerial());
            tv_list_view_fecha_st.setText(servicio_tecnico.getFecha_hora_inicio());
            tv_list_view_estado_st.setText(servicio_tecnico.getId_estado_servicio_tecnico());

            if(servicio_tecnico.getId_tipo_servicio_tecnico().equals("Visita periodica") || servicio_tecnico.getId_tipo_servicio_tecnico().equals("Rectificado")){
                linearLayout1.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(servicio_tecnico, getAdapterPosition());
                }
            });

            if(servicio_tecnico.getId_estado_servicio_tecnico().equals("Cerrado")){
                button_editar_st.setEnabled(false);
            }

            button_editar_st.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonEditarClickListener.onButtonEditarClick(servicio_tecnico, getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Servicio_Tecnico servicio_tecnico, int position);
    }

    public interface OnButtonEditarClickListener {
        void onButtonEditarClick(Servicio_Tecnico servicio_tecnico, int position);
    }
}
