import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 15.05.2016.
 */
public class TeamMapper implements RowMapper<Team> {
    public Team mapRow(ResultSet rs, int rowNum) throws SQLException {
        Team team = new Team();
        team.setId(rs.getInt("id"));
        team.setTitle(rs.getString("title"));
        return team;
    }
}