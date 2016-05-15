import javax.validation.constraints.NotNull;
import java.sql.*;
import java.util.Collections;
import java.util.List;

public abstract class AbstractDAO<T extends Identified<PK>, PK extends Integer> implements GenericDao<T, PK> {

    protected Connection connection;

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

    /**
     * <p>
     * UPDATE [Table] SET [column = ?, column = ?, ...] WHERE id = ?;
     */
    public abstract String getUpdateQuery();

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
    public T getByPK(Integer key) throws PersistException {
        T result = null;
        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            result = getByPKForTranz(key);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();//todo Логгер
            try {
                connection.rollback();
                throw new PersistException("Транзакция не выполнена");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.setTransactionIsolation(Connection.TRANSACTION_NONE);//отключаем поддержку транзакций (устанавливаем по-умолчанию)
            } catch (SQLException e) {
                e.printStackTrace();//todo Логгер
            }
        }
        return result;
    }
    public T getByPKForTranz(Integer key) throws PersistException, SQLException {
        List<T> list = null;
        String sql = getSelectQuery();
        sql += " WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, key);
        ResultSet rs = statement.executeQuery();
        list = parseResultSet(rs);
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
    public List<T> getAll() throws PersistException {
        List<T> list = null;
        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            list = getAllForTranz();
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();//todo Логгер
            try {
                connection.rollback();
                throw new PersistException("Транзакция не выполнена");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.setTransactionIsolation(Connection.TRANSACTION_NONE);//отключаем поддержку транзакций (устанавливаем по-умолчанию)
            } catch (SQLException e) {
                e.printStackTrace();//todo Логгер
            }
        }
        return list;
    }
    public List<T> getAllForTranz() throws PersistException, SQLException {
        List<T> list;
        String sql = getSelectQuery();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        list = parseResultSet(rs);


        return list;
    }

    @NotNull
    @Override
    public List<T> findByIntParam(String param, int value) throws PersistException {
        List<T> list = null;
        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            list = findByIntParamForTranz(param, value);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();//todo Логгер
            try {
                connection.rollback();
                throw new PersistException("Транзакция не выполнена");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.setTransactionIsolation(Connection.TRANSACTION_NONE);//отключаем поддержку транзакций (устанавливаем по-умолчанию)
            } catch (SQLException e) {
                e.printStackTrace();//todo Логгер
            }
        }
        return list;
    }
    public List<T> findByIntParamForTranz(String param, int value) throws PersistException, SQLException {
        List<T> list;
        String sql = getSelectQuery();
        sql += " WHERE " + param + " = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, value);
        ResultSet rs = statement.executeQuery();
        list = parseResultSet(rs);


        return list;
    }

    @NotNull
    @Override
    public List<T> findByStringParam(String param, String value) throws PersistException {
        List<T> list = null;
        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            list = findByStringParamForTranz(param, value);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();//todo Логгер
            try {
                connection.rollback();
                throw new PersistException("Транзакция не выполнена");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.setTransactionIsolation(Connection.TRANSACTION_NONE);//отключаем поддержку транзакций (устанавливаем по-умолчанию)
            } catch (SQLException e) {
                e.printStackTrace();//todo Логгер
            }
        }
        return list;
    }
    public List<T> findByStringParamForTranz(String param, String value) throws PersistException, SQLException {
        List<T> list;
        String sql = getSelectQuery();
        sql += " WHERE " + param + " LIKE  ? ";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, value);
        ResultSet rs = statement.executeQuery();
        list = parseResultSet(rs);
        return list;
    }

    @NotNull
    @Override
    public List<T> findByDateParam(String param, Date value) throws PersistException {
        List<T> list = null;
        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            list = findByDateParamForTranz(param, value);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();//todo Логгер
            try {
                connection.rollback();
                throw new PersistException("Транзакция не выполнена");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.setTransactionIsolation(Connection.TRANSACTION_NONE);//отключаем поддержку транзакций (устанавливаем по-умолчанию)
            } catch (SQLException e) {
                e.printStackTrace();//todo Логгер
            }
        }
        return list;
    }
    public List<T> findByDateParamForTranz(String param, Date value) throws PersistException, SQLException {
        List<T> list;
        String sql = getSelectQuery();
        sql += " WHERE " + param + " =  ? ";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setDate(1, value);
        ResultSet rs = statement.executeQuery();
        list = parseResultSet(rs);
        return list;
    }

    /**
     * сохраняем объект в базу данных и получаем новый объект с присвоенным Id
     */
    @NotNull
    @Override
    public T persist(T object) throws PersistException {
        T result = null;
        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            result = persistForTranz(object);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();//todo Логгер
            try {
                connection.rollback();
                throw new PersistException("Транзакция не выполнена");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.setTransactionIsolation(Connection.TRANSACTION_NONE);//отключаем поддержку транзакций (устанавливаем по-умолчанию)
            } catch (SQLException e) {
                e.printStackTrace();//todo Логгер
            }
        }
        return result;
    }
    public T persistForTranz(T object) throws PersistException, SQLException {
        if (object.getId() != null) {
            throw new PersistException("Object is already persist.");
        }
        T persistInstance;
        String sql = getInsertQuery();
        PreparedStatement statement = connection.prepareStatement(sql);
        prepareStatementForInsert(statement, object);
        if (statement.executeUpdate() != 1) {
            throw new PersistException("Добавление некорректно");
        }
        sql = getSelectQuery() + " WHERE id = lastval()";
        statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        List<T> list = parseResultSet(rs);
        if (list.size() != 1) {
            throw new PersistException("Добавление некорректно");
        }
        persistInstance = list.iterator().next();
        return persistInstance;
    }

    @NotNull
    @Override
    public T copy(Integer key) throws PersistException {
        T result = null;
        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            result = copyForTranz(key);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();//todo Логгер
            try {
                connection.rollback();
                throw new PersistException("Транзакция не выполнена");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.setTransactionIsolation(Connection.TRANSACTION_NONE);//отключаем поддержку транзакций (устанавливаем по-умолчанию)
            } catch (SQLException e) {
                e.printStackTrace();//todo Логгер
            }
        }
        return result;
    }
    public T copyForTranz(Integer key) throws PersistException, SQLException {
        if (key == null) {
            throw new PersistException("Указан не верный ключ");
        }
        T copyObject = getByPK(key);
        String sql = getInsertQuery();
        PreparedStatement statement = connection.prepareStatement(sql);
        prepareStatementForInsert(statement, copyObject);
        int count = statement.executeUpdate();
        if (count != 1) {
            throw new PersistException("Копирование не удалось");
        }
        sql = getSelectQuery() + " WHERE id = lastval();";
        statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        List<T> list = parseResultSet(rs);
        if (list.size() != 1) {
            throw new PersistException("Копирование не удалось");
        }
        return list.iterator().next();
    }

    @NotNull
    @Override
    public void update(T object) throws PersistException {
        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            updateForTranz(object);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();//todo Логгер
            try {
                connection.rollback();
                throw new PersistException("Транзакция не выполнена");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.setTransactionIsolation(Connection.TRANSACTION_NONE);//отключаем поддержку транзакций (устанавливаем по-умолчанию)
            } catch (SQLException e) {
                e.printStackTrace();//todo Логгер
            }
        }
    }
    public void updateForTranz(T object) throws PersistException, SQLException {
        String sql = getUpdateQuery();
        PreparedStatement statement = connection.prepareStatement(sql);
        prepareStatementForUpdate(statement, object);
        int count = statement.executeUpdate();
        if (count != 1) {
            throw new PersistException("Изменения не внесены");
        }
    }

    @NotNull
    @Override
    public void delete(T object) throws PersistException {
        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            deleteForTranz(object);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();//todo Логгер
            try {
                connection.rollback();
                throw new PersistException("Транзакция не выполнена");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.setTransactionIsolation(Connection.TRANSACTION_NONE);//отключаем поддержку транзакций (устанавливаем по-умолчанию)
            } catch (SQLException e) {
                e.printStackTrace();//todo Логгер
            }
        }
    }
    public void deleteForTranz(T object) throws PersistException, SQLException {
        String sql = getDeleteQuery();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setObject(1, object.getId());
        int count = statement.executeUpdate();
        if (count != 1) {
            throw new PersistException("Удаление не произошло");
        }
    }

    public AbstractDAO(Connection connection) {
        this.connection = connection;
    }
}