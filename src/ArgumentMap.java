import java.nio.file.Files;
import java.nio.file.Path;

public class ArgumentMap {

	FileRead fr = new FileRead();
	
	
	public void parse(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if (isFlag(args[i])) {
				/* Do something */
			}
		}
	}
	
	
	public static boolean isFlag(String arg) {
		try {
			arg = arg.trim();
			if (arg == "-path") {
				/* Do something */
				aPath(arg);
				return true;
			} else if (arg == "-index") {
				/* Do something */
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
	
	
	public void optionalPath(String arg) {
		
	}
	
	
}
