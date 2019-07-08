package android.primer.bryanalvarez.sige.Fragments;


import android.media.Image;
import android.os.Bundle;
import android.primer.bryanalvarez.sige.Util.Util;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.primer.bryanalvarez.sige.R;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Maquinaria extends Fragment {

    private ImageButton buttonCrearMaquinaria;
    private ImageButton buttonVerMaquinaria;

    public Fragment_Maquinaria() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_fragment__maquinaria, container, false);

        buttonCrearMaquinaria = (ImageButton) view.findViewById(R.id.button_crear_maquina);
        buttonVerMaquinaria = (ImageButton) view.findViewById(R.id.button_ver_maquina);

        buttonCrearMaquinaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Fragment_Crear_Maquina();
                Util.setFragmentActual(fragment);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        buttonVerMaquinaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Fragment_Ver_Maquina();
                Util.setFragmentActual(fragment);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new Fragment_Ver_Maquina())
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

}
