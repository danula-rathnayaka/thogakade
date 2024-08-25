package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;
    private Connection connection;

    private DBConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/thogakade", "root", "12345");
    }

    public static DBConnection getInstance() throws SQLException {
        return instance==null? new DBConnection() : instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
