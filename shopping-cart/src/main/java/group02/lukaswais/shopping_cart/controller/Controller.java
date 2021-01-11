package group02.lukaswais.shopping_cart.controller;

import com.google.gson.Gson;
import group02.lukaswais.shopping_cart.model.Books;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This list fills the list with books and executes the SQL queries.
 * It also provides the information for the servlet.
 *
 * @author Lukas Wais
 */
public class Controller {
    private final Map<String, List<String>> books;

    public Controller() {
        books = new Books().getBooksInCart();
        List<String> b = new ArrayList<>();
        b.add("IBAN: xyz");
        b.add("IBAN: kjk");
        books.put("123", b);
        books.put("456", b);
    }

    public Map<String, List<String>> getBooks() {
        return books;
    }

    public String getJsonBooks() {
        return new Gson().toJson(books);
    }
}
