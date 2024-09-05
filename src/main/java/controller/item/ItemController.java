package controller.item;

import javafx.collections.ObservableList;
import model.Item;

import java.sql.SQLException;

public interface ItemController {
    ObservableList<Item> getAllProducts() throws SQLException;
    boolean deleteItem(String code) throws SQLException;
    boolean editItem(Item item) throws SQLException;
    boolean addItem(Item item) throws SQLException;
}
