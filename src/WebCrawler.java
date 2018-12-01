import java.io.IOException;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

public class WebCrawler {

	private final Logger logger = LogManager.getLogger();
	private final Pattern SPLIT_REGEX = Pattern.compile("(?U)\\p{Space}+");
	private  final Pattern CLEAN_REGEX = Pattern.compile("(?U)[^\\p{Alpha}\\p{Space}]+");

	private final ThreadSafeInvertedIndex index;
	private int threads;
	private Queue<URL> Q;
	private List<URL> seen;

	/**
	 * Constructor, initializes the index and threads
	 * @param index thread safe index to store url contents in
	 * @param threads how many threads to run on
	 */
	public WebCrawler(ThreadSafeInvertedIndex index, int threads) {
		this.index = index;
		this.threads = threads;
		this.Q = new LinkedList<>();
		this.seen = new ArrayList<>();
	}

	/**
	 * Starts the process of web crawling using breadth first approach
	 * @param url first url process
	 * @param html first url's html content
	 * @param total the limit of the crawls
	 * @param redirects how many redirects a url can have
	 * @throws IOException if the url is invalid
	 */
	public void start(URL url, String html, int total, int redirects) throws IOException {
		WorkQueue queue = new WorkQueue(threads);
		int count = 0;

		if (html != null) {
			count++;
			Q.add(url);
			seen.add(url);
			queue.execute(new Crawler(url, html));
		}

		while (count < total) {
			if (!Q.isEmpty()) {
				url = Q.poll();
				html = HTMLFetcher.fetchHTML(url, redirects);

				if (!LinkParser.listLinks(url, html).isEmpty()) {
					for (URL ref : LinkParser.listLinks(url, html)) {
	
						if (count < total) {
							if (!seen.contains(ref)) {
								String newHTML = HTMLFetcher.fetchHTML(ref, redirects);
								count++;
	
								if (newHTML != null) {
									Q.add(ref);
									seen.add(ref);
									queue.execute(new Crawler(ref, newHTML));
								}
							}
						} else {
							break;
						}
					}
				} else {
					break;
				}
			} else {
				break;
			}
		}

		queue.finish();
		queue.shutdown();
	}

	/**
	 * Cleans the text by removing any non-alphabetic characters (e.g. non-letters
	 * like digits, punctuation, symbols, and diacritical marks like the umlaut)
	 * and converting the remaining characters to lowercase.
	 *
	 * @param text the text to clean
	 * @return cleaned text
	 */
	public String clean(CharSequence text) {
		String cleaned = Normalizer.normalize(text, Normalizer.Form.NFD);
		cleaned = CLEAN_REGEX.matcher(cleaned).replaceAll("");
		return cleaned.toLowerCase();
	}

	/**
	 * Splits the supplied text by whitespace. Does not perform any cleaning.
	 *
	 * @param text the text to split
	 * @return an array of {@link String} objects
	 *
	 * @see #clean(CharSequence)
	 * @see #parse(String)
	 */
	public String[] split(String text) {
		text = text.trim();
		return text.isEmpty() ? new String[0] : SPLIT_REGEX.split(text);
	}

	/**
	 * Cleans the text and then splits it by whitespace.
	 *
	 * @param text the text to clean and split
	 * @return an array of {@link String} objects
	 *
	 * @see #clean(CharSequence)
	 * @see #parse(String)
	 */
	public String[] parse(String text) {
		return split(clean(text));
	}

	/**
	 * Stems each word from the html content of the URL and stores it into the index
	 * @param url url to process
	 * @param html html content of the url
	 */
	private synchronized void stemHTML(URL url, String html) {
		int position = 1;
		Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
		String[] words = html.split(" ");
		ThreadSafeInvertedIndex local = new ThreadSafeInvertedIndex();

		for (String word : words) {
			String[] wordArr = parse(word);
			if (wordArr.length != 0) {
				for (String parsedW : wordArr) {
					parsedW = parsedW.toLowerCase();
					parsedW = stemmer.stem(parsedW).toString();
					if (!parsedW.isEmpty()) {
						local.add(parsedW, url.toString(), position);
						position++;
					}
				}
			}
		}

		synchronized (index) {
			index.addAll(local);
		}
	}

	/**
	 * Crawls each URL separately cleaning, stemming the html content and storing the parsed content
	 * it in index
	 * @author mushahidhassan
	 *
	 */
	private class Crawler implements Runnable {
		private URL url;
		private String html;

		/**
		 * Constructor for inner class
		 * @param url url to process
		 * @param html html content of the url
		 */
		private Crawler(URL url, String html) {
			this.url = url;
			this.html = html;
		}

		/**
		 * Cleans and stems the html and then stores it into the index
		 */
		@Override
		public void run() {
			html = HTMLCleaner.stripHTML(html);
			stemHTML(url, html);
		}
	}
}