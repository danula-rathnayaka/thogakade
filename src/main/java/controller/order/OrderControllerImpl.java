package controller.order;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Order;
import model.CartProducts;
import util.CrudUtil;
import util.ShowAlert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderControllerImpl implements OrderController{

    private static OrderControllerImpl instance;

    private OrderControllerImpl(){}

    public static OrderControllerImpl getInstance(){
        return instance == null? instance = new OrderControllerImpl() : instance;
    }

    @Override
    public ObservableList<Order> getAllOrders() {
        ObservableList<Order> orderList = FXCollections.observableArrayList();
        try {
            ResultSet rst = CrudUtil.execute("SELECT od.OrderID, cus.CustID, od.OrderDate, sum(orinfo.Discount) FROM orders od JOIN customer cus ON od.CustID = cus.CustID JOIN orderdetail orinfo ON od.OrderID = orinfo.OrderID GROUP BY orinfo.OrderID;");

//            while (rst.next()){
//                orderList.add(new Order(
//                        rst.getString(1),
//                        rst.getString(2),
//                        rst.getDate(3).toLocalDate(),
//                        rst.getInt(4),
////                      rst.getDouble(5)
//                        1000.00
//                ));
//            }
        } catch (SQLException e) {
            ShowAlert.databaseError();
        }

        return orderList;
    }

    @Override
    public boolean placeOrder(Order order){
        try {
            Connection conn = DBConnection.getInstance().getConnection();

            PreparedStatement psTm = conn.prepareStatement("INSERT INTO orders VALUES(?, ?, ?);");
            psTm.setObject(1, order.getId());
            psTm.setObject(2, order.getDate());
            psTm.setObject(3, order.getCustId());

            boolean isOrderAdded = psTm.executeUpdate() > 0;
            if (isOrderAdded){
                boolean isOrderProductsAdded = new OrderDetailControllerImpl().addOrderDetail(order.getOrderProducts());
                // TODO
            }
        } catch (SQLException e) {
            ShowAlert.databaseError();
            return false;
        }
        return true;
    }

    @Override
    public ObservableList<CartProducts> getOrderedProducts(String id) throws SQLException{
        ObservableList<CartProducts> cartProducts = FXCollections.observableArrayList();
        ResultSet rst = CrudUtil.execute("SELECT ordinfo.ItemCode, item.Description, item.PackSize, item.UnitPrice, ordinfo.OrderQTY, ordinfo.Discount FROM orderdetail ordinfo join item on ordinfo.ItemCode = item.ItemCode WHERE ordinfo.OrderID = '"+ id +"';");

//        while (rst.next()){
//            cartProducts.add(new CartProducts(
//                    rst.getString("ItemCode"),
//                    rst.getString("Description"),
//                    rst.getString("OrderQTY"),
//                    rst.getDouble("UnitPrice"),
//                    rst.getInt(""),
//                    rst.getInt("")
//
//            ));
//        }
        return cartProducts;
    }
}
