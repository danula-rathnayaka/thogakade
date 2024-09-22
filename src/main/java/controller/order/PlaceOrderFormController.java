package controller.order;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controller.customer.CustomerControllerImpl;
import controller.customer.CustomerDataController;
import controller.item.ItemControllerImpl;
import controller.user.UserControllerImpl;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.CartProducts;
import model.Customer;
import model.Item;
import model.User;
import util.ShowAlert;

import java.io.IOException;
import java.net.URL;
import java.sql.Array;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;

public class PlaceOrderFormController implements Initializable {

    @FXML
    public JFXButton btnCustomerOk;

    @FXML
    public JFXButton btnProductOk;

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
    private Label lblPCode;

    @FXML
    private Label lblPDescription;

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
    private TableView<CartProducts> tblCart;

    @FXML
    private TextField txtCustomerSearch;

    @FXML
    private JFXTextField txtPQtyOrdered;

    @FXML
    private TextField txtProductSearch;

    private Customer customerSelected;
    private Item itemSelected;
    private ObservableList<CartProducts> cart = FXCollections.observableArrayList();
    private User user;
    private Double totNetTotal = 0.0;
    private Double totDiscount = 0.0;
    private Double totGrandTotal = 0.0;

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
        if (txtPQtyOrdered.getText().matches("^[1-9][0-9]*$")){
            int pQtyOrdered = Integer.parseInt(txtPQtyOrdered.getText());
             if (pQtyOrdered >= Integer.parseInt(lblPQtyOnHand.getText())){
                 ShowAlert.customAlert("Error", "Do not have sufficient stocks.", Alert.AlertType.ERROR);
                 return;
             }
            double pUnitPrice = Double.parseDouble(lblPUnitPrice.getText());
            double netTotal = pQtyOrdered * pUnitPrice;

            cart.add(new CartProducts(
                    lblPCode.getText(),
                    lblPDescription.getText(),
                    pQtyOrdered,
                    pUnitPrice,
                    netTotal,
                    0.0,
                    netTotal - 0.0
                    ));
            tblCart.setItems(cart);

            setTotalsToLabels(netTotal,0.0,netTotal - 0.0);

            txtProductSearch.setText("P");
            Arrays.asList(lblPCode, lblPDescription, lblPPackSize, lblPUnitPrice, lblPQtyOnHand).forEach(label -> label.setText(""));
            txtPQtyOrdered.setText("");

            imgProductOk.setVisible(false);
            btnProductOk.setVisible(false);
            return;
        }
        ShowAlert.customAlert("Error", "Invalid ordered quantity.", Alert.AlertType.ERROR);
    }

    @FXML
    void btnCustomerOkSearchOnAction(ActionEvent event) {
        customerSelected = CustomerControllerImpl.getInstance().getCustomer(txtCustomerSearch.getText());
        if (customerSelected==null){
            ShowAlert.customAlert("No Item", "No item found for the given item code.", Alert.AlertType.ERROR);
        } else {
            lblCId.setText(customerSelected.getId());
            lblCName.setText(customerSelected.getTitle() + ". " + customerSelected.getName());
            lblCDob.setText(customerSelected.getDob().toString());
            lblCContact.setText("0712512312");
            lblCAddress.setText(customerSelected.getAddress());

            txtCustomerSearch.setText("C");
            imgCustomerOk.setVisible(false);
            btnCustomerOk.setVisible(false);
        }
    }

    @FXML
    void btnLogoutOnAction(ActionEvent event) {
        UserControllerImpl.getInstance().updateLastLogout(user.getId(), Timestamp.valueOf(LocalDateTime.now()));
        try {
            Stage stage = new Stage();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/login_form.fxml"))));
            stage.show();
        } catch (IOException e) {
            ShowAlert.fileNotFoundError();
        }
        Stage stage = (Stage) lblUserName.getScene().getWindow();
        stage.close();
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {
        // TODO
    }

    @FXML
    void btnProductsOkSearchOnAction(ActionEvent event) {
        itemSelected = ItemControllerImpl.getInstance().getItem(txtProductSearch.getText());
        if (itemSelected==null){
            ShowAlert.customAlert("No Item", "No item found for the given item code.", Alert.AlertType.ERROR);
        }
        else {
            lblPCode.setText(itemSelected.getCode());
            lblPDescription.setText(itemSelected.getDescription());
            lblPPackSize.setText(itemSelected.getPackSize());
            lblPUnitPrice.setText(itemSelected.getUnitPrice().toString());
            lblPQtyOnHand.setText(itemSelected.getQtnInHand().toString());
        }
    }

    @FXML
    void txtCustomerKeyPressed(KeyEvent event) {
        imgCustomerOk.setVisible(!txtCustomerSearch.getText().equals("C"));
        btnCustomerOk.setVisible(!txtCustomerSearch.getText().equals("C"));
    }

    @FXML
    void txtProductKeyPressed(KeyEvent event) {
        imgProductOk.setVisible(!txtProductSearch.getText().equals("P"));
        btnProductOk.setVisible(!txtProductSearch.getText().equals("P"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn<?, ?>[] cols = new TableColumn<?, ?>[] {colCode, colDescription, colQty, colUnitPrice, colNetTotal, colDiscount, colTotal};
        String[] colNames = new String[] {"itemCode", "description", "orderQty", "unitPrice", "netTotal", "discount", "total"};
        for (int i = 0; i < cols.length; i++) {
            cols[i].setCellValueFactory(new PropertyValueFactory<>(colNames[i]));
        }

        loadDateAndTime();
    }

    @FXML
    void btnCancelOrderOnAction(ActionEvent event) {
        cart = FXCollections.observableArrayList();
        tblCart.setItems(cart);
        Arrays.asList(lblPCode, lblPDescription, lblPQtyOnHand, lblPUnitPrice, lblPPackSize, lblCId, lblCName, lblCAddress,lblCAddress, lblCContact, lblCDob).forEach(label -> label.setText(""));
        Arrays.asList(lblNetTotal, lblTotalDiscount, lblGrandTotal).forEach(label -> label.setText("Rs. 0"));
    }

    public void setUserData(User user) {
        this.user = user;
        lblUserName.setText(user.getName());
    }

    private void loadDateAndTime() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));

//        -----------------------------------------------

        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime now = LocalTime.now();
            lblTime.setText(String.format("%02d:%02d:%02d", now.getHour(), now.getMinute(), now.getSecond()));
        }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void setTotalsToLabels(Double totNetTotal, Double totDiscount, Double totGrandTotal){

        this.totNetTotal += totNetTotal;
        this.totDiscount += totDiscount;
        this.totGrandTotal += totGrandTotal;

        lblNetTotal.setText("Rs. " + this.totNetTotal.toString());
        lblTotalDiscount.setText("Rs. " + this.totDiscount.toString());
        lblGrandTotal.setText("Rs. " + this.totGrandTotal.toString());
    }
}
