import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MultithreadedPathChecker {

	final static Logger logger = LogManager.getLogger();

	public static void filesInPath(Path path, int threads, ThreadSafeInvertedIndex index) throws IOException {
		WorkQueue queue = new WorkQueue(threads);
		filesInPathHelper(path, threads, index, queue);
		queue.shutdown();
	}

	private static void filesInPathHelper(Path path, int threads, ThreadSafeInvertedIndex index, 
			WorkQueue queue) throws IOException {
		try {
			if (Files.isRegularFile(path)) {
				String name = path.toString();
				if (name.toLowerCase().endsWith(".txt") || name.toLowerCase().endsWith(".text")) {
//					logger.debug("only executing{}", path);
					queue.execute(new FilesTask(path, index));
				}
			} else if (Files.isDirectory(path)) {
				try (DirectoryStream<Path> filePathStream = Files.newDirectoryStream(path)) {
					for (Path file: filePathStream) {
						filesInPath(file, threads, index);
					}
				}
			}
		} catch (IOException e) {
//			logger.debug(e.getMessage(), e);
		}
	}

	// TODO Will need to make this a static nested class if you make the static methods above
	private static class FilesTask implements Runnable {
		private Path path;
		private ThreadSafeInvertedIndex index;

		public FilesTask(Path path, ThreadSafeInvertedIndex index) {
			this.path = path;
			this.index = index;
		}

		@Override
		public void run() {
			try {
//				logger.debug("in here with {}", path);
				TextFileStemmer.stemFile(path, index);
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
