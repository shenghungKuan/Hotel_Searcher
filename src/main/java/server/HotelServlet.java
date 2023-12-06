package server;

import hotelapp.*;
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
import java.util.List;

/**
 * A servlet class used to handle hotelInfo request
 */
@SuppressWarnings("serial")
public class HotelServlet extends HttpServlet{
    /**
     * Called by the server (via the service method) to allow a servlet to handle a hotelInfo GET request.
     * @param request an HttpServletRequest object that contains the request the client has made of the servlet
     * @param response an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if the request for the GET could not be handled
     * @throws IOException if an input or output error is detected when the servlet handles the GET request
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html");
        String hotelId = request.getParameter("hotelId");
        hotelId = StringEscapeUtils.escapeHtml4(hotelId);
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        session.setAttribute("hotelId", hotelId);

        HotelSearcher hotelSearcher = (HotelSearcher) getServletContext().getAttribute("hotelSearcher");
        ReviewSearcher reviewSearcher = (ReviewSearcher) getServletContext().getAttribute("reviewSearcher");


        Hotel hotel = hotelSearcher.find(hotelId);
        if (hotel == null) {
            response.sendRedirect("/search");
            return;
        }
        List<Review> reviews = reviewSearcher.findReview(hotelId);

        int sum = 0;
        if (reviews != null && reviews.size() > 0) {
            for (Review review : reviews) {
                sum += review.getRatingOverall();
            }
            sum /= reviews.size();
        }

        VelocityEngine ve = (VelocityEngine) getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        Template template = ve.getTemplate("templates/HotelInfo.html");
        context.put("id", hotelId);
        context.put("rating", sum);
        context.put("hotel", hotel);
        context.put("link", hotel.getLink());
        context.put("reviews", reviews);


        template.merge(context, out);
    }
}
