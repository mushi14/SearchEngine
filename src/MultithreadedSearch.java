import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * TODO There is similar methods and functionality between the multithreaded and
 * singled threaded version of this class. To make that formal, create an interface
 * with the common methods and the implement that interface in both classes. This
 * will enable some clever upcasting in Driver later too.
 * 
 * This class should not need the queries list...
 * 
 * To multithread this class, everything that used to be inside the while loop for
 * going through the query file should now be in a task. It should almost be exactly
 * the same, except the task needs to synchronize access to results.
 */

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

	private class QueryLineSearch implements Runnable {
		private Set<String> query;
		String line;

		// TODO Only pass in the line and exact. Everything else you can either access directly (like the results) or should be a local variable inside of run().

		public QueryLineSearch(Set<String> query, Map<String, List<Search>> results, String line, boolean exact) {
			this.query = query;
			this.line = line;
		}

		@Override
		public void run() {
			List<Search> temp = new ArrayList<>();

			// TODO Move as much work here as possible, including all the query parsing.

			if (exact) {
				temp = threadSafeIndex.exactSearch(query);
			} else {
				temp = threadSafeIndex.partialSearch(query);
			}

			synchronized (results) {
				results.put(line, temp);
			}
		}
	}
}
