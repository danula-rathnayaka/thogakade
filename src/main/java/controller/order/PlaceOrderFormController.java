package controller.order;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.customer.CustomerControllerImpl;
import controller.item.ItemControllerImpl;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.util.Duration;
import model.Customer;
import model.Item;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.ResourceBundle;

public class PlaceOrderFormController implements Initializable {

    @FXML
    private TableColumn<?, ?> colCode;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colunitPrice;

    @FXML
    private JFXComboBox<String> comboCustomer;

    @FXML
    private JFXComboBox<String> comboItem;

    @FXML
    private Label lblCAddress;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblDescription;

    @FXML
    private Label lblNetTotal;

    @FXML
    private Label lblOrderID;

    @FXML
    private Label lblStock;

    @FXML
    private Label lblTime;

    @FXML
    private Label lblCName;

    @FXML
    private JFXTextField txtQty;

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDateTime();
        loadComboBoxData();

        comboCustomer.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if (t1 != null){
                setCustomerData(t1);
            }
        });

        comboItem.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if (t1 != null){
                setItemData(t1);
            }
        });
    }

    private void loadDateTime(){
        SimpleDateFormat f = new SimpleDateFormat("yyy-MM-dd");
        lblDate.setText(f.format(new Date()));

        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime now = LocalTime.now();
            lblTime.setText(String.format("%02d:%02d:%02d", now.getHour(), now.getMinute(), now.getSecond()));
        }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void loadComboBoxData(){
        comboCustomer.setItems(CustomerControllerImpl.getInstance().getCustomerIDs());
        comboItem.setItems(ItemControllerImpl.getInstance().getProductCodes());
    }

    private void setCustomerData(String id){
        Customer customer = CustomerControllerImpl.getInstance().getCustomer(id);
        lblCName.setText(customer.getId() + ". " + customer.getName());
        lblCAddress.setText(customer.getAddress());
    }

    private void setItemData(String id){
        Item item = ItemControllerImpl.getInstance().getItem(id);
        lblDescription.setText(item.getDescription());
        lblStock.setText(item.getQtnInHand().toString());
    }
}
