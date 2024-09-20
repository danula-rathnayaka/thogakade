package controller.order;

import com.jfoenix.controls.JFXTextField;
import controller.customer.CustomerDataController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import util.ShowAlert;

import java.io.IOException;

public class PlaceOrderFormController {

    @FXML
    private TableColumn<?, ?> colCode;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colDiscount;

    @FXML
    private TableColumn<?, ?> colNetTotal;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private ImageView imgCustomerOk;

    @FXML
    private ImageView imgProductOk;

    @FXML
    private Label lblCAddress;

    @FXML
    private Label lblCContact;

    @FXML
    private Label lblCDob;

    @FXML
    private Label lblCId;

    @FXML
    private Label lblCName;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblGrandTotal;

    @FXML
    private Label lblNetTotal;

    @FXML
    private Label lblNoItems;

    @FXML
    private Label lblNoProducts;

    @FXML
    private Label lblPCode;

    @FXML
    private Label lblPName;

    @FXML
    private Label lblPPackSize;

    @FXML
    private Label lblPQtyOnHand;

    @FXML
    private Label lblPUnitPrice;

    @FXML
    private Label lblTime;

    @FXML
    private Label lblTotalDiscount;

    @FXML
    private Label lblUserName;

    @FXML
    private TableView<?> tblCart;

    @FXML
    private TextField txtCustomerSearch;

    @FXML
    private JFXTextField txtPQtyOrdered;

    @FXML
    private TextField txtProductSearch;

    @FXML
    void btnAddCustomerOnAction(ActionEvent event) {
        try {
            Stage stage = new Stage();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/customer/data_form.fxml"))));
            stage.show();
        } catch (IOException e) {
            ShowAlert.fileNotFoundError();
        }
    }

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
        // TODO
    }

    @FXML
    void btnCustomerOkSearchOnAction(ActionEvent event) {
        // TODO
    }

    @FXML
    void btnLogoutOnAction(ActionEvent event) {
        // TODO
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {
        // TODO
    }

    @FXML
    void btnProductsOkSearchOnAction(ActionEvent event) {
        // TODO
    }

    @FXML
    void txtSearchTyped(KeyEvent event) {
        // TODO
    }
}
