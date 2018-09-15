import java.io.IOException;
import java.util.TreeMap;

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
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Fill in			
		TreeMap<String, WordIndex> index = new TreeMap<String, WordIndex>();
		ArgumentParse.parse(args, index);
	}

}
