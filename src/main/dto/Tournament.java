/**
 * Created by User on 19.03.2016.
 */
public class Tournament implements Identified<Integer> {
    private Integer id=null;
    private String title;
    private Integer numberOfTeams;

    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNumberOfTeams() {
        return numberOfTeams;
    }

    public void setNumberOfTeams(Integer numberOfTeams) {
        this.numberOfTeams = numberOfTeams;
    }
}
