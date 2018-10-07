import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.ArrayList;
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
	public static Map<Double, List<String>> finalQueries =  new TreeMap<>(Collections.reverseOrder());
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
			while(line != null) {
				String[] words = parse(line);
				for (String word : words) {
					word = stemmer.stem(word).toString();
					if (index.containsWord(word)) {
						if (index.containsLocation(word ,path.toString())) {
							index.addPosition(word, path.toString(), position);
							position++;
						} else {
							index.addPath(word, path.toString(), position);
							position++;
						}
					} else {
						index.addWord(word, path.toString(), position);
						position++;
					}
				}
				line = br.readLine();
			}
		} catch (NullPointerException e) {
			System.out.println("There was an issue fiding the text file: " + path);
		}
	}

	public static void stemQueryFile(InvertedIndex index, Path path) {
		try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			
			String line = br.readLine();
			Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
			List<String> seen = new ArrayList<>();

			while (line != null) {
				Set<String> queries = new TreeSet<>();
				String[] words = parse(line);

				for (String word : words) {
					word = stemmer.stem(word).toString();
					if (!seen.contains(word)) {
						seen.add(word);
						queries.add(word);
					}
				}
				

				if (!queries.isEmpty()) {
					QueryParser.addQueries(index, line, queries);
				}
				line = br.readLine();
			}
		} catch (IOException | NullPointerException e) {
			System.out.println("There was an issue finding the query file: " + path);
		}
	}
}
