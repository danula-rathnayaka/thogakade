//    @FXML
//    void btnCancelSearchOnAction(ActionEvent event) {
//        tblOrder.setItems(orderList);
//        txtSearch.setText("");
//        imgCancelSearch.setVisible(false);
//    }
//
//    @FXML
//    void btnReloadOnAction(ActionEvent event) {
//        loadData();
//        tblOrder.getSelectionModel().clearSelection();
//        groupViewOrder.setVisible(false);
//    }
//
//    @FXML
//    void btnViewOrderOnAction(ActionEvent event) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/order/view_order_details.fxml"));
//            Parent root = loader.load();
//
//            OrderViewController controller = loader.getController();
//            controller.setData(
//                    selectedData.getId(),
//                    service.getOrderedProducts(selectedData.getId()),
//                    CustomerControllerImpl.getInstance().getCustomer(selectedData.getCustId())
//            );
//
//            Stage stage = new Stage();
//            stage.setScene(new Scene(root));
//            stage.show();
//
//        } catch (SQLException e) {
//            showAlert("Database Error", "Could not connect to the Database.", Alert.AlertType.ERROR);
//            System.out.println(e);
//        } catch (IOException e){
//            showAlert("File Error", "File not Found", Alert.AlertType.ERROR);
//            System.out.println(e);
//        }
//    }
//
//    @FXML
//    void txtSearchTyped(KeyEvent event) {
//        String searchTxt = txtSearch.getText().toLowerCase();
//
//        if (searchTxt.equals("")){
//            tblOrder.setItems(orderList);
//            imgCancelSearch.setVisible(false);
//            return;
//        }
//
//        imgCancelSearch.setVisible(true);
//        tblOrder.setItems(orderList
//                .filtered(order ->
//                    order.getId().toLowerCase().contains(searchTxt) ||
//                    order.getCustId().toLowerCase().contains(searchTxt) ||
//                    order.getCustName().toLowerCase().startsWith(searchTxt))
//        );
//    }
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//
//
//        loadData();
//
//        tblOrder.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
//            if (newValue != null) {
//                selectedData = newValue;
//                groupViewOrder.setVisible(true);
//            }
//        });
//    }
//
//    private void loadData(){
//        try {
//            orderList = service.getAllOrders();
//            tblOrder.setItems(orderList);
//        } catch (SQLException e) {
//            showAlert("Database Error", "Could not connect to the Database.", Alert.AlertType.ERROR);
//        }
//    }
//
//    private void showAlert(String title, String errorMsg, Alert.AlertType type) {
//        Alert alert = new Alert(type, errorMsg, ButtonType.OK);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.showAndWait();
//    }

package controller.order;

import controller.customer.CustomerControllerImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import model.Customer;
import model.Order;
import model.OrderedProduct;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class OrderDashboardController implements Initializable {

    @FXML
    private TableColumn<?, ?> colBillTotal;

    @FXML
    private TableColumn<?, ?> colCode;

    @FXML
    private TableColumn<?, ?> colCustomerId;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colDiscount;

    @FXML
    private TableColumn<?, ?> colOrderDate;

    @FXML
    private TableColumn<?, ?> colOrderId;

    @FXML
    private TableColumn<?, ?> colPackSize;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    public TableColumn<?, ?> colTotalDiscount;

    @FXML
    private ImageView imgCancelSearch;

    @FXML
    private Label lblAddress;

    @FXML
    private Label lblCashier;

    @FXML
    private Label lblCity;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblDob;

    @FXML
    private Label lblName;

    @FXML
    private Label lblNumProducts;

    @FXML
    private Label lblProvince;

    @FXML
    private Label lblSalary;

    @FXML
    private Label lblTime;

    @FXML
    private Label lnlNumItems;

    @FXML
    private TableView<Order> tblOrder;

    @FXML
    private TableView<OrderedProduct> tblOrderProducts;

    @FXML
    private TextField txtSearch;

    private ObservableList<Order> orderList;
    private ObservableList<OrderedProduct> orderedProductsList;

    @FXML
    void btnCancelSearchOnAction(ActionEvent event) {
        tblOrder.setItems(orderList);
        txtSearch.setText("");
        imgCancelSearch.setVisible(false);
    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {
        orderList.clear();
        orderList.addAll(OrderControllerImpl.getInstance().getAllOrders());
        tblOrder.setItems(orderList);
    }

    @FXML
    void txtSearchTyped(KeyEvent event) {
        String searchTxt = txtSearch.getText().toLowerCase();

        if (searchTxt.equals("")) {
            tblOrder.setItems(orderList);
            imgCancelSearch.setVisible(false);
            return;
        }

        imgCancelSearch.setVisible(true);
        tblOrder.setItems(orderList
                .filtered(item ->
                        item.getId().toLowerCase().contains(searchTxt) ||
                        item.getCustId().toLowerCase().startsWith(searchTxt) ||
                        item.getDate().toString().startsWith(searchTxt))
        );
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPackSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("orderQty"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));

        colOrderId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("custId"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTotalDiscount.setCellValueFactory(new PropertyValueFactory<>("totDiscount"));
        colBillTotal.setCellValueFactory(new PropertyValueFactory<>("billTotal"));

        loadData();

        tblOrder.getSelectionModel().selectedItemProperty().addListener((observableValue, order, newValue) -> {
            if (newValue!=null){
                try {
                    orderedProductsList = OrderControllerImpl.getInstance().getOrderedProducts(newValue.getId());
                    tblOrderProducts.setItems(orderedProductsList);
                    Customer orderCustomer = CustomerControllerImpl.getInstance().getCustomer(newValue.getCustId());

                    lblName.setText(orderCustomer.getName());
                    lblDob.setText(orderCustomer.getDob().toString());
                    lblSalary.setText(orderCustomer.getSalary().toString());
                    lblAddress.setText(orderCustomer.getAddress());
                    lblCity.setText(orderCustomer.getCity());
                    lblProvince.setText(orderCustomer.getProvince());

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void loadData() {
        orderList = OrderControllerImpl.getInstance().getAllOrders();
        tblOrder.setItems(orderList);
    }
}

