import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

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
		Path path = Paths.get("/Users/mushahidhassan/Desktop/CS 212/ProjectTests/project-tests/text/simple/hello.txt");
		try (BufferedReader br = Files.newBufferedReader(path)) {
			System.out.println(br.readLine());
		} catch (IOException e) {
			System.out.println("file doesnt exist");
		}
		
		System.out.println(Arrays.toString(args));
	}

}
