package controller.customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Customer;
import util.CrudUtil;
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
            showErrorAlert();
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
                        rst.getString(1),
                        rst.getString(2),
                        rst.getString(3),
                        rst.getDate(4).toLocalDate(),
                        rst.getDouble(5),
                        rst.getString(6),
                        rst.getString(7),
                        rst.getString(8),
                        rst.getString(9)
                ));
            }
        } catch (SQLException e) {
            showErrorAlert();
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
            showErrorAlert();
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
            showErrorAlert();
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

            rst.next();

            return new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDate(4).toLocalDate(),
                    rst.getDouble(5),
                    rst.getString(6),
                    rst.getString(7),
                    rst.getString(8),
                    rst.getString(9)
            );
        } catch (SQLException e){
            showErrorAlert();
            return new Customer();
        }
    }

    private void showErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Could not connect the Database.", ButtonType.OK);
        alert.setTitle("Error Occurred.");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
