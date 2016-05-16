import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import org.springframework.jdbc.core.RowMapper;

public class MatchMapper implements RowMapper<Match> {
    public Match mapRow(ResultSet rs, int rowNum) throws SQLException {
        Match match = new Match();
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
        return match;
    }
}