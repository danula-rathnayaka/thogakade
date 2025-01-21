package service.custom;

import dto.User;
import javafx.collections.ObservableList;
import service.SuperService;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;

public interface UserService extends SuperService {
    ObservableList<User> getAllUsers();

    boolean deleteUser(String id);

    boolean addUser(User user) throws SQLIntegrityConstraintViolationException;

    boolean updateUser(User user) throws SQLIntegrityConstraintViolationException;

    User loginUser(String id, String password);

    void updateLastLogin(String userId, Timestamp lastLogin);

    void updateLastLogout(String userId, Timestamp lastLogout);
}
