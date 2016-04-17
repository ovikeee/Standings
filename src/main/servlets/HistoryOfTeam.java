import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * Created by User on 16.04.2016.
 */
@WebServlet(urlPatterns = "/historyOfTeam", name = "/HistoryOfTeam")
public class HistoryOfTeam extends HttpServlet {
    static DaoFactory<Connection> factory = new DaoFactoryPostgreSQL();
    static DaoMatchPostgreSQL matches;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
        try {
            Integer teamId;
            matches = (DaoMatchPostgreSQL) factory.getDao(Match.class);//создаем ДАО объект для работы с таблией матчей
            String type = request.getParameter("type");
            if (type != null) {
                switch (type) {
                    case "loadSelectBoxTournament":
                        teamId = Checker.getInt(request.getParameter("teamId"));
                        DaoTournamentPostgreSQL tournaments;
                        tournaments = (DaoTournamentPostgreSQL) factory.getDao(Tournament.class);
                        List<Tournament> tournamentList = tournaments.getTournamentsHasTeam(teamId);
                        if (!tournamentList.isEmpty()) {
                            sendSelectorsTournament(response, tournamentList);
                        } else {
                            response.setStatus(499);//эта команда не участвовала ни в каком турнире
                        }
                        break;
                    case "getMatches":
                        Integer tournamentId = Checker.getInt(request.getParameter("tournamentId"));
                        teamId = Checker.getInt(request.getParameter("teamId"));
                        List<Match> matchList = matches.getMatches(teamId, tournamentId);
                        sendMatch(response,matchList);
                        break;
                    default:
                        response.setStatus(490);
                }
            }
        } catch (PersistException e) {
            e.printStackTrace();
            response.setStatus(490);
        }
    }

    private void sendSelectorsTournament(HttpServletResponse response, List<Tournament> tournamentList) {
        try {

            response.setContentType("application/json;charset=UTF-8");
            org.json.JSONWriter jw = new org.json.JSONWriter(response.getWriter());
            jw.array();
            for (Tournament tournament : tournamentList) {
                jw.object();
                jw.key("tournaments");
                jw.value(tournament.getTitle());
                jw.key("tournamentId");
                jw.value(tournament.getId());
                jw.endObject();
            }
            jw.endArray();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void sendMatch(HttpServletResponse response, List<Match> matchList) {
        try {
            DaoTeamPostgreSQL teams;
            teams = (DaoTeamPostgreSQL) factory.getDao(Team.class);
            if (matchList != null && !matchList.isEmpty()) {
                response.setContentType("application/json;charset=UTF-8");
                org.json.JSONWriter jw = new org.json.JSONWriter(response.getWriter());
                jw.array();
                for (Match match : matchList) {
                    jw.object();
                    jw.key("match_id");
                    jw.value(match.getId());
                    jw.key("stage");
                    jw.value(match.getStage());
                    jw.key("data");
                    jw.value(match.getMatchData());
                    jw.key("owner");
                    if (match.getOwnerId() != null) {
                        jw.value(teams.getByPK(match.getOwnerId()).getTitle());
                    } else {
                        jw.value("");
                    }
                    jw.key("guests");
                    if (match.getGuestsId() != null) {
                        jw.value(teams.getByPK(match.getGuestsId()).getTitle());
                    } else {
                        jw.value("");
                    }
                    jw.key("result");
                    if (!(match.getOwnerScore() == null || match.getGuestsScore() == null)) {
                        jw.value(match.getOwnerScore() + " : " + match.getGuestsScore());
                    } else {
                        jw.value("");
                    }
                    jw.key("nextMatchId");
                    jw.value(match.getNextMatchId());
                    jw.key("state");
                    jw.value(match.getStatus());
                    jw.endObject();
                }
                jw.endArray();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (PersistException e) {
            e.printStackTrace();
        }
    }

}
