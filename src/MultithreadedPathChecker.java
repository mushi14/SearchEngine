import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * TODO
 * Try to mimic the single-threaded version. So if it has a single public static 
 * method, try to make a multi-threaded version with a single public static method.
 */

public class MultithreadedPathChecker {

	public final ThreadSafeInvertedIndex threadSafeIndex;
	private final WorkQueue queue;
	Logger logger = LogManager.getLogger(getClass());

	public MultithreadedPathChecker(Path path, int threads, ThreadSafeInvertedIndex threadSafeIndex) {
		this.threadSafeIndex = threadSafeIndex;
		this.queue = new WorkQueue(threads);
		this.parse(path);
		this.queue.shutdown();
	}

	private void parse(Path path) {
//		logger.debug("running parse again on {}", path);
		try {
			if (Files.isRegularFile(path)) {
				String name = path.toString();
				if (name.toLowerCase().endsWith(".txt") || name.toLowerCase().endsWith(".text")) {
//					logger.debug("only executing{}", path);
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

	/* TODO Try this:
	public static void filesInPath(Path path, ThreadSafeInvertedIndex index, int threads) throws IOException {
		WorkQueue queue = ...
		filesInPathHelper(path, index, queue);
		queue.finish();
		queue.shutdown();
	}
	
	private static void filesInPathHelper(Path path, ThreadSafeInvertedIndex index, WorkQueue queue) throws IOException {
		if (Files.isDirectory(path)) {
			try (DirectoryStream<Path> filePathStream = Files.newDirectoryStream(path)) {
				for (Path file: filePathStream) {
					filesInPath(file, index);
				}
			}
		} else if (Files.isRegularFile(path)) {
			String name = path.toString();
			if (name.toLowerCase().endsWith(".txt") || name.toLowerCase().endsWith(".text")) {
				** add task to queue here
			}
		}
	}
	*/

	// TODO Will need to make this a static nested class if you make the static methods above
	private class FilesTask implements Runnable {
		private Path path;

		public FilesTask(Path path) {
			this.path = path;
		}

		@Override
		public void run() {
			try {
//				logger.debug("in here with {}", path);
				TextFileStemmer.stemFile(path, threadSafeIndex);
//				logger.debug("Running: {}", path);
//				logger.debug("this is index afterwards: {}", threadSafeIndex);
				/*
				 * TODO Several small blocking adds will always be slower than a
				 * single large blocking add, because locking/unlocking is so
				 * expensive. Just like lecture code used "local data" to speed
				 * it up, you should use a "local index" here. For example:
				 * 
				 * InvertedIndex local = new InvertedIndex();
				 * TextFileStemmer.stemFile(path, local); <- does no blocking because uses local data
				 * index.addAll(local); <- you have to create this method, blocks once and efficiently adds everything
				 */
			} catch (IOException e) {
				System.out.println("File not found.");
			}
		}
	}
}
