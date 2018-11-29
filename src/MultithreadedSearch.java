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

/*
 * TODO There is similar methods and functionality between the multithreaded and
 * singled threaded version of this class. To make that formal, create an interface
 * with the common methods and the implement that interface in both classes. This
 * will enable some clever upcasting in Driver later too.
 * 
 * This class should not need the queries list...
 * 
 * To multithread this class, everything that used to be inside the while loop for
 * going through the query file should now be in a task. It should almost be exactly
 * the same, except the task needs to synchronize access to results.
 */

public class MultithreadedSearch implements QueryFileParser {

	private static ThreadSafeInvertedIndex index;
	private static Map<String, List<Search>> results;

	@Override
	public void stemQueryFile(Path path, boolean exact, InvertedIndex index) {
		try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			WorkQueue queue = new WorkQueue(thraeads);
			MultithreadedSearch.index = (ThreadSafeInvertedIndex) index;
			results = new TreeMap<String, List<Search>>();
			String line = br.readLine();

			while (line != null) {
				queue.execute(new QueryLineSearch(line, exact));
				line = br.readLine();
			}
		} catch (IOException | NullPointerException e) {
			System.out.println("There was an issue finding the query file: " + path);
		}
	}

//	/**
//	 * Stems query file performing partial or exact search and stores the results accordingly
//	 * @param index inverted index that contains the words, their locations, and their positions
//	 * @param path path of the file
//	 * @param exact boolean variable that ensures that an exact search must be performed
//	 */
//	public static void multithreadQueryFile(Path path, boolean exact, int threads,
//			ThreadSafeInvertedIndex index) {
//		try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
//			WorkQueue queue = new WorkQueue(threads);
//			MultithreadedSearch.index = index;
//			results = new TreeMap<String, List<Search>>();
//			String line = br.readLine();
//
//			while (line != null) {
//				queue.execute(new QueryLineSearch(line, exact));
//				line = br.readLine();
//			}
//		} catch (IOException | NullPointerException e) {
//			System.out.println("There was an issue finding the query file: " + path);
//		}
//	}

	/**
	 * Writes the search results to the file path in pretty json format
	 * @param path path to the file to write to
	 * @throws IOException in case there's any problem finding the file
	 */
	public static void writeJSON(Path path) throws IOException {
		TreeJSONWriter.asSearchResult(results, path);
	}

	/**
	 * Searches each line of the query and stores results to results map
	 * @author mushahidhassan
	 *
	 */
	private static class QueryLineSearch implements Runnable {
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
		 * Stems and reads the queries in the query line and performs search on it
		 */
		@Override
		public void run() {
			Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
			Set<String> queries = new TreeSet<>();
			String[] words = TextFileStemmer.parse(line);

			for (String word : words) {
				word = stemmer.stem(word).toString();
				queries.add(word);
			}

			String queryLine = String.join(" ", queries);
			if (!queries.isEmpty() && !results.containsKey(queryLine)) {
				if (exact) {
					synchronized (results) {
						results.put(queryLine, index.exactSearch(queries));
					}
				} else {
					synchronized (results) {
						results.put(queryLine, index.partialSearch(queries));
					}
				}
			}
		}
	}
}
