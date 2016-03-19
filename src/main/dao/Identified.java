import java.io.Serializable;

/**
 * ��������� ���������������� ��������.
 * �����, ��� ���� ����� � AbstractDao � ������ delete ��� ������ � id ������� �� �������� ���������� Dao..PostgreSQL �������
 * PK - ��������� ���� �������
 */
public interface Identified<PK extends Serializable> {
    /** ���������� ������������� ������� */
    public PK getId();
}