import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MultithreadedExactSearch {

	Logger logger = LogManager.getLogger(getClass());

	public final ThreadSafeInvertedIndex threadSafeIndex;
	public final Map<String, List<Search>> results;
	private final WorkQueue queue;
	private int pending;

	public MultithreadedExactSearch(ThreadSafeInvertedIndex threadSafeIndex, Map<String, List<Search>> results, 
			int threads, List<Set<String>> queries) {
		logger.debug("CONSTRUCTOR CALLED");
		this.results = results;
		this.threadSafeIndex = threadSafeIndex;
		this.queue = new WorkQueue(threads);
		this.pending = 0;
		this.search(queries);
		this.finish();
		this.queue.shutdown();
	}

	public void search(List<Set<String>> queries) {
		for (Set<String> set : queries) {
			queue.execute(new QueryLineSearch(set));
			logger.debug("NEW TASK for search on {}", set);
		}
	}

	private void incrementPending() {
		pending++;
		logger.debug("Incrementing pending to {}", pending);
	}

	private void decrementPending() {
		pending--;
		logger.debug("Incrementing pending to {}", pending);

		if (pending == 0) {
			this.notifyAll();
		}
	}

	private synchronized void finish() {
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

	private class QueryLineSearch implements Runnable {
		Set<String> queries;

		public QueryLineSearch(Set<String> queries) {
			this.queries = queries;
			incrementPending();
		}

		@Override
		public void run() {
			logger.debug("PERFORMING search on {}", queries);
			threadSafeIndex.exactSearch(results, queries);
			decrementPending();
			logger.debug("DONE with search on {}", queries);
		}
	}
}
