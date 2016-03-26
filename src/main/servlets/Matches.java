
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by User on 18.03.2016.
 */
@WebServlet(urlPatterns = "/matches", name = "/Matches")
public class Matches extends HttpServlet {
    static DaoFactory<Connection> factory = new DaoFactoryPostgreSQL();
    static DaoMatchPostgreSQL matches;


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
        try {
            matches = (DaoMatchPostgreSQL) factory.getDao(Match.class);//создаем ДАО объект для работы с таблией матчей
            String type = request.getParameter("type");
            if (type != null) {
                switch (type) {
                    case "showAllMatches":
                        showTable(response);
                        break;
                    case "addMatch":
                        addMatch(request);
                        showTable(response);
                        break;
                    case "removeMatch":
                        String matchId = request.getParameter("matchId");
                        if (matchId != null) {
                            if (!checkNextMatch(Integer.parseInt(matchId))) {//проверям, чтобы этот матч не был для кого-либо следующим
                                removeMatch(Integer.parseInt(matchId));//удаляем
                                showTable(response);
                            }else {//отправляем клиенту вопрос: желает ли он каскадно удалить этот матч?
                                response.setStatus(555);
                                getCascadeElements(response, Integer.parseInt(matchId));
                            }
                        }
                        break;
                    case "cascadeRemove":

                    default:
                        System.out.println("nothing has been done");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (PersistException e) {
            e.printStackTrace();
        }
    }

    private void getCascadeElements(HttpServletResponse response, int matchId) {
        try {
            List<Match> matchList = matches.getCascadeMatches(matchId);
            //настройка передачи данных
            response.setContentType("application/json;charset=UTF-8");
            org.json.JSONWriter jw = new org.json.JSONWriter(response.getWriter());
            jw.array();
            for (Match match : matchList) {
                jw.object();
                jw.key("match_id");
                jw.value(match.getId());
                jw.key("stage");
                jw.value(match.getStage());
                jw.key("tournamentId");
                jw.value(match.getTournamentId());
                jw.key("data");
                jw.value(match.getMatchData());
                jw.key("owner");
                jw.value(match.getOwnerId());
                jw.key("guests");
                jw.value(match.getGuestsId());
                jw.key("result");
                jw.value(match.getOwnerScore() + " : " + match.getGuestsScore());
                jw.key("nextMatchId");
                jw.value(match.getNextMatchId());
                jw.key("state");
                jw.value(match.getStatus());
                jw.endObject();
            }
            jw.endArray();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Если на этот матч кто-то ссылается, то вернет true
     * Если никто не ссылается, т.е нет матчей у которых этот матч является следующим,
     * то возвращаем false
     */
    private boolean checkNextMatch(int id) {
        return true;
    }

//    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            matches = factory.getDao(Match.class);
//            if (request.getParameter("type").equals("addMatch")) {
//                System.out.println("I'm here");
//                addMatch(request);
//            }
//            showTable(response);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        } catch (PersistException e) {
//            e.printStackTrace();
//        }
//    }

    private void addMatch(HttpServletRequest request) throws ParseException, PersistException {
        Match match = new Match(); //(Match) factory.getDao(Match.class).create();
        java.util.Date utilDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(request.getParameter("dateId"));
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        StringTokenizer st = new StringTokenizer(request.getParameter("scoreId"), ":");
        match.setTournamentId(Integer.parseInt(request.getParameter("tournamentId")));
        match.setStage(request.getParameter("stageId"));
        match.setMatchData(sqlDate);
        match.setOwnerId(Integer.parseInt(request.getParameter("ownerId")));
        match.setGuestsId(Integer.parseInt(request.getParameter("guestsId")));
        match.setOwnerScore(Integer.parseInt(st.nextToken()));
        match.setGuestsScore(Integer.parseInt(st.nextToken()));
        match.setNextMatchId(Integer.parseInt(request.getParameter("next_matchId")));
        match.setStatus(request.getParameter("statusId"));

        matches.persist(match);//сохранение в БД
    }

    private void removeMatch(int matchId) {

        Match match = new Match();
        match.setId(matchId);
        try {
            matches.delete(match);
        } catch (PersistException e) {
            e.printStackTrace();
        }


    }

    private void showTable(HttpServletResponse response) {
        try {
            List<Match> matchList = matches.getAll();
            //настройка передачи данных
            response.setContentType("application/json;charset=UTF-8");
            org.json.JSONWriter jw = new org.json.JSONWriter(response.getWriter());
            jw.array();
            for (Match match : matchList) {
                jw.object();
                jw.key("match_id");
                jw.value(match.getId());
                jw.key("stage");
                jw.value(match.getStage());
                jw.key("tournamentId");
                jw.value(match.getTournamentId());
                jw.key("data");
                jw.value(match.getMatchData());
                jw.key("owner");
                jw.value(match.getOwnerId());
                jw.key("guests");
                jw.value(match.getGuestsId());
                jw.key("result");
                jw.value(match.getOwnerScore() + " : " + match.getGuestsScore());
                jw.key("nextMatchId");
                jw.value(match.getNextMatchId());
                jw.key("state");
                jw.value(match.getStatus());
                jw.endObject();
            }
            jw.endArray();
        } catch (PersistException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}

