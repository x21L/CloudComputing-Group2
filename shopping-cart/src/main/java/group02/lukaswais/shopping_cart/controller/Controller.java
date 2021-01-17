package group02.lukaswais.shopping_cart.controller;

import com.google.gson.Gson;

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
        return new Gson().toJson(databaseController.getAll());
    }

    public void insertToCart(String user, String IBAN) {
        databaseController.insertNewItem(user, IBAN);
    }

    public String getJsonBooksFromUser(String userID) {
        return new Gson().toJson(databaseController.getUser(userID));
    }

    public String test() {
        return new Gson().toJson("test from the controller \n" + databaseController);
    }
}
