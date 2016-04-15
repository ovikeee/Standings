/**
 * Created by User on 19.03.2016.
 */

import java.io.Serializable;
import java.util.Date;

/**
 * Объектное представление сущности Team.
 */
public class Team implements Identified<Integer> {

    private Integer id = null;
    private String title;

    public Integer getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
