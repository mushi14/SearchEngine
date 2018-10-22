import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PathChecker {
	/** 
	 * Checks to see if the path provided is a text file or a directory. If a valid text
	 * file, then writes to the file. If a directory, then goes through the directory 
	 * to find its text files or directories. 
	 * 
	 * @param path path that is being checked 
	 * @param index inverted index that contains the stemmed words, their files, and their positions
	 * @throws IOException if unable to read to write to file 
	 */
	public static void filesInPath(Path path, InvertedIndex index) throws IOException {
		if (Files.isDirectory(path)) {
			try (DirectoryStream<Path> filePathStream = Files.newDirectoryStream(path)) {
				for (Path file: filePathStream) {
					filesInPath(file, index);
				}
			}
		} else if (Files.isRegularFile(path)) {
			String name = path.toString().toLowerCase();
			if (name.endsWith(".txt") || name.endsWith(".text")) {
				TextFileStemmer.stemFile(path, index);
			}
		}
	}

	/**
	 * Checks to see if the path provided for queries is a directory or file. 
	 * @param path path to the file or directory
	 * @return list of files valid query files
	 * @throws IOException
	 */
	public static List<String> queryFiles(Path path) throws IOException {
		List<String> files = new ArrayList<>();
		if (Files.isDirectory(path)) {
			try (DirectoryStream<Path> filePathStream = Files.newDirectoryStream(path)) {
				for (Path file: filePathStream) {
					queryFiles(file);
				}
			}
		} else if (Files.isRegularFile(path)) {
			String name = path.toString();
			if (name.toLowerCase().endsWith(".txt") || name.toLowerCase().endsWith(".text")) {
				files.add(name);
			}
		}
		return files;
	}
}
