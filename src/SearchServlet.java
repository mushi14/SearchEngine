import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

public class SearchServlet extends HttpServlet {

	private static final String TITLE = "Search";
	private static Logger logger = Log.getRootLogger();
	private String message;
	private ThreadSafeInvertedIndex index;
	private MultithreadedSearch search;

	public SearchServlet(ThreadSafeInvertedIndex index) {
		super();
		message = "";
		this.index = index;
		this.search = new MultithreadedSearch(this.index);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		logger.info("Message servlet ID " + this.hashCode() + " handling get status.");

		PrintWriter out = response.getWriter();
		out.printf("<html>%n%n");
		out.printf("<head><title>%s</title></head>%n", TITLE);
		out.printf("<body>%n");

		out.printf("<h1>Welcome to my Search Engine. You type, we search!</h1>%n%n");

		if (!message.isEmpty()) {
			out.printf("<p>%s</p>%n%n", message);
			out.printf("Results: \n");
			List<URL> locations = new ArrayList<>();
			for (String word : search.results.keySet()) {
				for (Search s : search.results.get(word)) {
					locations.add(new URL (s.getLocation()));
				}
			}

			for (URL loc : locations) {
				out.printf("%s\n", loc);
			}
		}

		printForm(request, response);

		String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
		out.printf("<p><center>The time is: %s.</center></p>%n", timeStamp);
		out.printf("%n</body>%n");
		out.printf("</html>%n");

		response.setStatus(HttpServletResponse.SC_OK);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		logger.info("MessageServlet ID " + this.hashCode() + " handling POST request.");

		String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

		String queries = request.getParameter("query");
		queries = queries == null ? "" : queries;

//		// Avoid XSS attacks using Apache Commons Text
//		// Comment out if you don't have this library installed
//		username = StringEscapeUtils.escapeHtml4(username);
//		message = StringEscapeUtils.escapeHtml4(message);

		String formatted = String.format("<br><center><font size=\"1\">Displaying results for '%s' at %s</font></center>.",
				queries, timeStamp.toString());

		synchronized (message) {
			message = formatted;
		}

		if (!queries.isEmpty()) {
			search.searchLine(queries, false);
		}

		response.setStatus(HttpServletResponse.SC_OK);
		response.sendRedirect(request.getServletPath());
	}

	private static void printForm(HttpServletRequest request, HttpServletResponse response) throws IOException {

		PrintWriter out = response.getWriter();
		out.printf("<form method=\"post\" action=\"%s\">%n", request.getServletPath());
		out.printf("<center>");
		out.printf("<table cellspacing=\"-2\" cellpadding=\"2\"%n");
		out.printf("<tr>%n");
		out.printf("\t<td nowrap></td>%n");
		out.printf("\t<td>%n");
		out.printf("\t\t<input type=\"text\" name=\"query\" maxlength=\"300\" size=\"80\">%n");
		out.printf("\t</td>%n");
		out.printf("</tr>%n");
		out.printf("</table>%n");
		out.printf("<p><center><input type=\"submit\" value=\"Search\"></center></p>\n%n");
		out.printf("</center></form>\n%n");
	}
}
