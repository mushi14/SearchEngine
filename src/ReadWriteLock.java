
// TODO Javadoc

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
		
		/*
		 * TODO Small efficiency issue: over-notification.
		 * 
		 * Suppose 10 threads call lockReadOnly(). Since there are no writers
		 * in the system, all of them are able to lock and readers = 10.
		 * 
		 * Now suppose those readers are still active and a new thread tries to
		 * lockReadWrite(). Since there are active readers, it is forced to wait.
		 * 
		 * Now suppose one of those reader threads calls unlockReadOnly. The
		 * number of readers goes from 10 to 9, and notifyAll() is called. This
		 * wakes up the waiting writer thread, but that thread still can't lock
		 * yet since there are still active readers. So you unnecessarily woke
		 * up that thread.
		 * 
		 * When should you actually try to call notifyAll()?
		 */
	}

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

	public synchronized void unlockReadWrite() {
		writers--;
		this.notifyAll();
	}
}
