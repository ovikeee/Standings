import java.sql.Date;
/**
 * Created by User on 19.03.2016.
 */
public class Match implements Identified<Integer> {
    private Integer id = null;
    private Integer tournamentId;
    private String stage;
    private Date matchData;
    private Integer ownerId;//BigInteger!!!!!!!!!!!
    private Integer guestsId;
    private Integer ownerScore;
    private Integer guestsScore;
    private Integer nextMatchId;
    private String status;

    protected void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {

        return id;
    }

    public Integer getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Integer tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public Date getMatchData() {
        return matchData;
    }

    public void setMatchData(Date matchData) {
        this.matchData = matchData;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getGuestsId() {
        return guestsId;
    }

    public void setGuestsId(Integer guestsId) {
        this.guestsId = guestsId;
    }

    public Integer getOwnerScore() {
        return ownerScore;
    }

    public void setOwnerScore(Integer ownerScore) {
        this.ownerScore = ownerScore;
    }

    public Integer getGuestsScore() {
        return guestsScore;
    }

    public void setGuestsScore(Integer guestsScore) {
        this.guestsScore = guestsScore;
    }

    public Integer getNextMatchId() {
        return nextMatchId;
    }

    public void setNextMatchId(Integer nextMatchId) {
        this.nextMatchId = nextMatchId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
