import java.io.IOException;
import java.net.URL;
import java.text.Normalizer;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

public class WebCrawler {

	private final Logger logger = LogManager.getLogger();
	public static final Pattern SPLIT_REGEX = Pattern.compile("(?U)\\p{Space}+");
	public static final Pattern CLEAN_REGEX = Pattern.compile("(?U)[^\\p{Alpha}\\p{Space}]+");

	public final ThreadSafeInvertedIndex threadSafeIndex;
	private final WorkQueue queue;
	private Queue<URL> Q;
	private int pending;
	private int count;

	public WebCrawler(URL url, int total, int threads, ThreadSafeInvertedIndex threadSafeIndex) throws IOException {
		this.threadSafeIndex = threadSafeIndex;
		this.queue = new WorkQueue(threads);
		this.Q = new LinkedList<>();
		this.pending = 0;
		this.count = 0;
		this.start(url, total);
		this.finish();
		this.queue.shutdown();
	}

	private void start(URL url, int total) throws IOException {
		count++;

		while (count <= total) {

			if (count == 1) {
				Q.add(url);
				String html = HTMLFetcher.fetchHTML(url);
				queue.execute(new Crawler(url, html));
			}

			url = Q.poll();
			String html = HTMLFetcher.fetchHTML(url);
			for (URL newURL : LinkParser.listLinks(url, html)) {
				String newHTML = HTMLFetcher.fetchHTML(newURL);
				if (newHTML != null) {
					count++;
					if (count <= total) {
						Q.add(newURL);
						
					} else {
						break;
					}
					queue.execute(new Crawler(newURL, newHTML));
				}
			}
		}
	}

	private synchronized void incrementPending() {
		pending++;
	}

	private synchronized void decrementPending() {
		pending--;

		if (pending == 0) {
			this.notifyAll();
		}
	}

	private synchronized void finish() {
		try {
			while (pending > 0) {
				this.wait();
			}
		} catch (InterruptedException e) {
			System.out.println("Thread interrupted.");
		}
	}

	public static String clean(CharSequence text) {
		String cleaned = Normalizer.normalize(text, Normalizer.Form.NFD);
		cleaned = CLEAN_REGEX.matcher(cleaned).replaceAll("");
		return cleaned.toLowerCase();
	}

	public static String[] split(String text) {
		text = text.trim();
		return text.isEmpty() ? new String[0] : SPLIT_REGEX.split(text);
	}

	public static String[] parse(String text) {
		return split(clean(text));
	}

	private synchronized void stemHTML(URL url, String html) {
		int position = 1;
		Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
		String[] words = html.split(" ");

		for (String word : words) {
			String[] wordArr = parse(word);
			if (wordArr.length != 0) {
				for (String parsedW : wordArr) {
					parsedW = parsedW.toLowerCase();
					parsedW = stemmer.stem(parsedW).toString();
					synchronized (threadSafeIndex) {
						threadSafeIndex.add(parsedW, url.toString(), position);
						position++;
					}
				}
			}
		}
	}

	private class Crawler implements Runnable {
		private URL url;
		private String html;

		private Crawler(URL url, String html) {
			this.url = url;
			this.html = html;
			incrementPending();
		}

		@Override
		public void run() {
			html = HTMLCleaner.stripHTML(html);
//			logger.debug("this is cleaned html {}", html);
			stemHTML(url, html);
			decrementPending();
		}
	}
}
