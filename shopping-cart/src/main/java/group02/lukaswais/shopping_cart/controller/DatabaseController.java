package group02.lukaswais.shopping_cart.controller;

import group02.lukaswais.shopping_cart.model.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseController {
    // Database
    private final Connection connection;

    public DatabaseController() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        this.connection = DBConnection.getInstance().getConnection();
    }

    public ResultSet getAll() {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery("select * from mysql.shopping_cart");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ResultSet getUser(String userID) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery("select * from mysql.shopping_cart WHERE user_id = '" + userID + "'");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void insertNewItem(String userID, String IBAN) {
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery("insert into mysql.shopping_cart (user_id, IBAN) VALUES ('" + userID + "', '" + IBAN + "');");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}