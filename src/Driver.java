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
		ArgumentMap argMap = new ArgumentMap(args);
		boolean exact = false;

		try {
			if (argMap.hasFlag("-path")) {
				if (argMap.flagPath("-path")) {
					PathChecker.readFiles(PathChecker.filesInPath(Paths.get(argMap.getPath("-path"))), index);
				}
			}
			if (argMap.hasFlag("-index")) {
				if (argMap.flagPath("-index")) {
					TreeJSONWriter.asInvertedIndex(index, Paths.get(argMap.getPath("-index")));
				} else {
					TreeJSONWriter.asInvertedIndex(index, Paths.get("index.json"));
				}
			}
			if (argMap.hasFlag("-search")) {
				if (argMap.flagPath("-search")) {
					for (String file : PathChecker.filesInPath(Paths.get(argMap.getPath("-search")))) {
						QueryParser.queryMap.clear();
						if (argMap.hasFlag("-exact")) {
							TextFileStemmer.stemQueryFile(index, Paths.get(file), exact);
						} else {
							TextFileStemmer.stemQueryFile(index, Paths.get(file));
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
					TreeJSONWriter.asSearchResult(QueryParser.queryMap, Paths.get(argMap.getPath("-results")));
				} else {
					TreeJSONWriter.asSearchResult(QueryParser.queryMap, Paths.get("results.json"));
				}
			}
			if (argMap.hasFlag("-locations")) {
				if (argMap.flagPath("-locations")) {
					TreeJSONWriter.asLocations(index, Paths.get(argMap.getPath("-locations")));
				} else {
					TreeJSONWriter.asLocations(index, Paths.get("locations.json"));
				}
			}
		} catch (IOException | NullPointerException e) {
				System.out.println("There was an issue finding the direcotry or file: ");
		}
	}
}
