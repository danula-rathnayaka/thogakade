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

    public <T extends SuperDao> T getDaoType(DaoType type) {
        return (T) switch (type) {
            case CUSTOMER -> CustomerDaoImpl.getInstance();
            case ITEM -> ItemDaoImpl.getInstance();
            case ORDER -> OrderDaoImpl.getInstance();
            case USER -> new UserDaoImpl();
        };
    }
}
