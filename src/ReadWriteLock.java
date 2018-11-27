/** 
 * Custom read write lock class
 * @author mushahidhassan
 *
 */
public class ReadWriteLock {
	private int readers;
	private int writers;

	/**
	 * Constructor. Initializes the readers and the writers to 0
	 */
	public ReadWriteLock() {
		readers = 0;
		writers = 0;
	}

	/**
	 * Locks only the read functions
	 */
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

	/**
	 * Unlocks only the read functions
	 */
	public synchronized void unlockReadOnly() {
		readers--;
		if (readers == 0) {
			this.notifyAll();
		}
	}

	/**
	 * Locks the read and/or write functions
	 */
	public synchronized void lockReadWrite() {
		while (writers > 0 || readers > 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				System.out.println("exception");
			}
		}
		writers++;
	}

	/**
	 * Unlocks the read and/or write functions
	 */
	public synchronized void unlockReadWrite() {
		writers--;
		this.notifyAll();
	}
}
