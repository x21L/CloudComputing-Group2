package group02.lukaswais.shopping_cart;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        try {
            String url = "jdbc:mysql://shoppingcart-environment.mysql-shoppingcart:3306";
            DriverManager.getConnection(url, "root", "password");

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
