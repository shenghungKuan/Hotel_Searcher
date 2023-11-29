package server;

import database.DatabaseHandler;
import hotelapp.Hotel;
import hotelapp.Review;
import hotelapp.ThreadSafeHotelSearcher;
import hotelapp.ThreadSafeReviewSearcher;
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
import java.util.Set;

public class ReviewServlet extends HttpServlet {
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
        HttpSession session = request.getSession();
        String hotelId = (String) session.getAttribute("hotelId");
        hotelId = StringEscapeUtils.escapeHtml4(hotelId);
        session.setAttribute("hotelId", hotelId);
        PrintWriter out = response.getWriter();

        String message = (String) session.getAttribute("message");
        if (message != null) {
            out.println(message);
        }

        VelocityEngine ve = (VelocityEngine) getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        Template template = ve.getTemplate("templates/ReviewHandler.html");
        context.put("hotelId", hotelId);

        template.merge(context, out);

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        HttpSession session = request.getSession();

        String hotelId = (String) session.getAttribute("hotelId");
        String username = (String) session.getAttribute("username");
        String submitValue = request.getParameter("choice");
        String title = request.getParameter("title");
        String text = request.getParameter("text");
        if (submitValue == null) {
            response.sendRedirect("/review?hotelId=" + hotelId);
            return;
        }

        ThreadSafeReviewSearcher reviewSearcher = (ThreadSafeReviewSearcher) getServletContext().getAttribute("reviewSearcher");
        Review review = reviewSearcher.findSpecificReview(hotelId, username);

        switch (submitValue) {
            case "add" -> {
                if (review == null) {
                    reviewSearcher.addReview(username, hotelId, title, text);
                    response.sendRedirect("/hotel?hotelId=" + hotelId);
                } else {
                    session.setAttribute("message", "You already left a review before");
                    response.sendRedirect("/review?hotelId=" + hotelId);
                }
            }
            case "modify" -> {
                if (review == null) {
                    session.setAttribute("message", "You don't have any reviews");
                    response.sendRedirect("/review?hotelId=" + hotelId);
                } else {
                    reviewSearcher.deleteReview(username, hotelId);
                    reviewSearcher.addReview(username, hotelId, title, text);
                    response.sendRedirect("/hotel?hotelId=" + hotelId);
                }
            }
            case "delete" -> {
                if (review == null) {
                    session.setAttribute("message", "You don't have any reviews");
                    response.sendRedirect("/review?hotelId=" + hotelId);
                } else {
                    reviewSearcher.deleteReview(username, hotelId);
                    response.sendRedirect("/hotel?hotelId=" + hotelId);
                }
            }
        }
    }
}
