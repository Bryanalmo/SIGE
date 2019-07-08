package android.primer.bryanalvarez.sige.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.primer.bryanalvarez.sige.Activities.CierreServicioTecnicoActivity;
import android.primer.bryanalvarez.sige.Util.Util;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.primer.bryanalvarez.sige.R;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Servicio_Tecnico extends Fragment {

    private ImageButton buttonCrearST;
    private ImageButton buttonVerST;

    public Fragment_Servicio_Tecnico() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment__servicio__tecnico, container, false);

        buttonCrearST = (ImageButton) view.findViewById(R.id.button_crear_servicio_tecnico);
        buttonVerST = (ImageButton) view.findViewById(R.id.button_ver_servicio_tecnico);

        buttonCrearST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Fragment_Crear_Servicio_Tecnico();
                Util.setFragmentActual(fragment);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new Fragment_Crear_Servicio_Tecnico())
                        .addToBackStack(null)
                        .commit();
            }
        });

        buttonVerST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Fragment_Ver_Servicio_Tecnico();
                Util.setFragmentActual(fragment);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new Fragment_Ver_Servicio_Tecnico())
                        .addToBackStack(null)
                        .commit();
                //Intent intent = new Intent(getContext().getApplicationContext(), CierreServicioTecnicoActivity.class);
                //startActivity(intent);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

}
