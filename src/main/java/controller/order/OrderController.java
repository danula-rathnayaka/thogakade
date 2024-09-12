package controller.order;

import javafx.collections.ObservableList;
import model.Customer;
import model.Order;
import model.OrderedProduct;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface OrderController {
    ObservableList<Order> getAllOrders() throws SQLException;
    boolean addOrder(Order order) throws SQLException;
    ObservableList<OrderedProduct> getOrderedProducts(String orderId) throws SQLException;
}
