package android.primer.bryanalvarez.sige.Models;

/**
 * Created by nayar on 1/10/2018.
 */

public class Maquina_Rectificado {

    private String id;
    private String modelo;
    private String serial;
    private boolean checked;

    public Maquina_Rectificado(String id, String modelo, String serial) {
        this.id = id;
        this.modelo = modelo;
        this.serial = serial;
        this.checked = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
