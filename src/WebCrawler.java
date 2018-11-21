import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

public class WebCrawler {

	private static final Logger logger = LogManager.getLogger();

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
//		Map<String, List<String>> results = new HashMap<>();
//		URLConnection urlConnection = url.openConnection();
//		results.putAll(urlConnection.getHeaderFields());
//
		String html = HTMLFetcher.fetchHTML(url);

		html = HTMLCleaner.stripHTML(html);
		stemHTML(url, html);

//		if (count == 0) {
//			Q.add(url);
//			queue.execute(new Crawler(url, html));
//		}
//
//		if (!LinkParser.listLinks(url, html).isEmpty()) {
//			while (count < total) {
//				url = Q.poll();
//				for (URL newURL : LinkParser.listLinks(url, html)) {
//					count++;
//					if (count < total) {
//						Q.add(newURL);
//						queue.execute(new Crawler(newURL, html));
//					} else { 
//						break;
//					}
//				}
//			}
//		}
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
			
			System.out.print(word + ", ");
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
