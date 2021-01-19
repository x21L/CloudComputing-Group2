package group02.lukaswais.shopping_cart.servlets;

import com.google.gson.Gson;
import group02.lukaswais.shopping_cart.controller.Controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * Servlet for the shopping cart.
 * Just responses with JSON and post.
 *
 * @author Lukas Wais
 */
@WebServlet(name = "ServletShoppingCart", value = "/ShoppingCart")
public class ServletShoppingCart extends HttpServlet {
    private Controller controller;

    public void init() {
        try {
            controller = new Controller();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
        processError(request, response); // error handling
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "getAll":
                getAll(response);
                break;
            case "insert":
                insert(response, request.getParameter("user"), request.getParameter("IBAN"));
                break;
            case "delete":
                delete(response, request.getParameter("user"), request.getParameter("IBAN"));
                break;
            case "getUser":
                getBooksFromUser(response, request.getParameter("user"));
            case "test":
                test(response);
                break;
            default:
                errorMessage(response, action + " is not a valid parameter");
        }
        processError(request, response); // error handling
    }


    private void getAll(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");

        try (PrintWriter writer = response.getWriter()) {
            writer.println(controller.getJsonItems());
        } catch (IOException | SQLException e) {
            errorMessage(response, e.getMessage());
        }
    }

    private void test(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");

        try (PrintWriter writer = response.getWriter()) {
            writer.println(controller.test());
        } catch (IOException | ClassNotFoundException | SQLException e) {
            errorMessage(response, e.getMessage());
        }
    }

    private void getBooksFromUser(HttpServletResponse response, String user) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");

        try (PrintWriter writer = response.getWriter()) {
            String json = controller.getJsonBooksFromUser(user);
            if (json == null) {
                errorMessage(response, user + " this user does not exist");
            } else {
                writer.println(json);
            }
        } catch (IOException | SQLException e) {
            errorMessage(response, e.getMessage());
        }
    }

    private void delete(HttpServletResponse response, String user, String IBAN) {
        controller.deleteFromCart(user, IBAN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");

        try (PrintWriter writer = response.getWriter()) {
            writer.println(new Gson().toJson("deleted user " + user));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insert(HttpServletResponse response, String user, String IBAN) {
        controller.insertToCart(user, IBAN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");

        try (PrintWriter writer = response.getWriter()) {
            writer.println(new Gson().toJson("inserted user " + user));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void errorMessage(HttpServletResponse response, String message) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");

        try (PrintWriter writer = response.getWriter()) {
            writer.println(new Gson().toJson(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processError(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Analyze the servlet exception
        Throwable throwable = (Throwable) request
                .getAttribute("javax.servlet.error.exception");
        Integer statusCode = (Integer) request
                .getAttribute("javax.servlet.error.status_code");
        String requestUri = (String) request
                .getAttribute("javax.servlet.error.request_uri");
        if (requestUri == null) {
            requestUri = "Unknown";
        }

        // Set response content type
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.write("<html><head><title>Exception/Error Details</title></head><body>");
        if (statusCode != 500) {
            out.write("<h3>Error Details</h3>");
            out.write("<strong>Status Code</strong>:" + statusCode + "<br>");
            out.write("<strong>Requested URI</strong>:" + requestUri);
        } else {
            out.write("<h3>Exception Details</h3>");
            out.write("<li>Exception Name:" + throwable.getClass().getName() + "</li>");
            out.write("<li>Requested URI:" + requestUri + "</li>");
            out.write("<li>Exception Message:" + throwable.getMessage() + "</li>");
            out.write("</ul>");
        }

        out.write("<br><br>");
        out.write("<a href=\"index.html\">Home Page</a>");
        out.write("</body></html>");
    }
}
