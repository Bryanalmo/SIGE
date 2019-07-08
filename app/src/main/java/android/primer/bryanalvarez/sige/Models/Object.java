package android.primer.bryanalvarez.sige.Models;

/**
 * Created by nayar on 16/07/2018.
 */

public class Object {
    private String id;
    private String label;
    private String additional;

    public Object(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }
}
