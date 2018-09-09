import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * TODO Fill in your own comments!
 */
public class Driver {

	/**
	 * Parses the command-line arguments to build and use an in-memory search
	 * engine from files or the web.
	 *
	 * @param args the command-line arguments to parse
	 * @return 0 if everything went well
	 */
	public static void main(String[] args) {
		// TODO Fill in		
		var index = new HashMap<String, HashMap<String, HashSet<Integer>>>();
		
		FileRead file_read = new FileRead();
		ArgumentMap am = new ArgumentMap();
		
		Path p = file_read.getPath("/Users/mushahidhassan/Desktop/CS 212/ProjectTests/project-tests/text/simple/symbols.txt");
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].isEmpty()) {
				System.out.println("No arguments passed");
			} else {
				
			}
 		}
		
		file_read.read(p, index);
		
//		for (String key : index.keySet()) {
//			System.out.print(key + "     ");
//			for (String key2 : index.get(key).keySet()) {
//				System.out.print("[");
//				for (int num : index.get(key).get(key2)) {
//				System.out.print(num + ",");
//				}
//				System.out.print("]");
//			}
//			System.out.println();
//		}
		
//		System.out.println(Arrays.toString(args));
	}

}
