package controller.order;

import model.OrderProducts;

import java.util.List;

public interface OrderDetailController {
    boolean addOrderProduct(List<OrderProducts> orderProducts);

    boolean addOrderProduct(OrderProducts orderProduct);
}
