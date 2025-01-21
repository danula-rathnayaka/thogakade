package controller;

import dto.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.UserService;
import util.ServiceType;
import util.ShowAlert;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class MainDashboardFormController implements Initializable {

    @FXML
    private Label lblUsername;

    @FXML
    private AnchorPane rootPane;

    private User user;

    private final UserService userService = ServiceFactory.getInstance().getServiceType(ServiceType.USER);

    @FXML
    void btnCustomerOnAction(ActionEvent event) {
        loadToAnchorPane("/view/customer/dashboard_form.fxml");
    }

    @FXML
    void btnOrdersOnAction(ActionEvent event) {
        loadToAnchorPane("/view/order/dashboard_form.fxml");
    }

    @FXML
    void btnProductOnAction(ActionEvent event) {
        loadToAnchorPane("/view/item/dashboard_form.fxml");
    }

    @FXML
    void btnUsersOnAction(ActionEvent event) {
        loadToAnchorPane("/view/user/dashboard_form.fxml");
    }

    @FXML
    public void btnLogoutOnAction(ActionEvent actionEvent) {
        userService.updateLastLogout(user.getId(), Timestamp.valueOf(LocalDateTime.now()));
        try {
            Stage stage = new Stage();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/login_form.fxml"))));
            stage.show();
        } catch (IOException e) {
            ShowAlert.fileNotFoundError();
        }
        Stage stage = (Stage) lblUsername.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadToAnchorPane("/view/customer/dashboard_form.fxml");
    }

    public void setUserData(User user) {
        this.user = user;
        lblUsername.setText(user.getUsername());
    }

    private void loadToAnchorPane(String location) {
        try {
            rootPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource(location)));
        } catch (IOException e) {
            ShowAlert.fileNotFoundError();
        }
    }
}
