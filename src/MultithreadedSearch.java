import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MultithreadedSearch {

	private final ThreadSafeInvertedIndex threadSafeIndex;
	private final Map<String, List<Search>> results;
	List<Set<String>> queries;
	public final WorkQueue queue;
	private int pending;
	private volatile boolean exact;

	public MultithreadedSearch(ThreadSafeInvertedIndex threadSafeIndex, Map<String, List<Search>> results, 
			int threads, List<Set<String>> queries, boolean exact) {
		this.threadSafeIndex = threadSafeIndex;
		this.results = results;
		this.queries = queries;
		this.queue = new WorkQueue(threads);
		this.pending = 0;
		this.exact = exact;
		this.search();
		this.finish();
		this.queue.shutdown();
	}

	private void search() {
		for (Set<String> query : queries) {
			String queryLine = String.join(" ", query);

			if (!results.containsKey(queryLine) && !queryLine.isEmpty()) {
				queue.execute(new QueryLineSearch(query, results, queryLine, exact));
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

	private class QueryLineSearch implements Runnable {
		private Set<String> query;
		String line;

		public QueryLineSearch(Set<String> query, Map<String, List<Search>> results, String line, boolean exact) {
			this.query = query;
			this.line = line;
			incrementPending();
		}

		@Override
		public void run() {
			List<Search> temp = new ArrayList<>();

			if (exact) {
				temp = threadSafeIndex.exactSearch(query);
			} else {
				temp = threadSafeIndex.partialSearch(query);
			}

			synchronized (results) {
				results.put(line, temp);
			}

			decrementPending();
		}
	}
}
