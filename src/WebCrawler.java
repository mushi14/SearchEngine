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
	public static final Pattern SPLIT_REGEX = Pattern.compile("(?U)\\p{Space}+");
	public static final Pattern CLEAN_REGEX = Pattern.compile("(?U)[^\\p{Alpha}\\p{Space}]+");

	public final ThreadSafeInvertedIndex threadSafeIndex;
	private final WorkQueue queue;
	private Queue<URL> Q;
	private List<URL> seen;
	private int pending;
	private int count;

	public WebCrawler(URL url, int total, int threads, ThreadSafeInvertedIndex threadSafeIndex) throws IOException {
		this.threadSafeIndex = threadSafeIndex;
		this.queue = new WorkQueue(threads);
		this.Q = new LinkedList<>();
		this.seen = new ArrayList<>();
		this.pending = 0;
		this.count = 0;
		this.start(url, total);
		this.finish();
		this.queue.shutdown();
	}

	private void start(URL url, int total) throws IOException {
		while (count < total) {
			if (count == 0) {
				String html = HTMLFetcher.fetchHTML(url);
				if (html != null) {
					count++;
					Q.add(url);
					seen.add(url);
					queue.execute(new Crawler(url, html));
				}
			} else {
				url = Q.poll();
				String html = HTMLFetcher.fetchHTML(url);

				for (URL ref : LinkParser.listLinks(url, html)) {
					String newHTML = HTMLFetcher.fetchHTML(ref);
					if (count < total) {
						if (!seen.contains(ref)) {
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
			}
		}
	}


//		while (count <= total) {
//
//			count++;
//			if (count == 1) {
//				Q.add(url);
//				String html = HTMLFetcher.fetchHTML(url);
//				queue.execute(new Crawler(url, html));
//			}
//
//			url = Q.poll();
//			String html = HTMLFetcher.fetchHTML(url);
//			for (URL newURL : LinkParser.listLinks(url, html)) {
//				String newHTML = HTMLFetcher.fetchHTML(newURL);
//				if (newHTML != null) {
//					count++;
//					if (count <= total) {
//						Q.add(newURL);
//						queue.execute(new Crawler(newURL, newHTML));
//					} else {
//						break;
//					}
//				}
//			}
//		}
//	}

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
			stemHTML(url, html);
			decrementPending();
		}
	}
}



//Running: out/index-url-numpy.json...
//Seed URL: https://www.cs.usfca.edu/~cs212/numpy/user/index.html the count is: 0
//NEW URL FROM QUEUE: https://www.cs.usfca.edu/~cs212/numpy/user/index.html the count is: 1
//HREFs URL: https://www.cs.usfca.edu/~cs212/numpy/index.html the count is: 1
//HREFs URL: https://www.cs.usfca.edu/~cs212/numpy/genindex.html the count is: 2
//HREFs URL: https://www.cs.usfca.edu/~cs212/numpy/user/setting-up.html the count is: 3
//HREFs URL: https://www.cs.usfca.edu/~cs212/numpy/contents.html the count is: 4
//NEW URL FROM QUEUE: https://www.cs.usfca.edu/~cs212/numpy/index.html the count is: 5
//NEW URL FROM QUEUE: https://www.cs.usfca.edu/~cs212/numpy/genindex.html the count is: 5
//NEW URL FROM QUEUE: https://www.cs.usfca.edu/~cs212/numpy/user/setting-up.html the count is: 5
//NEW URL FROM QUEUE: https://www.cs.usfca.edu/~cs212/numpy/contents.html the count is: 5
