
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO
 * ��������������� ��������� ���������� ������������� ���������� ��������
 * @param <T> ��� ������� ������������
 * @param <PK> ��� ���������� �����
 */

public interface GenericDao<T extends Identified<PK>, PK extends Serializable> {

    /** ������� ����� ������ � ��������������� �� ������ */
    public T create() throws PersistException;

    /** ������� ����� ������, ��������������� ������� object */
    public T persist(T object)  throws PersistException;

    /** ���������� ������ ��������������� ������ � ��������� ������ key ��� null */
    public T getByPK(PK key) throws PersistException;

    /** ��������� ��������� ������� group � ���� ������ */
    public void update(T object) throws PersistException;

    public T copy(PK key) throws PersistException ;

    /** ������� ������ �� ������� �� ���� ������ */
    public void delete(T object) throws PersistException;

    /** ���������� ������ �������� ��������������� ���� ������� � ���� ������ */
    public List<T> getAll() throws PersistException;

    /**���������� ������ �������� �������������� ��������� ��������� ������� � ��� �������� */
    public List<T> findByIntParam(String param, Object value) throws PersistException;

    /**���������� ������ �������� �������������� ��������� ��������� ������� � ��� �������� */
    public List<T> findByStringParam(String param, Object value) throws PersistException;

    public List<T> findByDateParam(String param, Object value) throws PersistException;
}