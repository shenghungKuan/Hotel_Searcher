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


/** An example that demonstrates how to process HTML forms with servlets.
 */
@SuppressWarnings("serial")
public class RegistrationServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();

		String username = (String) session.getAttribute("username");
		String message = (String) session.getAttribute("message");
		if (username != null) {
			response.sendRedirect("/search");
		}
		if (message != null) {
			out.println(message);
		}

		VelocityEngine ve = (VelocityEngine) getServletContext().getAttribute("templateEngine");
		VelocityContext context = new VelocityContext();
		Template template = ve.getTemplate("templates/Registration.html");
		context.put("title", "Registration");
		context.put("action", "/register");
		context.put("move", "Sign up");

		template.merge(context, out);

		response.setStatus(HttpServletResponse.SC_OK);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();

		String username = request.getParameter("username");
		username = StringEscapeUtils.escapeHtml4(username);
		String password = request.getParameter("pass");
		password = StringEscapeUtils.escapeHtml4(password);

		DatabaseHandler dbHandler = DatabaseHandler.getInstance();
		if (dbHandler.checkUsername(username)) {
			session.setAttribute("message", username + " is already in use");
			session.setAttribute("username", null);
			response.sendRedirect("/register");
		} else {
			dbHandler.registerUser(username, password);
			session.setAttribute("username", username);
			response.sendRedirect("/search");
		}
	}
}