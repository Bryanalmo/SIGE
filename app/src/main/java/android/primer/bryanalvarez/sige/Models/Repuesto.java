package android.primer.bryanalvarez.sige.Models;

/**
 * Created by nayar on 27/08/2018.
 */

public class Repuesto {

    private String id;
    private String referencia;
    private String descripcion;
    private boolean checked;

    public Repuesto(String id, String referencia, String descripcion) {
        this.id = id;
        this.referencia = referencia;
        this.descripcion = descripcion;
        this.checked = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
