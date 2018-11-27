 import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorkQueue {

	// TODO Make all loggers final static
	final Logger logger = LogManager.getLogger();

	private final ThreadPool[] workers;
	private final LinkedList<Runnable> queue;
	public int pending;
	private volatile boolean shutdown;

	// TODO Add Javadoc

	public WorkQueue(int threads) {
		workers = new ThreadPool[threads];
		queue = new LinkedList<>();
		pending = 0;

		for (int i = 0; i < threads; i++) {
			workers[i] = new ThreadPool();
			workers[i].start();
		}
	}

	public void execute(Runnable r) {
		incrementPending();
		synchronized (queue) {
			queue.addLast(r);
			queue.notifyAll();
		}
		finish();
	}

	public synchronized void incrementPending() {
		pending++;
	}

	public synchronized void decrementPending() {
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
//		logger.debug("finished work");
	}

	public void shutdown() {
		shutdown = true;
		synchronized (queue) {
			queue.notifyAll();
		}
	}

	private class ThreadPool extends Thread {
		Runnable r;

		@Override
		public void run() {
			while (true) {
				synchronized (queue) {
					while (queue.isEmpty() && !shutdown) {
						try {
							queue.wait();
						} catch (InterruptedException e) {
//							logger.debug(e.getMessage(), e);
						}
					}

					if (shutdown) {
						break;
					}

					r = queue.removeFirst();
				}

				try {
					r.run();
					decrementPending();
//					logger.debug("Pending: {}", pending);
				} catch (RuntimeException e) {
//					logger.debug(e.getMessage(), e);
				}
			}
		}
	}
}
