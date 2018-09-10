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

	public static HashMap<String, HashMap<String, HashSet<Integer>>> index = new HashMap<String, HashMap<String, HashSet<Integer>>>();

	
	public static void parse(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if (isFlag(args[i])) {
				if (args[i] == "-path") {
					if (isValidPath(args[i+1])) {
						aPath(args[i+1]);
						i++;
					}
				} else if (args[i] == "-index") {
					/* Do something */
					optionalPath(args[i+1]);
				}
			}
		}
	}
	
	
	public static boolean isFlag(String arg) {
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
	
	
	public static boolean isValidPath(String arg) {
		Path path = Paths.get(arg);
		try {
			if (Files.isDirectory(path)) {
				return true;
			} else if (Files.isRegularFile(path)){
				return true;
			} else {
				return false;
			}
		} catch (NullPointerException e) {
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
	
	public static void aPath(String arg) {
		Path path = FileRead.getPath(arg);
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
				if (isTextFile(file)) {
					FileRead.read(path, index);
				}
			}
		} else if (Files.isRegularFile(path)) {
			FileRead.read(path, index);
		}
	}
	
	
	/* Do something */
	public static void optionalPath(String arg) {
		
	}
}
