package group02.lukaswais.shopping_cart.controller;

import com.google.gson.Gson;
import group02.lukaswais.shopping_cart.model.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This list fills the list with books and executes the SQL queries.
 * It also provides the information for the servlet.
 *
 * @author Lukas Wais
 */
public class Controller {
    private final DatabaseController databaseController;

    public Controller() throws ClassNotFoundException {
        databaseController = new DatabaseController();
        databaseController.createTable();
    }

    public String getJsonItems() throws SQLException {
        List<Item> items = new ArrayList<>();
        ResultSet resultSet = databaseController.getAll();
        while (resultSet.next()) {
            final Item item = new Item(
                    resultSet.getString("user_id"),
                    resultSet.getString("IBAN")
            );
            items.add(item);
        }
        return new Gson().toJson(items);
    }

    public void insertToCart(String user, String IBAN) {
        databaseController.insertNewItem(user, IBAN);
    }

    public void deleteFromCart(String user, String IBAN) {
        databaseController.deleteFromCart(user, IBAN);
    }

    public String getJsonBooksFromUser(String userID) throws SQLException {
        List<String> books = new ArrayList<>();
        ResultSet resultSet = databaseController.getAll();
        while (resultSet.next()) {
            books.add(resultSet.getString("IBAN"));
        }
        return new Gson().toJson(books);
    }

    public String test() throws ClassNotFoundException, SQLException {
        return new Gson().toJson("test from the controller " + databaseController.getConnection() + " " /*+ Class.forName("com.mysql.jdbc.Driver") +
                " " + DriverManager.getConnection("jdbc:mysql://10.8.11.20:3306", "root", "password")*/ +
                databaseController.validConnection() + " " + databaseController.getAll());
    }
}
