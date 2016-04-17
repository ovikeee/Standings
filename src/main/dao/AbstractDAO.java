import javax.validation.constraints.NotNull;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;

/**
 * Абстрактный класс предоставляющий базовую реализацию CRUD операций с использованием JDBC.
 */
public abstract class AbstractDAO<T extends Identified<PK>, PK extends Integer> implements GenericDao<T, PK> {

    protected Connection connection;

    /**
     * Возвращает sql запрос для получения всех записей.
     * <p/>
     * SELECT * FROM [Table]
     */
    public abstract String getSelectQuery();

    /**
     * Возвращает sql запрос для вставки новой записи в базу данных.
     * <p/>
     * INSERT INTO [Table] ([column, column, ...]) VALUES (?, ?, ...);
     */
    public abstract String getInsertQuery();

    /**
     * Возвращает sql запрос для обновления записи.
     * <p/>
     * UPDATE [Table] SET [column = ?, column = ?, ...] WHERE id = ?;
     */
    public abstract String getUpdateQuery();

    /**
     * Возвращает sql запрос для удаления записи из базы данных.
     * <p/>
     * DELETE FROM [Table] WHERE id= ?;
     */
    public abstract String getDeleteQuery();

    /**
     * Разбирает ResultSet и возвращает список объектов соответствующих содержимому ResultSet.
     */
    protected abstract List<T> parseResultSet(ResultSet rs) throws PersistException;

    /**
     * Устанавливает аргументы insert запроса в соответствии со значением полей объекта object.
     */
    protected abstract void prepareStatementForInsert(PreparedStatement statement, T object) throws PersistException;

    /**
     * Устанавливает аргументы update запроса в соответствии со значением полей объекта object.
     */
    protected abstract void prepareStatementForUpdate(PreparedStatement statement, T object) throws PersistException;

    @Override
    public T getByPK(Integer key) throws PersistException {
        List<T> list;
        String sql = getSelectQuery();
        sql += " WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, key);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistException(e);
        }
        if (list == null || list.size() == 0) {
            return null;
        }
        if (list.size() > 1) {
            throw new PersistException("По ключу получено более 1-ой записи");
        }
        return list.iterator().next();
    }

    @Override
    public List<T> getAll() throws PersistException {
        List<T> list;
        String sql = getSelectQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistException(e);
        }

        return list;
    }

    @NotNull
    @Override
    public List<T> findByIntParam(String param, int value) throws PersistException {
        List<T> list;
        String sql = getSelectQuery();
        sql += " WHERE "+param+" = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1,value);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistException(e);
        }

        return list;
    }

    @Override
    public List<T> findByStringParam(String param, String value) throws PersistException {
        List<T> list;
        String sql = getSelectQuery();
        sql += " WHERE "+param+" LIKE  ? ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1,value);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistException(e);
        }

        return list;
    }

    @Override
    public List<T> findByDateParam(String param, Date value) throws PersistException {
        List<T> list;
        String sql = getSelectQuery();
        sql += " WHERE "+param+" =  ? ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1,value);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistException(e);
        }

        return list;
    }


    @Override
    public T persist(T object) throws PersistException {
        if (object.getId() != null) {
            throw new PersistException("Object is already persist.");
        }
        T persistInstance;
        // Добавляем запись
        String sql = getInsertQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForInsert(statement, object);
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistException("Кол-во вставленных записей: " + count);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        // Получаем только что вставленную запись
        sql = getSelectQuery() + " WHERE id = lastval()";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            List<T> list = parseResultSet(rs);
            if ((list == null) || (list.size() != 1)) {//если предыдущее добавление не удалось, то вылетает ошибка
                System.out.println("list.size="+list.size());///!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                throw new PersistException("Не получилось получить только что вставленную запись");
            }
            persistInstance = list.iterator().next();
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistException(e);
        }
        return persistInstance;
    }

    @Override

    public T copy(Integer key) throws PersistException {
        if (key == null) {
            throw new PersistException("Указанный ключ не имеет значения");
        }
        T copyObject = getByPK(key);
        T persistInstance;
        // Добавляем запись
        String sql = getInsertQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForInsert(statement, copyObject);
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistException("Кол-во вставленных записей: " + count);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        // Получаем только что вставленную запись
        sql = getSelectQuery() + " WHERE id = lastval();";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            List<T> list = parseResultSet(rs);//вернуть emptyList

            if ((list == null) || (list.size() != 1)) {
                System.out.println("list.size="+list.size());
                throw new PersistException("Не получилось получить только что вставленную запись");
            }
            persistInstance = list.iterator().next();
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistException(e);
        }
        return persistInstance;
    }


    @Override
    public void update(T object) throws PersistException {
        String sql = getUpdateQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForUpdate(statement, object); // заполнение аргументов запроса оставим на совесть потомков
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistException("Кол-во измененых записей: " + count);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    public void delete(T object) throws PersistException {
        String sql = getDeleteQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try {
                statement.setObject(1, object.getId());
            } catch (Exception e) {
                throw new PersistException(e);
            }
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistException("Кол-во удаленых записей: " + count);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    public AbstractDAO(Connection connection) {
        this.connection = connection;
    }
}