/**
 * Created by User on 19.03.2016.
 */


import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class DaoFactoryPostgreSQL implements DaoFactory<Connection> {

    private String url = "java:jboss/datasources/PostgreDataSource";//URL адрес
    private Map<Class, DaoCreator> creators;

    public Connection getContext() throws PersistException {
        Connection connection = null;
        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup(url);
            connection = ds.getConnection();
        } catch (SQLException e) {
            throw new PersistException(e);
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return  connection;
    }

    @Override
    public GenericDao getDao(Connection connection, Class dtoClass) throws PersistException {
        DaoCreator creator = creators.get(dtoClass);
        if (creator == null) {
            throw new PersistException("Dao object for " + dtoClass + " not found.");
        }
        return creator.create(connection);
    }

    public DaoFactoryPostgreSQL() {

        creators = new HashMap<Class, DaoCreator>();
        creators.put(Match.class, new DaoCreator<Connection>() {
            @Override
            public GenericDao create(Connection connection) {
                return new DaoMatchPostgreSQL(connection);
            }
        });
        creators.put(Team.class, new DaoCreator<Connection>() {
            @Override
            public GenericDao create(Connection connection) {
                return new DaoTeamPostgreSQL(connection);
            }
        });
        creators.put(Tournament.class, new DaoCreator<Connection>() {
            @Override
            public GenericDao create(Connection connection) {
                return new DaoTournamentPostgreSQL(connection);
            }
        });

    }
}