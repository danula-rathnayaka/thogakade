package controller.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.User;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;

public class UserControllerImpl implements UserController{

    private static UserControllerImpl instance;
    private UserControllerImpl(){}

    public static UserControllerImpl getInstance(){
        return instance == null? instance = new UserControllerImpl() : instance;
    }

    public ObservableList<User> getAllUsers(){
        ObservableList<User> users = FXCollections.observableArrayList();
        try {
            ResultSet rst = CrudUtil.execute("SELECT userID, username, password_hash, name, role, last_login, last_logout FROM users;");

            while (rst.next()){
                users.add(new User(
                        rst.getString("userID"),
                        rst.getString("username"),
                        rst.getString("name"),
                        rst.getString("password_hash"),
                        User.Role.valueOf(rst.getString("role").toUpperCase()),
                        rst.getTimestamp("last_login"),
                        rst.getTimestamp("last_logout")
                ));
            }
        } catch (SQLException e) {
            showErrorAlert();
        }
        return users;
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
        } catch (SQLIntegrityConstraintViolationException e){
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

        } catch (SQLIntegrityConstraintViolationException e){
            throw new SQLIntegrityConstraintViolationException();
        } catch (SQLException e) {
            showErrorAlert();
            return false;
        }
    }

    @Override
    public void updateLastLogin(String userId, Timestamp lastLogin) {
        try{
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
        try{
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
    public User loginUser(String username, String password){
        ObservableList<User> users = getAllUsers();
        for(User user : users){
            if(user.getUsername().equals(username) && user.getPassword_hash().equals(password)){
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
