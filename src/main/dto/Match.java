import java.util.Date;

/**
 * Created by User on 19.03.2016.
 */
public class Match implements Identified<Integer> {
    private Integer id = null;
    private int tournamentId;
    private Date matchData;
    private int ownerId;
    private int guestsId;
    private int ownerScore;
    private int guestsScore;
    private int nextMatchId;
    private String status;

    public Integer getId() {

        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNextMatchId() {
        return nextMatchId;
    }

    public void setNextMatchId(int nextMatchId) {
        this.nextMatchId = nextMatchId;
    }

    public int getGuestsScore() {
        return guestsScore;
    }

    public void setGuestsScore(int guestsScore) {
        this.guestsScore = guestsScore;
    }

    public int getOwnerScore() {
        return ownerScore;
    }

    public void setOwnerScore(int ownerScore) {
        this.ownerScore = ownerScore;
    }

    public int getGuestsId() {
        return guestsId;
    }

    public void setGuestsId(int guestsId) {
        this.guestsId = guestsId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public Date getMatchData() {
        return matchData;
    }

    public void setMatchData(Date matchData) {
        this.matchData = matchData;
    }

    public int getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(int tournamentId) {
        this.tournamentId = tournamentId;
    }

}
