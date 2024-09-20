package controller.order;

import model.OrderProducts;

import java.util.List;

public interface OrderDetailController {
    boolean addOrderDetail(List<OrderProducts> orderProducts);
    boolean addOrderDetail(OrderProducts orderProduct);
}
