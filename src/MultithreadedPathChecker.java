import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MultithreadedPathChecker {

	final static Logger logger = LogManager.getLogger();

	/**
	 * Gets the starting path of the file and initializes the Work Queue
	 * @param path path of the file
	 * @param threads how many threads to run on
	 * @param index thread safe inverted index to populate
	 * @throws IOException if the path of the file isn't readable
	 */
	public static void filesInPath(Path path, int threads, ThreadSafeInvertedIndex index) throws IOException {
		WorkQueue queue = new WorkQueue(threads);
		filesInPathHelper(path, threads, index, queue);
		queue.shutdown();
	}

	/**
	 * Helper method, traverses through directories to find valid text files to read
	 * @param path path of the file
	 * @param threads how many threads to run on
	 * @param index thread safe index to populate
	 * @param queue work queue to use
	 * @throws IOException if the path of the file isn't readable
	 */
	private static void filesInPathHelper(Path path, int threads, ThreadSafeInvertedIndex index, 
			WorkQueue queue) throws IOException {
		try {
			if (Files.isRegularFile(path)) {
				String name = path.toString();
				if (name.toLowerCase().endsWith(".txt") || name.toLowerCase().endsWith(".text")) {
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
			System.out.println("The was trouble reading the file.");
		}
	}

	/**
	 * Static nested class for assigning tasks to threads 
	 * @author mushahidhassan
	 *
	 */
	private static class FilesTask implements Runnable {
		private Path path;
		private ThreadSafeInvertedIndex index;

		/**
		 * Constructor for static nested class
		 * @param path path of the file
		 * @param index thread safe index to populate
		 */
		public FilesTask(Path path, ThreadSafeInvertedIndex index) {
			this.path = path;
			this.index = index;
		}

		/**
		 * Populates the thread safe index
		 */
		@Override
		public void run() {
			try {
				InvertedIndex local = new InvertedIndex();
				TextFileStemmer.stemFile(path, local);
				synchronized (index) {
					index.addAll(local);
				}
			} catch (IOException e) {
				System.out.println("File not found.");
			}
		}
	}
}
