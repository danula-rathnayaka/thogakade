package controller.customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Customer;
import util.CrudUtil;
import util.ShowAlert;

import java.sql.*;

public class CustomerControllerImpl implements CustomerController{

    private static CustomerControllerImpl instance;

    private CustomerControllerImpl(){}

    public static CustomerControllerImpl getInstance(){
        return instance==null? instance = new CustomerControllerImpl() : instance;
    }

    @Override
    public boolean deleteCustomer(String id) {
        try {
            return CrudUtil.execute("DELETE FROM customer WHERE CustID = ?", id);
        } catch (SQLException e) {
            ShowAlert.databaseError();
            return false;
        }
    }

    @Override
    public ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        try {
            ResultSet rst = CrudUtil.execute("SELECT CustID, CustTitle, CustName, DOB, salary, CustAddress, City, Province, PostalCode FROM customer;");

            while (rst.next()){
                customerList.add(new Customer(
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
    public boolean addCustomer(Customer customer) throws SQLIntegrityConstraintViolationException {
        try {
            return CrudUtil.execute("INSERT INTO customer (CustID, CustTitle, CustName, DOB, salary, CustAddress, City, Province, PostalCode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);",
                                        customer.getId(),
                                        customer.getTitle(),
                                        customer.getName(),
                                        customer.getDob(),
                                        customer.getSalary(),
                                        customer.getAddress(),
                                        customer.getCity(),
                                        customer.getProvince(),
                                        customer.getPostalCode()
                                    );
        } catch (SQLIntegrityConstraintViolationException e){
            throw new SQLIntegrityConstraintViolationException();
        } catch (SQLException e) {
            ShowAlert.databaseError();
            return false;
        }
    }

    @Override
    public boolean updateCustomer(Customer customer) throws SQLIntegrityConstraintViolationException {
        try {
            return CrudUtil.execute("UPDATE customer SET CustTitle = ?, CustName = ?, DOB = ?, salary = ?, CustAddress = ?, City = ?, Province = ?, PostalCode = ? WHERE CustID = ?;",
                    customer.getTitle(),
                    customer.getName(),
                    customer.getDob(),
                    customer.getSalary(),
                    customer.getAddress(),
                    customer.getCity(),
                    customer.getProvince(),
                    customer.getPostalCode(),
                    customer.getId()
            );
        } catch (SQLIntegrityConstraintViolationException e){
            throw new SQLIntegrityConstraintViolationException();
        } catch (SQLException e) {
            ShowAlert.databaseError();
            return false;
        }
    }

    @Override
    public ObservableList<String> getCustomerIDs() {
        ObservableList<String> customerIdList = FXCollections.observableArrayList();
        getAllCustomers().forEach(customer -> customerIdList.add(customer.getId()));
        return customerIdList;
    }

    @Override
    public Customer getCustomer(String id) {
        try {
            ResultSet rst = CrudUtil.execute("SELECT CustID, CustTitle, CustName, DOB, salary, CustAddress, City, Province, PostalCode FROM customer WHERE CustID = '" + id + "';");

            if(!rst.next()){
                return null;
            }

            return new Customer(
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
        } catch (SQLException e){
            ShowAlert.databaseError();
            return new Customer();
        }
    }
}
