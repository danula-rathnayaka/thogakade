package controller.item;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Item;
import model.OrderProducts;
import util.CrudUtil;
import util.ShowAlert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class ItemControllerImpl implements ItemController{

    private static ItemControllerImpl instance;

    private ItemControllerImpl(){}

    public static ItemControllerImpl getInstance(){
        return instance==null? instance = new ItemControllerImpl() : instance;
    }

    @Override
    public ObservableList<Item> getAllProducts() {
        ObservableList<Item> itemList = FXCollections.observableArrayList();
        try {
            ResultSet rst = CrudUtil.execute("SELECT ItemCode, Description, PackSize, UnitPrice, QtyOnHand FROM item;");

            while (rst.next()) {
                itemList.add(new Item(
                        rst.getString("ItemCode"),
                        rst.getString("Description"),
                        rst.getString("PackSize"),
                        rst.getDouble("UnitPrice"),
                        rst.getInt("QtyOnHand")
                ));
            }
        } catch (SQLException e) {
            ShowAlert.databaseError();
        }
        return itemList;
    }

    @Override
    public boolean deleteItem(String code) {
        try {
            return CrudUtil.execute("DELETE FROM item WHERE ItemCode = ?;", code);
        } catch (SQLException e) {
            ShowAlert.databaseError();
            return false;
        }
    }

    @Override
    public boolean editItem(Item item) throws SQLIntegrityConstraintViolationException {
        try {
            return CrudUtil.execute("UPDATE item SET Description = ?, PackSize = ?, UnitPrice = ?, QtyOnHand = ? WHERE ItemCode = ?;",
                    item.getDescription(),
                    item.getPackSize(),
                    item.getUnitPrice(),
                    item.getQtnInHand(),
                    item.getCode()
                    );
        }catch (SQLIntegrityConstraintViolationException e){
            throw new SQLIntegrityConstraintViolationException();
        } catch (SQLException e) {
            ShowAlert.databaseError();
            return false;
        }
    }

    @Override
    public boolean addItem(Item item) throws SQLIntegrityConstraintViolationException {
        try {
            return CrudUtil.execute("INSERT INTO item (ItemCode, Description, PackSize, UnitPrice, QtyOnHand) VALUES (?, ?, ?, ?, ?);",
                    item.getCode(),
                    item.getDescription(),
                    item.getPackSize(),
                    item.getUnitPrice(),
                    item.getQtnInHand()
            );
        }catch (SQLIntegrityConstraintViolationException e){
            throw new SQLIntegrityConstraintViolationException();
        } catch (SQLException e) {
            ShowAlert.databaseError();
            return false;
        }
    }

    @Override
    public ObservableList<String> getProductCodes() {
        ObservableList<String> customerIdList = FXCollections.observableArrayList();
        getAllProducts().forEach(customer -> customerIdList.add(customer.getCode()));
        return customerIdList;
    }

    @Override
    public Item getItem(String code) {
        try {
            ResultSet rst = CrudUtil.execute("SELECT ItemCode, Description, PackSize, UnitPrice, QtyOnHand FROM item WHERE ItemCode = '"+ code +"';");

            rst.next();

            return new Item(
                    rst.getString("ItemCode"),
                    rst.getString("Description"),
                    rst.getString("PackSize"),
                    rst.getDouble("UnitPrice"),
                    rst.getInt("QtyOnHand")
            );
        } catch (SQLException e) {
            ShowAlert.databaseError();
            return new Item();
        }
    }

    public boolean updateStock(OrderProducts orderProducts){
//        CrudUtil.execute()
        return true;
    }
}
