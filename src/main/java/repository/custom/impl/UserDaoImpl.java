package repository.custom.impl;

import entity.UserEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import repository.custom.UserDao;
import util.CrudUtil;
import util.Role;
import util.ShowAlert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class UserDaoImpl implements UserDao {
    @Override
    public boolean save(UserEntity entity) throws SQLIntegrityConstraintViolationException {
        return false;
    }

    @Override
    public boolean update(UserEntity entity) throws SQLIntegrityConstraintViolationException {
        return false;
    }

    @Override
    public List<UserEntity> findAll() {
        ObservableList<UserEntity> users = FXCollections.observableArrayList();
        try {
            ResultSet rst = CrudUtil.execute("SELECT userID, username, password_hash, name, role, last_login, last_logout FROM users;");

            while (rst.next()) {
                users.add(new UserEntity(
                        rst.getString("userID"),
                        rst.getString("username"),
                        rst.getString("name"),
                        rst.getString("password_hash"),
                        Role.valueOf(rst.getString("role").toUpperCase()),
                        rst.getTimestamp("last_login"),
                        rst.getTimestamp("last_logout")
                ));
            }
        } catch (SQLException e) {
            ShowAlert.databaseError();
        }
        return users;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public List<String> getIDs() {
        return null;
    }

    @Override
    public UserEntity getItem(String id) {
        return null;
    }
}
