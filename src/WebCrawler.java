import java.nio.file.Path;

public class WebCrawler {

	public final ThreadSafeInvertedIndex threadSafeIndex;
	private final WorkQueue queue;
	private int pending;

	public WebCrawler(Path path, int threads, ThreadSafeInvertedIndex threadSafeIndex) {
		this.threadSafeIndex = threadSafeIndex;
		this.queue = new WorkQueue(threads);
		this.pending = 0;
	}

	private void parse() {
		
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
