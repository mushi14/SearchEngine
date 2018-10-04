import java.io.IOException;
import java.nio.file.Paths;
import java.util.TreeSet;

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
		TreeSet<String> queries = QueryParser.getQueries();
		
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
					QueryParser.parse(index, Paths.get(argMap.getPath("-search")));
				}
			}
			if (argMap.hasFlag("-exact")) {
				if (argMap.flagPath("-exact")) {
					Search.exactSearch(index, QueryParser.parse(index, Paths.get(argMap.getPath("-exact"))));
				}
			}
//			if (argMap.hasFlag("-results")) {
//				if (argMap.flagPath("-results")) {
//					TreeJSONWriter.asSearchResult(index, Paths.get(argMap.getPath("-results")), queries, 
//							Search.score(index, queries));
//				} else {
//					TreeJSONWriter.asSearchResult(index, Paths.get("results.json"), QueryParser.getQueries(), 
//							Search.score(index, QueryParser.getQueries()));
//				}
//			}
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
		
//		System.out.println(index);
		TreeSet<String> q = new TreeSet<>();
		q.add("lori");
		q.add("narwhal");
		q.add("tarsier");
		
//		TreeJSONWriter.asSearchResult(index, Paths.get("/Users/mushahidhassan/Desktop/result.json"), q, Search.score(index, q));
// 		System.out.println(Search.score(index, q));
//		try {
//			System.out.println(TextFileStemmer.stemQueryFile(index, Paths.get(argMap.getPath("-search")), queries));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}
}
