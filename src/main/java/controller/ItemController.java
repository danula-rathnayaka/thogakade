package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import model.Item;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ItemController implements Initializable {

    @FXML
    private TableColumn colDsc;

    @FXML
    private TableColumn colItemCode;

    @FXML
    private TableColumn colPackSize;

    @FXML
    private TableColumn colQtnOnHand;

    @FXML
    private TableColumn colUnitPrice;

    @FXML
    private Label lblUser;

    @FXML
    private TableView<Item> tblProducts;

    @FXML
    private TextField txtSearch;

    @FXML
    void btnAddOnAction(ActionEvent event) {
        //TODO
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        //TODO
    }

    @FXML
    void btnEditOnAction(ActionEvent event) {
        //TODO
    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {
        loadData();
    }

    @FXML
    void txtSearchTyped(KeyEvent event) {
        //TODO
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDsc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPackSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtnOnHand.setCellValueFactory(new PropertyValueFactory<>("qtnInHand"));

        loadData();
    }

    private void loadData(){
        try {
            ObservableList<Item> itemList = FXCollections.observableArrayList();

            ResultSet rst = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT * FROM item;");

            while (rst.next()){
                itemList.add(new Item(
                        rst.getString(1),
                        rst.getString(2),
                        rst.getString(3),
                        rst.getDouble(4),
                        rst.getInt(5)
                ));
            }

            tblProducts.setItems(itemList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
