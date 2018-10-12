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
					System.out.println("There was an issue finding the path to flag '-path'"
							+ ". A valid path is needed to build the index");
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
				try {
					if (argMap.flagPath("-search")) {
						Path path = Paths.get(argMap.getPath("-search"));
						for (String file : PathChecker.queryFiles(path)) {
							QueryParser.results.clear();
							if (argMap.hasFlag("-exact")) {
								exact = true;
							}
							TextFileStemmer.stemQueryFile(index, Paths.get(file), exact);
						}
					}
				} catch (IOException | NullPointerException e) {
					System.out.println("Unable to open the query file or directory provided. A valid query or "
							+ "directory is needed to search.");
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
