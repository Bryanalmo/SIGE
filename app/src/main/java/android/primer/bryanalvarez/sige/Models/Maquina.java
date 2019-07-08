package android.primer.bryanalvarez.sige.Models;

import java.util.ArrayList;

/**
 * Created by nayar on 17/07/2018.
 */

public class Maquina {

    private String Id;
    private String Serial;
    private String Placa_activo_fijo_club;
    private String Numero_maquina_club;
    private String Modelo_motor;
    private String Serial_motor;
    private String Codigo_motor;
    private String Serial_unidades_corte;
    private String Fecha_compra;
    private String Fecha_entrega;
    private String Horas;
    private String Operador_asignado;
    private String Observaciones;
    private String Estado_registro_fabrica;
    private String Fecha_regisro_fabrica;
    private String Año_produccion;
    private String Distribuidor_maquina;
    private String Ubicacion;
    private String Tecnico_asignado;
    private String Marca;
    private String Modelo_maquina;
    private String Funcion;
    private String Cliente;
    private String Estado_maquina;
    private String foto;
    private String manual_partes;
    private ArrayList<Repuesto> Repuestos_modelo_maquina;

    public Maquina(){}

    public Maquina(String id, String serial, String marca, String modelo_maquina, String estado_maquina, ArrayList<Repuesto> repuestos_modelo_maquina) {
        Id = id;
        Serial = serial;
        Marca = marca;
        Modelo_maquina = modelo_maquina;
        Estado_maquina = estado_maquina;
        this.Repuestos_modelo_maquina = repuestos_modelo_maquina;
    }

    public Maquina(String id, String serial, String placa_activo_fijo_club, String numero_maquina_club, String modelo_motor, String serial_motor, String codigo_motor, String serial_unidades_corte, String fecha_compra, String fecha_entrega, String horas, String operador_asignado, String observaciones, String estado_registro_fabrica, String fecha_regisro_fabrica, String año_produccion, String distribuidor_maquina, String ubicacion, String tecnico_asignado, String marca, String modelo_maquina, String funcion, String cliente, String estado_maquina, String foto) {
        Id = id;
        Serial = serial;
        Placa_activo_fijo_club = placa_activo_fijo_club;
        Numero_maquina_club = numero_maquina_club;
        Modelo_motor = modelo_motor;
        Serial_motor = serial_motor;
        Codigo_motor = codigo_motor;
        Serial_unidades_corte = serial_unidades_corte;
        Fecha_compra = fecha_compra;
        Fecha_entrega = fecha_entrega;
        Horas = horas;
        Operador_asignado = operador_asignado;
        Observaciones = observaciones;
        Estado_registro_fabrica = estado_registro_fabrica;
        Fecha_regisro_fabrica = fecha_regisro_fabrica;
        Año_produccion = año_produccion;
        Distribuidor_maquina = distribuidor_maquina;
        Ubicacion = ubicacion;
        Tecnico_asignado = tecnico_asignado;
        Marca = marca;
        Modelo_maquina = modelo_maquina;
        Funcion = funcion;
        Cliente = cliente;
        Estado_maquina = estado_maquina;
        this.foto = foto;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getEstado_maquina() {
        return Estado_maquina;
    }

    public void setEstado_maquina(String estado_maquina) {
        Estado_maquina = estado_maquina;
    }

    public String getSerial() {
        return Serial;
    }

    public void setSerial(String serial) {
        Serial = serial;
    }

    public String getPlaca_activo_fijo_club() {
        return Placa_activo_fijo_club;
    }

    public void setPlaca_activo_fijo_club(String placa_activo_fijo_club) {
        Placa_activo_fijo_club = placa_activo_fijo_club;
    }

    public String getNumero_maquina_club() {
        return Numero_maquina_club;
    }

    public void setNumero_maquina_club(String numero_maquina_club) {
        Numero_maquina_club = numero_maquina_club;
    }

    public String getModelo_motor() {
        return Modelo_motor;
    }

    public void setModelo_motor(String modelo_motor) {
        Modelo_motor = modelo_motor;
    }

    public String getSerial_motor() {
        return Serial_motor;
    }

    public void setSerial_motor(String serial_motor) {
        Serial_motor = serial_motor;
    }

    public String getCodigo_motor() {
        return Codigo_motor;
    }

    public void setCodigo_motor(String codigo_motor) {
        Codigo_motor = codigo_motor;
    }

    public String getSerial_unidades_corte() {
        return Serial_unidades_corte;
    }

    public void setSerial_unidades_corte(String serial_unidades_corte) {
        Serial_unidades_corte = serial_unidades_corte;
    }

    public String getFecha_compra() {
        return Fecha_compra;
    }

    public void setFecha_compra(String fecha_compra) {
        Fecha_compra = fecha_compra;
    }

    public String getFecha_entrega() {
        return Fecha_entrega;
    }

    public void setFecha_entrega(String fecha_entrega) {
        Fecha_entrega = fecha_entrega;
    }

    public String getHoras() {
        return Horas;
    }

    public void setHoras(String horas) {
        Horas = horas;
    }

    public String getOperador_asignado() {
        return Operador_asignado;
    }

    public void setOperador_asignado(String operador_asignado) {
        Operador_asignado = operador_asignado;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String observaciones) {
        Observaciones = observaciones;
    }

    public String getEstado_registro_fabrica() {
        return Estado_registro_fabrica;
    }

    public void setEstado_registro_fabrica(String estado_registro_fabrica) {
        Estado_registro_fabrica = estado_registro_fabrica;
    }

    public String getFecha_regisro_fabrica() {
        return Fecha_regisro_fabrica;
    }

    public void setFecha_regisro_fabrica(String fecha_regisro_fabrica) {
        Fecha_regisro_fabrica = fecha_regisro_fabrica;
    }

    public String getAño_produccion() {
        return Año_produccion;
    }

    public void setAño_produccion(String año_produccion) {
        Año_produccion = año_produccion;
    }

    public String getDistribuidor_maquina() {
        return Distribuidor_maquina;
    }

    public void setDistribuidor_maquina(String distribuidor_maquina) {
        Distribuidor_maquina = distribuidor_maquina;
    }

    public String getUbicacion() {
        return Ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        Ubicacion = ubicacion;
    }

    public String getTecnico_asignado() {
        return Tecnico_asignado;
    }

    public void setTecnico_asignado(String tecnico_asignado) {
        Tecnico_asignado = tecnico_asignado;
    }

    public String getMarca() {
        return Marca;
    }

    public void setMarca(String marca) {
        Marca = marca;
    }

    public String getModelo_maquina() {
        return Modelo_maquina;
    }

    public void setModelo_maquina(String modelo_maquina) {
        Modelo_maquina = modelo_maquina;
    }

    public String getFuncion() {
        return Funcion;
    }

    public void setFuncion(String funcion) {
        Funcion = funcion;
    }

    public String getCliente() {
        return Cliente;
    }

    public void setCliente(String cliente) {
        Cliente = cliente;
    }

    public ArrayList<Repuesto> getRepuestos_modelo_maquina() {
        return Repuestos_modelo_maquina;
    }

    public String getManual_partes() {
        return manual_partes;
    }

    public void setManual_partes(String manual_partes) {
        this.manual_partes = manual_partes;
    }

    public void setRepuestos_modelo_maquina(ArrayList<Repuesto> repuestos_modelo_maquina) {
        Repuestos_modelo_maquina = repuestos_modelo_maquina;
    }
}
