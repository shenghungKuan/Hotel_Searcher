package server;

import hotelapp.ThreadSafeHotelSearcher;
import hotelapp.ThreadSafeReviewSearcher;
import org.apache.velocity.app.VelocityEngine;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class JettyHotelServer {
	public static final int PORT = 8080;

	public static void main(String[] args)  {
		// FILL IN CODE, and add more classes as needed
		Server server = new Server(PORT);
		ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);

		handler.addServlet(PortalServlet.class, "/portal");
		handler.addServlet(SearchServlet.class, "/search");
		handler.addServlet(HotelServlet.class, "/hotel");
		handler.addServlet(LogoutServlet.class, "/logout");
		handler.addServlet(ReviewServlet.class, "/review");
		VelocityEngine velocity = new VelocityEngine();
		velocity.init();

		ThreadSafeHotelSearcher hotelSearcher = new ThreadSafeHotelSearcher("input/hotels/hotels.json");
		ThreadSafeReviewSearcher reviewSearcher = new ThreadSafeReviewSearcher("3", "input/reviews");
		handler.setAttribute("hotelSearcher", hotelSearcher);
		handler.setAttribute("reviewSearcher", reviewSearcher);
		handler.setAttribute("templateEngine", velocity);
		server.setHandler(handler);

		try {
			server.start();
			server.join();
		}
		catch (Exception e) {
			System.out.println(e);
		}

	}
}