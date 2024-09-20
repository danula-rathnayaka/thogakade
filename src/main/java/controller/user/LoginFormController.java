package controller.user;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import controller.MainDashboardFormController;
import controller.order.PlaceOrderFormController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class LoginFormController {

    @FXML
    private ImageView imgPasswordToggle;

    @FXML
    private JFXPasswordField pwdPassword;

    @FXML
    private JFXTextField txtPassword;

    @FXML
    private JFXTextField txtUsername;

    private boolean showPassword = false;

    @FXML
    void btnLoginOnAction(ActionEvent event) {
        User user = UserControllerImpl.getInstance().loginUser(txtUsername.getText(), showPassword? txtPassword.getText() : pwdPassword.getText());
        if (user==null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Incorrect Username or Password.", ButtonType.OK);
            alert.setTitle("Login Fail");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }
        loadUserForm(user);
        Stage stage = (Stage) txtUsername.getScene().getWindow();
        stage.close();
    }

    @FXML
    void btnPasswordToggleOnAction(ActionEvent event) {
        if (!showPassword){
            txtPassword.setText(pwdPassword.getText());
            pwdPassword.setVisible(false);
            txtPassword.setVisible(true);
            imgPasswordToggle.setImage(new Image("img/hide_password.png"));
            showPassword = true;
        } else{
            pwdPassword.setText(txtPassword.getText());
            txtPassword.setVisible(false);
            pwdPassword.setVisible(true);
            imgPasswordToggle.setImage(new Image("img/show_password.png"));
            showPassword = false;
        }
    }

    private void loadUserForm(User user){
        try {
            UserControllerImpl.getInstance().updateLastLogin(user.getId(), Timestamp.valueOf(LocalDateTime.now()));

            FXMLLoader loader = new FXMLLoader(getClass().getResource(user.getRole() == User.Role.MANAGER? "/view/main_dashboard.fxml" : "/view/order/place_order_form.fxml"));
            Parent root = loader.load();

            if (user.getRole() == User.Role.MANAGER) {
                MainDashboardFormController controller = loader.getController();
                controller.setUserData(user);
            } else {
                PlaceOrderFormController controller = loader.getController();
                controller.setUserData(user);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            showAlert("Error", "Could not load files.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String errorMsg, Alert.AlertType type) {
        Alert alert = new Alert(type, errorMsg, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

}
