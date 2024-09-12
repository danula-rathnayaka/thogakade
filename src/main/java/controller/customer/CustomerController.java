package controller.customer;

import javafx.collections.ObservableList;
import model.Customer;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public interface CustomerController {
    boolean deleteCustomer(String id);
    ObservableList<Customer> getAllCustomers();
    boolean addCustomer(Customer customer) throws SQLIntegrityConstraintViolationException;
    boolean updateCustomer(Customer customer) throws SQLIntegrityConstraintViolationException;
    ObservableList<String> getCustomerIDs();
    Customer getCustomer(String id);
}
