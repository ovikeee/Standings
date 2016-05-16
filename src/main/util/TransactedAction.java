import javax.sql.DataSource;

/**
 * Created by User on 16.05.2016.
 */
public interface TransactedAction {
    Object execute() throws Exception;
    //DataSource getDataSource();
}
