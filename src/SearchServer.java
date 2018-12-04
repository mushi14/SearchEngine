import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class SearchServer {

	private static final int PORT = 8080;
	private ThreadSafeInvertedIndex index;

	public SearchServer(ThreadSafeInvertedIndex index) throws Exception {
		this.index = index;
		this.newServ();
	}

	public void newServ() throws Exception {
		Server server = new Server(PORT);

		ServerConnector connector = new ServerConnector(server);
		connector.setHost("localhost");
		connector.setPort(PORT);

		ServletHandler handler = new ServletHandler();
		handler.addServletWithMapping(new ServletHolder(new SearchServlet(index)), "/");

		server.addConnector(connector);
		server.setHandler(handler);
		server.start();
		server.join();
	}
}
