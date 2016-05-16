import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class TournamentMapper implements RowMapper<Tournament> {
    public Tournament mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tournament tournament = new Tournament();
        tournament.setId(rs.getInt("id"));
        tournament.setTitle(rs.getString("title"));
        tournament.setNumberOfTeams(rs.getInt("number_of_teams"));
        return tournament;
    }
}