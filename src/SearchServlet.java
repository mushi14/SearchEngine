import java.io.IOException;
import java.io.PrintWriter;
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
	private int threads;

	public SearchServlet(ThreadSafeInvertedIndex index, int threads) {
		super();
		this.message = "";
		this.index = index;
		this.search = new MultithreadedSearch(this.index, threads);
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

		if (!search.results.isEmpty()) {
			List<String> locations = new ArrayList<>();
			for (String word : search.results.keySet()) {
				for (Search s : search.results.get(word)) {
					locations.add(s.getLocation());
				}
			}

			if (locations.isEmpty()) {
				message = "No results found.";
			}
			out.printf("<p>%s</p>%n%n<br />", message);

			for (String loc : locations) {
				out.printf("<a href='%s' >%s</a><br />", loc, loc);
			}
		}

		search.results.clear();

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


		String queries = request.getParameter("query");
		queries = queries == null ? "" : queries;

		String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

//		// Avoid XSS attacks using Apache Commons Text
//		// Comment out if you don't have this library installed
//		username = StringEscapeUtils.escapeHtml4(username);
//		message = StringEscapeUtils.escapeHtml4(message);

		if (!queries.isEmpty()) {
			search.searchLine(queries, false);
			synchronized (message) {
				message = String.format("<br>Displaying results for '%s' at %s</font>",
						queries, timeStamp.toString());	
			}
		}

		response.setStatus(HttpServletResponse.SC_OK);
		response.sendRedirect(request.getServletPath());
	}

	private static void printForm(HttpServletRequest request, HttpServletResponse response) throws IOException {

		PrintWriter out = response.getWriter();
		out.printf("<form method=\"post\" action=\"%s\">%n", request.getServletPath());
		out.printf("<div class='topnav' id='header'>");
		out.printf("<center><input <font size='60' style='font-family:verdana;' type='text' "
				+ "name='query' placeholder='Search..'></font></center>");
		out.printf("</div>");
		out.printf("<form action='action_page.php'>");
		out.printf("<center><p><input type='submit' value='Search'></p></center>");
		out.printf("</form>");
		out.printf("</form>\n%n");

//		out.printf("<center>");
//		out.printf("<table cellspacing=\"-2\" cellpadding=\"2\"%n");
//		out.printf("<tr>%n");
//		out.printf("\t<td nowrap></td>%n");
//		out.printf("\t<td>%n");
//		out.printf("\t\t<input type=\"text\" name=\"query\" maxlength=\"300\" size=\"80\">%n");
//		out.printf("\t</td>%n");
//		out.printf("</tr>%n");
//		out.printf("</table>%n");
//		out.printf("<p><center><input type=\"submit\" value=\"Search\"></center></p>\n%n");
//		out.printf("</center></form>\n%n");
	}
}
