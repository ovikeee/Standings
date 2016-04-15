import java.io.Serializable;

/**
 * Интерфейс идентифицируемых объектов.
 * Нужен, для того чтобы в AbstractDao в методе delete был доступ к id заранее не извесной реализации Dao..PostgreSQL объекта
 * PK - первичный ключ объекта
 */
public interface Identified< PK extends Serializable> {
    /** Возвращает идентификатор объекта */
    public PK getId();
}