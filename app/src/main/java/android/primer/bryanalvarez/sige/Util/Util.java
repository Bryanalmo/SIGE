package android.primer.bryanalvarez.sige.Util;

import android.content.SharedPreferences;
import android.primer.bryanalvarez.sige.Models.Maquina;
import android.primer.bryanalvarez.sige.Models.Novedad;
import android.primer.bryanalvarez.sige.Models.Servicio_Tecnico;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by nayar on 12/07/2018.
 */

public class Util {

    public static Fragment fragmentActual;

    public  static String Usuario;

    public static String Id_usuario;

    public static String Cargo_usuario;

    public static Maquina maquina;

    public static Novedad novedad;

    public static Servicio_Tecnico servicio_tecnico;

    public static Fragment getFragmentActual() {
        return fragmentActual;
    }

    public static void setFragmentActual(Fragment fragmentActual) {
        Util.fragmentActual = fragmentActual;
    }

    public static String getUsuario() {
        return Usuario;
    }

    public static String getId_usuario() {
        return Id_usuario;
    }

    public static void setId_usuario(String id_usuario) {
        Id_usuario = id_usuario;
    }

    public static String getCargo_usuario() {
        return Cargo_usuario;
    }

    public static void setCargo_usuario(String cargo_usuario) {
        Cargo_usuario = cargo_usuario;
    }

    public static Servicio_Tecnico getServicio_tecnico() {
        return servicio_tecnico;
    }

    public static void setServicio_tecnico(Servicio_Tecnico servicio_tecnico) {
        Util.servicio_tecnico = servicio_tecnico;
    }

    public static void setMaquina(Maquina maquina) {
        Util.maquina = maquina;
    }

    public static Maquina getMaquina() {
        return maquina;
    }

    public static Novedad getNovedad() {
        return novedad;
    }

    public static void setNovedad(Novedad novedad) {
        Util.novedad = novedad;
    }

    public static void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public static String getuserUserPrefs(SharedPreferences preferences){
        return preferences.getString("User","");
    }
    public static String getuserPasswordPrefs(SharedPreferences preferences){
        return preferences.getString("Pass","");
    }
    public static String getuserIdPrefs(SharedPreferences preferences){
        return preferences.getString("Id","");
    }
    public static String getuserCargoPrefs(SharedPreferences preferences){
        return preferences.getString("Cargo","");
    }
    public static void deleteUserandPass(SharedPreferences preferences){
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("User");
        editor.remove("Pass");
        editor.apply();
    }
}
