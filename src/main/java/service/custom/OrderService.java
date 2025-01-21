package service.custom;

import dto.CartProducts;
import dto.Order;
import dto.OrderProducts;
import javafx.collections.ObservableList;
import service.SuperService;

import java.sql.SQLException;
import java.util.List;

public interface OrderService extends SuperService {
    ObservableList<Order> getAllOrders();

    boolean placeOrder(Order order) throws SQLException;

    ObservableList<CartProducts> getOrderedProducts(String orderId) throws SQLException;

    boolean addOrderProduct(List<OrderProducts> orderProducts);

    boolean addOrderProduct(OrderProducts orderProduct);
}
