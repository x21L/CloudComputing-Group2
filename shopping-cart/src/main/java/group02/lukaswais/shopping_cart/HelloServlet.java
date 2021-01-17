package group02.lukaswais.shopping_cart;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://10.8.11.20:3306";
            Connection conn = DriverManager.getConnection(url, "root", "password");
            Statement statement = conn.createStatement();
            // statement.executeQuery("insert into shopping_cart (user_id, IBAN) VALUES ('abc123', 'def456');");
            statement.execute(
                    "CREATE DATABASE  IF NOT EXISTS  books;" +
                            " USE books;" +
                            " CREATE TABLE IF NOT EXISTS shoppingcart ("
                            + "user_id VARCHAR(255) NOT NULL, "
                            + "IBAN VARCHAR(255) NOT NULL, "
                            + "PRIMARY KEY (user_id,IBAN))");
            // Hello
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h1>" + message + "</h1>");
            out.println("</body></html>");
        } catch (Exception e) {
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            e.printStackTrace(out);
            out.println("</body></html>");
        }
    }

    public void destroy() {
    }
}
