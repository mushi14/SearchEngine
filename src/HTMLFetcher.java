import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLFetcher {

	/**
	 * Given a map of headers (as returned either by {@link URLConnection#getHeaderFields()}
	 * or by {@link HttpsFetcher#fetchURL(URL)}, determines if the content type of the
	 * response is HTML.
	 *
	 * @param headers map of HTTP headers
	 * @return true if the content type is html
	 *
	 * @see URLConnection#getHeaderFields()
	 * @see HttpsFetcher#fetchURL(URL)
	 */
	public static boolean isHTML(Map<String, List<String>> headers) {
		boolean html = false;
		if (headers.containsKey("Content-Type")) {

			for (String entry : headers.get("Content-Type")) {

				if (entry.contains("text/html")) {
					html =  true;
				} else {
					html = false;
				}
			}
		}

		return html;
	}

	/**
	 * Given a map of headers (as returned either by {@link URLConnection#getHeaderFields()}
	 * or by {@link HttpsFetcher#fetchURL(URL)}, returns the status code as an int value.
	 * Returns -1 if any issues encountered.
	 *
	 * @param headers map of HTTP headers
	 * @return status code or -1 if unable to determine
	 *
	 * @see URLConnection#getHeaderFields()
	 * @see HttpsFetcher#fetchURL(URL)
	 */
	public static int getStatusCode(Map<String, List<String>> headers) {
		int statusCode = -1;
		if (headers.containsKey(null)) {

			for (var entry : headers.get(null)) {
				String regex = "\\d{3}";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(entry);

				if (matcher.find()) {
					for (int i = 0; i <= matcher.groupCount(); i++) {
						statusCode = Integer.valueOf(matcher.group(i));
					}
				}
			}
		}

		return statusCode;
	}

	/**
	 * Given a map of headers (as returned either by {@link URLConnection#getHeaderFields()}
	 * or by {@link HttpsFetcher#fetchURL(URL)}, returns whether the status code
	 * represents a redirect response *and* the location header is properly included.
	 *
	 * @param headers map of HTTP headers
	 * @return true if the HTTP status code is a redirect and the location header is non-empty
	 * @throws IOException 
	 *
	 * @see URLConnection#getHeaderFields()
	 * @see HttpsFetcher#fetchURL(URL)
	 */
	public static boolean isRedirect(Map<String, List<String>> headers) {
		int statusCode = HTMLFetcher.getStatusCode(headers);
		if (statusCode >= 300 && statusCode < 400) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Uses {@link HttpsFetcher#fetchURL(URL)} to fetch the headers and content of the
	 * specified url. If the response was HTML, returns the HTML as a single {@link String}.
	 * If the response was a redirect and the value of redirects is greater than 0, will
	 * return the result of the redirect (decrementing the number of allowed redirects).
	 * Otherwise, will return {@code null}.
	 *
	 * @param url the url to fetch and return as html
	 * @param redirects the number of times to follow a redirect response
	 * @return the html as a single String if the response code was ok, otherwise null
	 * @throws IOException
	 *
	 * @see #isHTML(Map)
	 * @see #getStatusCode(Map)
	 * @see #isRedirect(Map)
	 */
	public static String fetchHTML(URL url, int redirects) throws IOException {
		Map<String, List<String>> headers = HttpsFetcher.fetchURL(url);
		StringBuilder html = new StringBuilder();
		int statusCode = HTMLFetcher.getStatusCode(headers);

		if (HTMLFetcher.isHTML(headers) && statusCode == 200) {
			int temp = 0;
			for (var entry : headers.get("Content")) {
				temp++;
				if (temp < headers.get("Content").size()) {
					html.append(entry + "\n");
				} else {
					html.append(entry);
				}
			}

			return html.toString();
		} else if (HTMLFetcher.isRedirect(headers) && redirects > 0) {
			String newURL = "";
			for (var entry : headers.get("Location")) {
				newURL = String.join(" ", entry);
			}
			url = new URL(newURL);

			return fetchHTML(url, redirects - 1);
		} else {
			return null;
		}
	}

	/**
	 * @see #fetchHTML(URL, int)
	 */
	public static String fetchHTML(String url) throws IOException {
		return fetchHTML(new URL(url), 0);
	}

	/**
	 * @see #fetchHTML(URL, int)
	 */
	public static String fetchHTML(String url, int redirects) throws IOException {
		return fetchHTML(new URL(url), redirects);
	}

	/**
	 * @see #fetchHTML(URL, int)
	 */
	public static String fetchHTML(URL url) throws IOException {
		return fetchHTML(url, 0);
	}

}