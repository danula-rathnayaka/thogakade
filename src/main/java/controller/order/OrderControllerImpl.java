package controller.order;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Order;
import model.OrderedProduct;
import util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderControllerImpl implements OrderController{

    private static OrderControllerImpl instance;

    private OrderControllerImpl(){}

    public static OrderControllerImpl getInstance(){
        return instance == null? instance = new OrderControllerImpl() : instance;
    }

    @Override
    public ObservableList<Order> getAllOrders() {
        ObservableList<Order> orderList = FXCollections.observableArrayList();
        try {
            ResultSet rst = CrudUtil.execute("SELECT od.OrderID, cus.CustID, od.OrderDate, sum(orinfo.Discount) FROM orders od JOIN customer cus ON od.CustID = cus.CustID JOIN orderdetail orinfo ON od.OrderID = orinfo.OrderID GROUP BY orinfo.OrderID;");

            while (rst.next()){
                orderList.add(new Order(
                        rst.getString(1),
                        rst.getString(2),
                        rst.getDate(3).toLocalDate(),
                        rst.getInt(4),
//                      rst.getDouble(5)
                        1000.00
                ));
            }
        } catch (SQLException e) {
            showErrorAlert();
        }

        return orderList;
    }

    @Override
    public boolean addOrder(Order order){
        return true;
    }

    @Override
    public ObservableList<OrderedProduct> getOrderedProducts(String id) throws SQLException{
        ObservableList<OrderedProduct> orderedProducts = FXCollections.observableArrayList();
        ResultSet rst = CrudUtil.execute("SELECT ordinfo.ItemCode, item.Description, item.PackSize, item.UnitPrice, ordinfo.OrderQTY, ordinfo.Discount FROM orderdetail ordinfo join item on ordinfo.ItemCode = item.ItemCode WHERE ordinfo.OrderID = '"+ id +"';");

        while (rst.next()){
            orderedProducts.add(new OrderedProduct(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4),
                    rst.getInt(5),
                    rst.getInt(6)

            ));
        }
        return orderedProducts;
    }

    private void showErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Could not connect the Database.", ButtonType.OK);
        alert.setTitle("Error Occurred.");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
