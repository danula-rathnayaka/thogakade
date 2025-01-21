package service.custom.impl;

import dto.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.UserDao;
import service.custom.UserService;
import util.CrudUtil;
import util.DaoType;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;

public class UserServiceImpl implements UserService {
    private static UserServiceImpl instance;
    private final UserDao repository = DaoFactory.getInstance().getDaoType(DaoType.USER);

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        return instance == null ? instance = new UserServiceImpl() : instance;
    }

    public ObservableList<User> getAllUsers() {
        ObservableList<User> userObservableList = FXCollections.observableArrayList();
        repository.findAll().forEach(userEntity -> userObservableList.add(new ModelMapper().map(userEntity, User.class)));
        return userObservableList;
    }

    @Override
    public boolean deleteUser(String id) {
        try {
            return CrudUtil.execute("DELETE FROM users WHERE userID = ?", id);
        } catch (SQLException e) {
            showErrorAlert();
            return false;
        }
    }

    @Override
    public boolean addUser(User user) throws SQLIntegrityConstraintViolationException {
        try {
            return CrudUtil.execute(
                    "INSERT INTO users (userID, username, password_hash, name, role) VALUES (?, ?, ?, ?, ?);",
                    user.getId(),
                    user.getUsername(),
                    user.getPassword_hash(),
                    user.getName(),
                    user.getRole().name()
            );
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLIntegrityConstraintViolationException();
        } catch (SQLException e) {
            showErrorAlert();
            return false;
        }
    }

    @Override
    public boolean updateUser(User user) throws SQLIntegrityConstraintViolationException {
        try {
            return CrudUtil.execute("UPDATE users SET username = ?, password_hash = ?, name = ?, role = ? WHERE userID = ?;",
                    user.getUsername(),
                    user.getPassword_hash(),
                    user.getName(),
                    user.getRole().name(),
                    user.getId()
            );

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLIntegrityConstraintViolationException();
        } catch (SQLException e) {
            showErrorAlert();
            return false;
        }
    }

    @Override
    public void updateLastLogin(String userId, Timestamp lastLogin) {
        try {
            CrudUtil.execute(
                    "UPDATE users SET last_login = ? WHERE userID = ?;",
                    lastLogin,
                    userId
            );
        } catch (SQLException e) {
            showErrorAlert();
        }
    }

    @Override
    public void updateLastLogout(String userId, Timestamp lastLogout) {
        try {
            CrudUtil.execute(
                    "UPDATE users SET last_logout = ? WHERE userID = ?;",
                    lastLogout,
                    userId
            );
        } catch (SQLException e) {
            showErrorAlert();
        }
    }

    @Override
    public User loginUser(String username, String password) {
        ObservableList<User> users = getAllUsers();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword_hash().equals(password)) {
                return user;
            }
        }
        return null;
    }

    private void showErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Could not connect the Database.", ButtonType.OK);
        alert.setTitle("Error Occurred.");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
