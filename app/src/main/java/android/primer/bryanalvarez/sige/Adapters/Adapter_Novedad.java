package android.primer.bryanalvarez.sige.Adapters;

import android.content.Context;
import android.primer.bryanalvarez.sige.Models.Novedad;
import android.primer.bryanalvarez.sige.R;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nayar on 18/07/2018.
 */

public class Adapter_Novedad extends RecyclerView.Adapter<Adapter_Novedad.ViewHolder> {

    private Context context;
    private List<Novedad> list;
    private int layout;
    private OnItemClickListener itemClickListener;
    private OnButtonEditarClickListener btnEditarClickListener;

    public Adapter_Novedad(Context context, List<Novedad> list, int layout, OnItemClickListener itemClickListener, OnButtonEditarClickListener btnEditarClickListener) {
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


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView serial;
        public TextView cliente;
        public TextView tipo_novedad;
        public TextView estado_novedad;
        public ImageButton editar_novedad;

        public ViewHolder(View itemView) {
            super(itemView);

            serial = (TextView) itemView.findViewById(R.id.tv_list_view_serial_novedad);
            cliente = (TextView) itemView.findViewById(R.id.tv_list_view_cliente_novedad);
            tipo_novedad = (TextView) itemView.findViewById(R.id.tv_list_view_tipo_novedad);
            estado_novedad= (TextView) itemView.findViewById(R.id.tv_list_view_estado_novedad);
            editar_novedad = (ImageButton) itemView.findViewById(R.id.button_editar_novedad);
        }

        public void bind(final Novedad novedad, final OnButtonEditarClickListener onButtonEditarClickListener, final OnItemClickListener itemClickListener) {

            serial.setText(novedad.getSerial());
            cliente.setText(novedad.getCliente());
            tipo_novedad.setText(novedad.getTipo_novedad());
            estado_novedad.setText(novedad.getEstado_novedad());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(novedad, getAdapterPosition());
                }
            });

            if(novedad.getEstado_novedad().equals("Cerrado")){
                editar_novedad.setEnabled(false);
            }

            editar_novedad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonEditarClickListener.onButtonEditarClick(novedad, getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Novedad novedad, int position);
    }

    public interface OnButtonEditarClickListener {
        void onButtonEditarClick(Novedad novedad, int position);
    }
}
