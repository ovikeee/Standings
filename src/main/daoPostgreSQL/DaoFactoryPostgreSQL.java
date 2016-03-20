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

/**
 * ���������� DaoFactory ��� ���� PostgreSQL.
 *
 * */
public class DaoFactoryPostgreSQL implements DaoFactory<Connection> {

    private String jndi = "java:jboss/datasources/PostgreDataSource";//JNDI
    private Map<Class, DaoCreator> creators; //Map � ������ ���� Class, ������������ ����� ���������� ��������������� ����� ��� ��������
    private Connection connection = null; //���������� � ��

    @Override
    public Connection getContext() throws PersistException {
        return  connection;
    }

    /**
     * ���������� DAO ������, ��� ���������������� ������
     * */
    @Override
    public GenericDao getDao(Class dtoClass) throws PersistException {
        DaoCreator creator = creators.get(dtoClass);
        if (creator == null) {
            throw new PersistException("Dao object for " + dtoClass + " not found.");
        }
        return creator.create(connection);
    }

    public DaoFactoryPostgreSQL() {
        try {
        //�������� �����������
        InitialContext ic = new InitialContext();
        DataSource ds = (DataSource) ic.lookup(jndi);
        connection = ds.getConnection();
        //������������� �����
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
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}