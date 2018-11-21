 import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorkQueue {

	// TODO Make all loggers final static
	private static final Logger logger = LogManager.getLogger();

	private final ThreadPool[] workers;
	private final LinkedList<Runnable> queue;
	private volatile boolean shutdown;

	// TODO Add Javadoc
	// TODO Move the pending variable into WorkQueue, just like the homework required.
	
	public WorkQueue(int threads) {
		workers = new ThreadPool[threads];
		queue = new LinkedList<>();

		for (int i = 0; i < threads; i++) {
			workers[i] = new ThreadPool();
			workers[i].start();
		}
	}

	public void execute(Runnable r) {
		synchronized (queue) {
			queue.addLast(r);
			queue.notifyAll();
		}
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
							logger.debug(e.getMessage(), e);
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
					logger.debug(e.getMessage(), e);
				}
			}
		}
	}
}
