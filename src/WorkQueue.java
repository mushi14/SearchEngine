 import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorkQueue {

	final static Logger logger = LogManager.getLogger();

	private final ThreadPool[] workers;
	private final LinkedList<Runnable> queue;
	private int pending;
	private volatile boolean shutdown;

	/**
	 * Constructor. Initializes the work queue and starts the worker threads
	 * @param threads how many threads to run on
	 */
	public WorkQueue(int threads) {
		workers = new ThreadPool[threads];
		queue = new LinkedList<>();
		pending = 0;

		for (int i = 0; i < threads; i++) {
			workers[i] = new ThreadPool();
			workers[i].start();
		}
		
		// TODO log.info the number of threads used by your work queue
	}

	/**
	 * Executes the given task
	 * @param r task assigned to the work queue
	 */
	public void execute(Runnable r) {
		incrementPending();
		synchronized (queue) {
			queue.addLast(r);
			queue.notifyAll();
		}
	}

	/**
	 * Increase the pending variable
	 */
	private synchronized void incrementPending() {
		pending++;
	}

	/**
	 * Decreases the pending variable
	 */
	private synchronized void decrementPending() {
		pending--;

		if (pending == 0) {
			this.notifyAll();
		}
	}

	/**
	 * Finishes the task
	 */
	public synchronized void finish() {
		try {
			while (pending > 0) {
				this.wait();
			}
		} catch (InterruptedException e) {
			System.out.println("Thread interrupted.");
		}
	}

	/**
	 * Tells the threads to shutdown when no more work is left to do
	 */
	public void shutdown() {
		shutdown = true;
		synchronized (queue) {
			queue.notifyAll();
		}
	}

	/**
	 * Nested class used to run the tasks
	 * @author mushahidhassan
	 *
	 */
	private class ThreadPool extends Thread {
		Runnable r;

		/**
		 * Runs the given task
		 */
		@Override
		public void run() {
			while (true) {
				synchronized (queue) {
					while (queue.isEmpty() && !shutdown) {
						try {
							queue.wait();
						} catch (InterruptedException e) {
							System.out.println("Thread Interrupted");
						}
					}

					if (shutdown) {
						break;
					}

					r = queue.removeFirst();
				}

				try {
					r.run();
				} catch (RuntimeException e) {
					System.out.println("Runtime exception");
				} finally {
					decrementPending();
				}
			}
			
		}
	}
}
