package group02.lukaswais.shopping_cart.controller;

import com.google.gson.Gson;
import group02.lukaswais.shopping_cart.model.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * This list fills the list with books and executes the SQL queries.
 * It also provides the information for the servlet.
 *
 * @author Lukas Wais
 */
public class Controller {
    private final List<Item> allItems;

    public Controller() {
        allItems = new ArrayList<>();
        List<String> books = new ArrayList<>();
        books.add("asdasd");
        books.add("dsdfsd");

        allItems.add(new Item("xyz", books));
        allItems.add(new Item("abc", books));
        allItems.add(new Item("xyz", books));
    }

    public List<Item> getAllItems() {
        return allItems;
    }


    public String getJsonItems() {
        return new Gson().toJson(allItems);
    }

    public void insertToCart(String user, String IBAN) {

        // search if user already has items in cart
        for (Item i : allItems) {
            if (i.getUser().equals(user)) {
                i.getBooks().add(IBAN);
                return;
            }
        }

        // otherwise insert new item in list
        List<String> books = new ArrayList<>();
        books.add(IBAN);
        allItems.add(new Item(user, books));
    }

    public String getJsonBooksFromUser(String user) {
        Item item = allItems.stream().filter(i -> i.getUser().equals(user)).findAny().orElse(null);
        return new Gson().toJson(item);
    }
}
