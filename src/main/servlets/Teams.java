
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
@WebServlet(urlPatterns = "/teams", name = "/Teams")
public class Teams extends HttpServlet {
    static DaoFactory<Connection> factory = new DaoFactoryPostgreSQL();
    static DaoTeamPostgreSQL teams;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
        try {
            teams = (DaoTeamPostgreSQL) factory.getDao(Team.class);//создаем ДАО объект для работы с таблией команд
            String type = request.getParameter("type");
            List<Team> teamList = null;
            if (type != null) {
                switch (type) {
                    case "showAllTeam":
                        teamList = teams.getAll(); //получение всех турниров
                        break;
                    case "find":
                        teamList = findBy(request.getParameter("findType"), request.getParameter("value"));
                        break;
                    case "createTeam":
                        if (!createTeam(request)) {
                            response.setStatus(445);//добавление не удалось
                        }
                        teamList = teams.getAll(); //получение всех матчей
                        break;
                    case "editTeam":
                        if (!editTeam(request)) {            //изменяем матч
                            response.setStatus(446);//изменение не удалось
                        }
                        teamList = teams.getAll(); //получение всех матчей
                        break;
                    case "removeTeam":
                        String teamId = request.getParameter("teamId");
                        if (teamId != null) {//i8puhjmnnnnnnnnnnnnnnnnnnnnnnnnnnnnnujjjjjjjjjjjjjjjjjo.0lok
                            teams.delete(teams.getByPK(Integer.parseInt(teamId)));//удаляем
                        } else {//удалить не удалось
                            response.setStatus(490);
                        }
                        teamList = teams.getAll(); //получение всех матчей
                        break;
                    default:
                        response.setStatus(490);
                        teamList = teams.getAll(); //получение всех матчей
                }
                sendJSON(response, teamList);//отправка JSON данных
            }
        } catch (PersistException e1) {
            e1.printStackTrace();
            response.setStatus(490);
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(490);
        }
    }

    private List<Team> findBy(String findType, String value) {
        List<Team> result = null;// = new LinkedList();
        try {
            switch (findType) {
                case "title":
                    result = teams.findByStringParam(findType, value);
                    break;
                default:
                    result = teams.findByIntParam(findType, Integer.parseInt(value));
            }
        } catch (PersistException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void sendJSON(HttpServletResponse response, List<Team> teamList) {
        try {
            if (teamList != null) {
                response.setContentType("application/json;charset=UTF-8");
                org.json.JSONWriter jw = new org.json.JSONWriter(response.getWriter());
                jw.array();
                for (Team team : teamList) {
                    jw.object();
                    jw.key("teamId");
                    jw.value(team.getId());
                    jw.key("teamTitle");
                    jw.value(team.getTitle());
                    jw.endObject();
                }
                jw.endArray();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private boolean createTeam(HttpServletRequest request) {
        try {
            Team team = new Team();
          String title = request.getParameter("teamTitle");
            if (title!= null && title != "") {
                team.setTitle(title);
            } else {
                return false;
            }
            return  teams.persist(team) != null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean editTeam(HttpServletRequest request) {
        try {
            Team team = new Team();
            Integer checkInt = Checker.getInt(request.getParameter("teamId"));
            String title = request.getParameter("teamTitle");
            if (checkInt != null) {
                team.setId(checkInt);
            } else {
                throw new PersistException("Поле teamId не заданно");
            }
            if (title!= null && title != "") {
                team.setTitle(title);
            } else {
                return false;
            }
           teams.update(team);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
