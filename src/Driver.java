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
	 */
	public static void main(String[] args) {
		
		InvertedIndex index = new InvertedIndex();
		ArgumentMap argMap = new ArgumentMap(args);

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
					System.out.println("There was an issue finding the path. A valid path is needed to build the index.");
				}
			}

			if (argMap.hasFlag("-index")) {
				try {
					Path path = Paths.get(argMap.getPath("-index"));
					if (argMap.flagPath("-index")) {
						index.writeJSON(path);
					} else {
						index.writeJSON(Paths.get("index.json"));
					}
				} catch (IOException | NullPointerException e) {
					// TODO See if the fix to TreeJSONWriter lets you delete this try/catch
//					try {
//						index.writeJSON(Paths.get("index.json"));
//					} catch (IOException x) {
						System.out.println("File not found, index cannot be printed in json format.");
//					}
				}
			}

//			if (argMap.hasFlag("-search")) {
//				try {
//					if (argMap.flagPath("-search")) {
//						Path path = Paths.get(argMap.getPath("-search"));
//						for (String file : PathChecker.queryFiles(path)) {
//							QueryParser.results.clear();
//							boolean exact = false;
//							if (argMap.hasFlag("-exact")) {
//								exact = true;
//							}
//							TextFileStemmer.stemQueryFile(index, Paths.get(file), exact);
//						}
//					}
//				} catch (IOException | NullPointerException e) {
//					System.out.println("Unable to open the query file or directory provided. A valid query file or "
//							+ "directory is needed to search.");
//				}
//			}
//
//			if (argMap.hasFlag("-results")) {
//				if (argMap.flagPath("-results")) {
//					TreeJSONWriter.asSearchResult(QueryParser.results, Paths.get(argMap.getPath("-results")));
//				} else {
//					TreeJSONWriter.asSearchResult(QueryParser.results, Paths.get("results.json"));
//					QueryParser.results.clear();
//				}
//			}
//
//			if (argMap.hasFlag("-locations")) {
//				if (argMap.flagPath("-locations")) {
//					TreeJSONWriter.asLocations(index, Paths.get(argMap.getPath("-locations")));
//				} else {
//					TreeJSONWriter.asLocations(index, Paths.get("locations.json"));
//				}
//			}
		}
	}
}
