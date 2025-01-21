package repository.custom;

import entity.ItemEntity;
import entity.OrderProductsEntity;
import repository.CrudRepository;

import java.util.List;

public interface ItemDao extends CrudRepository<ItemEntity> {
    boolean updateStock(List<OrderProductsEntity> orderProducts);

    boolean updateStock(OrderProductsEntity orderProduct);
}
