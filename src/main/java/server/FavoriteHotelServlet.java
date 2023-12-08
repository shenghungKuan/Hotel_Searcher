package server;

import database.DatabaseHandler;
import hotelapp.Hotel;
import hotelapp.HotelSearcher;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavoriteHotelServlet extends HttpServlet {
    /**
     * Called by the server (via the service method) to allow a servlet to handle a favorite hotel GET request.
     *
     * @param request  an HttpServletRequest object that contains the request the client has made of the servlet
     * @param response an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if the request for the GET could not be handled
     * @throws IOException      if an input or output error is detected when the servlet handles the GET request
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html");

        String hotelId = request.getParameter("hotelId");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (username == null) {
            response.sendRedirect("/portal");
            return;
        }

        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        HotelSearcher hotelSearcher = (HotelSearcher) getServletContext().getAttribute("hotelSearcher");

        if (hotelId != null) {
            dbHandler.addFavorite(hotelId, username);
        }

        VelocityEngine ve = (VelocityEngine) getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        Template template = ve.getTemplate("static/Favorite.html");
        List<String> favorite = dbHandler.getFavorite(username);
        List<Hotel> hotels = new ArrayList<>();
        if (favorite != null && favorite.size() > 0) {
            for (String id: favorite) {
                hotels.add(hotelSearcher.find(id));
            }
        }
        context.put("hotels", hotels);
        String message = (String) session.getAttribute("message");
        context.put("message", Objects.requireNonNullElse(message, ""));

        session.setAttribute("message", null);


        template.merge(context, out);
    }

    /**
     * Called by the server (via the service method) to allow a servlet to handle a favorite hotel POST request.
     *
     * @param request  an HttpServletRequest object that contains the request the client has made of the servlet
     * @param response an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if the request for the GET could not be handled
     * @throws IOException      if an input or output error is detected when the servlet handles the POST request
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html");
        HttpSession session = request.getSession();

        String username = (String) session.getAttribute("username");

        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        dbHandler.clearFavorite(username);

        session.setAttribute("message", "Favorite cleared!");
        response.sendRedirect("/favorite");
    }
}
