import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		HashMap<String, HashMap<String, HashSet<Integer>>> index = new HashMap<String, HashMap<String, HashSet<Integer>>>();

		ArgumentParse.parse(args, index);
		
//		System.out.println(index.keySet());
		
		
//		for (String key : index.keySet()) {
//			System.out.print(key + "     ");
//			for (String key2 : index.get(key).keySet()) {
//				System.out.print(key2 + "      " + "[");
//				for (int num : index.get(key).get(key2)) {
//				System.out.print(num + ",");
//				}
//				System.out.print("]");
//			}
//			System.out.println();
//		}
//		
	}

}
