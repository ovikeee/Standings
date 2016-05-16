import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by User on 19.03.2016.
 */
public class DaoTeamPostgreSQL extends AbstractDAO<Team, Integer> {

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
        return "insert into teams (title) values(?)";
    }

    @Override
    public PreparedStatementSetter getPSSInsertQuery(Team team) {
        final Team object = team;
        PreparedStatementSetter result = new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, object.getTitle());
            }
        };
        return result;
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE teams SET" +
                " title= ?" +
                " WHERE id= ?";
    }

    @Override
    public PreparedStatementSetter getPSSUpdateQuery(Team team) {
        final Team object = team;
        PreparedStatementSetter result = new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, object.getTitle());
                preparedStatement.setInt(2, object.getId());
            }
        };
        return result;
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM teams WHERE id= ?";
    }

    @Override
    public Team create() throws PersistException {
        Team g = new Team();
        return persist(g);
    }

    public DaoTeamPostgreSQL(DataSource dataSource) throws SQLException {
        super(dataSource);
    }

    @Override
    protected List<Team> parseResultSet(ResultSet rs) throws PersistException {
        LinkedList<Team> result = new LinkedList<Team>();
        try {
            while (rs.next()) {
                PersistTeam team = new PersistTeam();
                team.setId(rs.getInt("id"));
                team.setTitle(rs.getString("title"));
                result.add(team);
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

    @Override
    protected RowMapper<Team> getMapper() {
        return new TeamMapper();
    }

}
