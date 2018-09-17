import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeMap;

public class ArgumentParse {
	
	public static void parse(String[] args, TreeMap<String, WordIndex> index) throws IOException {		
		for (int i = 0; i < args.length; i++) {
			if (isFlag(args[i])) {
				if (args[i].equals("-path")) {
					try {
						if ((i + 1) < args.length && isValidPath(args[i+1])) {
							Path path = Paths.get(args[i+1]);
							aPath(path, index);
						} 
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
					i++;
				} else if (args[i].equals("-index")) {
					try {
						if ((i + 1) < args.length) {
							if (!isFlag(args[i + 1])) {
								TreeJSONWriter.asInvertedIndex(index, Paths.get(args[i+1]));
							} else {
								TreeJSONWriter.asInvertedIndex(index, Paths.get("index.json"));
							}
						} else {
							TreeJSONWriter.asInvertedIndex(index, Paths.get("index.json"));
						}
					} catch (NullPointerException e) {
						TreeJSONWriter.asInvertedIndex(index, 
								Paths.get("index.json"));
					}
					i++;
				}
			}
		}
	}
	
	
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
	
	
	public static boolean isValidPath(String p) {
		Path path = Paths.get(p);
		if (Files.isDirectory(path) || Files.isRegularFile(path)) {
			return true;
		} else {
			return false;
		}	
	}

	
	public static boolean isTextFile(String file) {
		file = file.toLowerCase();
		if (file.substring(file.length() - 4, file.length()).equals(".txt")) {
			return true;
		} else if (file.substring(file.length() - 5, file.length()).equals(".text")) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public static void aPath(Path path, TreeMap<String, WordIndex> index) throws IOException {
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
			        	aPath(path, index);
			        }
			    }
			} catch (NullPointerException e) {
				e.printStackTrace();
			}

		} else if (Files.isRegularFile(path)) {
			TextFileStemmer.stemFile(path, index);
		}
	}	
}
