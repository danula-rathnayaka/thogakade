package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

        String code = txtCode.getText();
        String description = txtDescription.getText();
        String pktSize = txtPackSize.getText();
        Double price = Double.parseDouble(txtPrice.getText());
        String qtnOnHand = txtQtnOnHand.getText();

        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();

            PreparedStatement pst;

            if (isAdd) {
                pst = conn.prepareStatement("INSERT INTO item (ItemCode, Description, PackSize, UnitPrice, QtyOnHand) VALUES (?, ?, ?, ?, ?);");

                pst.setString(1, "P" + code);
                pst.setString(2, description);
                pst.setString(3, pktSize);
                pst.setDouble(4, price);
                pst.setString(5, qtnOnHand);
            } else {
                pst = conn.prepareStatement("UPDATE item SET Description = ?, PackSize = ?, UnitPrice = ?, QtyOnHand = ? WHERE ItemCode = ?;");

                pst.setString(1, description);
                pst.setString(2, pktSize);
                pst.setDouble(3, price);
                pst.setString(4, qtnOnHand);
                pst.setString(5, "P" + code);
            }
            try {
                if (pst.executeUpdate() > 0) {
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

        } catch (SQLException | NullPointerException e) {
            showAlert("Database Error", "Could not connect to the Database.", Alert.AlertType.ERROR);
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
