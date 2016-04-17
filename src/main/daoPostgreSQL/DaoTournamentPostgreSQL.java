import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by User on 19.03.2016.
 */
public class DaoTournamentPostgreSQL extends AbstractDAO<Tournament, Integer> {

    private class PersistTournament extends Tournament {
        public void setId(int id) {
            super.setId(id);
        }
    }

    @Override
    public String getSelectQuery() {
        return "SELECT * FROM tournament";
    }

    @Override
    public String getInsertQuery() {
        return "insert into tournament (title, number_of_teams) values(?,?)";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE tournament SET" +
                " title= ?," +
                " number_of_teams= ?" +
                " WHERE id= ?";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM tournament WHERE id= ?";
    }

    public List<Tournament> getTournamentsHasTeam(Integer id) throws PersistException {
        LinkedList<Tournament> result = new LinkedList<Tournament>();
        String sql = "select id, title from tournament where id in (select distinct tournament_id from matches  where guests_id = ? OR owner_id = ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1,id);
            statement.setInt(2,id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                PersistTournament tournament = new PersistTournament();
                tournament.setId(rs.getInt("id"));
                tournament.setTitle(rs.getString("title"));
                result.add(tournament);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    public Tournament create() throws PersistException {
        Tournament g = new Tournament();
        return persist(g);
    }

    public DaoTournamentPostgreSQL(Connection connection) {
        super(connection);
    }

    @Override
    protected List<Tournament> parseResultSet(ResultSet rs) throws PersistException {
        LinkedList<Tournament> result = new LinkedList<Tournament>();
        try {
            while (rs.next()) {
                PersistTournament tournament = new PersistTournament();
                tournament.setId(rs.getInt("id"));
                tournament.setTitle(rs.getString("title"));
                tournament.setNumberOfTeams(rs.getInt("number_of_teams"));
                result.add(tournament);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Tournament object) throws PersistException {
        try {
            statement.setString(1, object.getTitle());
            statement.setInt(2, object.getNumberOfTeams());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Tournament object) throws PersistException {
        try {
            statement.setString(1, object.getTitle());
            statement.setInt(2, object.getNumberOfTeams());
            statement.setInt(3, object.getId());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }
}
