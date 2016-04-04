import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Управление таблицой matches в БД
 * CRUD операции, получение объектного представления объектов БД.
 */
public class DaoMatchPostgreSQL extends AbstractDao<Match, Integer> {

    private class PersistMatch extends Match {
        public void setId(int id) {
            super.setId(id);
        }
    }

    public DaoMatchPostgreSQL(Connection connection) {
        super(connection);
    }

    public List<Match> getCascadeMatches(int id) {
        List<Match> list = null;
        String sql = "WITH RECURSIVE r AS ( SELECT a.* FROM matches a  WHERE id = " + id + " UNION ALL SELECT b.* FROM matches b JOIN r  ON b.next_match_id = r.id ) SELECT * FROM r;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // statement.setInt(1, 3);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            // throw new PersistException(e);
        }
        return list;
    }

    /**
     * находит матчи у которых поле nextMatch равен введеному id
     * возвращает список типа Match
     */
    public List<Match> getPreviousMatches(int id) {
        List<Match> list = null;
        String sql = "SELECT id FROM matches WHERE next_match_id=?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs != null) {
                list = parseResultSet(rs);
            }
        } catch (Exception e) {
            // throw new PersistException(e);
        }
        return list;
    }

    @Override
    public String getSelectQuery() {
        return "SELECT * FROM matches";
    }

    @Override
    public String getInsertQuery() {
        return "insert into matches (stage, tournament_id, match_data,owner_id, guests_id, owner_id_score, guests_id_score," +
                " next_match_id, status ) values(?,?,?,?,?,?,?,?,?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE matches SET" +
                " stage =?," +
                " tournament_id =?," +
                " match_data =?," +
                " owner_id =?," +
                " guests_id =?," +
                " owner_id_score =?," +
                " guests_id_score =?," +
                " next_match_id =?," +
                " status =? WHERE id=?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM matches WHERE id= ?;";
    }

    @Override
    public Match create() throws PersistException {
        Match g = new Match();
        return g;
    }

    @Override
    protected List<Match> parseResultSet(ResultSet rs) throws PersistException {
        LinkedList<Match> result = null;
        try {
            if (rs != null) {
                result = new LinkedList<>();
            } else {
                return null;
            }
            while (rs.next()) {
                PersistMatch match = new PersistMatch();
                match.setId(rs.getInt("id"));
                match.setStage(rs.getString("stage"));
                match.setTournamentId(rs.getInt("tournament_id"));
                match.setMatchData(rs.getDate("match_data"));
                match.setOwnerId(Checker.getInteger(rs, "owner_id"));//Integer или null
                match.setGuestsId(Checker.getInteger(rs, "guests_id"));
                match.setOwnerScore(Checker.getInteger(rs, "owner_id_score"));
                match.setGuestsScore(Checker.getInteger(rs, "guests_id_score"));
                match.setNextMatchId(Checker.getInteger(rs, "next_match_id"));
                match.setStatus(rs.getString("status"));
                result.add(match);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Match object) throws PersistException {
        try {
            statement.setString(1, object.getStage());
            statement.setInt(2, object.getTournamentId());
            statement.setDate(3, (Date) object.getMatchData());
            Checker.setInt(statement, 4, object.getOwnerId());//на случай, если значение null
            Checker.setInt(statement, 5, object.getGuestsId());
            Checker.setInt(statement, 6, object.getOwnerScore());
            Checker.setInt(statement, 7, object.getGuestsScore());
            Checker.setInt(statement, 8, object.getNextMatchId());
            statement.setString(9, object.getStatus());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Match object) throws PersistException {
        try {
            statement.setString(1, object.getStage());
            statement.setInt(2, object.getTournamentId());
            statement.setDate(3, (Date) object.getMatchData());
            Checker.setInt(statement, 4, object.getOwnerId());//на случай, если значение null
            Checker.setInt(statement, 5, object.getGuestsId());
            Checker.setInt(statement, 6, object.getOwnerScore());
            Checker.setInt(statement, 7, object.getGuestsScore());
            Checker.setInt(statement, 8, object.getNextMatchId());
            statement.setString(9, object.getStatus());
            statement.setInt(10, object.getId());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }
}