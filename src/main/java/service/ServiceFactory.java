package service;

import service.custom.impl.CustomerServiceImpl;
import service.custom.impl.ItemServiceImpl;
import service.custom.impl.OrderServiceImpl;
import service.custom.impl.UserServiceImpl;
import util.ServiceType;

public class ServiceFactory {
    private static ServiceFactory instance;

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        return instance == null ? instance = new ServiceFactory() : instance;
    }

    public <T extends SuperService> T getServiceType(ServiceType type) {
        return (T) switch (type) {
            case CUSTOMER -> CustomerServiceImpl.getInstance();
            case ITEM -> ItemServiceImpl.getInstance();
            case ORDER -> OrderServiceImpl.getInstance();
            case USER -> UserServiceImpl.getInstance();
        };
    }
}
