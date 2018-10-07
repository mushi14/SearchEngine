import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
	public static List<String> filesInPath(Path path) throws IOException {
		List<String> textFiles = new ArrayList<String>();
		if (Files.isDirectory(path)) {
			try (DirectoryStream<Path> filePathStream = Files.newDirectoryStream(path)) {
				for (Path file: filePathStream) {
					if (Files.isRegularFile(file)) {
						path = Paths.get(file.toString());
						if (file.toString().toLowerCase().endsWith(".txt") || file.toString().toLowerCase().endsWith(".text")) {
							textFiles.add(path.toString());
						}
					} else if (Files.isDirectory(file)) {
						path = Paths.get(file.toString());
						for (String fileInDirectory : filesInPath(path)) {
							textFiles.add(fileInDirectory);
						}
					}
				}
			} catch (NullPointerException e) {
				System.out.println("There was an issue fiding the directory: " + path);
			}
		} else if (Files.isRegularFile(path)) {
			textFiles.add(path.toString());
		}
		return textFiles;
	}
	
	public static void readFiles(List<String> files, InvertedIndex index) throws IOException {
		try {
			for (String file : files) {
				if (!file.isEmpty()) {
					TextFileStemmer.stemFile(Paths.get(file), index);
				}
			}
		} catch (NullPointerException e) {
			System.out.println("There was an issue fiding the directory.");
		}
	}
}
