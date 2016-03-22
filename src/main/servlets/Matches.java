
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
@WebServlet(urlPatterns = "/matches", name = "/Matches")
public class Matches extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
        try {
            final DaoFactory<Connection> factory = new DaoFactoryPostgreSQL();
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup("java:jboss/datasources/PostgreDataSource");//JNDI
            Connection connection = ds.getConnection();
            if (connection != null) {
                //Match match = (Match)  factory.getDao( Match.class).create();
                DaoMatchPostgreSQL matches = new DaoMatchPostgreSQL(connection);
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
            }
        } catch (NamingException e) {

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PersistException e) {
            e.printStackTrace();
        }
    }
}

