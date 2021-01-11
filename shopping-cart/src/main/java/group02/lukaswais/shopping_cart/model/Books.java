package group02.lukaswais.shopping_cart.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a map of books with the corresponding user in the shopping cart.
 * It is loaded from the database.
 *
 * @author Lukas Wais
 */
public class Books {
    private final Map<String, List<String>> booksInCart;

    public Books() {
        this.booksInCart = new HashMap<>();
    }

    public Map<String, List<String>> getBooksInCart() {
        return booksInCart;
    }
}
