package repository.custom.impl;

import entity.ItemEntity;
import entity.OrderProductsEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import repository.custom.ItemDao;
import util.CrudUtil;
import util.ShowAlert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class ItemDaoImpl implements ItemDao {
    private static ItemDaoImpl instance;

    private ItemDaoImpl() {
    }

    public static ItemDaoImpl getInstance() {
        return instance == null ? instance = new ItemDaoImpl() : instance;
    }

    @Override
    public boolean save(ItemEntity entity) throws SQLIntegrityConstraintViolationException {
        try {
            return CrudUtil.execute("INSERT INTO item (ItemCode, Description, PackSize, UnitPrice, QtyOnHand) VALUES (?, ?, ?, ?, ?);",
                    entity.getCode(),
                    entity.getDescription(),
                    entity.getPackSize(),
                    entity.getUnitPrice(),
                    entity.getQtnInHand()
            );
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLIntegrityConstraintViolationException();
        } catch (SQLException e) {
            ShowAlert.databaseError();
            return false;
        }
    }

    @Override
    public boolean update(ItemEntity entity) throws SQLIntegrityConstraintViolationException {
        try {
            return CrudUtil.execute("UPDATE item SET Description = ?, PackSize = ?, UnitPrice = ?, QtyOnHand = ? WHERE ItemCode = ?;",
                    entity.getDescription(),
                    entity.getPackSize(),
                    entity.getUnitPrice(),
                    entity.getQtnInHand(),
                    entity.getCode()
            );
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLIntegrityConstraintViolationException();
        } catch (SQLException e) {
            ShowAlert.databaseError();
            return false;
        }
    }

    @Override
    public List<ItemEntity> findAll() {
        ObservableList<ItemEntity> itemList = FXCollections.observableArrayList();
        try {
            ResultSet rst = CrudUtil.execute("SELECT ItemCode, Description, PackSize, UnitPrice, QtyOnHand FROM item;");

            while (rst.next()) {
                itemList.add(new ItemEntity(
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
    public boolean delete(String id) {
        try {
            return CrudUtil.execute("DELETE FROM item WHERE ItemCode = ?;", id);
        } catch (SQLException e) {
            ShowAlert.databaseError();
            return false;
        }
    }

    @Override
    public List<String> getIDs() {
        ObservableList<String> idList = FXCollections.observableArrayList();
        try {
            ResultSet rst = CrudUtil.execute("SELECT ItemCode FROM item;");

            while (rst.next()) {
                idList.add(rst.getString("ItemCode"));
            }
        } catch (SQLException e) {
            ShowAlert.databaseError();
        }
        return idList;
    }

    @Override
    public ItemEntity getItem(String id) {
        try {
            ResultSet rst = CrudUtil.execute("SELECT ItemCode, Description, PackSize, UnitPrice, QtyOnHand FROM item WHERE ItemCode = '" + id + "';");

            if (!rst.next()) {
                return null;
            }

            return new ItemEntity(
                    rst.getString("ItemCode"),
                    rst.getString("Description"),
                    rst.getString("PackSize"),
                    rst.getDouble("UnitPrice"),
                    rst.getInt("QtyOnHand")
            );
        } catch (SQLException e) {
            ShowAlert.databaseError();
            return new ItemEntity();
        }
    }

    @Override
    public boolean updateStock(List<OrderProductsEntity> orderProducts) {
        for (OrderProductsEntity orderProduct : orderProducts) {
            if (!updateStock(orderProduct)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean updateStock(OrderProductsEntity orderProduct) {
        try {
            return CrudUtil.execute("UPDATE item SET QtyOnHand=QtyOnHand-? WHERE ItemCode=?", orderProduct.getOrderQty(), orderProduct.getItemCode());
        } catch (SQLException e) {
            ShowAlert.databaseError();
            return false;
        }
    }
}
