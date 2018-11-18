import java.net.URL;

public class WebCrawler {

	private final ThreadSafeInvertedIndex threadSafeIndex;
	private final WorkQueue queue;
	private int pending;

	public WebCrawler(URL url, int total, int threads, ThreadSafeInvertedIndex threadSafeIndex) {
		this.threadSafeIndex = threadSafeIndex;
		this.queue = new WorkQueue(threads);
		this.pending = 0;
		this.start(url, total);
		this.finish();
		this.queue.shutdown();
	}

	private void start(URL url, int total) {
		
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

		@Override
		public void run() {
			
		}
		
	}
}
