import java.io.IOException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WebCrawler {

	private static final Logger logger = LogManager.getLogger();

	private final ThreadSafeInvertedIndex threadSafeIndex;
	private final WorkQueue queue;
	private int pending;
	private int count;

	public WebCrawler(URL url, int total, int threads, ThreadSafeInvertedIndex threadSafeIndex) {
		this.threadSafeIndex = threadSafeIndex;
		this.queue = new WorkQueue(threads);
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
				String html = HTMLFetcher.fetchHTML(url);
				LinkParser.listLinks(url, html);
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

	private class Crawler implements Runnable {
		private String html;

		private Crawler(String html) {
			this.html = html;
		}

		@Override
		public void run() {
			/* Parse the doctype html and store each word in the thread safe inverted index*/
		}
		
	}
}
