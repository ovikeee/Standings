
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
        try {
//            final DaoFactory<Connection> factory = new DaoFactoryPostgreSQL();
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup("java:jboss/datasources/PostgreDataSource");//JNDI
            Connection connection = ds.getConnection();
            if (connection != null) {
                DaoTournamentPostgreSQL teams = new DaoTournamentPostgreSQL(connection);
                List<Tournament> tournamentsList = teams.getAll();

                //настройка передачи данных
                response.setContentType("application/json;charset=UTF-8");
                org.json.JSONWriter jw = new org.json.JSONWriter(response.getWriter());
                jw.array();
                for (Tournament tournament: tournamentsList) {
                    jw.object();
                    jw.key("tournamentId");
                    jw.value(tournament.getId());
                    jw.key("tournamentTitle");
                    jw.value(tournament.getTitle());
                    jw.key("numberOfTeams");
                    jw.value(tournament.getNumberOfTeams());
                    jw.key("season");
                    jw.value(tournament.getSeason());
                    jw.endObject();
                }
                jw.endArray();
            }
        } catch (NamingException e) {

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PersistException e) {
            e.printStackTrace();
        }
    }
}

