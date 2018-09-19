import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeMap;

// think of class names as job titles
public class ArgumentParser {
	
	/*
	 * TODO
	 * Anything project-specific should stay in Driver
	 * (includes looking for specific flag/values from the user)
	 * 
	 * All other code should be generalized
	 */
	
	/** Parses the command line arguments and makes any necessary changes to 
	 * inverted index accordingly. 
	 * 
	 * @param args array of command line arguments
	 * @param index inverted index that contains stemmed words, their files, and their positions
	 * @throws IOException if unable to read to write to file 
	 * 
	 */
	public static void isPath(String arg, Path path, TreeMap<String, WordIndex> index) throws IOException {
		if (isFlag(arg)) {
			filesInPath(path, index);
		}
	}
	
	
	public static void isIndex(String arg, Path path, TreeMap<String, WordIndex> index) throws IOException {
		if (isFlag(arg)) {
			
		}
	}
//	public static void parse(String[] args, TreeMap<String, WordIndex> index) throws IOException {
//		for (int i = 0; i < args.length; i++) {
//			if (isFlag(args[i])) {
//				if (args[i].equals("-path")) {
//					try {
//						if ((i + 1) < args.length && isValidPath(args[i+1])) {
//							Path path = Paths.get(args[i+1]);
//							aPath(path, index);
//						} 
//					} catch (NullPointerException e) {
//						e.printStackTrace(); // TODO No stack trace
//						// TODO Try to eliminate what causes this exception
//					}
//					i++;
//				} else if (args[i].equals("-index")) {
//					try {
//						if ((i + 1) < args.length) {
//							if (!isFlag(args[i + 1])) {
//								TreeJSONWriter.asInvertedIndex(index, Paths.get(args[i+1]));
//							} else {
//								TreeJSONWriter.asInvertedIndex(index, Paths.get("index.json"));
//							}
//						} else {
//							TreeJSONWriter.asInvertedIndex(index, Paths.get("index.json"));
//						}
//					} catch (NullPointerException e) {
//						TreeJSONWriter.asInvertedIndex(index, 
//								Paths.get("index.json"));
//					}
//					i++;
//				}
//			}
//		}
//	}
	
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
				if (arg.equals("-path")) {
					return true;
				} else if (arg.equals("-index")) {
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
	public static void filesInPath(Path path, TreeMap<String, WordIndex> index) throws IOException {
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
//				e.printStackTrace();
			}

		} else if (Files.isRegularFile(path)) {
			TextFileStemmer.stemFile(path, index);
		}
	}	
}
