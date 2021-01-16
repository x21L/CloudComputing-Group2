package group02.lukaswais.shopping_cart.model;

import java.util.List;

/**
 * This class represents a List of items/books with the corresponding user in the shopping cart.
 * It is loaded from the database.
 *
 * @author Lukas Wais
 */
public class Item {
    private final String user;
    private final List<String> books;

    public Item(String user, List<String> books) {
        this.user = user;
        this.books = books;
    }

    public String getUser() {
        return user;
    }

    public List<String> getBooks() {
        return books;
    }

    @Override
    public String toString() {
        return "Item{" +
                "user='" + user + '\'' +
                ", books=" + books +
                '}';
    }
}
