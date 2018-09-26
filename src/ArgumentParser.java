import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// think of class names as job titles
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
	public static void isIndex(String arg, Path path, InvertedIndex index) throws IOException {
		if (!isFlag(path.toString())) {
			TreeJSONWriter.asInvertedIndex(index, path);
		} else {
			TreeJSONWriter.asInvertedIndex(index, Paths.get("index.json"));
		}
	}
	
	/** TODO
	 * 
	 * @param arg
	 * @param path
	 * @param index
	 * @throws IOException
	 */
	public static void isSearch(String arg, Path path) throws IOException {
		try {
			if (!isFlag(path.toString())) {
				for (String file : filesInPath(path)) {
					QueryParser.parse(Paths.get(file));
				}
			} 
		} catch (NullPointerException e) {
			System.out.println("There was an issue finding query file: " + path);
		}
	}
	
	/** Checks to see if argument passed is a valid flag or not 
	 * 
	 * @param arg argument from the command line
	 * @return true if the argument is -path or -index
	 * 
	 */
	public static boolean isFlag(String arg) {
		if (arg.isEmpty()) { 
			return false;
		} else {
			try {
				arg = arg.trim();
				if (arg.equals("-path") || arg.equals("-index") || arg.equals("-search") || arg.equals("exact") ||
						arg.equals("results") || arg.equals("results")) {
					return true;
				} else {
					return false;
				}
			} catch (NullPointerException e) {
				return false;
			}
		}
	}
	
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
	

	/** Checks to see if the path provided is a text file or a directory. If a valid text
	 * file, then writes to the file. If a directory, then goes through the directory 
	 * to find its text files or directories. 
	 * 
	 * @param path path that is being checked 
	 * @param index inverted index that contains the stemmed words, their files, and their positions
	 * @throws IOException if unable to read to write to file 
	 * 
	 */
	public static List<String> filesInPath(Path path) throws IOException {
		List<String> files = new ArrayList<String>();
		if (Files.isDirectory(path)) {
			try (DirectoryStream<Path> filePathStream = Files.newDirectoryStream(path)) {
				for (Path file: filePathStream) {
					if (Files.isRegularFile(file)) {
						if (isTextFile(file.toString())) {
							files.add(file.toString());
						}
					} else if (Files.isDirectory(file)) {
						path = Paths.get(file.toString());
						for (String fileInDir : filesInPath(path)) {
							files.add(fileInDir);
						}
					}
				}
			} catch (NullPointerException e) {
				System.out.println("There was an issue fiding the directory: " + path);
			}
		} else if (Files.isRegularFile(path)) {
			files.add(path.toString());
		}
		return files;
	}
	
	/** 
	 * @throws IOException 
	 *
	 */
	public static void addStemmedWords(List<String> files, InvertedIndex index) throws IOException {
		for (String file : files) {
			TextFileStemmer.stemFile(Paths.get(file), index);
		}
	}
}
