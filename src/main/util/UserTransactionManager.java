import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;

/**
 * Created by User on 16.05.2016.
 */
public class UserTransactionManager {
    private static final UserTransactionManager INSTANCE = new UserTransactionManager();
    ;

    private UserTransactionManager() {
    }

    public static UserTransactionManager getInstance() {
        return INSTANCE;
    }

    void doInTransaction(TransactedAction action, DataSource dataSource) {
        DataSourceTransactionManager dstm = new DataSourceTransactionManager(dataSource);//
        TransactionStatus ts = dstm.getTransaction(new DefaultTransactionDefinition());
        try {
            action.execute();
            dstm.commit(ts);
        }catch (Exception e){
            dstm.rollback(ts);
            //log
        }
    }

}
