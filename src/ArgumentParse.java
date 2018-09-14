import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.stream.Stream;

public class ArgumentParse {
	
	public static void parse(String[] args, TreeMap<String, WordIndex> index) throws IOException {
		for (int i = 0; i < args.length; i++) {
			if (isFlag(args[i])) {
				if (args[i].equals("-path")) {
					try {
						if ((i + 1) < args.length) {
							Path path = Paths.get(args[i+1]);
							if (Files.isDirectory(path) || Files.isRegularFile(path)) {
								aPath(path, index);
								i++;
							}
						} 
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				} else if (args[i].equals("-index")) {
					try {
						if ((i + 1) < args.length) {
							if (!args[i+1].isEmpty()) {
								Path path = Paths.get(args[i+1]);
								if (Files.isRegularFile(path)) {
									optionalPath(path, index);
									i++;
								} else {
									path = Paths.get("/Users/mushahidhassan/Desktop/CS212/Project1/project-mushi14/index.json");
									optionalPath(path, index);
								}
							}
						} else {
							Path path = Paths.get("/Users/mushahidhassan/Desktop/CS212/Project1/project-mushi14/index.json");
							optionalPath(path, index);
						}
					} catch (NullPointerException e) {
						Path path = Paths.get("/Users/mushahidhassan/Desktop/CS212/Project1/project-mushi14/index.json");
						optionalPath(path, index);
					}
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
			ArrayList<String> files = new ArrayList<String>();
			try (Stream<Path> filePathStream = Files.walk(Paths.get(path.toString()))) {
			    filePathStream.forEach(filePath -> {
			        if (Files.isRegularFile(filePath)) {
			        	files.add(filePath.toString());
			        }
			    });
			} catch (IOException e) {
				e.printStackTrace();
			}

			for (String file : files) {
				path = Paths.get(file);
				if (isTextFile(file)) {
					TextFileStemmer.stemFile(path, index);
				} else if (Files.isDirectory(path)) {
					aPath(path, index);
				}
			}
		} else if (Files.isRegularFile(path)) {
			TextFileStemmer.stemFile(path, index);
		}
	}
	
	
	public static void optionalPath(Path path, TreeMap<String, WordIndex> index) throws IOException {
		StringWriter writer = new StringWriter();
		TreeJSONWriter.asInvertedIndex(index);
		TreeJSONWriter.asInvertedIndex(index, path);
		TreeJSONWriter.asInvertedIndex(index, writer, 0);
	}
}
