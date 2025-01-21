package controller.user;

import com.jfoenix.controls.JFXButton;
import dto.User;
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
import service.ServiceFactory;
import service.custom.UserService;
import util.ServiceType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserDashboardFormController implements Initializable {

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnEdit;

    @FXML
    private Group btnEditBtnDelete;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colLastLogin;

    @FXML
    private TableColumn<?, ?> colLastLogout;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colRole;

    @FXML
    private TableColumn<?, ?> colUserName;

    @FXML
    private ImageView imgCancelSearch;

    @FXML
    private ImageView imgDelete;

    @FXML
    private ImageView imgEdit;

    @FXML
    private TableView<User> tblUsers;

    @FXML
    private TextField txtSearch;

    private ObservableList<User> userList;

    private User selectedData;

    private final UserService service = ServiceFactory.getInstance().getServiceType(ServiceType.USER);

    @FXML
    void btnAddOnAction(ActionEvent event) {
        loadUserDataForm(null);
    }

    @FXML
    void btnCancelSearchOnAction(ActionEvent event) {
        tblUsers.setItems(userList);
        txtSearch.setText("");
        imgCancelSearch.setVisible(false);
    }

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

    private void deleteCustomer() {
        if(showConfirmationDialog("delete")){
            if (service.deleteUser(selectedData.getId())) {
                showAlert("Success", "Deleted Successfully.", Alert.AlertType.INFORMATION);

                loadData();
            } else {
                showAlert("Error", "Could not Delete.", Alert.AlertType.ERROR);
            }
        }
    }

    private void editCustomer() {
        if (showConfirmationDialog("edit")) {
            loadUserDataForm(selectedData);
        }
    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {
        loadData();
        tblUsers.getSelectionModel().clearSelection();
        btnEditBtnDelete.setVisible(false);
    }

    @FXML
    void txtSearchTyped(KeyEvent event) {
        String searchTxt = txtSearch.getText().toLowerCase();

        if (searchTxt.equals("")) {
            tblUsers.setItems(userList);
            imgCancelSearch.setVisible(false);
            return;
        }

        imgCancelSearch.setVisible(true);
        tblUsers.setItems(userList
                .filtered(user ->
                        user.getId().toLowerCase().contains(searchTxt) ||
                                user.getName().toLowerCase().startsWith(searchTxt))
        );
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("username"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colLastLogin.setCellValueFactory(new PropertyValueFactory<>("lastLogin"));
        colLastLogout.setCellValueFactory(new PropertyValueFactory<>("lastLogout"));

        loadData();

        tblUsers.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                selectedData = newValue;
                btnEditBtnDelete.setVisible(true);
            }
        });
    }

    private void loadData() {
        userList = service.getAllUsers();
        tblUsers.setItems(userList);
    }

    private void loadUserDataForm(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user/data_form.fxml"));
            Parent root = loader.load();

            UserDataFormController controller = loader.getController();
            if (user != null) {
                controller.setUser(user);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            showAlert("Error", "Could not load files.", Alert.AlertType.ERROR);
        }
    }

    private boolean showConfirmationDialog(String operation) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to %s this? %n%nCode: %s%nUsername: %s%nName: %s%nRole: %s%nLast Login: %s%nLast Logout: %s".formatted(
                        operation,
                        selectedData.getId(),
                        selectedData.getUsername(),
                        selectedData.getName(),
                        selectedData.getRole(),
                        selectedData.getLastLogin(),
                        selectedData.getLastLogout()
                ),
                ButtonType.YES,
                ButtonType.NO);

        alert.setTitle("Conformation");
        alert.setHeaderText(null);

        ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
        return result == ButtonType.YES;
    }

    private void showAlert(String title, String errorMsg, Alert.AlertType type) {
        Alert alert = new Alert(type, errorMsg, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
