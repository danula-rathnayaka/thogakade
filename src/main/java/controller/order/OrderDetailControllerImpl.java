package controller.order;

import model.OrderProducts;
import util.CrudUtil;

import java.sql.SQLException;
import java.util.List;

public class OrderDetailControllerImpl implements OrderDetailController{
    public boolean addOrderDetail(List<OrderProducts> orderProducts) {
        for (OrderProducts orderProduct : orderProducts){
            boolean isOrderDetailsAdded = addOrderDetail(orderProduct);
            if (!isOrderDetailsAdded){
                return false;
            }
        }
        return true;
    }

    public boolean addOrderDetail(OrderProducts orderProduct) {
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
