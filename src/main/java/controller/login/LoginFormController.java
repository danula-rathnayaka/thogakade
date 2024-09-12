package controller.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

}
