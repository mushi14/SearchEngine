import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

public class QueryFileParser {

	private final InvertedIndex index;
	private final ThreadSafeInvertedIndex threadSafeInvertedIndex;
	private final Map<String, List<Search>> results;

	/** 
	 * Constructor for QueryFileParser
	 * @param results Map of results to store the search data in
	 * @param index inverted index to retrive the information from
	 */
	public QueryFileParser(Map<String, List<Search>> results, InvertedIndex index, 
			ThreadSafeInvertedIndex threadSafeInvertedIndex) {
		this.results = new TreeMap<String, List<Search>>();
		this.index = index;
		this.threadSafeInvertedIndex = threadSafeInvertedIndex;
	}

	/**
	 * Stems query file performing partial or exact search and stores the results accordingly
	 * @param index inverted index that contains the words, their locations, and their positions
	 * @param path path of the file
	 * @param exact boolean variable that ensures that an exact search must be performed
	 */
	public void stemQueryFile(Path path, boolean exact) {
		try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line = br.readLine();
			Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);

			while (line != null) {
				Set<String> queries = new TreeSet<>();
				String[] words = TextFileStemmer.parse(line);

				for (String word : words) {
					word = stemmer.stem(word).toString();
					queries.add(word);
				}
				

				String queryLine = String.join(" ", queries);
				if (!queries.isEmpty() && !results.containsKey(queryLine)) {
					if (exact == true) {
						results.put(queryLine, index.exactSearch(queries));
					} else {
						results.put(queryLine, index.partialSearch(queries));
					}
				}

				line = br.readLine();
			}
		} catch (IOException | NullPointerException e) {
			System.out.println("There was an issue finding the query file: " + path);
		}
	}

	/**
	 * Stems query file performing partial or exact search and stores the results accordingly
	 * @param index inverted index that contains the words, their locations, and their positions
	 * @param path path of the file
	 * @param exact boolean variable that ensures that an exact search must be performed
	 */
	public Map<String, List<Search>> multithreadQueryFile(Path path, boolean exact, int threads) {
		try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line = br.readLine();
			Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
			List<Set<String>> queries = new ArrayList<Set<String>>();

			while (line != null) {
				Set<String> temp = new TreeSet<>();
				String[] words = TextFileStemmer.parse(line);

				for (String word : words) {
					word = stemmer.stem(word).toString();
					temp.add(word);
				}

				queries.add(temp);
				line = br.readLine();
			}

			if (!queries.isEmpty()) {
				MultithreadedSearch search = new MultithreadedSearch(threadSafeInvertedIndex, 
						results, threads, queries, exact);
			}

			return results;

		} catch (IOException | NullPointerException e) {
			System.out.println("There was an issue finding the query file: " + path);
			return Collections.emptyMap();
		}
	}

	/**
	 * Writes the search results to the file path in pretty json format
	 * @param path path to the file to write to
	 * @throws IOException in case there's any problem finding the file
	 */
	public void writeJSON(Path path) throws IOException {
		TreeJSONWriter.asSearchResult(results, path);
	}
}
