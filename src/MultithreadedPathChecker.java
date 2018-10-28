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

	private MultithreadedPathChecker() {
		paths = new HashSet<>();
	}

	private class FilesWorker implements Runnable {

		private Path path;
		public FilesWorker(Path path) {
			this.path = path;
			logger.debug("Worker for {} created", path);
		}

		@Override
		public void run() {
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
//					TextFileStemmer.stemFile(path, index);
					paths.add(path.toString());
					Thread worker = new FilesWorker(path);
					worker.start();
					logger.debug("New worker created and started for {}", path);
				}
			}

			logger.debug("Worker for {} finished", path);
		}
	}
}
