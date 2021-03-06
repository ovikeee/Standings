
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
import java.util.List;

/**
 * Created by User on 18.03.2016.
 */
@WebServlet(urlPatterns = "/tournaments", name = "/Tournaments")
public class Tournaments extends HttpServlet {
    static DaoFactory<Connection> factory = new DaoFactoryPostgreSQL();
    static DaoTournamentPostgreSQL tournaments;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
        try {
            tournaments = (DaoTournamentPostgreSQL) factory.getDao(Tournament.class);//создаем ДАО объект для работы с таблией турниров

            String type = request.getParameter("type");
            List<Tournament> tournamentList = null;
            if (type != null) {
                switch (type) {
                    case "showAllTournament":
                        tournamentList = tournaments.getAll(); //получение всех турниров
                        break;
                    case "find":
                        tournamentList = findBy(request.getParameter("findType"), request.getParameter("value"));
                        break;
                    case "createTournament":
                        if (!createTournament(request)) {
                            response.setStatus(445);//добавление не удалось
                        }
                        tournamentList = tournaments.getAll(); //получение всех матчей
                        break;
                    case "editTournament":
                        if (!editTournament(request)) {            //изменяем матч
                            response.setStatus(446);//изменение не удалось
                        }
                        tournamentList = tournaments.getAll(); //получение всех матчей
                        break;
                    case "removeTournament":
                        String tournamentId = request.getParameter("tournamentId");
                        if (tournamentId != null) {//i8puhjmnnnnnnnnnnnnnnnnnnnnnnnnnnnnnujjjjjjjjjjjjjjjjjo.0lok
                            tournaments.delete(tournaments.getByPK(Integer.parseInt(tournamentId)));//удаляем
                        } else {//удалить не удалось
                            response.setStatus(490);
                        }
                        tournamentList = tournaments.getAll(); //получение всех матчей
                        break;
                    default:
                        response.setStatus(490);
                        tournamentList = tournaments.getAll(); //получение всех матчей
                }
                sendJSON(response, tournamentList);//отправка JSON данных
            }
        } catch (PersistException e1) {
            e1.printStackTrace();
            response.setStatus(490);
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(490);
        }
    }

    private List<Tournament> findBy(String findType, String value) {
        List<Tournament> result = null;// = new LinkedList();
        try {
            switch (findType) {
                case "title":
                    result = tournaments.findByStringParam(findType, value);
                    break;
                default:
                    result = tournaments.findByIntParam(findType, Integer.parseInt(value));//получаем все матчи, где учавствует команда
            }
        } catch (PersistException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void sendJSON(HttpServletResponse response, List<Tournament> tournamentList) {
        try {
            if (tournamentList != null) {
                response.setContentType("application/json;charset=UTF-8");
                org.json.JSONWriter jw = new org.json.JSONWriter(response.getWriter());
                jw.array();
                for (Tournament tournament : tournamentList) {
                    jw.object();
                    jw.key("tournamentId");
                    jw.value(tournament.getId());
                    jw.key("tournamentTitle");
                    jw.value(tournament.getTitle());
                    jw.key("numberOfTeams");
                    if (tournament.getNumberOfTeams() != null) {
                        jw.value(tournament.getNumberOfTeams());
                    } else {
                        jw.value("");
                    }
                    jw.endObject();
                }
                jw.endArray();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private boolean createTournament(HttpServletRequest request) {
        try {
            Tournament tournament = new Tournament(); //(Match) factory.getDao(Match.class).create();
            Integer teamNumber = Checker.getInt(request.getParameter("teamNumber"));
            tournament.setNumberOfTeams(teamNumber);//установит либо число либо null
            if (request.getParameter("tournamentTitle") != null && request.getParameter("tournamentTitle") != "") {
                tournament.setTitle(request.getParameter("tournamentTitle"));
            } else {
                return false;
            }
            Tournament persistTournament = tournaments.persist(tournament);
            if (persistTournament != null) {//создаем матчи
                int tournamentId = persistTournament.getId();
                DaoFactory<Connection> factory = new DaoFactoryPostgreSQL();
                DaoMatchPostgreSQL matches = (DaoMatchPostgreSQL) factory.getDao(Match.class);//создаем ДАО объект для работы с таблией матчей
                Match match;
                int k = 0;
                int nextMatchId = 2;
                int startMatchId;
                String stage;
                Match persistMatch;

                //финальный матч
                match = new Match();
                match.setTournamentId(tournamentId);
                match.setStage("final");
                persistMatch = matches.persist(match);
                if (persistMatch == null) {
                    System.out.println("Матч не создан");
                    throw new Exception();
                } else {
                    startMatchId = persistMatch.getId();//сохраняем id добавленого матча
                }


//Не будет работать, если кто-нибудь во время создания таблицы добавит матч.
                for (int i = 2; i < teamNumber; i *= 2) {//пробегаем по этапу турнира (от полуфинала к начальным матчам)
                    if (i * 2 >= teamNumber) {//если команды не покроют все матчи более раннего этапа, то прекращаем создавать этапы
                        k = teamNumber - i + 1; //досоздаем оставшиеся матчи
                    } else {
                        k = i; //создаем матчи текущего этапа
                    }
                    stage="1/"+i;
                    for (int j = 0; j < k; j++) {////создаем или досоздаем матчи текущего этапа
                        match = new Match();
                        match.setTournamentId(tournamentId);
                        match.setStage(stage);
                        match.setNextMatchId(startMatchId+nextMatchId/2-1);//устанавливаем id следующего матча для 1-ого(финального) null
                        persistMatch = matches.persist(match);
                        if(persistMatch==null){
                            System.out.println("Матч не создан");
                            throw  new Exception();
                        }else {
                            nextMatchId++;
                        }
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean editTournament(HttpServletRequest request) {
        try {
            Tournament tournament = new Tournament(); //(Match) factory.getDao(Match.class).create();
            Integer checkInt = Checker.getInt(request.getParameter("tournamentId"));
            if (checkInt != null) {
                tournament.setId(checkInt);
            } else {
                throw new PersistException("Поле tournamentId не заданно");
            }
            tournament.setNumberOfTeams(Checker.getInt(request.getParameter("teamNumber")));//установит либо число либо null
            if (request.getParameter("tournamentTitle") != null && request.getParameter("tournamentTitle") != "") {
                tournament.setTitle(request.getParameter("tournamentTitle"));
            }
            tournaments.update(tournament);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

}

