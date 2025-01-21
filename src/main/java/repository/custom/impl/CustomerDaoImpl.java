package repository.custom.impl;

import entity.CustomerEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import repository.custom.CustomerDao;
import util.CrudUtil;
import util.ShowAlert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {
    private static CustomerDaoImpl instance;

    private CustomerDaoImpl() {
    }

    public static CustomerDaoImpl getInstance() {
        return instance == null ? instance = new CustomerDaoImpl() : instance;
    }

    @Override
    public boolean save(CustomerEntity entity) throws SQLIntegrityConstraintViolationException {
        try {
            return CrudUtil.execute("INSERT INTO customer (CustID, CustTitle, CustName, DOB, salary, CustAddress, City, Province, PostalCode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);",
                    entity.getId(),
                    entity.getTitle(),
                    entity.getName(),
                    entity.getDob(),
                    entity.getSalary(),
                    entity.getAddress(),
                    entity.getCity(),
                    entity.getProvince(),
                    entity.getPostalCode()
            );
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLIntegrityConstraintViolationException();
        } catch (SQLException e) {
            ShowAlert.databaseError();
            return false;
        }
    }

    @Override
    public boolean update(CustomerEntity entity) throws SQLIntegrityConstraintViolationException {
        try {
            return CrudUtil.execute("UPDATE customer SET CustTitle = ?, CustName = ?, DOB = ?, salary = ?, CustAddress = ?, City = ?, Province = ?, PostalCode = ? WHERE CustID = ?;",
                    entity.getTitle(),
                    entity.getName(),
                    entity.getDob(),
                    entity.getSalary(),
                    entity.getAddress(),
                    entity.getCity(),
                    entity.getProvince(),
                    entity.getPostalCode(),
                    entity.getId()
            );
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLIntegrityConstraintViolationException();
        } catch (SQLException e) {
            ShowAlert.databaseError();
            return false;
        }
    }

    @Override
    public List<CustomerEntity> findAll() {
        ObservableList<CustomerEntity> customerList = FXCollections.observableArrayList();
        try {
            ResultSet rst = CrudUtil.execute("SELECT CustID, CustTitle, CustName, DOB, salary, CustAddress, City, Province, PostalCode FROM customer;");

            while (rst.next()) {
                customerList.add(new CustomerEntity(
                        rst.getString("CustID"),
                        rst.getString("CustTitle"),
                        rst.getString("CustName"),
                        rst.getDate("DOB").toLocalDate(),
                        rst.getDouble("salary"),
                        rst.getString("CustAddress"),
                        rst.getString("City"),
                        rst.getString("Province"),
                        rst.getString("PostalCode")
                ));
            }
        } catch (SQLException e) {
            ShowAlert.databaseError();
        }
        return customerList;
    }

    @Override
    public boolean delete(String id) {
        try {
            return CrudUtil.execute("DELETE FROM customer WHERE CustID = ?", id);
        } catch (SQLException e) {
            ShowAlert.databaseError();
            return false;
        }
    }

    @Override
    public List<String> getIDs() {
        ObservableList<String> customerIdList = FXCollections.observableArrayList();
        try {
            ResultSet rst = CrudUtil.execute("SELECT CustID FROM customer;");

            while (rst.next()) {
                customerIdList.add(rst.getString("CustID"));
            }
        } catch (SQLException e) {
            ShowAlert.databaseError();
        }
        return customerIdList;
    }

    @Override
    public CustomerEntity getItem(String id) {
        try {
            ResultSet rst = CrudUtil.execute("SELECT CustID, CustTitle, CustName, DOB, salary, CustAddress, City, Province, PostalCode FROM customer WHERE CustID = '" + id + "';");

            if (!rst.next()) {
                return null;
            }

            return new CustomerEntity(
                    rst.getString("CustID"),
                    rst.getString("CustTitle"),
                    rst.getString("CustName"),
                    rst.getDate("DOB").toLocalDate(),
                    rst.getDouble("salary"),
                    rst.getString("CustAddress"),
                    rst.getString("City"),
                    rst.getString("Province"),
                    rst.getString("PostalCode")
            );
        } catch (SQLException e) {
            ShowAlert.databaseError();
            return new CustomerEntity();
        }
    }
}
