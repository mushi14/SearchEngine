import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MultithreadedPathChecker {

	Logger logger = LogManager.getLogger(getClass());

	public final Set<Path> paths;
	private final WorkQueue queue;
	private int pending;
	public final ThreadSafeInvertedIndex threadSafeIndex;

	public MultithreadedPathChecker(Path path, int threads, ThreadSafeInvertedIndex threadSafeIndex) {
		this.paths = new HashSet<>();
		this.queue = new WorkQueue(threads);
		this.pending = 0;
		this.threadSafeIndex = threadSafeIndex;
		parse(path);
		finish();
		this.queue.shutdown();
	}

	public void parse(Path path) {
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
			logger.debug(e.getMessage(), e);
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
				logger.debug("woke up with pending at {}", pending);
			}
			logger.debug("Worker done!");
		} catch (InterruptedException e) {
			logger.debug(e.getMessage(), e);
		}
	}

	private class  FilesTask implements Runnable {

		private Path path;

		public FilesTask(Path path) {
			this.path = path;
			incrementPending();
		}

		@Override
		public void run() {
			try {
				synchronized (threadSafeIndex) {
	//				logger.debug("Adding {} to index", path.toString().substring(path.toString().lastIndexOf("/simple", path.toString().length())));
					TextFileStemmer.stemFile(path, threadSafeIndex);
				}
			} catch (IOException e) {
				logger.debug(e.getMessage(), e);
			}

			decrementPending();
//			logger.debug("Worker for {} FINISHED", path.toString().substring(path.toString().lastIndexOf("/simple", path.toString().length())));
		}
	}
}
