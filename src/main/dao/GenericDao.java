
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**
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

    /** ������� ������ �� ������� �� ���� ������ */
    public void delete(T object) throws PersistException;

    /** ���������� ������ �������� ��������������� ���� ������� � ���� ������ */
    public List<T> getAll() throws PersistException;
}