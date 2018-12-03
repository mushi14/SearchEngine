import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class SearchServer {

	private int PORT = 8080;

	public SearchServer(int port) {
		this.PORT = port;
	}

	public void newServ() {
		Server server = new Server(PORT);

		ServletHandler handler = new ServletHandler();
		handler.addServletMapping(new ServletHolder(new SearchServlet()), "/test");

		server.setHandler(handler);
		server.start();
		server.join();
	}
}
