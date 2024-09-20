package controller.user;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.customer.CustomerControllerImpl;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.User;

import java.net.URL;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.ResourceBundle;

public class UserDataFormController implements Initializable {

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnDone;

    @FXML
    private JFXComboBox<User.Role> cmbRole;

    @FXML
    private Label lblTitle;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtPassword;

    @FXML
    private JFXTextField txtUsername;

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

        User user = new User(
                "U" + txtId.getText(),
                txtUsername.getText(),
                txtName.getText(),
                txtPassword.getText(),
                cmbRole.getValue(),
                Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now())
        );

        try {
            if (isAdd? UserControllerImpl.getInstance().addUser(user) : UserControllerImpl.getInstance().updateUser(user)) {
                showAlert("Success", "Successfully updated the Database.\nPlease reload the table.", Alert.AlertType.INFORMATION);

                if (isAdd) {
                    Arrays.asList(txtId, txtUsername, txtPassword, txtName).forEach(JFXTextField::clear);
                    cmbRole.setValue(User.Role.CASHIER);
                } else {
                    Stage stage = (Stage) txtId.getScene().getWindow();
                    stage.close();
                }

            } else {
                showAlert("Error", "Could not update the Database.\nPlease reload the table.", Alert.AlertType.ERROR);
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            showAlert("Item Code Error", "Enter a unique code for the item.", Alert.AlertType.ERROR);
        }

    }

    void setUser(User user){
        isAdd = false;

        lblTitle.setText("Update User");
        btnDone.setText("Update User");

        txtId.setText(user.getId().substring(1));
        txtUsername.setText(user.getUsername());
        txtName.setText(user.getName());
        txtPassword.setText(user.getPassword_hash());
        cmbRole.setValue(user.getRole());

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
        if (txtUsername.getText().length() > 30) {
            errorMsg += "Username can only have max 30 characters.\n";
        }
        if (txtPassword.getText().length() > 30) {
            errorMsg += "Password can only have max 30 characters.\n";
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbRole.setItems(FXCollections.observableArrayList(User.Role.CASHIER, User.Role.MANAGER));
    }
}
