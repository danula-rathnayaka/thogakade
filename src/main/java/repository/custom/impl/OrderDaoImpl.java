package repository.custom.impl;

public class OrderDaoImpl {
    private static OrderDaoImpl instance;

    private OrderDaoImpl() {
    }

    public static OrderDaoImpl getInstance() {
        return instance == null ? instance = new OrderDaoImpl() : instance;
    }
}
