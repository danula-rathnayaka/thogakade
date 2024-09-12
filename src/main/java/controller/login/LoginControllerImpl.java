package controller.login;

public class LoginControllerImpl {

    private static LoginControllerImpl instance;

    private LoginControllerImpl(){}

    public static LoginControllerImpl getInstance(){
        return instance == null? instance = new LoginControllerImpl() : instance;
    }
}
