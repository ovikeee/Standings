
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
        try {//[][[][]
            matches = (DaoMatchPostgreSQL) factory.getDao(Match.class);//создаем ДАО объект для работы с таблией матчей
            String type = request.getParameter("type");
            List<Match> matchList = null;
            if (type != null) {
                switch (type) {
                    case "showAllMatches":
                        matchList = matches.getAll(); //получение всех матчей
                        break;
                    case "copyMatch":
                        System.out.println(request.getParameter("matchId"));
                        matches.copy(Checker.isInt(request.getParameter("matchId")));
                        matchList = matches.getAll();
                        break;
                    case "find":
                        String tmp = request.getParameter("findType");
                        if(tmp.equals("stage")||tmp.equals("status")){
                            matchList = matches.findByStringParam(request.getParameter("findType"), request.getParameter("value"));
                        }else if(tmp.equals("match_data")){
                            matchList = matches.findByDateParam(request.getParameter("findType"), request.getParameter("value"));
                        }else {
                            matchList = matches.findByIntParam(request.getParameter("findType"), request.getParameter("value"));
                        }
                        break;
                    case "addMatch":
                        if (!addMatch(request)) {            //добавляем матч
                            response.setStatus(445);//добавление не удалось
                        }
                        matchList = matches.getAll(); //получение всех матчей
                        break;
                    case "editMatch":
                        if (!editMatch(request)) {            //изменяем матч
                            response.setStatus(446);//изменение не удалось
                        }
                        matchList = matches.getAll(); //получение всех матчей
                        break;
                    case "removeMatch":
                        String matchId = request.getParameter("matchId");
                        if (matchId != null) {//i8puhjmnnnnnnnnnnnnnnnnnnnnnnnnnnnnnujjjjjjjjjjjjjjjjjo.0lok
                            matchList = matches.getCascadeMatches(Integer.parseInt(matchId));//иерархия матчей, которая удалится
                            if (matchList.size() == 1) {////проверям, чтобы этот матч не был ни для кого следующим
                                removeMatch(Integer.parseInt(matchId));//удаляем
                                matchList = matches.getAll(); //получение всех матчей
                            } else {//отправляем клиенту вопрос: желает ли он каскадно удалить этот матч?
                                response.setStatus(555);//сообщаем клиенту, что удалится более одного матча
                            }
                        } else {//операция не выполнена: matchId null
                            response.setStatus(444);
                        }
                        break;
                    case "cascadeRemove":
                        matchId = request.getParameter("matchId");
                        if (matchId != null) {
                            removeMatch(Integer.parseInt(matchId));//удаляем
                            matchList = matches.getAll(); //получение всех матчей
                        }
                        break;
                    default:
                        response.setStatus(490);
                        matchList = matches.getAll(); //получение всех матчей
                }
                sendJSON(response, matchList);//отправка JSON данных
            }
        } catch (PersistException e) {
            e.printStackTrace();
            response.setStatus(490);
        }
    }

    /**
     * вернет true - если добавление прошло успешно,
     * иначе false
     */
    private boolean addMatch(HttpServletRequest request) {
        try {
            Match match = new Match(); //(Match) factory.getDao(Match.class).create();
            setMatch(match,request);
            return matches.persist(match) != null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean editMatch(HttpServletRequest request){
        try {
            Match match = new Match(); //(Match) factory.getDao(Match.class).create();
            Integer checkInt = Checker.isInt(request.getParameter("matchId"));
            if (checkInt != null) {
                match.setId(checkInt);
            } else {
                throw new PersistException("Поле matchId не заданно");
            }
            setMatch(match,request);
            matches.update(match);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

    private void setMatch(Match match, HttpServletRequest request) throws PersistException, ParseException {

        StringTokenizer st = new StringTokenizer(request.getParameter("scoreId"), ":");
        Integer checkInt;
        checkInt = Checker.isInt(request.getParameter("tournamentId"));
        if (checkInt != null) {
            match.setTournamentId(checkInt);
        } else {//не должно быть null и такой турнир должен существовать
            throw new PersistException("Поле tournamentId не заданно");
        }
        match.setStage(Checker.notEmpty(request.getParameter("stageId")));//установил null или переданное значение
        java.util.Date utilDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(request.getParameter("dateId"));
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        match.setMatchData(sqlDate);//если excepion не вылетел выше, то добавляем
        match.setOwnerId(Checker.isInt(request.getParameter("ownerId")));//установил либо null либо значение
        match.setGuestsId(Checker.isInt(request.getParameter("guestsId")));
        if (st.hasMoreTokens()) {
            match.setOwnerScore(Checker.isInt(st.nextToken()));
        }
        if (st.hasMoreTokens()) {
            match.setGuestsScore(Checker.isInt(st.nextToken()));
        }
        match.setNextMatchId(Checker.isInt(request.getParameter("next_matchId")));
        match.setStatus(Checker.notEmpty(request.getParameter("statusId")));


    }

    private void removeMatch(int matchId) throws PersistException {
        Match match = new Match();
        match.setId(matchId);
        matches.delete(match);
    }

    private void sendJSON(HttpServletResponse response, List<Match> matchList) {
        try {
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
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}

