package controller.order;

import javafx.collections.ObservableList;
import model.Order;
import model.CartProducts;

import java.sql.SQLException;

public interface OrderController {
    ObservableList<Order> getAllOrders() throws SQLException;
    boolean placeOrder(Order order) throws SQLException;
    ObservableList<CartProducts> getOrderedProducts(String orderId) throws SQLException;
}
