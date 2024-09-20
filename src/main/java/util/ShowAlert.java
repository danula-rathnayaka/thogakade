package util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ShowAlert {
    public static void customAlert(String title, String errorMsg, Alert.AlertType type) {
        Alert alert = new Alert(type, errorMsg, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public static void fileNotFoundError(){
        customAlert("Error", "Could not load files.", Alert.AlertType.ERROR);
    }

    public static boolean showConfirmationDialog(String message) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION, message,
                ButtonType.YES,
                ButtonType.NO);

        alert.setTitle("Conformation");
        alert.setHeaderText(null);

        ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
        return result==ButtonType.YES;
    }

    public static void databaseError() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Could not connect the Database.", ButtonType.OK);
        alert.setTitle("Error Occurred.");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
