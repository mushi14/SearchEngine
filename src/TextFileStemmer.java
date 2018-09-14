import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

public class TextFileStemmer {

	/**
	 * Returns a list of cleaned and stemmed words parsed from the provided line.
	 * Uses the English {@link SnowballStemmer.ALGORITHM} for stemming.
	 *
	 * @param line the line of words to clean, split, and stem
	 * @return list of cleaned and stemmed words
	 *
	 * @see SnowballStemmer
	 * @see SnowballStemmer.ALGORITHM#ENGLISH
	 * @see #stemLine(String, Stemmer)
	 */
	public static List<String> stemLine(String line) {
		// This is provided for you.
		return stemLine(line, new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH));
	}

	/**
	 * Returns a list of cleaned and stemmed words parsed from the provided line.
	 *
	 * @param line the line of words to clean, split, and stem
	 * @param stemmer the stemmer to use
	 * @return list of cleaned and stemmed words
	 *
	 * @see Stemmer#stem(CharSequence)
	 * @see TextParser#parse(String)
	 */
	public static List<String> stemLine(String line, Stemmer stemmer) {
		String[] words = TextParser.parse(line);
		List<String> stemmed_words = new ArrayList<>();
		for (String word : words) {
			stemmed_words.add(stemmer.stem(word).toString());
		}
		return stemmed_words;
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
	public static void stemFile(Path path, TreeMap<String, WordIndex> index) throws IOException {
		try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			int count = 1;
			String line = br.readLine();
			while(line != null) {
				for (String word : stemLine(line)) {
					if (index.containsKey(word)) {
						if (index.get(word).contains(path.toString())) {
							index.get(word).get(path.toString()).add(count);
							count++;
						} else {
							index.get(word).put(path.toString(), new HashSet<Integer>());
							index.get(word).get(path.toString()).add(count);
							count++;
						}
					} else {
						index.put(word, new WordIndex());
						index.get(word).put(path.toString(), new HashSet<Integer>());
						index.get(word).get(path.toString()).add(count);
						count++;
					}
				}
				line = br.readLine();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Uses {@link #stemFile(Path, Path)} to stem a single hard-coded file. Useful
	 * for development.
	 *
	 * @param args unused
	 * @throws IOException
	 */
//	public static void main(String[] args) throws IOException {
//		Path inputPath = Paths.get("test", "words.tExT");
//		Path outputPath = Paths.get("out", "words.tExT");
//
//		Files.createDirectories(Paths.get("out"));
//
//		stemFile(inputPath, outputPath);
//	}
}
