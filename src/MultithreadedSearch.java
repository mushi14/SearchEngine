import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

public class MultithreadedSearch implements QueryFileParser {

	private final ThreadSafeInvertedIndex index;
	public final Map<String, List<Search>> results;
	private final int threads;

	/**
	 * Constructor for searching the index for queries via multithreading
	 * @param index inverted index to search from
	 */
	public MultithreadedSearch(ThreadSafeInvertedIndex index, int threads) {
		this.index = index;
		this.results = new TreeMap<String, List<Search>>();
		this.threads = threads;
	}

	/**
	 * Stems query file performing partial or exact search and stores the results accordingly
	 * @param index inverted index that contains the words, their locations, and their positions
	 * @param path path of the file
	 * @param exact boolean variable that ensures that an exact search must be performed
	 */
	@Override
	public void stemQueryFile(Path path, boolean exact) {
		WorkQueue queue = new WorkQueue(threads);
		try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line = br.readLine();

			while (line != null) {
				queue.execute(new QueryLineSearch(line, exact));
				line = br.readLine();
			}
		} catch (IOException | NullPointerException e) {
			System.out.println("There was an issue finding the query file: " + path);
		} finally {
			queue.finish();
			queue.shutdown();
		}
	}

	/**
	 * Interface method for searching each specific line of queries separately
	 */
	@Override
	public void searchLine(String line, boolean exact) {
		Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
		Set<String> queries = new TreeSet<>();
		String[] words = TextFileStemmer.parse(line);
		Map<String, List<Search>> local = new TreeMap<String, List<Search>>();

		for (String word : words) {
			word = stemmer.stem(word).toString();
			queries.add(word);
		}

		String queryLine = String.join(" ", queries);
		if (!queries.isEmpty()) {
			synchronized (results) {
				if (results.containsKey(queryLine)) {
					return;
				}
			}

			List<Search> temp;
			if (exact) {
				temp =  index.exactSearch(queries);
				synchronized (results) {
					results.put(queryLine, temp);
				}
			} else {
				local.put(queryLine, index.partialSearch(queries));
				synchronized (results) {
					results.putAll(local);
				}
			}
		}
	}

	/**
	 * Writes the search results to the file path in pretty json format
	 * @param path path to the file to write to
	 * @throws IOException in case there's any problem finding the file
	 */
	@Override
	public void writeJSON(Path path) {
		synchronized (results) {
			TreeJSONWriter.asSearchResult(results, path);
		}
	}

	/**
	 * Searches each line of the query and stores results to results map
	 * @author mushahidhassan
	 *
	 */
	private class QueryLineSearch implements Runnable {
		String line;
		boolean exact;

		/**
		 * Constructor for QueryLineSearch
		 * @param line line of queries to search
		 * @param exact where exact or partial search should be performed
		 */
		public QueryLineSearch(String line, boolean exact) {
			this.line = line;
			this.exact = exact;
		}

		/**
		 * Calls searchLine which searches the index for the qeuries in the line
		 */
		@Override
		public void run() {
			searchLine(line, exact);
		}
	}
}
