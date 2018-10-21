
public class ReadWriteLock {
	private int readers;
	private int writers;

	public ReadWriteLock() {
		readers = 0;
		writers = 0;
	}

	public synchronized void lockReadOnly() {
		while (writers > 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				System.out.println("exception");
			}
		}
		readers++;
	}

	public synchronized void unlockReadOnly() {
		readers--;
		this.notifyAll();
	}

	public synchronized void lockReadWrite() {
		while (writers > 0 && readers > 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				System.out.println("exception");
			}
		}
		writers++;
	}

	public synchronized void unlockReadWrite() {
		writers--;
		this.notifyAll();
	}
}
