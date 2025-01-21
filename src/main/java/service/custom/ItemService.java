package service.custom;

import dto.Item;
import dto.OrderProducts;
import javafx.collections.ObservableList;
import service.SuperService;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public interface ItemService extends SuperService {
    ObservableList<Item> getAllProducts();

    boolean deleteItem(String code);

    boolean editItem(Item item) throws SQLIntegrityConstraintViolationException;

    boolean addItem(Item item) throws SQLIntegrityConstraintViolationException;

    ObservableList<String> getProductCodes();

    Item getItem(String code);

    boolean updateStock(List<OrderProducts> orderProducts);

    boolean updateStock(OrderProducts orderProduct);
}
