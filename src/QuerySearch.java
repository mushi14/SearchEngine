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

public class QuerySearch implements QueryFileParser {

	private final Map<String, List<Search>> results;
	private final InvertedIndex index;

	/**
	 * Constructor, initializes the inverted index
	 * @param index inverted index to search from
	 */
	public QuerySearch(InvertedIndex index) {
		this.index = index;
		this.results = new TreeMap<String, List<Search>>();
	}

	/**
	 * Stems query file performing partial or exact search and stores the results accordingly
	 * @param index inverted index that contains the words, their locations, and their positions
	 * @param path path of the file
	 * @param exact boolean variable that ensures that an exact search must be performed
	 */
	@Override
	public void stemQueryFile(Path path, boolean exact) {
		try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line = br.readLine();

			while (line != null) {
				searchLine(line, exact);
				line = br.readLine();
			}
		} catch (IOException | NullPointerException e) {
			System.out.println("There was an issue finding the query file: " + path);
		}
	}

	/**
	 * Interface method for searching each specific line of queries separately
	 */
	@Override
	public void searchLine(String line, boolean exact) {
		Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
		String[] words = TextFileStemmer.parse(line);
		Set<String> queries = new TreeSet<>();

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