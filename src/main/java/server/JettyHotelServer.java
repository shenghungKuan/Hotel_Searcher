package server;

import hotelapp.*;
import org.apache.velocity.app.VelocityEngine;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class JettyHotelServer {
	public static final int PORT = 8080;

	public static void main(String[] args)  {
		Server server = new Server(PORT);
		ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);

		handler.addServlet(PortalServlet.class, "/portal");
		handler.addServlet(SearchServlet.class, "/search");
		handler.addServlet(HotelServlet.class, "/hotel");
		handler.addServlet(LogoutServlet.class, "/logout");
		handler.addServlet(ReviewServlet.class, "/review");
		handler.addServlet(ShowReviewServlet.class, "/showreview");
		handler.addServlet(ExpediaHistoryServlet.class, "/history");
		handler.addServlet(FavoriteHotelServlet.class, "/favorite");
		VelocityEngine velocity = new VelocityEngine();
		velocity.init();

		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setResourceBase("static");

		HandlerList handlers = new HandlerList();
		handlers.addHandler(resourceHandler);

		HotelSearcher hotelSearcher = new HotelSearcher();
		ReviewSearcher reviewSearcher = new ReviewSearcher();
		handler.setAttribute("hotelSearcher", hotelSearcher);
		handler.setAttribute("reviewSearcher", reviewSearcher);
		handler.setAttribute("templateEngine", velocity);
		handlers.addHandler(handler);
		server.setHandler(handlers);

		try {
			server.start();
			server.join();
		}
		catch (Exception e) {
			System.out.println(e);
		}

	}
}