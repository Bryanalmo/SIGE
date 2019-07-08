package android.primer.bryanalvarez.sige.Models;

/**
 * Created by nayar on 20/08/2018.
 */

public class PagerModel {

    String id;
    String title;

    public PagerModel(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
