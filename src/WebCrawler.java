import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

public class WebCrawler {

	private static final Logger logger = LogManager.getLogger();

	private final ThreadSafeInvertedIndex threadSafeIndex;
	private final WorkQueue queue;
	private List<Boolean> seen;
	private int pending;
	private int count;

	public WebCrawler(URL url, int total, int threads, ThreadSafeInvertedIndex threadSafeIndex) {
		this.threadSafeIndex = threadSafeIndex;
		this.queue = new WorkQueue(threads);
		seen = new ArrayList<>();
		this.pending = 0;
		this.count = 0;
		this.start(url, total);
		this.finish();
		this.queue.shutdown();
	}

	private void start(URL url, int total) {
		count++;
		if (count <= total) {
			try {
				if (!seen.contains(url)) {
					String html = HTMLFetcher.fetchHTML(url);
					queue.execute(new Crawler(url, html));
				} else {
					
				}

				List<URL> href = new ArrayList<>();
				href = LinkParser.listLinks(url, html);
				for (URL newURL : href) {
					String newHTML = HTMLFetcher.fetchHTML(newURL);
					queue.execute(new Crawler(newURL, newHTML));
				}
			} catch (IOException e) {
				logger.debug("Exception with fetch html");
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

	private synchronized void stemHTML(URL url, String html) {
		int position = 1;
		Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
		String[] words = html.split(" ");

		for (String word : words) {
			word = stemmer.stem(word).toString();
			synchronized (threadSafeIndex) {
				threadSafeIndex.add(word, url.toString(), position);
				position++;
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
			/* Parse the doctype html and store each word in the thread safe inverted index*/
			html = HTMLCleaner.stripHTML(html);
			stemHTML(url, html);
			decrementPending();
		}
	}
}
