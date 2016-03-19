import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by User on 19.03.2016.
 */
public class DaoTournamentPostgreSQL extends AbstractDao<Tournament, Integer> {

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
        return "insert into tournament (title, number_of_teams, season) values(?,?,?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE tournament SET" +
                " title ?," +
                " number_of_teams ?," +
                " season ?" +
                " WHERE id= ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM tournament WHERE id= ?;";
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
                PersistTournament Tournament = new PersistTournament();
                Tournament.setId(rs.getInt("id"));
                Tournament.setTitle(rs.getString("title"));
                Tournament.setNumberOfTeams(rs.getInt("number_of_teams"));
                Tournament.setSeason(rs.getString("season"));
                result.add(Tournament);
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
            statement.setString(3, object.getSeason());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Tournament object) throws PersistException {
        try {
            statement.setString(1, object.getTitle());
            statement.setInt(2, object.getNumberOfTeams());
            statement.setString(3, object.getSeason());
            statement.setInt(4, object.getId());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }
}
