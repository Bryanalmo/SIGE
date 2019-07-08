package android.primer.bryanalvarez.sige.Models;

/**
 * Created by nayar on 18/07/2018.
 */

public class Novedad {

    private String id;
    private String Cliente;
    private String Serial;
    private String Fecha;
    private String Descripcion_corta;
    private String Descripción_novedad_inicial;
    private String Descripción_novedad_cierre;
    private String Tipo_novedad;
    private String Estado_novedad;
    private String Estado_maquina;
    private String Horas_trabajo_maquina;
    private String Foto_novedad;

    public Novedad(){}

    public Novedad(String id, String cliente, String serial, String fecha, String descripcion_corta, String descripción_novedad_inicial, String tipo_novedad, String estado_novedad, String estado_maquina, String horas_trabajo_maquina, String foto_novedad) {
        this.id = id;
        Cliente = cliente;
        Serial = serial;
        Fecha = fecha;
        Descripcion_corta = descripcion_corta;
        Descripción_novedad_inicial = descripción_novedad_inicial;
        Tipo_novedad = tipo_novedad;
        Estado_novedad = estado_novedad;
        Estado_maquina = estado_maquina;
        Horas_trabajo_maquina = horas_trabajo_maquina;
        Foto_novedad = foto_novedad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCliente() {
        return Cliente;
    }

    public void setCliente(String cliente) {
        Cliente = cliente;
    }

    public String getSerial() {
        return Serial;
    }

    public void setSerial(String serial) {
        Serial = serial;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getDescripcion_corta() {
        return Descripcion_corta;
    }

    public void setDescripcion_corta(String descripcion_corta) {
        Descripcion_corta = descripcion_corta;
    }

    public String getDescripción_novedad_inicial() {
        return Descripción_novedad_inicial;
    }

    public void setDescripción_novedad_inicial(String descripción_novedad_inicial) {
        Descripción_novedad_inicial = descripción_novedad_inicial;
    }

    public String getDescripción_novedad_cierre() {
        return Descripción_novedad_cierre;
    }

    public void setDescripción_novedad_cierre(String descripción_novedad_cierre) {
        Descripción_novedad_cierre = descripción_novedad_cierre;
    }

    public String getTipo_novedad() {
        return Tipo_novedad;
    }

    public void setTipo_novedad(String tipo_novedad) {
        Tipo_novedad = tipo_novedad;
    }

    public String getEstado_novedad() {
        return Estado_novedad;
    }

    public void setEstado_novedad(String estado_novedad) {
        Estado_novedad = estado_novedad;
    }

    public String getEstado_maquina() {
        return Estado_maquina;
    }

    public void setEstado_maquina(String estado_maquina) {
        Estado_maquina = estado_maquina;
    }

    public String getHoras_trabajo_maquina() {
        return Horas_trabajo_maquina;
    }

    public void setHoras_trabajo_maquina(String horas_trabajo_maquina) {
        Horas_trabajo_maquina = horas_trabajo_maquina;
    }

    public String getFoto_novedad() {
        return Foto_novedad;
    }

    public void setFoto_novedad(String foto_novedad) {
        Foto_novedad = foto_novedad;
    }
}
