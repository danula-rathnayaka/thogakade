package controller.customer;

import javafx.collections.ObservableList;
import model.Customer;
import java.sql.SQLException;

public interface CustomerController {
    boolean deleteCustomer(String id) throws SQLException;
    ObservableList<Customer> getAllCustomers() throws SQLException;
    public boolean addCustomer(Customer customer) throws SQLException;
    boolean updateCustomer(Customer customer) throws SQLException;
}
