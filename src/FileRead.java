import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;


public class FileRead {
			
	/* Method for getting the path */
	public static Path getPath(String p) {
		Path path = Paths.get(p);	
		return path;
	}
	
	
	/* Method for reading the line */
	public static void read(Path p, HashMap<String, HashMap<String, HashSet<Integer>>> index) {
		int count = 1;
		try { 
			BufferedReader br = Files.newBufferedReader(p);
			String line = br.readLine();
			while (line != null) {
				String[] words = line.split("[^A-Za-z]");
				for (int i = 0; i < words.length; i++) {
					if (!words[i].isEmpty()) {
						if (index.containsKey(words[i])) {
							if (index.get(words[i]).containsKey(p.toString())) {
								index.get(words[i]).get(p.toString()).add(count);
								count++;
							} else {
								index.get(words[i]).put(p.toString(), new HashSet<Integer>());
								index.get(words[i]).get(p.toString()).add(count);
								count++;
							}
						} else {
							index.put(words[i], new HashMap<String, HashSet<Integer>>());
							index.get(words[i]).put(p.toString(), new HashSet<Integer>());
							index.get(words[i]).get(p.toString()).add(count);
							count++;
						}
					}
				}
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
            e.printStackTrace();
		} catch (IOException e) {
            e.printStackTrace();
		}
	}
}
