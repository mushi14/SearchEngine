import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileRead {
	
	/* Method for getting the path */
	public Path getPath(String p) {
		Path path = Paths.get(p);	
		return path;
	}
	
	
	/* Method for reading the line */
	public ArrayList<String> read(Path p) {
		ArrayList<String> temp = new ArrayList<String>();
		try { 
			BufferedReader br = Files.newBufferedReader(p);
			while (br.readLine() != null) {
				temp.add(br.readLine());
			}
		} catch (FileNotFoundException e) {
			System.err.println("file couldn't be found");
            e.printStackTrace();
		} catch (IOException e) {
			System.err.println("unable to read");
            e.printStackTrace();
		}
		return temp;
	}
	
	
	
	
	/* Method for checking to see if the argument is a flag */
//	public static boolean isFlag(String arg) {
//		try {
//			arg = arg.trim();
//			
//			if (arg.length() >= 2) {
//				if (arg.charAt(0) == '-' && arg.charAt(1) != ' ') {
//					return true;
//				} else {
//					return false;
//				}
//			} else {
//				return false;
//			}
//		} catch (NullPointerException e) {
//			return false;
//		}
//	}
	
	
	/* Method for checking to see if the argument is a value */
//	public static boolean isValue(String arg) {
//		try {
//			arg = arg.trim();
//
//			if (!arg.isEmpty() && arg.charAt(0) != '-') {
//				return true;
//			} else {
//				return false;
//			}
//		} catch(NullPointerException e) {
//			return false;
//		}
//	}

}
