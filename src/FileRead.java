import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class FileRead {
		
	Parsing p = new Parsing();
	
	/* Method for getting the path */
	public Path getPath(String p) {
		Path path = Paths.get(p);	
		return path;
	}
	
	
	/* Method for reading the line */
	public HashMap<String, HashMap<String, HashSet<Integer>>> read(Path p, HashMap<String, HashMap<String, HashSet<Integer>>> index) {
				
		try { 
			BufferedReader br = Files.newBufferedReader(p);
			String line = br.readLine();
			while (line != null) {
				int count = 1;
				String words[] = line.split(" ");
				for (int i = 0; i < words.length; i++) {
					if (index.containsKey(words[i])) {
						if (index.get(words[i]).containsKey(p.toString())) {
							index.get(words[i]).get(p.toString()).add(count);
						} else {
							index.get(words[i]).put(p.toString(), new HashSet<Integer>());
							index.get(words[i]).get(p.toString()).add(count);
						}
					} else {
						index.put(words[i], new HashMap<String, HashSet<Integer>>());
						index.get(words[i]).put(p.toString(), new HashSet<Integer>());
						index.get(words[i]).get(p.toString()).add(count);
					}
				}
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			System.err.println("file couldn't be found");
            e.printStackTrace();
		} catch (IOException e) {
			System.err.println("unable to read");
            e.printStackTrace();
		}
		
		return index;
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
