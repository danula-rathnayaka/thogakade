package controller.item;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Item;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;

public class ItemDataController {

    @FXML
    public JFXButton btnCancel;

    @FXML
    private JFXButton btnDone;

    @FXML
    private Label lblTitle;

    @FXML
    private JFXTextField txtCode;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtPackSize;

    @FXML
    private JFXTextField txtPrice;

    @FXML
    private JFXTextField txtQtnOnHand;
    private boolean isAdd = true;

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

        Item item = new Item(
        "P" + txtCode.getText(),
        txtDescription.getText(),
        txtPackSize.getText(),
        Double.parseDouble(txtPrice.getText()),
        Integer.parseInt(txtQtnOnHand.getText()));

        try {
            if (isAdd? ItemControllerImpl.getInstance().addItem(item) : ItemControllerImpl.getInstance().editItem(item)) {
                showAlert("Success", "Successfully updated the Database.\nPlease reload the table.", Alert.AlertType.INFORMATION);

                if (isAdd) {
                    Arrays.asList(txtCode, txtDescription, txtPrice, txtPackSize, txtQtnOnHand).forEach(JFXTextField::clear);
                } else {
                    Stage stage = (Stage) txtCode.getScene().getWindow();
                    stage.close();
                }

            } else {
                showAlert("Error", "Could not update the Database.\nPlease reload the table.", Alert.AlertType.ERROR);
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            showAlert("Item Code Error", "Enter a unique code for the item.", Alert.AlertType.ERROR);
        }
    }

    void setItem(Item item) {
        isAdd = false;

        lblTitle.setText("Update Product");
        btnDone.setText("Update Product");

        txtCode.setText(item.getCode().substring(1));
        txtDescription.setText(item.getDescription());
        txtPackSize.setText(item.getPackSize());
        txtPrice.setText(String.format("%.2f", item.getUnitPrice()));
        txtQtnOnHand.setText(item.getQtnInHand() + "");

        txtCode.setEditable(false);
    }

    private boolean validate() {
        String errorMsg = "";

        if (!txtCode.getText().matches("^\\d{3}$")) {
            errorMsg += "Code must be exactly 3 digits.\n";
        }

        if (!txtDescription.getText().matches("^[\\w\\s\\p{P}]+$")) {
            errorMsg += "Description can only contain letters, numbers, spaces, and punctuation.\n";
        }

        if (!txtPackSize.getText().matches("^\\d+(\\.\\d+)?\\s*[a-zA-Z]+$")) {
            errorMsg += "Pack size must be a number and then a unit of measurement.\n";
        }

        if (!txtPrice.getText().matches("^\\d+\\.\\d{2}$")) {
            errorMsg += "Price must be a valid amount with two decimal places.\n";
        }

        if (!txtQtnOnHand.getText().matches("^\\d+$")) {
            errorMsg += "Quantity on hand must be a positive integer.\n";
        }

        if (!errorMsg.isEmpty()) {
            showAlert("Validation Error", errorMsg, Alert.AlertType.ERROR);
            return false;
        } else {
            return true;
        }
    }

    private void showAlert(String title, String errorMsg, Alert.AlertType type) {
        Alert alert = new Alert(type, errorMsg, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
