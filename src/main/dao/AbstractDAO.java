import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.sql.*;
import java.util.Collections;
import java.util.List;

public abstract class AbstractDAO<T extends Identified<PK>, PK extends Integer> implements GenericDao<T, PK> {

    // protected Connection connection;
    private DataSource dataSource;
    protected JdbcTemplate jdbcTemplate;

    /**
     * <p>
     * SELECT * FROM [Table]
     */
    public abstract String getSelectQuery();

    /**
     * <p>
     * INSERT INTO [Table] ([column, column, ...]) VALUES (?, ?, ...);
     */
    public abstract String getInsertQuery();


    public abstract PreparedStatementSetter getPSSInsertQuery(T object);

    /**
     * <p>
     * UPDATE [Table] SET [column = ?, column = ?, ...] WHERE id = ?;
     */
    public abstract String getUpdateQuery();


    public abstract PreparedStatementSetter getPSSUpdateQuery(T object);

    /**
     * <p>
     * DELETE FROM [Table] WHERE id= ?;
     */
    public abstract String getDeleteQuery();



    protected abstract List<T> parseResultSet(ResultSet rs) throws PersistException;

    protected abstract void prepareStatementForInsert(PreparedStatement statement, T object) throws PersistException;

    protected abstract void prepareStatementForUpdate(PreparedStatement statement, T object) throws PersistException;

    @NotNull
    @Override
//    public T getByPKForTranz(Integer key) throws PersistException {
//        T result = null;
//        try {
//            connection.setAutoCommit(false);
//            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
//            result = getByPKForTranz(key);
//            connection.commit();
//        } catch (Exception e) {
//            e.printStackTrace();//todo Логгер
//            try {
//                connection.rollback();
//                throw new PersistException("Транзакция не выполнена");
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//        } finally {
//            try {
//                connection.setAutoCommit(true);
//                connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);//отключаем поддержку транзакций (устанавливаем по-умолчанию)
//            } catch (SQLException e) {
//                e.printStackTrace();//todo Логгер
//            }
//        }
//        return result;
//    }

    public T getByPK(Integer key) throws PersistException{//, SQLException {
        List<T> list = null;
         list = jdbcTemplate.query(getSelectQuery() +  " WHERE id = ?",new Object[]{key}, getMapper());
        if (list.size() == 0) {
            throw new PersistException("Element not found!");
        }
        if (list.size() > 1) {
            throw new PersistException("Found several elements by one PK!");
        }
        return list.iterator().next();
    }

    @NotNull
    @Override
//    public List<T> getAllForTranz() throws PersistException {
//        List<T> list = null;
//        try {
//            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
//            connection.setAutoCommit(false);
//            list = getAllForTranz();
//            connection.commit();
//        } catch (Exception e) {
//            e.printStackTrace();//todo Логгер
//            try {
//                connection.rollback();
//                throw new PersistException("Транзакция не выполнена");
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//        } finally {
//            try {
//                connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);//отключаем поддержку транзакций (устанавливаем по-умолчанию)
//                connection.setAutoCommit(true);
//            } catch (SQLException e) {
//                e.printStackTrace();//todo Логгер
//            }
//        }
//        return list;
//    }
    public List<T> getAll() throws PersistException{//, SQLException {
        return jdbcTemplate.query(getSelectQuery(), getMapper());
    }

    @NotNull
    @Override
//    public List<T> findByIntParamForTranz(String param, int value) throws PersistException {
//        List<T> list = null;
//        try {
//            connection.setAutoCommit(false);
//            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
//            list = findByIntParamForTranz(param, value);
//            connection.commit();
//        } catch (Exception e) {
//            e.printStackTrace();//todo Логгер
//            try {
//                connection.rollback();
//                throw new PersistException("Транзакция не выполнена");
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//        } finally {
//            try {
//                connection.setAutoCommit(true);
//                connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);//отключаем поддержку транзакций (устанавливаем по-умолчанию)
//            } catch (SQLException e) {
//                e.printStackTrace();//todo Логгер
//            }
//        }
//        return list;
//    }

    public List<T> findByIntParam(String param, int value) throws PersistException{//, SQLException {
        return jdbcTemplate.query(getSelectQuery() + " WHERE " + param + " = ?", new Object[]{value},getMapper());
    }


//    public List<T> findByStringParamForTranz(String param, String value) throws PersistException {
//        List<T> list = null;
//        try {
//            connection.setAutoCommit(false);
//            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
//            list = findByStringParamForTranz(param, value);
//            connection.commit();
//        } catch (Exception e) {
//            e.printStackTrace();//todo Логгер
//            try {
//                connection.rollback();
//                throw new PersistException("Транзакция не выполнена");
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//        } finally {
//            try {
//                connection.setAutoCommit(true);
//                connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);//отключаем поддержку транзакций (устанавливаем по-умолчанию)
//            } catch (SQLException e) {
//                e.printStackTrace();//todo Логгер
//            }
//        }
//        return list;
//    }
@NotNull
@Override
    public List<T> findByStringParam(String param, String value) throws PersistException{//, SQLException {
        return jdbcTemplate.query(getSelectQuery() + " WHERE " + param + " LIKE  ? ", new Object[]{value},getMapper());
    }

    @NotNull
    @Override
//    public List<T> findByDateParamForTranz(String param, Date value) throws PersistException {
//        List<T> list = null;
//        try {
//            connection.setAutoCommit(false);
//            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
//            list = findByDateParamForTranz(param, value);
//            connection.commit();
//        } catch (Exception e) {
//            e.printStackTrace();//todo Логгер
//            try {
//                connection.rollback();
//                throw new PersistException("Транзакция не выполнена");
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//        } finally {
//            try {
//                connection.setAutoCommit(true);
//                connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);//отключаем поддержку транзакций (устанавливаем по-умолчанию)
//            } catch (SQLException e) {
//                e.printStackTrace();//todo Логгер
//            }
//        }
//        return list;
//    }

    public List<T> findByDateParam(String param, Date value) throws PersistException{//, SQLException {
        return jdbcTemplate.query(getSelectQuery() +  " WHERE " + param + " =  ? ", new Object[]{value},getMapper());
    }

    /**
     * сохраняем объект в базу данных и получаем новый объект с присвоенным Id
     */
    @NotNull
    @Override
//    public T persistForTranz(T object) throws PersistException {
//        T result = null;
//        try {
//            connection.setAutoCommit(false);
//            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
//            result = persistForTranz(object);
//            connection.commit();
//        } catch (Exception e) {
//            e.printStackTrace();//todo Логгер
//            try {
//                connection.rollback();
//                throw new PersistException("Транзакция не выполнена");
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//        } finally {
//            try {
//                connection.setAutoCommit(true);
//                connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);//отключаем поддержку транзакций (устанавливаем по-умолчанию)
//            } catch (SQLException e) {
//                e.printStackTrace();//todo Логгер
//            }
//        }
//        return result;
//    }
    //
    // todo  @Transactional
    public T persist(T object) throws PersistException{//, SQLException {
        if (object.getId() != null) {
            throw new PersistException("Object is already persist.");
        }
        if (jdbcTemplate.update(getInsertQuery(), getPSSInsertQuery(object)) != 1) {
            throw new PersistException("Добавление некорректно");
        }
        List<T> list = jdbcTemplate.query(getSelectQuery() + " WHERE id = lastval()", getMapper());
        if (list.size() != 1) {
            throw new PersistException("Добавление некорректно");
        }
        return list.iterator().next();
    }

    protected abstract RowMapper<T> getMapper();

    @NotNull
    @Override
//    public T copyForTranz(Integer key) throws PersistException {
//        T result = null;
//        try {
//            connection.setAutoCommit(false);
//            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
//            result = copyForTranz(key);
//            connection.commit();
//        } catch (Exception e) {
//            e.printStackTrace();//todo Логгер
//            try {
//                connection.rollback();
//                throw new PersistException("Транзакция не выполнена");
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//        } finally {
//            try {
//                connection.setAutoCommit(true);
//                connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);//отключаем поддержку транзакций (устанавливаем по-умолчанию)
//            } catch (SQLException e) {
//                e.printStackTrace();//todo Логгер
//            }
//        }
//        return result;
//    }

    public T copy(Integer key) throws PersistException{
        if (key == null) {
            throw new PersistException("Указан не верный ключ");
        }
        T copyObject = getByPK(key);
        if (jdbcTemplate.update(getInsertQuery(), getPSSInsertQuery(copyObject)) != 1) {
            throw new PersistException("Добавление некорректно");
        }
        List<T> list = jdbcTemplate.query(getSelectQuery() + " WHERE id = lastval()", getMapper());
        if (list.size() != 1) {
            throw new PersistException("Копирование не удалось");
        }
        return list.iterator().next();
    }

    @NotNull
    @Override
//    public void updateForTranz(T object) throws PersistException {
//        try {
//            connection.setAutoCommit(false);
//            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
//            updateForTranz(object);
//            connection.commit();
//        } catch (Exception e) {
//            e.printStackTrace();//todo Логгер
//            try {
//                connection.rollback();
//                throw new PersistException("Транзакция не выполнена");
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//        } finally {
//            try {
//                connection.setAutoCommit(true);
//                connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);//отключаем поддержку транзакций (устанавливаем по-умолчанию)
//            } catch (SQLException e) {
//                e.printStackTrace();//todo Логгер
//            }
//        }
//    }

    public void update(T object) throws PersistException{//, SQLException {
//        String sql = getUpdateQuery();
//        PreparedStatement statement = connection.prepareStatement(sql);
//        prepareStatementForUpdate(statement, object);
//        int count = statement.executeUpdate();
        if (jdbcTemplate.update(getUpdateQuery(), getPSSUpdateQuery(object)) != 1) {
            throw new PersistException("Изменения не внесены");
        }else {
            System.out.println("Изменения внесены");
        }
    }

    @NotNull
    @Override
//    public void deleteForTranz(T object) throws PersistException {
//        try {
//            connection.setAutoCommit(false);
//            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
//            deleteForTranz(object);
//            connection.commit();
//        } catch (Exception e) {
//            e.printStackTrace();//todo Логгер
//            try {
//                connection.rollback();
//                throw new PersistException("Транзакция не выполнена");
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//        } finally {
//            try {
//                connection.setAutoCommit(true);
//                connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);//отключаем поддержку транзакций (устанавливаем по-умолчанию)
//            } catch (SQLException e) {
//                e.printStackTrace();//todo Логгер
//            }
//        }
//    }

    public void delete(T object) throws PersistException{//, SQLException {
        DataSourceTransactionManager dstm = new DataSourceTransactionManager(dataSource);//
        TransactionStatus ts = dstm.getTransaction(new DefaultTransactionDefinition());
        try {
            //dstm.commit();
            if (jdbcTemplate.update(getDeleteQuery(), new Object[]{object.getId()}) != 1) {
                throw new PersistException("Удаление не произошло");
            }
            dstm.commit(ts);
        }
        catch (Exception e){
            dstm.rollback(ts);
        }
    }

    public AbstractDAO(DataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public DataSource getDataSource() {
        return dataSource;
    }
    //todo getDataSource
}