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
import java.util.TreeSet;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

public class QueryFileParser {

	private Path path;

	public QueryFileParser(Path path) {
		this.path = path;
	}

	/**
	 * Stems query file performing partial or exact search and stores the results accordingly
	 * @param index inverted index that contains the words, their locations, and their positions
	 * @param path path of the file
	 * @param exact boolean variable that ensures that an exact search must be performed
	 */
	public Map<String, List<Search>> stemQueryFile(Path path, boolean exact) {
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

				if (!queries.isEmpty()) {
					if (exact == true) {
						String queryLine = String.join(" ", queries);
						if (!results.containsKey(queryLine)) {
							results.put(queryLine, new ArrayList<>());
							for (Search query : index.exactSearch(queries)) {
								results.get(queryLine).add(query);
							}
						}
					} else {
						String queryLine = String.join(" ", queries);
						if (!results.containsKey(queryLine)) {
							results.put(queryLine, new ArrayList<>());
							for (Search query : index.partialSearch(queries)) {
								results.get(queryLine).add(query);
							}
						}
					}
				}

				line = br.readLine();
			}

			return results;

		} catch (IOException | NullPointerException e) {
			System.out.println("There was an issue finding the query file: " + path);
			return Collections.emptyMap();
		}
	}

//	/**
//	 * Stems query file performing partial or exact search and stores the results accordingly
//	 * @param index inverted index that contains the words, their locations, and their positions
//	 * @param path path of the file
//	 * @param exact boolean variable that ensures that an exact search must be performed
//	 */
//	public Map<String, List<Search>> multithreadQueryFile(ThreadSafeInvertedIndex index, boolean exact, 
//			int threads) {
//		try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
//			String line = br.readLine();
//			Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
//			List<Set<String>> temp = new ArrayList<>();
//			Map<String, List<Search>> results = new TreeMap<>();
//
//			while (line != null) {
//				Set<String> queries = new TreeSet<>();
//				String[] words = TextFileStemmer.parse(line);
//				for (String word : words) {
//					word = stemmer.stem(word).toString();
//					queries.add(word);
//				}
//				if (!queries.isEmpty()) {
//					if (exact == true) {
//						temp.add(queries);
//					} else {
////						index.partialSearch(results, queries);
//					}
//				}
//				line = br.readLine();
//			}
//			
//			MultithreadedExactSearch exactSearch = new MultithreadedExactSearch(index, results, threads, temp);
////			System.out.println(exactSearch.results);
//			
//			return results;
//
//		} catch (IOException | NullPointerException e) {
//			System.out.println("There was an issue finding the query file: " + path);
//			return Collections.emptyMap();
//		}
//	}

	/**
	 * Writes the search results to the file path in pretty json format
	 * @param path path to the file to write to
	 * @throws IOException in case there's any problem finding the file
	 */
	public void writeJSON(Path path) throws IOException {
		TreeJSONWriter.asSearchResult(results, path);
	}
}
