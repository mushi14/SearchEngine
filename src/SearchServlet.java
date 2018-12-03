import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

public class SearchServlet extends HttpServlet {

	private static final String TITLE = "Search";
	private static Logger logger = Log.getRootLogger();
	private ConcurrentLinkedQueue<String> messages;

	public SearchServlet() {
		super();
		messages = new ConcurrentLinkedQueue<>();
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

		for (String word : messages) {
			out.printf("<p>%s</p>%n%n", word);
		}

		printForm(request, response);

		out.printf("<p>This request was handled by thread %s.</p>%n", Thread.currentThread().getName());

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

		String username = request.getParameter("username");
		String message = request.getParameter("message");

		username = username == null ? "anonymous" : username;
		message = message == null ? "" : message;

//		// Avoid XSS attacks using Apache Commons Text
//		// Comment out if you don't have this library installed
//		username = StringEscapeUtils.escapeHtml4(username);
//		message = StringEscapeUtils.escapeHtml4(message);

		String formatted = String.format("%s<br><font size=\"-2\">[ posted by %s at %s ]</font>", message, username,
				getDate());

		// Keep in mind multiple threads may access at once
		messages.add(formatted);

		// Only keep the latest 5 messages
		if (messages.size() > 5) {
			String first = messages.poll();
			logger.info("Removing message: " + first);
		}

		response.setStatus(HttpServletResponse.SC_OK);
		response.sendRedirect(request.getServletPath());
	}

	private static void printForm(HttpServletRequest request, HttpServletResponse response) throws IOException {

		PrintWriter out = response.getWriter();
		out.printf("<form method=\"post\" action=\"%s\">%n", request.getServletPath());
		out.printf("<table cellspacing=\"10\" cellpadding=\"20\"%n");
		out.printf("<tr>%n");
		out.printf("\t<td nowrap></td>%n");
		out.printf("\t<td>%n");
		out.printf("\t\t<input type=\"text\" name=\"username\" maxlength=\"50\" size=\"20\">%n");
		out.printf("\t</td>%n");
		out.printf("</tr>%n");
		out.printf("</table>%n");
		out.printf("<p><input type=\"submit\" value=\"Search\"></p>\n%n");
		out.printf("</form>\n%n");
	}

	private static String getDate() {
		String format = "hh:mm a 'on' EEEE, MMMM dd yyyy";
		DateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(new Date());
	}
}
