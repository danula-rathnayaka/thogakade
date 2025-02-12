package controller.customer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import dto.Customer;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.CustomerService;
import util.ServiceType;
import util.ShowAlert;

import java.net.URL;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.ResourceBundle;

public class CustomerDataController implements Initializable {

    @FXML
    private DatePicker bdayDatePicker;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnDone;

    @FXML
    private JFXComboBox<String> comboTitle;

    @FXML
    private Label lblTitle;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtCity;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtPostalCode;

    @FXML
    private JFXTextField txtProvince;

    @FXML
    private JFXTextField txtSalary;

    private boolean isAdd = true;

    private final CustomerService service = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);

    @FXML
    void btnCancelOnAction(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void btnDoneOnAction(ActionEvent event) {
        if (!validate()) {
            return;
        }

        Customer customer = new Customer(
        "C" + txtId.getText(),
        comboTitle.getValue(),
        txtName.getText(),
        bdayDatePicker.getValue(),
        Double.parseDouble(txtSalary.getText()),
        txtAddress.getText(),
        txtCity.getText(),
        txtProvince.getText(),
        txtPostalCode.getText()
        );

        try {
            if (isAdd ? service.addCustomer(customer) : service.updateCustomer(customer)) {
                ShowAlert.customAlert("Success", "Successfully updated the Database.\nPlease reload the table.", Alert.AlertType.INFORMATION);

                if (isAdd) {
                    Arrays.asList(txtId, txtName, txtSalary, txtAddress, txtCity, txtProvince, txtPostalCode).forEach(JFXTextField::clear);
                    comboTitle.setValue("Mr");
                    bdayDatePicker.setValue(null);
                } else {
                    Stage stage = (Stage) txtId.getScene().getWindow();
                    stage.close();
                }

            } else {
                ShowAlert.customAlert("Error", "Could not update the Database.\nPlease reload the table.", Alert.AlertType.ERROR);
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            ShowAlert.customAlert("Item Code Error", "Enter a unique code for the item.", Alert.AlertType.ERROR);
        }
    }

    void setCustomer(Customer customer) {
        isAdd = false;

        lblTitle.setText("Update Customer");
        btnDone.setText("Update Customer");

        txtId.setText(customer.getId().substring(1));
        comboTitle.setValue(customer.getTitle());
        txtName.setText(customer.getName());
        bdayDatePicker.setValue(customer.getDob());
        txtSalary.setText(String.format("%.2f", customer.getSalary()));
        txtAddress.setText(customer.getAddress());
        txtCity.setText(customer.getCity());
        txtProvince.setText(customer.getProvince());
        txtPostalCode.setText(customer.getPostalCode());

        txtId.setEditable(false);
    }

    private boolean validate() {
        String errorMsg = "";

        if (!txtId.getText().matches("^\\d{3}$")) {
            errorMsg += "ID must be exactly 3 digits.\n";
        }

        if (!txtName.getText().matches("^[A-Za-z\\s]{1,30}$")) {
            errorMsg += "Name can only contain letters and spaces.\n";
        }

        if (!txtSalary.getText().matches("^\\d{1,8}\\.\\d{2}$")) {
            errorMsg += "Salary must be a valid amount with two decimal places.\n";
        }

        if (!txtAddress.getText().matches("^[\\w\\s,.#-]{1,30}$")) {
            errorMsg += "Address must contain only letters, numbers, spaces, and punctuation. \n";
        }

        if (!txtCity.getText().matches("^[A-Za-z\\s]{1,20}$")) {
            errorMsg += "City can only contain letters and spaces.\n";
        }

        if (!txtProvince.getText().matches("^[A-Za-z\\s]{1,20}$")) {
            errorMsg += "Province can only contain letters and spaces.\n";
        }

        if (!txtPostalCode.getText().matches("^\\d{1,9}$")) {
            errorMsg += "Postal Code must be between 1 and 9 digits.\n";
        }


        if (!errorMsg.isEmpty()) {
            ShowAlert.customAlert("Validation Error", errorMsg, Alert.AlertType.ERROR);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboTitle.setItems(FXCollections.observableArrayList("Mr", "Ms", "Miss"));
    }
}
