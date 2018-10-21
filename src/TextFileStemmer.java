import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

public class TextFileStemmer {

	public static final Pattern SPLIT_REGEX = Pattern.compile("(?U)\\p{Space}+");
	public static final Pattern CLEAN_REGEX = Pattern.compile("(?U)[^\\p{Alpha}\\p{Space}]+");
	
	/**
	 * Cleans the text by removing any non-alphabetic characters (e.g. non-letters
	 * like digits, punctuation, symbols, and diacritical marks like the umlaut)
	 * and converting the remaining characters to lowercase.
	 *
	 * @param text the text to clean
	 * @return cleaned text
	 */
	public static String clean(CharSequence text) {
		String cleaned = Normalizer.normalize(text, Normalizer.Form.NFD);
		cleaned = CLEAN_REGEX.matcher(cleaned).replaceAll("");
		return cleaned.toLowerCase();
	}

	/**
	 * Splits the supplied text by whitespace. Does not perform any cleaning.
	 *
	 * @param text the text to split
	 * @return an array of {@link String} objects
	 *
	 * @see #clean(CharSequence)
	 * @see #parse(String)
	 */
	public static String[] split(String text) {
		text = text.trim();
		return text.isEmpty() ? new String[0] : SPLIT_REGEX.split(text);
	}

	/**
	 * Cleans the text and then splits it by whitespace.
	 *
	 * @param text the text to clean and split
	 * @return an array of {@link String} objects
	 *
	 * @see #clean(CharSequence)
	 * @see #parse(String)
	 */
	public static String[] parse(String text) {
		return split(clean(text));
	}

	/**
	 * Reads a file line by line, parses each line into cleaned and stemmed words,
	 * and then writes that line to a new file.
	 *
	 * @param inputFile the input file to parse
	 * @param outputFile the output file to write the cleaned and stemmed words
	 * @throws IOException if unable to read or write to file
	 *
	 * @see #stemLine(String)
	 * @see TextParser#parse(String)
	 */
	public static void stemFile(Path path, InvertedIndex index) throws IOException {
		try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			int position = 1;
			String line = br.readLine();
			Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
			String name = path.toString();
			while(line != null) {
				String[] words = parse(line);
				for (String word : words) {
					word = stemmer.stem(word).toString();
					index.add(word, name, position);
					position++;
				}
				line = br.readLine();
			}
		}
	}


	/**
	 * Stems query file performing partial or exact search and stores the results accordingly
	 * @param index inverted index that contains the words, their locations, and their positions
	 * @param path path of the file
	 * @param exact boolean variable that ensures that an exact search must be performed
	 */
	public static Map<String, List<Search>> stemQueryFile(InvertedIndex index, Path path, boolean exact) {
		try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line = br.readLine();
			Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
			Map<String, List<Search>> results = new TreeMap<>();

			while (line != null) {
				Set<String> queries = new TreeSet<>();
				String[] words = parse(line);
				for (String word : words) {
					word = stemmer.stem(word).toString();
					queries.add(word);
				}
				if (!queries.isEmpty()) {
					if (exact == true) {
						index.exactSearch(results, queries);
					} else {
						index.partialSearch(results, queries);
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
}