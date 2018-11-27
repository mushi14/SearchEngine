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

public class QueryFileParser {

	private static InvertedIndex index;
	private static Map<String, List<Search>> results;

	/**
	 * Stems query file performing partial or exact search and stores the results accordingly
	 * @param index inverted index that contains the words, their locations, and their positions
	 * @param path path of the file
	 * @param exact boolean variable that ensures that an exact search must be performed
	 */
	public static void stemQueryFile(Path path, boolean exact) {
		try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			index = new InvertedIndex();
			results = new TreeMap<String, List<Search>>();
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
	 * Writes the search results to the file path in pretty json format
	 * @param path path to the file to write to
	 * @throws IOException in case there's any problem finding the file
	 */
	public static void writeJSON(Path path) throws IOException {
		TreeJSONWriter.asSearchResult(results, path);
	}
}
