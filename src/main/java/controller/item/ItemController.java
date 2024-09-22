package controller.item;

import javafx.collections.ObservableList;
import model.Item;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public interface ItemController {
    ObservableList<Item> getAllProducts();
    boolean deleteItem(String code);
    boolean editItem(Item item) throws SQLIntegrityConstraintViolationException;
    boolean addItem(Item item) throws SQLIntegrityConstraintViolationException;
    ObservableList<String> getProductCodes();
    Item getItem(String code);
}
