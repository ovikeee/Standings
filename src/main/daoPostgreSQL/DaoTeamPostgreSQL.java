import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by User on 19.03.2016.
 */
public class DaoTeamPostgreSQL extends AbstractDao<Team, Integer> {

    private class PersistTeam extends Team {
        public void setId(int id) {
            super.setId(id);
        }
    }

    @Override
    public String getSelectQuery() {
        return "SELECT * FROM teams";
    }

    @Override
    public String getInsertQuery() {
        return "insert into teams (title) values(?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE teams SET" +
                " title ?" +
                " WHERE id= ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM teams WHERE id= ?;";
    }

    @Override
    public Team create() throws PersistException {
        Team g = new Team();
        return persist(g);
    }

    public DaoTeamPostgreSQL(Connection connection) {
        super(connection);
    }

    @Override
    protected List<Team> parseResultSet(ResultSet rs) throws PersistException {
        LinkedList<Team> result = new LinkedList<Team>();
        try {
            while (rs.next()) {
                PersistTeam Team = new PersistTeam();
                Team.setId(rs.getInt("id"));
                Team.setTitle(rs.getString("title"));
                result.add(Team);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Team object) throws PersistException {
        try {
            statement.setString(1, object.getTitle());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Team object) throws PersistException {
        try {
            statement.setString(1, object.getTitle());
            statement.setInt(2, object.getId());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

}
