package controller.item;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Item;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemControllerImpl implements ItemController{
    @Override
    public ObservableList<Item> getAllProducts() throws SQLException {
        ObservableList<Item> itemList = FXCollections.observableArrayList();

        ResultSet rst = CrudUtil.execute("SELECT ItemCode, Description, PackSize, UnitPrice, QtyOnHand FROM item;");

        while (rst.next()) {
            itemList.add(new Item(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4),
                    rst.getInt(5)
            ));
        }

        return itemList;
    }

    @Override
    public boolean deleteItem(String code) throws SQLException {
        return CrudUtil.execute("DELETE FROM item WHERE ItemCode = ?;", code);
    }

    @Override
    public boolean editItem(Item item) throws SQLException {
        return CrudUtil.execute("UPDATE item SET Description = ?, PackSize = ?, UnitPrice = ?, QtyOnHand = ? WHERE ItemCode = ?;",
                item.getDescription(),
                item.getPackSize(),
                item.getUnitPrice(),
                item.getQtnInHand(),
                item.getCode()
                );
    }

    @Override
    public boolean addItem(Item item) throws SQLException {
        return CrudUtil.execute("INSERT INTO item (ItemCode, Description, PackSize, UnitPrice, QtyOnHand) VALUES (?, ?, ?, ?, ?);",
                item.getCode(),
                item.getDescription(),
                item.getPackSize(),
                item.getUnitPrice(),
                item.getQtnInHand()
        );
    }
}
