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
    }

    public String getJsonItems() {
        return new Gson().toJson(getAllItems());
    }

    public void insertToCart(String user, String IBAN) {
        databaseController.insertNewItem(user, IBAN);
    }

    public String getJsonBooksFromUser(String user) {
        Item item = getAllItems().stream().filter(i -> i.getUser().equals(user)).findAny().orElse(null);
        return new Gson().toJson(item);
    }

    private List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        ResultSet resultSet = databaseController.getAll();
        try {
            while (resultSet.next()) {
                String user = resultSet.getString(1);
                String IBAN = resultSet.getString(2);
                if (items.isEmpty()) {// insert first item
                    List<String> books = new ArrayList<>();
                    books.add(IBAN);
                    items.add(new Item(user, books));
                }
                // check if user already has some books in the shopping cart
                Item item = items.stream().filter(i -> i.getUser().equals(user)).findAny().orElse(null);

                if (item == null) {
                    List<String> books = new ArrayList<>();
                    books.add(IBAN);
                    items.add(new Item(user, books));
                } else { // add to bock list
                    item.getBooks().add(IBAN);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return items;
    }

    public String test() {
        return "test from the controller";
    }
}
