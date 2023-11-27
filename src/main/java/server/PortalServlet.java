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


/** An example that demonstrates how to process HTML forms with servlets.
 */
@SuppressWarnings("serial")
public class PortalServlet extends HttpServlet {

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
		Template template = ve.getTemplate("templates/Portal.html");
		context.put("action", "/portal");

		template.merge(context, out);
		session.setAttribute("message", null);

		response.setStatus(HttpServletResponse.SC_OK);

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
		String submitValue = request.getParameter("register");
		if (submitValue == null) {
			response.sendRedirect("/portal");
			return;
		}

		DatabaseHandler dbHandler = DatabaseHandler.getInstance();
		switch (submitValue) {
			case "Sign up" -> {
				if (dbHandler.checkUsername(username)) {
					session.setAttribute("message", username + " is already in use");
					session.setAttribute("username", null);
					response.sendRedirect("/portal");
				} else {
					if (!password.matches(".{8,}")) {
						session.setAttribute("message", "Password is too short (at least 8 characters)");
						session.setAttribute("username", null);
						response.sendRedirect("/portal");
					} else {
						dbHandler.registerUser(username, password);
						session.setAttribute("username", username);
						response.sendRedirect("/search");
					}
				}
			}
			case "Sign in" -> {
				if (!dbHandler.authenticateUser(username, password)) {
					session.setAttribute("message", "Invalid username or password");
					session.setAttribute("username", null);
					response.sendRedirect("/portal");
				} else {
					session.setAttribute("username", username);
					session.setAttribute("message", null);
					response.sendRedirect("/search");
				}
			}
		}
	}
}