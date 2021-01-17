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
        this.connection = DBConnection.getInstance().getConnection();
        // createTable();
    }

    public Connection getConnection() {
        return connection;
    }

    public ResultSet getAll() {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery("select * from shopping_cart");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ResultSet getUser(String userID) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery("select * from shopping_cart WHERE user_id = '" + userID + "'");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ResultSet getAllTables() {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery("select * from information_schema.tables");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void insertNewItem(String userID, String IBAN) {
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery("insert into shopping_cart (user_id, iban) VALUES ('" + userID + "', '" + IBAN + "');");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean validConnection() throws SQLException {
        return connection.isValid(10);
    }

    public void createTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(String.format(
                    "CREATE TABLE IF NOT EXISTS shopping_cart  %s ("
                            + "%s VARCHAR(128) PRIMARY KEY NOT NULL, "
                            + "%s VARCHAR(128) PRIMARY KEY NOT NULL) ",
                    "shopping_cart", "user_id", "IBAN"));
        } catch (SQLException throwables) {
            System.out.println("Could not create table \n" + throwables.getMessage());
        }
    }
}
