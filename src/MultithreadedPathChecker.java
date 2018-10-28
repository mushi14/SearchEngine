import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MultithreadedPathChecker {

	Logger logger = LogManager.getLogger();

	private final Set<String> paths;
	ThreadSafeInvertedIndex index = new ThreadSafeInvertedIndex();

	private MultithreadedPathChecker() {
		paths = new HashSet<>();
	}

	private void parse(Path path) {
		Thread worker = new FilesWorker(path);
		worker.start();
	}

	private class FilesWorker extends Thread {

		private Path path;

		public FilesWorker(Path path) {
			this.path = path;
			logger.debug("Worker for {} created", path);
		}

		@Override
		public void run() {
			try {
				if (Files.isDirectory(path)) {
					try (DirectoryStream<Path> filePathStream = Files.newDirectoryStream(path)) {
						for (Path file: filePathStream) {
							paths.add(file.toString());
							Thread worker = new FilesWorker(file);
							worker.start();
							logger.debug("New worker created and started for {}", file);
						}
					}
				} else if (Files.isRegularFile(path)) {
					String name = path.toString();
					if (name.toLowerCase().endsWith(".txt") || name.toLowerCase().endsWith(".text")) {
						paths.add(path.toString());
//						TextFileStemmer.stemFile(path, index);
					}
				}
			} catch (IOException e) {
				logger.debug(e.getMessage(), e);
			}

			logger.debug("Worker for {} finished", path);
		}
	}
}
