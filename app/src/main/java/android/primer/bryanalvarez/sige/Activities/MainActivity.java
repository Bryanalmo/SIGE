package android.primer.bryanalvarez.sige.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.primer.bryanalvarez.sige.Fragments.Fragment_Crear_Maquina;
import android.primer.bryanalvarez.sige.Fragments.Fragment_Crear_Novedad;
import android.primer.bryanalvarez.sige.Fragments.Fragment_Crear_Servicio_Tecnico;
import android.primer.bryanalvarez.sige.Fragments.Fragment_Maquinaria;
import android.primer.bryanalvarez.sige.Fragments.Fragment_Novedades;
import android.primer.bryanalvarez.sige.Fragments.Fragment_Revision_Maquina;
import android.primer.bryanalvarez.sige.Fragments.Fragment_Servicio_Tecnico;
import android.primer.bryanalvarez.sige.R;
import android.primer.bryanalvarez.sige.Util.Util;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private SharedPreferences prefs;

    private TextView usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolBar();

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        Util.setId_usuario(Util.getuserIdPrefs(prefs));
        Util.setUsuario(Util.getuserUserPrefs(prefs));
        Util.setCargo_usuario(Util.getuserCargoPrefs(prefs));

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navview);
        View view = navigationView.getHeaderView(0);
        usuario = (TextView) view.findViewById(R.id.usuario);
        usuario.setText(Util.getuserUserPrefs(prefs));

        setFragmentByDefault();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                boolean fragmentTransaction = false;
                boolean isRevision = false;
                Fragment fragment = null;

                switch (item.getItemId()){

                    case R.id.menu_maquinas:
                        fragment = new Fragment_Maquinaria();
                        fragmentTransaction = true;
                        break;
                    case R.id.menu_novedades:
                        fragment = new Fragment_Novedades();
                        fragmentTransaction = true;
                        break;
                    case R.id.menu_servicio_tecnico:
                        fragment = new Fragment_Servicio_Tecnico();
                        fragmentTransaction = true;
                        break;
                    case R.id.menu_revision_maquina:
                        fragment = new Fragment_Revision_Maquina();

                        fragmentTransaction = true;
                        isRevision = true;
                        break;
                    case R.id.menu_cerrar:
                        logOut();
                        removeSharedPreferences();
                        break;
                }

                if(fragmentTransaction){
                    if (isRevision){
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment)
                                .addToBackStack(null)
                                .commit();
                    }else{
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment)
                                .commit();
                    }
                    item.setChecked(true);
                    getSupportActionBar().setTitle(item.getTitle());
                    drawerLayout.closeDrawers();
                }

                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {

        final Fragment fragmentActual = Util.fragmentActual;
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            if(fragmentActual instanceof  Fragment_Crear_Maquina || fragmentActual instanceof Fragment_Crear_Novedad || fragmentActual instanceof Fragment_Crear_Servicio_Tecnico) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirmación");
                builder.setMessage("¿Esta seguro que desea salir del formulario?");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, fragmentActual)
                                .addToBackStack(null)
                                .commit();
                    }
                });
                builder.show();
            }
        } else {
            getFragmentManager().popBackStack();
        }


    }

    private void setToolBar(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setFragmentByDefault(){
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Fragment_Novedades()).commit();
        MenuItem item = navigationView.getMenu().getItem(1);
        item.setChecked(true);
        getSupportActionBar().setTitle(item.getTitle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                //abrir menu lateral
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }


    }

    private void logOut(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void removeSharedPreferences(){
        Util.deleteUserandPass(prefs);
    }
}
