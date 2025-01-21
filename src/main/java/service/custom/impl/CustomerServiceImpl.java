package service.custom.impl;

import dto.Customer;
import entity.CustomerEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.CustomerDao;
import service.custom.CustomerService;
import util.DaoType;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private static CustomerServiceImpl instance;
    private final CustomerDao repository = DaoFactory.getInstance().getDaoType(DaoType.CUSTOMER);

    private CustomerServiceImpl() {
    }

    public static CustomerServiceImpl getInstance() {
        return instance == null ? instance = new CustomerServiceImpl() : instance;
    }

    @Override
    public boolean deleteCustomer(String id) {
        return repository.delete(id);
    }

    @Override
    public ObservableList<Customer> getAllCustomers() {
        List<CustomerEntity> customerEntityList = repository.findAll();
        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        customerEntityList.forEach(customerEntity -> customerList.add(new ModelMapper().map(customerEntity, Customer.class)));
        return customerList;
    }

    @Override
    public boolean addCustomer(Customer customer) throws SQLIntegrityConstraintViolationException {
        return repository.save(new ModelMapper().map(customer, CustomerEntity.class));
    }

    @Override
    public boolean updateCustomer(Customer customer) throws SQLIntegrityConstraintViolationException {
        return repository.update(new ModelMapper().map(customer, CustomerEntity.class));
    }

    @Override
    public ObservableList<String> getCustomerIDs() {
        ObservableList<String> idList = FXCollections.observableArrayList();
        idList.addAll(repository.getIDs());
        return idList;
    }

    @Override
    public Customer getCustomer(String id) {
        return new ModelMapper().map(repository.getItem(id), Customer.class);
    }
}
