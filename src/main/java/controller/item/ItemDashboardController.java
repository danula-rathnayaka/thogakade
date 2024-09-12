package controller.item;

import com.jfoenix.controls.JFXButton;
import db.DBConnection;
import javafx.collections.FXCollections;
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
import model.Item;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ItemDashboardController implements Initializable {

    @FXML
    public ImageView imgCancelSearch;
    public JFXButton btnDelete;
    public JFXButton btnEdit;
    public ImageView imgDelete;
    public ImageView imgEdit;
    public Group btnEditBtnDelete;

    @FXML
    private TableColumn<Item, String> colDsc;

    @FXML
    private TableColumn<Item, String> colItemCode;

    @FXML
    private TableColumn<Item, String> colPackSize;

    @FXML
    private TableColumn<Item, Integer> colQtnOnHand;

    @FXML
    private TableColumn<Item, Double> colUnitPrice;

    @FXML
    private Label lblUser;

    @FXML
    private TableView<Item> tblProducts;

    @FXML
    private TextField txtSearch;

    private ObservableList<Item> itemList;
    private Item selectedData;

    @FXML
    void btnAddOnAction(ActionEvent event) {
        loadItemDataForm(null);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        deleteItem();
    }

    @FXML
    void btnDeleteOnMouseClick(MouseEvent event) {
        deleteItem();
    }

    @FXML
    void btnEditOnAction(ActionEvent event) {
        editItem();
    }

    @FXML
    void btnEditOnMouseClick(MouseEvent event) {
        editItem();
    }

    private void deleteItem() {
        if (showConfirmationDialog("delete")) {
            if (ItemControllerImpl.getInstance().deleteItem(selectedData.getCode())) {
                showAlert("Success", "Deleted Successfully.", Alert.AlertType.INFORMATION);

                loadData();
            } else {
                showAlert("Error", "Could not Delete.", Alert.AlertType.ERROR);
            }
        }
    }

    private void editItem() {
        if (showConfirmationDialog("edit")) {
            loadItemDataForm(selectedData);
        }
    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {
        loadData();
        tblProducts.getSelectionModel().clearSelection();
        btnEditBtnDelete.setVisible(false);
    }

    @FXML
    void txtSearchTyped(KeyEvent event) {
        String searchTxt = txtSearch.getText().toLowerCase();

        if (searchTxt.equals("")) {
            tblProducts.setItems(itemList);
            imgCancelSearch.setVisible(false);
            return;
        }

        imgCancelSearch.setVisible(true);
        tblProducts.setItems(itemList
                .filtered(item ->
                        item.getCode().toLowerCase().contains(searchTxt) ||
                        item.getDescription().toLowerCase().startsWith(searchTxt))
        );
    }

    @FXML
    void btnCancelSearchOnAction(ActionEvent event) {
        tblProducts.setItems(itemList);
        txtSearch.setText("");
        imgCancelSearch.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDsc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPackSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtnOnHand.setCellValueFactory(new PropertyValueFactory<>("qtnInHand"));

        loadData();

        tblProducts.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                selectedData = newValue;
                btnEditBtnDelete.setVisible(true);
            }
        });
    }

    private void loadData() {
        itemList = ItemControllerImpl.getInstance().getAllProducts();
        tblProducts.setItems(itemList);
    }

    private void loadItemDataForm(Item item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/item/data_form.fxml"));
            Parent root = loader.load();

            ItemDataController controller = loader.getController();
            if (item != null) {
                controller.setItem(item);
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
                "Are you sure you want to %s this. %n%nCode: %s%nDescription: %s%nPack Size: %s%nUnit Price: %s%nQtn In Hand: %d".formatted(
                        operation,
                        selectedData.getCode(),
                        selectedData.getDescription(),
                        selectedData.getPackSize(),
                        selectedData.getUnitPrice(),
                        selectedData.getQtnInHand()
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
