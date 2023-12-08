package server;

import database.DatabaseHandler;
import hotelapp.Hotel;
import hotelapp.HotelSearcher;
import hotelapp.Review;
import hotelapp.ReviewSearcher;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExpediaHistoryServlet extends HotelServlet{
    /**
     * Called by the server (via the service method) to allow a servlet to handle a hotelInfo GET request.
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
//        hotelId = StringEscapeUtils.escapeHtml4(hotelId);
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
            Hotel hotel = hotelSearcher.find(hotelId);
            dbHandler.addExpediaHistory(hotelId, username);
        }

        VelocityEngine ve = (VelocityEngine) getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        Template template = ve.getTemplate("static/ExpediaHistory.html");
        List<String> history = dbHandler.getExpediaHistory(username);
        List<Hotel> hotels = new ArrayList<>();
        if (history != null && history.size() > 0) {
            for (String id: history) {
                hotels.add(hotelSearcher.find(id));
            }
        }
        context.put("hotels", hotels);
        String message = (String) session.getAttribute("message");
        context.put("message", Objects.requireNonNullElse(message, ""));

        session.setAttribute("message", null);


        template.merge(context, out);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html");
        HttpSession session = request.getSession();

        String username = (String) session.getAttribute("username");

        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        dbHandler.clearHistory(username);

        session.setAttribute("message", "History cleared!");
        response.sendRedirect("/history");
    }
}
