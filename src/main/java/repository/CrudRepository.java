package repository;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public interface CrudRepository<T> extends SuperDao {
    boolean save(T entity) throws SQLIntegrityConstraintViolationException;

    boolean update(T entity) throws SQLIntegrityConstraintViolationException;

    List<T> findAll();

    boolean delete(String id);

    List<String> getIDs();

    T getItem(String id);
}
