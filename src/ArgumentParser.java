import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// TODO Restore the general ArgumentParser class from your homework

public class ArgumentParser {
	/** Checks if the argument passed is a flag, if it is, then calls the TreeJSONWriter and writes the index
	 * in JSONWriter format the path given (if path is valid), if invalid path, then writes in JSONWriter format
	 * to "index.json" .
	 * 
	 * 
	 * @param args array of command line arguments
	 * @param index inverted index that contains stemmed words, their files, and their positions
	 * @throws IOException if unable to read to write to file 
	 * 
	 */
//	public static void isIndex(String arg, Path path, InvertedIndex index) throws IOException {
//		if (!isFlag(path.toString())) {
//			TreeJSONWriter.asInvertedIndex(index, path);
//		} else {
//			TreeJSONWriter.asInvertedIndex(index, Paths.get("index.json"));
//		}
//	}
	
	
	// TODO Create a class specific to building an index from text files
	// TODO Also include in this builder class TextFileStemmer.stemFile(Path path, InvertedIndex index)
	
	/** Checks to see if the path provided is a valid path 
	 * 
	 * @param p path being checked
	 * @return true if path is a valid file or a directory
	 * 
	 */
	public static boolean isValidPath(String p) {
		Path path = Paths.get(p);
		return Files.isDirectory(path) || Files.isRegularFile(path);
	}


	/** Checks to see if the path provided is a text file or a directory. If a valid text
	 * file, then writes to the file. If a directory, then goes through the directory 
	 * to find its text files or directories. 
	 * 
	 * @param path path that is being checked 
	 * @param index inverted index that contains the stemmed words, their files, and their positions
	 * @throws IOException if unable to read to write to file 
	 * 
	 */
	public static void filesInPath(Path path, InvertedIndex index) throws IOException {
		if (Files.isDirectory(path)) {
			try (DirectoryStream<Path> filePathStream = Files.newDirectoryStream(path)) {
				for (Path file: filePathStream) {
					if (Files.isRegularFile(file)) {
						path = Paths.get(file.toString());
						if (isTextFile(file.toString())) {
							TextFileStemmer.stemFile(path, index);
						}
					} else if (Files.isDirectory(file)) {
						path = Paths.get(file.toString());
						filesInPath(path, index);
					}
				}
			} catch (NullPointerException e) {
				System.out.println("There was an issue fiding the directory: " + path);
			}
		} else if (Files.isRegularFile(path)) {
			TextFileStemmer.stemFile(path, index);
		}
	}	
	
	/** Checks to see if the file is a text file
	 * 
	 * @param file takes in a file to check if is a text file
	 * @return true if the given file ends in ".txt" or ".text"
	 * 
	 */
	public static boolean isTextFile(String file) {
		file = file.toLowerCase();
		return file.endsWith(".txt") || file.endsWith(".text");
	}
//	
//	/** Checks to see if the path provided is a valid path 
//	 * 
//	 * @param p path being checked
//	 * @return true if path is a valid file or a directory
//	 * 
//	 */
//	public static boolean isValidPath(String p) {
//		return Files.isDirectory(Paths.get(p)) || Files.isRegularFile(Paths.get(p));
//	}
}
