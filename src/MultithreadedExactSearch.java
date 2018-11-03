import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MultithreadedExactSearch {

	Logger logger = LogManager.getLogger(getClass());

	public final ThreadSafeInvertedIndex threadSafeIndex;
	public final Map<String, List<Search>> results;
	List<Set<String>> queries;
	public final WorkQueue queue;
	private int pending;

	public MultithreadedExactSearch(ThreadSafeInvertedIndex threadSafeIndex, Map<String, List<Search>> results, 
			int threads, List<Set<String>> queries) {
//		logger.debug("CONSTRUCTOR CALLED");
		this.threadSafeIndex = threadSafeIndex;
		this.results = results;
		this.queries = queries;
		this.queue = new WorkQueue(threads);
		this.pending = 0;
		this.search();
	}

	private void search() {
		for (Set<String> query : queries) {
			String queryLine = String.join(" ", query);
			if (!results.containsKey(queryLine)) {
				results.put(queryLine, new ArrayList<>());
				queue.execute(new QueryLineSearch(query, results, queryLine));
				logger.debug("NEW TASK for search on {}", query);
			}
		}
		this.finish();
		this.queue.shutdown();
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
		private Set<String> query;
		String line;

		public QueryLineSearch(Set<String> query, Map<String, List<Search>> results, String line) {
			this.query = query;
			this.line = line;
			incrementPending();
		}

		@Override
		public void run() {
			List<Search> temp = new ArrayList<>();
			logger.debug("PERFORMING search on {}", query);
			temp = threadSafeIndex.exactSearch(query);
			synchronized (results) {
				results.put(line, temp);
			}
			logger.debug("DONE with search on {}", query);
			decrementPending();
		}
	}
}
