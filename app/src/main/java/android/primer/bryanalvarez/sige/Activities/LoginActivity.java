package android.primer.bryanalvarez.sige.Activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.primer.bryanalvarez.sige.Util.Util;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.primer.bryanalvarez.sige.R;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.widget.Toast.LENGTH_LONG;

public class LoginActivity extends AppCompatActivity{

    private SharedPreferences prefs;

    private EditText editTextUsuario;
    private EditText editTextPassword;
    private Switch switchRemember;
    private Button buttonLogin;
    private AlertDialog alertDialog;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage("Enviando datos...");
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        bindUI();

        setCredentialsifExist();

        if(validarPermisos()){
            buttonLogin.setEnabled(true);
        }{
            //buttonLogin.setEnabled(false);
            //Toast.makeText(this, "Debe aceptar los permisos", LENGTH_LONG).show();
        }

        request = Volley.newRequestQueue(this);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()){

                    alertDialog.show();
                    cargarWebService();
                }else{
                    Toast.makeText(LoginActivity.this, "Debe llenar todos los campos", LENGTH_LONG).show();
                }
            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100){
            if(grantResults.length==2 && grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                buttonLogin.setEnabled(true);
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
                }
            }
        }
    }

    private Boolean validarPermisos() {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }
        //if((checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){
          //  return true;
        //}
        if((shouldShowRequestPermissionRationale(CAMERA)) || (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))){
            dialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        }
        return false;
    }

    private void dialogoRecomendacion() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(LoginActivity.this);
        dialogo.setTitle("Permisos desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el buen funcionamiento de la aplicación");
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
                }
            }
        });
        dialogo.show();
    }

    private void safeOnPreferences(String user, String password, String Id, String Cargo){

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("User",user);
            editor.putString("Pass",password);
            editor.putString("Id",Id);
            editor.putString("Cargo",Cargo);
            editor.commit();
            editor.apply();



    }

    private void bindUI(){

        editTextUsuario = (EditText) findViewById(R.id.editTextUsuario);
        editTextPassword= (EditText) findViewById(R.id.editTextPassword);
        switchRemember = (Switch) findViewById(R.id.switchRemember);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
    }

    private void cargarWebService(){

        String url = "https://sige.golfyturf.com/AppWebServices/getAutenticacionEmpleado.php";
        url = url.replace(" ", "%20");

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if(jsonObject.getString("validacion").equals("true")){
                        alertDialog.dismiss();
                        goToMain();
                        safeOnPreferences(editTextUsuario.getText().toString(),editTextPassword.getText().toString(), jsonObject.getString("id").toString(), jsonObject.getString("cargo") );
                        Util.setUsuario(jsonObject.getString("nombres").toString());
                        Util.setId_usuario(jsonObject.getString("id"));
                        Util.setCargo_usuario(jsonObject.getString("cargo"));
                        //Toast.makeText(LoginActivity.this, Util.getCargo_usuario(), Toast.LENGTH_SHORT).show();
                    }else{
                        alertDialog.dismiss();
                        Toast.makeText(LoginActivity.this,"Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this,"Acceso a internet muy debil. Revise su conexión", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String usuario = editTextUsuario.getText().toString();
                String password = editTextPassword.getText().toString();

                Map<String,String> parametros = new HashMap<>();
                parametros.put("usuario",usuario);
                parametros.put("password",password);

                return parametros;
            }
        };
        request.add(stringRequest);
    }

    private void goToMain(){
        if(isOnline(this)){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            Toast.makeText(this,"No tiene conección a internet",LENGTH_LONG).show();
        }

    }

    private void setCredentialsifExist(){
        String user = Util.getuserUserPrefs(prefs);
        String password = Util.getuserPasswordPrefs(prefs);
        if(!TextUtils.isEmpty(user) && !TextUtils.isEmpty(password)){
            editTextUsuario.setText(user);
            editTextPassword.setText(password);
            switchRemember.setChecked(true);
        }

    }

    private boolean check(){
        if(editTextPassword.getText().toString().equals("") || editTextUsuario.getText().toString().equals("") ){
            return false;
        }else{
            return true;
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
}
