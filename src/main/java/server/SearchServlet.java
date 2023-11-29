package server;

import database.DatabaseHandler;
import hotelapp.Hotel;
import hotelapp.ThreadSafeHotelSearcher;
import org.apache.commons.text.StringEscapeUtils;
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
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();

        String username = (String) session.getAttribute("username");
        if (username == null) {
            response.sendRedirect("/portal");
            return;
        }

        String hotelName = request.getParameter("hotelName");
        hotelName = StringEscapeUtils.escapeHtml4(hotelName);

        ThreadSafeHotelSearcher searcher = (ThreadSafeHotelSearcher) getServletContext().getAttribute("hotelSearcher");

        VelocityEngine ve = (VelocityEngine) getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        StringWriter writer = new StringWriter();
        Template template = ve.getTemplate("templates/Searching.html");

        List<Hotel> hotels;
        if (hotelName == null) {
            hotels = (List<Hotel>) searcher.getHotels();
        } else {
            hotels = searcher.search(hotelName);
        }
        context.put("hotels", hotels);

        template.merge(context, writer);
        out.println(writer);

        response.setStatus(HttpServletResponse.SC_OK);

    }
}
