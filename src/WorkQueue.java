import java.util.LinkedList;

public class WorkQueue {

	private final Thread[] workers;
	private final LinkedList<Runnable> queue;

	public WorkQueue(int threads) {
		workers = new Thread[threads];
		queue = new LinkedList<>();

		for (int i = 0; i < threads; i++) {
			workers[i] = new Thread();
			workers[i].start();
		}
	}

}
