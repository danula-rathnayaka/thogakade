package controller.order;

import controller.item.ItemControllerImpl;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.CartProducts;
import model.Order;
import model.OrderProducts;
import util.CrudUtil;
import util.ShowAlert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
            ResultSet rst = CrudUtil.execute("SELECT OrderId, OrderDate, CustId from orders;");

            while (rst.next()) {
                String orderId = rst.getString("OrderId");
                ObservableList<CartProducts> orderProducts = OrderControllerImpl.getInstance().getOrderedProducts(orderId);
                ArrayList<OrderProducts> orderProductsList = new ArrayList<>();

                Double totDiscount = 0.0;
                Double total = 0.0;

                for (int i = 0; i < orderProducts.size(); i++) {
                    totDiscount += orderProducts.get(i).getDiscount();
                    total += orderProducts.get(i).getTotal();

                    orderProductsList.add(
                            new OrderProducts(
                                    orderId,
                                    orderProducts.get(i).getItemCode(),
                                    orderProducts.get(i).getOrderQty(),
                                    0.0)
                    );
                }


                orderList.add(new Order(
                        orderId,
                        rst.getDate("OrderDate").toLocalDate(),
                        rst.getString("CustId"),
                        totDiscount,
                        total,
                        orderProductsList
                ));
            }
        } catch (SQLException e) {
            ShowAlert.databaseError();
        }

        return orderList;
    }

    @Override
    public boolean placeOrder(Order order) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();

        try {
            conn.setAutoCommit(false);
            PreparedStatement psTm = conn.prepareStatement("INSERT INTO orders VALUES(?,?,?);");
            psTm.setObject(1, order.getId());
            psTm.setObject(2, order.getDate());
            psTm.setObject(3, order.getCustId());
            if (psTm.executeUpdate() > 0) {
                boolean isOrderProductsAdded = OrderDetailControllerImpl.getInstance().addOrderProduct(order.getOrderProducts());
                if (isOrderProductsAdded) {
                    if (ItemControllerImpl.getInstance().updateStock(order.getOrderProducts())) {
                        conn.commit();
                        return true;
                    }
                }
            }
            conn.rollback();
            return false;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    @Override
    public ObservableList<CartProducts> getOrderedProducts(String id) throws SQLException{
        ObservableList<CartProducts> cartProducts = FXCollections.observableArrayList();
        ResultSet rst = CrudUtil.execute("SELECT ordinfo.ItemCode, item.Description, item.PackSize, item.UnitPrice, ordinfo.OrderQTY, ordinfo.Discount FROM orderdetail ordinfo join item on ordinfo.ItemCode = item.ItemCode WHERE ordinfo.OrderID = '"+ id +"';");

        while (rst.next()) {
            Integer orderQty = rst.getInt("OrderQTY");
            Double unitPrice = rst.getDouble("UnitPrice");
            cartProducts.add(new CartProducts(
                    rst.getString("ItemCode"),
                    rst.getString("Description"),
                    rst.getString("PackSize"),
                    orderQty,
                    unitPrice,
                    unitPrice * orderQty,
                    rst.getDouble("Discount"),
                    (unitPrice * orderQty) - rst.getDouble("Discount")
            ));
        }
        return cartProducts;
    }
}
