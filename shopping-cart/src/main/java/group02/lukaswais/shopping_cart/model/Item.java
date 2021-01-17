package group02.lukaswais.shopping_cart.model;

/**
 * This class represents a List of items/books with the corresponding user in the shopping cart.
 * It is loaded from the database.
 *
 * @author Lukas Wais
 */
public class Item {
    private final String user;
    private final String book;

    public Item(String user, String book) {
        this.user = user;
        this.book = book;
    }

    public String getUser() {
        return user;
    }

    public String getBooks() {
        return book;
    }

    @Override
    public String toString() {
        return "Item{" +
                "user='" + user + '\'' +
                ", books=" + book +
                '}';
    }
}
