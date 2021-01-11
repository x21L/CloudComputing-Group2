package group02.lukaswais.shopping_cart.servlets;

import group02.lukaswais.shopping_cart.controller.Controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet for the shopping cart.
 * Just responses with JSON and post.
 *
 * @author Lukas Wais
 */
@WebServlet(name = "ServletShoppingCart", value = "/ShoppingCart")
public class ServletShoppingCart extends HttpServlet {
    Controller controller;

    public ServletShoppingCart() {
        controller = new Controller();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter writer = response.getWriter()) {
            writer.println(controller.getJsonBooks());
        }
    }
}
