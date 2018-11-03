import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class MultithreadedPathChecker {

//	Logger logger = LogManager.getLogger(getClass());

	public final ThreadSafeInvertedIndex threadSafeIndex;
	private final WorkQueue queue;
	private int pending;

	public MultithreadedPathChecker(Path path, int threads, ThreadSafeInvertedIndex threadSafeIndex) {
//		logger.debug("NEW CONSTRUCTOR CALLED");
		this.threadSafeIndex = threadSafeIndex;
		this.queue = new WorkQueue(threads);
		this.pending = 0;
		this.parse(path);
		this.finish();
		this.queue.shutdown();
	}

	private void parse(Path path) {
		try {
			if (Files.isRegularFile(path)) {
				String name = path.toString();
				if (name.toLowerCase().endsWith(".txt") || name.toLowerCase().endsWith(".text")) {
//					logger.debug("Worker for {} CREATED", path.toString().substring(path.toString().lastIndexOf("/simple", path.toString().length())));
					queue.execute(new FilesTask(path));
				}
			} else if (Files.isDirectory(path)) {
				try (DirectoryStream<Path> filePathStream = Files.newDirectoryStream(path)) {
					for (Path file: filePathStream) {
						parse(file);
					}
				}
			}
		} catch (IOException e) {
//			logger.debug(e.getMessage(), e);
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
//				logger.debug("woke up with pending at {}", pending);
			}
//			logger.debug("Worker done!");
		} catch (InterruptedException e) {
//			logger.debug(e.getMessage(), e);
		}
	}

	private class FilesTask implements Runnable {
		private Path path;

		public FilesTask(Path path) {
			this.path = path;
			incrementPending();
		}

		@Override
		public void run() {
			try {
//				logger.debug("Adding {} to index", path.toString().substring(path.toString().lastIndexOf("/simple", path.toString().length())));
				TextFileStemmer.stemFile(path, threadSafeIndex);
			} catch (IOException e) {
//				logger.debug(e.getMessage(), e);
			}

			decrementPending();
//			logger.debug("Worker for {} FINISHED", path.toString().substring(path.toString().lastIndexOf("/simple", path.toString().length())));
		}
	}
}