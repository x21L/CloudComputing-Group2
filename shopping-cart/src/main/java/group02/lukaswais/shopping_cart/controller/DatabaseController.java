package group02.lukaswais.shopping_cart.controller;

import group02.lukaswais.shopping_cart.model.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseController {
    // Database
    private final Connection connection;


    public DatabaseController() {
        this.connection = DBConnection.getInstance().getConnection();
        createTable();
    }

    public Connection getConnection() {
        return connection;
    }

    public ResultSet getAll() {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("USE books;");
            return statement.executeQuery("select * from shopping_cart;");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ResultSet getUser(String userID) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("USE books;");
            return statement.executeQuery("select * from shopping_cart WHERE user_id = '" + userID + "';");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    public void insertNewItem(String userID, String IBAN) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("USE books;");
            statement.execute("insert into shopping_cart (user_id, IBAN) VALUES ('" + userID + "', '" + IBAN + "');");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteFromCart(String userID, String IBAN) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("USE books;");
            statement.execute("delete from shopping_cart where user_id = '" + userID + "' and IBAN = '" + IBAN + "';");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean validConnection() throws SQLException {
        return connection.isValid(10);
    }

    public void createTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS books;");
            statement.executeUpdate("USE books;");
            statement.execute(
                    " CREATE TABLE IF NOT EXISTS shopping_cart ("
                            + "user_id VARCHAR(255) NOT NULL, "
                            + "IBAN VARCHAR(255) NOT NULL, "
                            + "PRIMARY KEY (user_id,IBAN))");
        } catch (SQLException throwables) {
            System.out.println("Could not create table \n" + throwables.getMessage());
        }
    }
}
