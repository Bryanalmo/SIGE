package android.primer.bryanalvarez.sige.Fragments;


import android.os.Bundle;
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
public class Fragment_Novedades extends Fragment {

    private ImageButton buttonCrearNovedad;
    private ImageButton buttonVerNovedades;

    public Fragment_Novedades() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_fragment__novedades, container, false);

        buttonCrearNovedad = (ImageButton) view.findViewById(R.id.button_crear_novedad);
        buttonVerNovedades = (ImageButton) view.findViewById(R.id.button_ver_novedad);

        buttonCrearNovedad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Fragment_Crear_Novedad();
                Util.setFragmentActual(fragment);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new Fragment_Crear_Novedad())
                        .addToBackStack(null)
                        .commit();
            }
        });

        buttonVerNovedades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Fragment_Ver_Novedades();
                Util.setFragmentActual(fragment);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new Fragment_Ver_Novedades())
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
