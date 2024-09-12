package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainDashboardFormController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    void btnCustomerOnAction(ActionEvent event) {
        try {
            rootPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("/view/customer/dashboard_form.fxml")));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @FXML
    void btnOrdersOnAction(ActionEvent event) {
        try {
            rootPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("/view/order/dashboard_form.fxml")));
        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnProductOnAction(ActionEvent event) {
        try {
            rootPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("/view/item/dashboard_form.fxml")));
        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnUsersOnAction(ActionEvent event) {

    }

    @FXML 
    public void btnLogoutOnAction(ActionEvent actionEvent) {
    }
}
