import java.nio.file.Files;
import java.nio.file.Path;

public class ArgumentMap {

	FileRead fr = new FileRead();
	
	
	public static void parse(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if (isFlag(args[i])) {
				/* Do something */
				if (args[i] == "-path") {
					aPath(args[i+1]);
					i++;
				} else if (args[i] == "-index") {
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
	
	
	public static boolean isPath(String arg) {
		try {
			arg = arg.trim();
			if (arg != "-path" && arg != "-index") {
				return true;
			} else {
				
				return false;
			}
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	
	public static void aPath(String arg) {
		Path path = FileRead.getPath(arg);
		if (Files.isDirectory(path)) {
			
		}
		else if (Files.isRegularFile(path)) {
			
		}
	}
	
	
	public static void optionalPath(String arg) {
		
	}
	
	
}
