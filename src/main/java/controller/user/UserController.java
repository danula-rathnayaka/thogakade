package controller.user;

import javafx.collections.ObservableList;
import model.User;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;

public interface UserController {
    ObservableList<User> getAllUsers();
    boolean deleteUser(String id);
    boolean addUser(User user) throws SQLIntegrityConstraintViolationException;
    boolean updateUser(User user) throws SQLIntegrityConstraintViolationException;
    User loginUser(String id, String password);
    void updateLastLogin(String userId, Timestamp lastLogin);
    void updateLastLogout(String userId, Timestamp lastLogout);
}
