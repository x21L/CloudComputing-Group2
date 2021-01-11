package group02.lukaswais.shopping_cart.controller;

import group02.lukaswais.shopping_cart.model.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseController {
    private static DatabaseController instance;
    // Database
    private final Connection connection;

    public DatabaseController() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        this.connection = DBConnection.getInstance().getConnection();
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DatabaseController databaseController = new DatabaseController();
        Statement statement = databaseController.connection.createStatement();
        ResultSet resultSet = databaseController.getUser("ABC");// statement.executeQuery("select * from mysql.shopping_cart");

        while (resultSet.next()) {
            System.out.println(resultSet.getString(1) + " " + resultSet.getString(2));
        }
    }

    public ResultSet getAll() {
        try {
            DatabaseController databaseController = new DatabaseController();
            Statement statement = connection.createStatement();
            return statement.executeQuery("select * from mysql.shopping_cart");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ResultSet getUser(String userID) {
        try {
            DatabaseController databaseController = new DatabaseController();
            Statement statement = connection.createStatement();
            return statement.executeQuery("select * from mysql.shopping_cart WHERE user_id = '" + userID + "'");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void insertNewItem(String userID, String IBAN) {
        try {
            DatabaseController databaseController = new DatabaseController();
            Statement statement = connection.createStatement();
            statement.executeQuery("insert into mysql.shopping_cart (user_id, IBAN) VALUES ('" + userID + "', '" + IBAN + "');");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
