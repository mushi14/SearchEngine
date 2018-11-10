import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
		Map<String, List<Search>> results = new TreeMap<>();
		QueryFileParser search = new QueryFileParser(results, index);

		if (!argMap.isEmpty()) {
			if (argMap.hasFlag("-path")) {
				try {
					Path path = argMap.getPath("-path");
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
					Path path = argMap.getPath("-index");
					if (argMap.flagPath("-index")) {
						index.writeIndexJSON(path);
					} else {
						index.writeIndexJSON(Paths.get("index.json"));
					}
				} catch (IOException | NullPointerException e) {
						System.out.println("File not found, index cannot be printed in json format.");
				}
			}

			if (argMap.hasFlag("-search")) {
				try {
					if (argMap.flagPath("-search")) {
						Path path = argMap.getPath("-search");
						boolean exact = false;

						if (argMap.hasFlag("-exact")) {
							exact = true;
						}

						search.stemQueryFile(path, exact);
						
						// TODO search.stemQueryFile(path, argMap.hasFlag("-exact"));
					}
				} catch (NullPointerException e) {
					System.out.println("Unable to open the query file or directory provided. A valid query file or "
							+ "directory is needed to search.");
				}
			}

			if (argMap.hasFlag("-results")) {
				try {
					if (argMap.flagPath("-results")) {
						Path path = argMap.getPath("-results");
						search.writeJSON(path);
						results.clear();
					} else {
						search.writeJSON(Paths.get("results.json"));
						results.clear();
					}
				} catch (IOException | NullPointerException e) {
					System.out.println("File not found, search results cannot be printed in json format.");
				}
			}

			if (argMap.hasFlag("-locations")) {
				try {
					if (argMap.flagPath("-locations")) {
						Path path = argMap.getPath("-locations");
						index.writeLocJSON(path);
					} else {
						index.writeLocJSON(Paths.get("locations.json"));
					}
				} catch (IOException | NullPointerException e) {
						System.out.println("File not found, locations cannot be printed in json format.");
				}
			}
		}
	}
}
