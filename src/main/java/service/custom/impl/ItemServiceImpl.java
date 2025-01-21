package service.custom.impl;

import dto.Item;
import dto.OrderProducts;
import entity.ItemEntity;
import entity.OrderProductsEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.ItemDao;
import service.custom.ItemService;
import util.DaoType;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class ItemServiceImpl implements ItemService {
    private static ItemServiceImpl instance;
    private final ItemDao repository = DaoFactory.getInstance().getDaoType(DaoType.ITEM);

    private ItemServiceImpl() {
    }

    public static ItemServiceImpl getInstance() {
        return instance == null ? instance = new ItemServiceImpl() : instance;
    }

    @Override
    public ObservableList<Item> getAllProducts() {
        List<ItemEntity> itemEntityList = repository.findAll();
        ObservableList<Item> itemObservableList = FXCollections.observableArrayList();
        itemEntityList.forEach(itemEntity -> {
            itemObservableList.add(new ModelMapper().map(itemEntity, Item.class));
        });
        return itemObservableList;
    }

    @Override
    public boolean deleteItem(String code) {
        return repository.delete(code);
    }

    @Override
    public boolean editItem(Item item) throws SQLIntegrityConstraintViolationException {
        return repository.update(new ModelMapper().map(item, ItemEntity.class));
    }

    @Override
    public boolean addItem(Item item) throws SQLIntegrityConstraintViolationException {
        return repository.save(new ModelMapper().map(item, ItemEntity.class));
    }

    @Override
    public ObservableList<String> getProductCodes() {
        ObservableList<String> idObservableList = FXCollections.observableArrayList();
        idObservableList.addAll(repository.getIDs());
        return idObservableList;
    }

    @Override
    public Item getItem(String code) {
        return new ModelMapper().map(repository.getItem(code), Item.class);
    }

    public boolean updateStock(List<OrderProducts> orderProducts) {
        List<OrderProductsEntity> orderProductsEntityList = FXCollections.observableArrayList();
        orderProducts.forEach(orderProduct -> orderProductsEntityList.add(new ModelMapper().map(orderProduct, OrderProductsEntity.class)));
        return repository.updateStock(orderProductsEntityList);
    }

    public boolean updateStock(OrderProducts orderProduct) {
        return repository.updateStock(new ModelMapper().map(orderProduct, OrderProductsEntity.class));
    }
}
