import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArgumentParse {
	
	public static HashMap<String, HashMap<String, HashSet<Integer>>> parse(String[] args, HashMap<String, 
			HashMap<String, HashSet<Integer>>> index) {
		for (int i = 0; i < args.length; i++) {
			System.out.println(args[i]);
			if (isFlag(args[i])) {
				if (args[i] == "-path") {
					try {
						if (!args[i+1].isEmpty()) {
							Path path = Paths.get(args[i+1]);
							if (Files.isDirectory(path) || Files.isRegularFile(path)) {
								aPath(path, index);
								i++;
							}
						}
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				} else if (args[i] == "-index") {
					try {
						if (!args[i+1].isEmpty()) {
							Path path = Paths.get(args[i+1]);
							if (Files.isRegularFile(path)) {
								/* Do something */
								optionalPath(path, index);
								i++;
							} else {
								path = Paths.get("/Users/mushahidhassan/Desktop/CS 212/Project1/project-mushi14/index.json.rtf");
								optionalPath(path, index);
							}
						}
					} catch (NullPointerException e) {
						Path path = Paths.get("/Users/mushahidhassan/Desktop/CS 212/Project1/project-mushi14/index.json.rtf");
						optionalPath(path, index);
					}
				}
			}
		}
		return index;
	}
	
	
	public static boolean isFlag(String arg) {
		if (arg.isEmpty()) { 
			return false;
		} else {
			try {
				arg = arg.trim();
				if (arg == "-path") {
					return true;
				} else if (arg == "-index") {
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
	
	
	public static void aPath(Path path, HashMap<String, HashMap<String, HashSet<Integer>>> index) {
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
					FileRead.read(path, index);
				} else if (Files.isDirectory(path)) {
					aPath(path, index);
				}
			}
		} else if (Files.isRegularFile(path)) {
			FileRead.read(path, index);
		}
	}
	
	
	/* Do something */
	public static void optionalPath(Path path, HashMap<String, HashMap<String, HashSet<Integer>>> index) {

	}
}










