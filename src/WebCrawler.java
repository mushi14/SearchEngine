import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

public class WebCrawler {

	private final Logger logger = LogManager.getLogger();

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

		if (count == 1) {
			Q.add(url);
			String html = HTMLFetcher.fetchHTML(url);
//			logger.debug("first task created on url {}", url);
			queue.execute(new Crawler(url, html));
		}

		boolean done = false;
		while (done == false) {
			url = Q.poll();
			String html = HTMLFetcher.fetchHTML(url);

			for (URL newURL : LinkParser.listLinks(url, html)) {
				count++;

				if (count <= total) {
					Q.add(newURL);
					String newHTML = HTMLFetcher.fetchHTML(newURL);
					if (newHTML != null) {
//						logger.debug("creating task for url {}", newURL);
						queue.execute(new Crawler(newURL, newHTML));
					}
				} else {
					break;
				}
			}

			if (count == total) {
				done = true;
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
			if (!word.isEmpty()) {
				String regex = ".*\\w";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(word);

				if (matcher.find()) {
					for (int i = 0; i <= matcher.groupCount(); i++) {
						word = matcher.group(i);
					}

					synchronized (threadSafeIndex) {
						word = stemmer.stem(word).toString();
						threadSafeIndex.add(word, url.toString(), position);
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
