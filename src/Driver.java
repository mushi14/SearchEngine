import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

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
		ThreadSafeInvertedIndex threadSafeIndex = new ThreadSafeInvertedIndex();
		ArgumentMap argMap = new ArgumentMap(args);
		QuerySearch search = new QuerySearch(index);
		MultithreadedSearch multiSearch = new MultithreadedSearch(threadSafeIndex);
		WebCrawler crawl = new WebCrawler(threadSafeIndex, argMap.getThreads("-threads", 5));

		boolean multithread = argMap.hasFlag("-threads");

		if (!argMap.isEmpty()) {

			if (argMap.hasFlag("-url")) {
				try {

					if (argMap.flagPath("-url")) {
						multithread = true;
						URL url = argMap.getURL("-url");
						String html = HTMLFetcher.fetchHTML(url, 3);
						Map<String, List<String>> headers = HttpsFetcher.fetchURL(url);

						if (HTMLFetcher.getStatusCode(headers) == 200) {
							crawl.start(url, html, argMap.getLimit("-limit", 50), 3);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (argMap.hasFlag("-path")) {
				try {
					Path path = argMap.getPath("-path");

					if (argMap.flagPath("-path")) {

						if (multithread) {
							MultithreadedPathChecker.filesInPath(path, argMap.getThreads("-threads", 5), threadSafeIndex);
						} else {
							PathChecker.filesInPath(path, index);
						}
					} else {
						System.out.println("There is no path provided. A valid path is needed to build the index.");
					}
				} catch (IOException | NullPointerException e) {
					System.out.println("There was an issue finding the path. A valid path is needed to build the index.");
				}
			}

			if (argMap.hasFlag("-index")) {
				try {
					Path path = argMap.getPath("-index", Paths.get("index.json"));
					if (multithread) {
						threadSafeIndex.writeIndexJSON(path);
					} else {
						index.writeIndexJSON(path);
					}
				} catch (IOException | NullPointerException e) {
						System.out.println("File not found, index cannot be printed in json format.");
				}
			}

			if (argMap.hasFlag("-search")) {
				try {
					Path path = argMap.getPath("-search");
					if (argMap.flagPath("-search")) {

						if (multithread) {
							multiSearch.stemQueryFile(path, argMap.hasFlag("-exact"), argMap.getThreads("-threads", 5));
						} else {
							search.stemQueryFile(path, argMap.hasFlag("-exact"), 0);
						}
					}
				} catch (NullPointerException e) {
					System.out.println("Unable to open the query file or directory provided. A valid query file or "
							+ "directory is needed to search.");
				}
			}

			if (argMap.hasFlag("-results")) {
				try {
					Path path = argMap.getPath("-results", Paths.get("results.json"));

					if (multithread) {
						multiSearch.writeJSON(path);
					} else {
						search.writeJSON(path);
					}
				} catch (IOException | NullPointerException e) {
					System.out.println("File not found, search results cannot be printed in json format.");
				}
			}

			if (argMap.hasFlag("-locations")) {
				try {
					Path path = argMap.getPath("-locations", Paths.get("locations.json"));
					if (multithread) {
						threadSafeIndex.writeLocJSON(path);
					} else {
						index.writeLocJSON(path);
					}
				} catch (IOException | NullPointerException e) {
						System.out.println("File not found, locations cannot be printed in json format.");
				}
			}

			try {
				SearchServer server = new SearchServer(threadSafeIndex);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}