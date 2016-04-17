

//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.json.simple.JSONObject;
//import org.json.simple.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
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
                    case "loadSelectBox":
                        sendSelectors(response);
                        //    sendJSON(response, matchList);//отправка JSON данных
                        break;
                    case "loadSelectBoxTournament":
                        sendSelectorsTournament(response);
                        break;
                    case "copyMatch":
                        //System.out.println(request.getParameter("matchId"));
                        matches.copy(Checker.getInt(request.getParameter("matchId")));
                        matchList = matches.getAll();
                        break;
                    case "find":
                        matchList = findBy(request.getParameter("findType"), request.getParameter("value"));
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
                if (type != "loadSelectBox" && type !="loadSelectBoxTournament" ) {
                    sendJSON(response, matchList);//отправка JSON данных
                }
            }
        } catch (PersistException e) {
            e.printStackTrace();
            response.setStatus(490);
        }
    }

    private List<Match> findBy(String findType, String value) {
        List<Match> result = null;// = new LinkedList();
        DaoTeamPostgreSQL teams;
        DaoTournamentPostgreSQL tournaments;
        try {
            teams = (DaoTeamPostgreSQL) factory.getDao(Team.class);
            tournaments = (DaoTournamentPostgreSQL) factory.getDao(Tournament.class);
            switch (findType) {
                case "stage":
                case "status":
                    result = matches.findByStringParam(findType, value);
                    break;
                case "match_data":
                    result = matches.findByDateParam(findType, Date.valueOf(value));
                    break;
                case "team_title":
                    List<Team> teamList = teams.findByStringParam("title", value);
                    if(teamList!=null){
                        int id = teamList.iterator().next().getId();
                        result = matches.findByIntParam("owner_id", id);//получаем все матчи, где учавствует команда
                        result.addAll(matches.findByIntParam("guests_id", id));// с названием value
                    }
                    break;
                case "tournament_title" :
                    List<Tournament> tournamentList = tournaments.findByStringParam("title", value);
                    if(tournamentList!=null){
                        int id = tournamentList.iterator().next().getId();
                        result = matches.findByIntParam("tournament_id", id);//получаем все матчи турнира value
                    }
                    break;
                default:
                    result = matches.findByIntParam(findType, Integer.parseInt(value));//получаем все матчи, где учавствует команда
            }
        } catch (PersistException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * вернет true - если добавление прошло успешно,
     * иначе false
     */
    private boolean addMatch(HttpServletRequest request) {
        try {
            Match match = new Match(); //(Match) factory.getDao(Match.class).create();
            setMatch(match, request);
            return matches.persist(match) != null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean editMatch(HttpServletRequest request) {
        try {
            Match match = new Match(); //(Match) factory.getDao(Match.class).create();
            Integer checkInt = Checker.getInt(request.getParameter("matchId"));
            if (checkInt != null) {
                match.setId(checkInt);
            } else {
                throw new PersistException("Поле matchId не заданно");
            }
            setMatch(match, request);
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
        checkInt = Checker.getInt(request.getParameter("tournamentId"));
        if (checkInt != null) {
            match.setTournamentId(checkInt);
        } else {//не должно быть null и такой турнир должен существовать
            throw new PersistException("Поле tournamentId не заданно");
        }
        match.setStage(Checker.notEmpty(request.getParameter("stageId")));//установил null или переданное значение
        java.util.Date utilDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(request.getParameter("dateId"));
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        match.setMatchData(sqlDate);//если excepion не вылетел выше, то добавляем
        match.setOwnerId(Checker.getInt(request.getParameter("ownerId")));//установил либо null либо значение
        match.setGuestsId(Checker.getInt(request.getParameter("guestsId")));
        if (st.hasMoreTokens()) {
            match.setOwnerScore(Checker.getInt(st.nextToken()));
        }
        if (st.hasMoreTokens()) {
            match.setGuestsScore(Checker.getInt(st.nextToken()));
        }
        match.setNextMatchId(Checker.getInt(request.getParameter("next_matchId")));
        match.setStatus(Checker.notEmpty(request.getParameter("statusId")));


    }

    private void removeMatch(int matchId) throws PersistException {
        Match match = new Match();
        match.setId(matchId);
        matches.delete(match);
    }

    private void sendJSON(HttpServletResponse response, List<Match> matchList) {
        try {
            DaoTeamPostgreSQL teams;
            DaoTournamentPostgreSQL tournaments;
            teams = (DaoTeamPostgreSQL) factory.getDao(Team.class);
            tournaments = (DaoTournamentPostgreSQL) factory.getDao(Tournament.class);
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
                    jw.key("tournament");
                    jw.value(tournaments.getByPK(match.getTournamentId()).getTitle());
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

    private void sendSelectors(HttpServletResponse response) {
        try {
//
//            JSONArray arTeam = new JSONArray();
//            JSONArray arTournament = new JSONArray();
//            JSONObject obj = new JSONObject();
//            JSONObject resultJson = new JSONObject();

            DaoTeamPostgreSQL teams;
            DaoTournamentPostgreSQL tournaments;

            teams = (DaoTeamPostgreSQL) factory.getDao(Team.class);
            tournaments = (DaoTournamentPostgreSQL) factory.getDao(Tournament.class);

            response.setContentType("application/json;charset=UTF-8");
            org.json.JSONWriter jw = new org.json.JSONWriter(response.getWriter());

//            for (Team team : teams.getAll()) {
//                arTeam.add(team.getTitle());
//            }
//            System.out.println(arTeam.toString());
//
//            for (Tournament team : tournaments.getAll()) {
//                arTournament.add(team.getTitle());
//            }
//            System.out.println(arTournament.toString());
//jw.array();
//            jw.object();
//            jw.key("teams");
//            jw.value(arTeam);
//            jw.endObject();
//
//            jw.object();
//            jw.key("tournaments");
//            jw.value(arTournament);
//            jw.endObject();
//jw.endArray();

            jw.array();
            //jw.key("array1");
            for (Team team : teams.getAll()) {
                jw.object();
                jw.key("teams");
                jw.value(team.getTitle());
                jw.key("teamId");
                jw.value(team.getId());
                jw.endObject();
            }
            jw.endArray();

//            jw.array();
//            for (Tournament tournament : tournaments.getAll()) {
//                jw.object();
//                jw.key("tournaments");
//                jw.value(tournament.getTitle());
//                jw.key("tournamentId");
//                jw.value(tournament.getId());
//                jw.endObject();
//            }
//            jw.endArray();


        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (PersistException e) {
            e.printStackTrace();
        }
    }

    private void sendSelectorsTournament(HttpServletResponse response) {
        try {
//
//            JSONArray arTeam = new JSONArray();
//            JSONArray arTournament = new JSONArray();
//            JSONObject obj = new JSONObject();
//            JSONObject resultJson = new JSONObject();

            DaoTournamentPostgreSQL tournaments;

            tournaments = (DaoTournamentPostgreSQL) factory.getDao(Tournament.class);

            response.setContentType("application/json;charset=UTF-8");
            org.json.JSONWriter jw = new org.json.JSONWriter(response.getWriter());

//            for (Team team : teams.getAll()) {
//                arTeam.add(team.getTitle());
//            }
//            System.out.println(arTeam.toString());
//
//            for (Tournament team : tournaments.getAll()) {
//                arTournament.add(team.getTitle());
//            }
//            System.out.println(arTournament.toString());
//jw.array();
//            jw.object();
//            jw.key("teams");
//            jw.value(arTeam);
//            jw.endObject();
//
//            jw.object();
//            jw.key("tournaments");
//            jw.value(arTournament);
//            jw.endObject();
//jw.endArray();


            jw.array();
            for (Tournament tournament : tournaments.getAll()) {
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
        } catch (PersistException e) {
            e.printStackTrace();
        }
    }

}

