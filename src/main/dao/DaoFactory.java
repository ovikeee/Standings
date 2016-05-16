
import java.sql.Connection;
import java.sql.SQLException;

/** Фабрика объектов для работы с базой данных */
public interface DaoFactory<DataSource> {

    //вспомогательный интерфейс
    public interface DaoCreator<DataSource> {
        public GenericDao create(DataSource dataSource) throws SQLException;
    }

    /** Возвращает подключение к базе данных */
//    public DataSource getContext() throws PersistException;

    /** Возвращает объект для управления персистентным состоянием объекта */
    public GenericDao getDao(Class dtoClass) throws PersistException, SQLException;
}