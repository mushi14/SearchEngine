import java.io.IOException;
import java.nio.file.Paths;

public class Driver {

	/**
	 * Parses the command-line arguments to build and use an in-memory search
	 * engine from files or the web.
	 *
	 * @param args the command-line arguments to parse
	 * @return 0 if everything went well
	 * @throws IOException 
	 */
	public static void main(String[] args) {		
		InvertedIndex index = new InvertedIndex();
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-path")) {
				try {
					if ((i + 1) < args.length && ArgumentParser.isValidPath(args[i + 1])) {
						ArgumentParser.isPath(args[i], Paths.get(args[i + 1]), index);
					} 
				} catch (NullPointerException | IOException e) {
					System.out.println("There was an issue finding the direcotry or file: " + args[i + 1]);
				}
				i++;
			} else if (args[i].equals("-index")) {
				try {
					if ((i + 1) < args.length) {
						ArgumentParser.isIndex(args[i], Paths.get(args[i + 1]), index);
					} else {
						TreeJSONWriter.asInvertedIndex(index, Paths.get("index.json"));
					}
				} catch (NullPointerException | IOException e) {
					try {
						TreeJSONWriter.asInvertedIndex(index, Paths.get("index.json"));
					} catch (IOException e1) {
						System.out.println("There was an issue finding the direcotry or file: " + args[i + 1]);
					}
				}
				i++;
			}
		}
	}
}
