
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
@WebServlet(urlPatterns = "/teams", name = "/Teams")
public class Teams extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
        try {
//            final DaoFactory<Connection> factory = new DaoFactoryPostgreSQL();
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup("java:jboss/datasources/PostgreDataSource");//JNDI
            Connection connection = ds.getConnection();
            if (connection != null) {
                DaoTeamPostgreSQL teams = new DaoTeamPostgreSQL(connection);
                List<Team> teamList = teams.getAll();

                //настройка передачи данных
                response.setContentType("application/json;charset=UTF-8");
                org.json.JSONWriter jw = new org.json.JSONWriter(response.getWriter());
                jw.array();
                for (Team team: teamList) {
                    jw.object();
                    jw.key("teamId");
                    jw.value(team.getId());
                    jw.key("teamTitle");
                    jw.value(team.getTitle());
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

