package controller.order;

import model.OrderProducts;
import util.CrudUtil;

import java.sql.SQLException;
import java.util.List;

public class OrderDetailControllerImpl implements OrderDetailController{

    private static OrderDetailControllerImpl instance;

    private OrderDetailControllerImpl() {
    }

    public static OrderDetailControllerImpl getInstance() {
        return instance == null ? instance = new OrderDetailControllerImpl() : instance;
    }

    public boolean addOrderProduct(List<OrderProducts> orderProducts) {
        for (OrderProducts orderProduct : orderProducts){
            boolean isOrderDetailsAdded = addOrderProduct(orderProduct);
            if (!isOrderDetailsAdded){
                return false;
            }
        }
        return true;
    }

    public boolean addOrderProduct(OrderProducts orderProduct) {
        try {
            return CrudUtil.execute(
                    "INSERT INTO orderdetail VALUES(?, ?, ?, ?);",
                    orderProduct.getOrderId(),
                    orderProduct.getItemCode(),
                    orderProduct.getOrderQty(),
                    orderProduct.getDiscount());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
