import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet(urlPatterns ="/forward", name = "/Forward")
public class Forward extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {

        System.out.println("11111");
        System.out.println("tournament_editor= " + request.getParameter("tournament_editor"));
        System.out.println("team_editor= " + request.getParameter("team_editor"));

        RequestDispatcher requestDispatcher;
        if (request.getParameter("tournament_editor") != null) {
            System.out.println("22222");
            requestDispatcher = request.getRequestDispatcher("/tournament_editor.jsp");
            requestDispatcher.forward(request, response);
//            PrintWriter out = response.getWriter();
//            try {
//                out.print("<HTML><BODY>This is text document, which has "
//                        + "<A href=\"other.html\">link</A>"
//                        + " to another document</BODY></HTML>");
//            } finally {
//                out.close();
//            }
        } else if (request.getParameter("team_editor") != null) {
            System.out.println("333333");
            requestDispatcher = request.getRequestDispatcher("/team_editor.jsp");
            requestDispatcher.forward(request, response);
        }
        else if (request.getParameter("find_match") != null) {


            //Тестируем Сессию и БД
            System.out.println("5555");
            String mydata="";
            try {
            InitialContext ic = new InitialContext();

            DataSource ds = (DataSource) ic.lookup("java:jboss/datasources/PostgreDataSource");

            Connection connection = ds.getConnection();


            if (connection != null) {

                System.out.println("You made it, take control your database now!");
                Statement statS = connection.createStatement();
                ResultSet rs = statS.executeQuery("SELECT * FROM teams");
               // while (rs.next()) {
                rs.next();
                    mydata=rs.getString(1)+" "+rs.getString(2);
                    //pw.write("Car_id: " + rs.getString(1) + "Model: " + rs.getString(2));

            } else {
                System.out.println("Failed to make connection!");
            }
                connection.close();
                HttpSession session = request.getSession();
                session.setAttribute("myData",mydata);


            requestDispatcher = request.getRequestDispatcher("/index2.jsp");
            requestDispatcher.forward(request, response);
            //запись чего-то в сессию
            } catch (SQLException e) {
                    e.printStackTrace();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
        }
    }
}
