package server;

import database.DatabaseHandler;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /*response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();

        String username = (String) session.getAttribute("username");
        if (username != null) {
            out.println(username);
        }

        VelocityEngine ve = (VelocityEngine) getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        //Template template = ve.getTemplate("templates/hotelInfo.html");
        Template template = ve.getTemplate("templates/CATravel.html");
//		context.put("name", name);

        String title = "Registration Servlet";
        String header = "<!DOCTYPE html\n>" + "	<head>\n" + "<title>" + title + "</title>\n" + "</head>\n";

        String body = "	<body>\n" + "<p>Hello! You have visited " + " time(s).</p>\n";

        body = body + "	</body>\n";
        String footer = "</html>";

        String page = header + body + footer;
        out.println(page);

        String format = "yyyy-MM-dd hh:mm a";
        DateFormat formatter = new SimpleDateFormat(format);
        username = formatter.format(Calendar.getInstance().getTime());
        session.setAttribute("date", username);


        response.setStatus(HttpServletResponse.SC_OK);*/

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        HttpSession session = request.getSession();

        String username = request.getParameter("username");
        username = StringEscapeUtils.escapeHtml4(username);
        String password = request.getParameter("pass");
        password = StringEscapeUtils.escapeHtml4(password);

        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        dbHandler.registerUser(username, password);

        response.getWriter().println("Successfully registered the user " + username);
        session.setAttribute("username", username);
        response.sendRedirect("/search");
        // Can redirect to another page here.
    }
}
