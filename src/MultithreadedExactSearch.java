import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MultithreadedExactSearch {

	Logger logger = LogManager.getLogger(getClass());

	public final Map<String, List<Search>> results;
	private final WorkQueue queue;
	private int pending;

	public MultithreadedExactSearch(Map<String, List<Search>> results, int threads, Set<String> queries) {
		this.results = results;
		this.queue = new WorkQueue(threads);
		this.pending = 0;
		this.search(queries);
		this.finish();
		this.queue.shutdown();
	}

	private void search(Set<String> queries) {
		queue.execute(new LineSearch(queries));
	}

	private void incrementPending() {
		pending++;
	}

	private void decrementPending() {
		pending--;

		if (pending == 0) {
			this.notifyAll();
		}
	}

	private void finish() {
		try {
			while (pending > 0) {
				this.wait();
				logger.debug("woke up with pending at {}", pending);
			}
			logger.debug("Worker done!");
		} catch (InterruptedException e) {
			logger.debug(e.getMessage(), e);
		}
	}

	private class LineSearch implements Runnable {
		ThreadSafeInvertedIndex threadSafeIndex = new ThreadSafeInvertedIndex();
		Set<String> queries;

		public LineSearch(Set<String> queries) {
			this.queries = queries;
			incrementPending();
		}

		@Override
		public void run() {
			threadSafeIndex.exactSearch(results, queries);
			decrementPending();
		}
	}
}
