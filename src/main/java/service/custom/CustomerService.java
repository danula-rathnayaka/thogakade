package service.custom;

import dto.Customer;
import javafx.collections.ObservableList;
import service.SuperService;

import java.sql.SQLIntegrityConstraintViolationException;

public interface CustomerService extends SuperService {
    boolean deleteCustomer(String id);

    ObservableList<Customer> getAllCustomers();

    boolean addCustomer(Customer customer) throws SQLIntegrityConstraintViolationException;

    boolean updateCustomer(Customer customer) throws SQLIntegrityConstraintViolationException;

    ObservableList<String> getCustomerIDs();

    Customer getCustomer(String id);
}
