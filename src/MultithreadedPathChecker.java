import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class MultithreadedPathChecker {

	public final ThreadSafeInvertedIndex threadSafeIndex;
	private final WorkQueue queue;
	private int pending;

	public MultithreadedPathChecker(Path path, int threads, ThreadSafeInvertedIndex threadSafeIndex) {
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
			System.out.println("The path is invalid, cannot build an index.");
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
			}
		} catch (InterruptedException e) {
			System.out.println("Thread interrupted.");
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
				TextFileStemmer.stemFile(path, threadSafeIndex);
			} catch (IOException e) {
				System.out.println("File not found.");
			}

			decrementPending();
		}
	}
}
