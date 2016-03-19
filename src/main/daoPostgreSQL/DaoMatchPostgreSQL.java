import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class DaoMatchPostgreSQL extends AbstractDao<Match, Integer> {

    private class PersistMatch extends Match {
        public void setId(int id) {
            super.setId(id);
        }
    }

    @Override
    public String getSelectQuery() {
        return "SELECT * FROM matches";
    }

//    public String getSelectResulQuery() {
//        return "select match_id,match_data,t1.title,t2.title,owner_id_score, guests_id_score, next_match_id,status from matches,teams t1, teams t2 where owner_id=t1.id and guests_id=t2.id;";
//    }

    @Override
    public String getInsertQuery() {
        return "insert into matches (tournament_id, match_data, next_match_id, guests_id, owner_id_score, guests_id_score," +
                " next_match_id, status ) values(?,?,?,?,?,?,?,?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE matches SET" +
                " tournament_id ?," +
                " match_data ?," +
                " owner_id integer ?," +
                " guests_id ?," +
                " owner_id_score ?," +
                " guests_id_score ?," +
                " next_match_id ?," +
                " status ? WHERE match_id= ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM matches WHERE match_id= ?;";
    }

    @Override
    public Match create() throws PersistException {
        Match g = new Match();
        return persist(g);
    }

    public DaoMatchPostgreSQL(Connection connection) {
        super(connection);
    }

    @Override
    protected List<Match> parseResultSet(ResultSet rs) throws PersistException {
        LinkedList<Match> result = new LinkedList<Match>();
        try {
            while (rs.next()) {
                PersistMatch Match = new PersistMatch();
                Match.setId(rs.getInt("match_id"));
                Match.setTournamentId(rs.getInt("tournament_id"));
                Match.setMatchData(rs.getDate("match_data"));
                Match.setOwnerId(rs.getInt("owner_id"));
                Match.setGuestsId(rs.getInt("guests_id"));
                Match.setOwnerScore(rs.getInt("owner_id_score"));
                Match.setGuestsScore(rs.getInt("guests_id_score"));
                Match.setNextMatchId(rs.getInt("next_match_id"));
                Match.setStatus(rs.getString("status"));
                result.add(Match);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Match object) throws PersistException {
        try {
            statement.setInt(1, object.getTournamentId());
            statement.setDate(2, (Date) object.getMatchData());
            statement.setInt(3, object.getOwnerId());
            statement.setInt(4, object.getGuestsId());
            statement.setInt(5, object.getOwnerScore());
            statement.setInt(6, object.getGuestsScore());
            statement.setInt(7, object.getNextMatchId());
            statement.setString(8, object.getStatus());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Match object) throws PersistException {
        try {
            statement.setInt(1, object.getTournamentId());
            statement.setDate(2, (Date) object.getMatchData());
            statement.setInt(3, object.getOwnerId());
            statement.setInt(4, object.getGuestsId());
            statement.setInt(5, object.getOwnerScore());
            statement.setInt(6, object.getGuestsScore());
            statement.setInt(7, object.getNextMatchId());
            statement.setString(8, object.getStatus());
            statement.setInt(9, object.getId());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }
}