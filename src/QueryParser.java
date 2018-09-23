import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;

public class QueryParser {

	public static void parse(Path path, InvertedIndex index) throws IOException {
		try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line = br.readLine();
			while(line != null) {
				line = line.toLowerCase();
				Set<String> words = new TreeSet<String>(TextFileStemmer.stemLine(line));
				
			}
		} catch (NullPointerException e) {
			System.out.println("There was an issue finding the file: " + path);

		}
	}
}
