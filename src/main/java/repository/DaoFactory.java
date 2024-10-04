package repository;

import repository.custom.impl.CustomerDaoImpl;
import repository.custom.impl.ItemDaoImpl;
import repository.custom.impl.OrderDaoImpl;
import repository.custom.impl.UserDaoImpl;
import util.DaoType;

public class DaoFactory {
    private static DaoFactory instance;

    private DaoFactory() {
    }

    public static DaoFactory getInstance() {
        return instance == null ? instance = new DaoFactory() : instance;
    }

    private <T extends SuperDao> T getDaoType(DaoType type) {
        return switch (type) {
            case CUSTOMER -> (T) new CustomerDaoImpl();
            case ITEM -> (T) new ItemDaoImpl();
            case ORDER -> (T) new OrderDaoImpl();
            case USER -> (T) new UserDaoImpl();
        }
    }
}
