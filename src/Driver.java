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
		boolean exact = false;

		if (!argMap.isEmpty()) {
			if (argMap.hasFlag("-path")) {
				try {
					Path path = Paths.get(argMap.getPath("-path"));
					if (argMap.flagPath("-path")) {
						PathChecker.filesInPath(path, index);
					} else {
						System.out.println("There is no path provided. A valid path is needed to build the index.");
					}
				} catch (IOException | NullPointerException e) {
					System.out.println("There was an issue finding the path. A valid path is needed to build the index");
				}
			}
			if (argMap.hasFlag("-index")) {
				try {
					Path path = Paths.get(argMap.getPath("-index"));
					if (argMap.flagPath("-index")) {
						TreeJSONWriter.asInvertedIndex(index, path);
					} else {
						TreeJSONWriter.asInvertedIndex(index, Paths.get("index.json"));
					}
				} catch (IOException | NullPointerException e) {
					try {
						TreeJSONWriter.asInvertedIndex(index, Paths.get("index.json"));
					} catch (IOException x) {
						System.out.println("File not found, index cannot be printed.");
					}
				}
			}
			if (argMap.hasFlag("-search")) {
				if (argMap.flagPath("-search")) {
					for (String file : PathChecker.filesInPath(Paths.get(argMap.getPath("-search")))) {
						QueryParser.queryMap.clear();
						QueryParser.results.clear();
						if (argMap.hasFlag("-exact")) {
							TextFileStemmer.stemQueryFile(index, Paths.get(file), exact);
						} else { 
							TextFileStemmer.stemQueryFile(index, Paths.get(file));
							index.clear();
						}
					}
				}
			}
			if (argMap.flagPath("-exact")) {
				if (argMap.hasFlag("-exact")) {
					exact = true;
				}
			}
			if (argMap.hasFlag("-results")) {
				if (argMap.flagPath("-results")) {
					TreeJSONWriter.asSearchResult(QueryParser.results, Paths.get(argMap.getPath("-results")));
				} else {
					TreeJSONWriter.asSearchResult(QueryParser.results, Paths.get("results.json"));
					QueryParser.results.clear();
				}
			}
			if (argMap.hasFlag("-locations")) {
				if (argMap.flagPath("-locations")) {
					TreeJSONWriter.asLocations(index, Paths.get(argMap.getPath("-locations")));
				} else {
					TreeJSONWriter.asLocations(index, Paths.get("locations.json"));
				}
			}
		}
	}
}
