import javax.validation.constraints.NotNull;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;

/**
 * ����������� ����� ��������������� ������� ���������� CRUD �������� � �������������� JDBC.
 */
public abstract class AbstractDAO<T extends Identified<PK>, PK extends Integer> implements GenericDao<T, PK> {

    protected Connection connection;

    /**
     * ���������� sql ������ ��� ��������� ���� �������.
     * <p/>
     * SELECT * FROM [Table]
     */
    public abstract String getSelectQuery();

    /**
     * ���������� sql ������ ��� ������� ����� ������ � ���� ������.
     * <p/>
     * INSERT INTO [Table] ([column, column, ...]) VALUES (?, ?, ...);
     */
    public abstract String getInsertQuery();

    /**
     * ���������� sql ������ ��� ���������� ������.
     * <p/>
     * UPDATE [Table] SET [column = ?, column = ?, ...] WHERE id = ?;
     */
    public abstract String getUpdateQuery();

    /**
     * ���������� sql ������ ��� �������� ������ �� ���� ������.
     * <p/>
     * DELETE FROM [Table] WHERE id= ?;
     */
    public abstract String getDeleteQuery();

    /**
     * ��������� ResultSet � ���������� ������ �������� ��������������� ����������� ResultSet.
     */
    protected abstract List<T> parseResultSet(ResultSet rs) throws PersistException;

    /**
     * ������������� ��������� insert ������� � ������������ �� ��������� ����� ������� object.
     */
    protected abstract void prepareStatementForInsert(PreparedStatement statement, T object) throws PersistException;

    /**
     * ������������� ��������� update ������� � ������������ �� ��������� ����� ������� object.
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
            throw new PersistException("�� ����� �������� ����� 1-�� ������");
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
        // ��������� ������
        String sql = getInsertQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForInsert(statement, object);
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistException("���-�� ����������� �������: " + count);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        // �������� ������ ��� ����������� ������
        sql = getSelectQuery() + " WHERE id = lastval()";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            List<T> list = parseResultSet(rs);
            if ((list == null) || (list.size() != 1)) {//���� ���������� ���������� �� �������, �� �������� ������
                System.out.println("list.size="+list.size());///!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                throw new PersistException("�� ���������� �������� ������ ��� ����������� ������");
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
            throw new PersistException("��������� ���� �� ����� ��������");
        }
        T copyObject = getByPK(key);
        T persistInstance;
        // ��������� ������
        String sql = getInsertQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForInsert(statement, copyObject);
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistException("���-�� ����������� �������: " + count);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        // �������� ������ ��� ����������� ������
        sql = getSelectQuery() + " WHERE id = lastval();";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            List<T> list = parseResultSet(rs);//������� emptyList

            if ((list == null) || (list.size() != 1)) {
                System.out.println("list.size="+list.size());
                throw new PersistException("�� ���������� �������� ������ ��� ����������� ������");
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
            prepareStatementForUpdate(statement, object); // ���������� ���������� ������� ������� �� ������� ��������
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistException("���-�� ��������� �������: " + count);
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
                throw new PersistException("���-�� �������� �������: " + count);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    public AbstractDAO(Connection connection) {
        this.connection = connection;
    }
}