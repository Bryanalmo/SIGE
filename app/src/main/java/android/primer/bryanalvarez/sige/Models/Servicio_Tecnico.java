package android.primer.bryanalvarez.sige.Models;

import java.util.ArrayList;

/**
 * Created by nayar on 19/07/2018.
 */

public class Servicio_Tecnico {

    private String Id;
    private String Id_tipo_falla;
    private String Fecha_programacion;
    private String Fecha_hora_inicio;
    private String Fecha_hora_fin;
    private String Horometro;
    private String Repuestos_utilizados;
    private String Descripcion_corta;
    private String Descripcion;
    private String Descripcion_cierre;
    private String Id_novedad;
    private String Id_empleado;
    private String Id_estado_servicio_tecnico;
    private String Id_tipo_servicio_tecnico;
    private String Persona_cargo;
    private String Cliente;
    private String Serial;
    private String Id_nota_salida;
    private String Id_cliente_visitar;


    public Servicio_Tecnico(){}

    public String getId_cliente_visitar() {
        return Id_cliente_visitar;
    }

    public void setId_cliente_visitar(String id_cliente_visitar) {
        Id_cliente_visitar = id_cliente_visitar;
    }

    public String getId_nota_salida() {
        return Id_nota_salida;
    }

    public void setId_nota_salida(String id_nota_salida) {
        Id_nota_salida = id_nota_salida;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getSerial() {
        return Serial;
    }

    public void setSerial(String serial) {
        Serial = serial;
    }

    public String getId_tipo_falla() {
        return Id_tipo_falla;
    }

    public void setId_tipo_falla(String id_tipo_falla) {
        Id_tipo_falla = id_tipo_falla;
    }

    public String getFecha_programacion() {
        return Fecha_programacion;
    }

    public void setFecha_programacion(String fecha_programacion) {
        Fecha_programacion = fecha_programacion;
    }

    public String getFecha_hora_inicio() {
        return Fecha_hora_inicio;
    }

    public void setFecha_hora_inicio(String fecha_hora_inicio) {
        Fecha_hora_inicio = fecha_hora_inicio;
    }

    public String getFecha_hora_fin() {
        return Fecha_hora_fin;
    }

    public void setFecha_hora_fin(String fecha_hora_fin) {
        Fecha_hora_fin = fecha_hora_fin;
    }

    public String getHorometro() {
        return Horometro;
    }

    public void setHorometro(String horometro) {
        Horometro = horometro;
    }

    public String getRepuestos_utilizados() {
        return Repuestos_utilizados;
    }

    public void setRepuestos_utilizados(String repuestos_utilizados) {
        Repuestos_utilizados = repuestos_utilizados;
    }

    public String getDescripcion_corta() {
        return Descripcion_corta;
    }

    public void setDescripcion_corta(String descripcion_corta) {
        Descripcion_corta = descripcion_corta;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getDescripcion_cierre() {
        return Descripcion_cierre;
    }

    public void setDescripcion_cierre(String descripcion_cierre) {
        Descripcion_cierre = descripcion_cierre;
    }

    public String getId_novedad() {
        return Id_novedad;
    }

    public void setId_novedad(String id_novedad) {
        Id_novedad = id_novedad;
    }

    public String getId_empleado() {
        return Id_empleado;
    }

    public void setId_empleado(String id_empleado) {
        Id_empleado = id_empleado;
    }

    public String getId_estado_servicio_tecnico() {
        return Id_estado_servicio_tecnico;
    }

    public void setId_estado_servicio_tecnico(String id_estado_servicio_tecnico) {
        Id_estado_servicio_tecnico = id_estado_servicio_tecnico;
    }

    public String getId_tipo_servicio_tecnico() {
        return Id_tipo_servicio_tecnico;
    }

    public void setId_tipo_servicio_tecnico(String id_tipo_servicio_tecnico) {
        Id_tipo_servicio_tecnico = id_tipo_servicio_tecnico;
    }

    public String getPersona_cargo() {
        return Persona_cargo;
    }

    public void setPersona_cargo(String persona_cargo) {
        Persona_cargo = persona_cargo;
    }

    public String getCliente() {
        return Cliente;
    }

    public void setCliente(String cliente) {
        Cliente = cliente;
    }
}
