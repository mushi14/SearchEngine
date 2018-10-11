import java.io.IOException;
import java.nio.file.Path;
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
		ArgumentMap argMap = new ArgumentMap(args);

		if (argMap.hasFlag("-path")) {
			Path path = Paths.get(argMap.getPath("-path"));
			try {
				if (argMap.flagPath("-path")) {
					PathChecker.filesInPath(path, index);
				}
			} catch (IOException | NullPointerException e) {
				System.out.printf("There was an issue finding the path %s. %s is needed to build the index", path);
			}
		}
//		if (argMap.hasFlag("-index")) {
//			Path path = Paths.get(argMap.getPath("-index"));
//			try {
//				if (argMap.flagPath("-index")) {
//					TreeJSONWriter.asInvertedIndex(index, path);
//				} else {
//					TreeJSONWriter.asInvertedIndex(index, Paths.get("index.json"));
//				}
//			} catch (IOException | NullPointerException e) {
//				System.out.printf("There was a problem finding the file %s. The results of the index"
//						+ "will be printed to 'index.json'", path);
//			}
//		}
		/* TODO
		if (argMap.hasFlag("-path")) {
			Path path = 
			try {
				if (argMap.flagPath("-path")) {
					PathChecker.filesInPath(Paths.get(argMap.getPath("-path")), index);
				}
			}
			catch (IOException e) {
				System.out.println("There was an issue building the index from path: " + path);
			}
		}
		
		if (argMap.hasFlag("-index")) {
			if (argMap.flagPath("-index")) {
				TreeJSONWriter.asInvertedIndex(index, Paths.get(argMap.getPath("-index")));
			} else {
				TreeJSONWriter.asInvertedIndex(index, Paths.get("index.json"));
			}
		}
		*/
	}
}
