package controller.customer;

import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Customer;
import util.ShowAlert;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CustomerDashboardController implements Initializable {

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnEdit;

    @FXML
    private Group btnEditBtnDelete;

    @FXML
    private TableColumn<Customer, String> colAddress;

    @FXML
    private TableColumn<Customer, String> colCity;

    @FXML
    private TableColumn<Customer, LocalDate> colDob;

    @FXML
    private TableColumn<Customer, String> colId;

    @FXML
    public TableColumn<Customer, String> colTitle;

    @FXML
    private TableColumn<Customer, String> colName;

    @FXML
    private TableColumn<Customer, String> colPostalCode;

    @FXML
    private TableColumn<Customer, String> colProvince;

    @FXML
    private TableColumn<Customer, Double> colSalary;

    @FXML
    private ImageView imgCancelSearch;

    @FXML
    private ImageView imgDelete;

    @FXML
    private ImageView imgEdit;

    @FXML
    private Label lblUser;

    @FXML
    private TableView<Customer> tblCustomers;

    @FXML
    private TextField txtSearch;

    private ObservableList<Customer> customerList;

    private Customer selectedData;

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        deleteCustomer();
    }

    @FXML
    void btnDeleteOnMouseClick(MouseEvent event) {
        deleteCustomer();
    }

    @FXML
    void btnEditOnAction(ActionEvent event) {
        editCustomer();
    }

    @FXML
    void btnEditOnMouseClick(MouseEvent event) {
        editCustomer();
    }

    private void deleteCustomer(){
        if (
                ShowAlert.showConfirmationDialog("Are you sure you want to Delete this? %n%nCode: %s%nTitle: %s%nName: %s%nDate of Birth: %s%nSalary: %s%nAddress: %s%nCity: %s%nProvince: %s%nPostal Code: %s".formatted(
                        selectedData.getId(),
                        selectedData.getTitle(),
                        selectedData.getName(),
                        selectedData.getDob(),
                        selectedData.getSalary(),
                        selectedData.getAddress(),
                        selectedData.getCity(),
                        selectedData.getProvince(),
                        selectedData.getPostalCode()))
        ){
            if (CustomerControllerImpl.getInstance().deleteCustomer(selectedData.getId())) {
                ShowAlert.customAlert("Success", "Deleted Successfully.", Alert.AlertType.INFORMATION);

                loadData();
            } else {
                ShowAlert.customAlert("Error", "Could not Delete.", Alert.AlertType.ERROR);
            }
        }
    }

    private void editCustomer() {
        if (
                ShowAlert.showConfirmationDialog("Are you sure you want to Edit this? %n%nCode: %s%nTitle: %s%nName: %s%nDate of Birth: %s%nSalary: %s%nAddress: %s%nCity: %s%nProvince: %s%nPostal Code: %s".formatted(
                selectedData.getId(),
                selectedData.getTitle(),
                selectedData.getName(),
                selectedData.getDob(),
                selectedData.getSalary(),
                selectedData.getAddress(),
                selectedData.getCity(),
                selectedData.getProvince(),
                selectedData.getPostalCode()))
        ){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customer/data_form.fxml"));
                Parent root = loader.load();

                CustomerDataController controller = loader.getController();
                controller.setCustomer(selectedData);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                ShowAlert.fileNotFoundError();
            }
        }
    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {
        loadData();
        tblCustomers.getSelectionModel().clearSelection();
        btnEditBtnDelete.setVisible(false);
    }

    @FXML
    void btnCancelSearchOnAction(ActionEvent event) {
        tblCustomers.setItems(customerList);
        txtSearch.setText("");
        imgCancelSearch.setVisible(false);
    }

    @FXML
    void txtSearchTyped(KeyEvent event) {
        String searchTxt = txtSearch.getText().toLowerCase();

        if (searchTxt.equals("")){
            tblCustomers.setItems(customerList);
            imgCancelSearch.setVisible(false);
            return;
        }

        imgCancelSearch.setVisible(true);
        tblCustomers.setItems(customerList
                .filtered(customer ->
                        customer.getId().toLowerCase().contains(searchTxt) ||
                        customer.getName().toLowerCase().startsWith(searchTxt))
        );
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn<?, ?>[] cols = new TableColumn<?, ?>[] {colId, colTitle, colName, colDob, colSalary, colAddress, colCity, colProvince, colPostalCode};
        String[] colNames = new String[] {"id", "title", "name", "dob", "salary", "address", "city", "province", "postalCode"};
        for (int i = 0; i < cols.length; i++) {
            cols[i].setCellValueFactory(new PropertyValueFactory<>(colNames[i]));
        }


        loadData();

        tblCustomers.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                selectedData = newValue;
                btnEditBtnDelete.setVisible(true);
            }
        });
    }

    private void loadData(){
        customerList = CustomerControllerImpl.getInstance().getAllCustomers();
        tblCustomers.setItems(customerList);
    }
}
