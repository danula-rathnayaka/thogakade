package controller.customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import util.CrudUtil;

import java.sql.*;

public class CustomerControllerImpl implements CustomerController{

    @Override
    public boolean deleteCustomer(String id) throws SQLException {
        return CrudUtil.execute("DELETE FROM customer WHERE CustID = ?", id);
    }

    @Override
    public ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
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

        return customerList;
    }

    @Override
    public boolean addCustomer(Customer customer) throws SQLException {
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
    }

    @Override
    public boolean updateCustomer(Customer customer) throws SQLException {
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
    }
}
