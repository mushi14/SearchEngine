//import java.io.BufferedReader;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.text.Normalizer;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.TreeMap;
//import java.util.TreeSet;
//import java.util.regex.Pattern;
//
//import opennlp.tools.stemmer.Stemmer;
//import opennlp.tools.stemmer.snowball.SnowballStemmer;
//
//public class TextFileStemmer {
//
//	public static final Pattern SPLIT_REGEX = Pattern.compile("(?U)\\p{Space}+");
//	public static final Pattern CLEAN_REGEX = Pattern.compile("(?U)[^\\p{Alpha}\\p{Space}]+");
//
//	/**
//	 * Returns a list of cleaned and stemmed words parsed from the provided line.
//	 * Uses the English {@link SnowballStemmer.ALGORITHM} for stemming.
//	 *
//	 * @param line the line of words to clean, split, and stem
//	 * @return list of cleaned and stemmed words
//	 *
//	 * @see SnowballStemmer
//	 * @see SnowballStemmer.ALGORITHM#ENGLISH
//	 * @see #stemLine(String, Stemmer)
//	 */
//	
//	/**
//	 * Cleans the text by removing any non-alphabetic characters (e.g. non-letters
//	 * like digits, punctuation, symbols, and diacritical marks like the umlaut)
//	 * and converting the remaining characters to lowercase.
//	 *
//	 * @param text the text to clean
//	 * @return cleaned text
//	 */
//	public static String clean(CharSequence text) {
//		String cleaned = Normalizer.normalize(text, Normalizer.Form.NFD);
//		cleaned = CLEAN_REGEX.matcher(cleaned).replaceAll("");
//		return cleaned.toLowerCase();
//	}
//	
//	
//	/**
//	 * Splits the supplied text by whitespace. Does not perform any cleaning.
//	 *
//	 * @param text the text to split
//	 * @return an array of {@link String} objects
//	 *
//	 * @see #clean(CharSequence)
//	 * @see #parse(String)
//	 */
//	public static String[] split(String text) {
//		text = text.trim();
//		return text.isEmpty() ? new String[0] : SPLIT_REGEX.split(text);
//	}
//
//	
//	/**
//	 * Cleans the text and then splits it by whitespace.
//	 *
//	 * @param text the text to clean and split
//	 * @return an array of {@link String} objects
//	 *
//	 * @see #clean(CharSequence)
//	 * @see #parse(String)
//	 */
//	public static String[] parse(String text) {
//		return split(clean(text));
//	}
//	
//	/**
//	 * Returns a list of cleaned and stemmed words parsed from the provided line.
//	 * Uses the English {@link SnowballStemmer.ALGORITHM} for stemming.
//	 *
//	 * @param line the line of words to clean, split, and stem
//	 * @return list of cleaned and stemmed words
//	 *
//	 * @see SnowballStemmer
//	 * @see SnowballStemmer.ALGORITHM#ENGLISH
//	 * @see #stemLine(String, Stemmer)
//	 */
//	public static List<String> stemLine(String line) {
//		return stemLine(line, new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH));
//	}
//
//	/**
//	 * Returns a list of cleaned and stemmed words parsed from the provided line.
//	 *
//	 * @param line the line of words to clean, split, and stem
//	 * @param stemmer the stemmer to use
//	 * @return list of cleaned and stemmed words
//	 *
//	 * @see Stemmer#stem(CharSequence)
//	 * @see TextParser#parse(String)
//	 */
//	public static List<String> stemLine(String line, Stemmer stemmer) {
//		String[] words = parse(line);
//		List<String> stemmedWords = new ArrayList<>();
//		for (String word : words) {
//			stemmedWords.add(stemmer.stem(word).toString());
//		}
//		return stemmedWords;
//	}
//
//	/**
//	 * Reads a file line by line, parses each line into cleaned and stemmed words,
//	 * and then writes that line to a new file.
//	 *
//	 * @param inputFile the input file to parse
//	 * @param outputFile the output file to write the cleaned and stemmed words
//	 * @throws IOException if unable to read or write to file
//	 *
//	 * @see #stemLine(String)
//	 * @see TextParser#parse(String)
//	 */
//	public static void stemFile(Path path, InvertedIndex index) throws IOException {
//		try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
//			int count = 1;
//			String line = br.readLine();
//			while(line != null) {
//				for (String word : stemLine(line)) {
//					if (index.containsWord(word)) {
//						if (index.containsPath(word ,path.toString())) {
//							index.get(word, path.toString()).add(count);
//							count++;
//						} else {
//							index.put(word, path.toString(), new TreeSet<Integer>());
//							index.get(word, path.toString()).add(count);
//							count++;
//						}
//					} else {
//						index.put(word, new TreeMap<String, TreeSet<Integer>>());
//						index.put(word, path.toString(), new TreeSet<Integer>());
//						index.get(word, path.toString()).add(count);
//						count++;
//					}
//				}
//				line = br.readLine();
//			}
//		} catch (NullPointerException e) {
//			System.out.println("There was an issue finding the directory or file: " + path);
//		}
//	}
//	
//	public static void stemQueryFile(Path path, TreeSet<String> queries) throws IOException {
//		try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
//			String line = br.readLine();
//			while(line != null) {
//				for (String word : stemLine(line)) {
//					queries.add(word);
//				}
//				line = br.readLine();
//			}
//		} catch (NullPointerException e) {
//			System.out.println("There was an issue finding the query file: " + path);
//		}
//	}
//}






import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

public class TextFileStemmer {

	public static final Pattern SPLIT_REGEX = Pattern.compile("(?U)\\p{Space}+");
	public static final Pattern CLEAN_REGEX = Pattern.compile("(?U)[^\\p{Alpha}\\p{Space}]+");
	
	/** 
	 * Checks to see if the path provided is a text file or a directory. If a valid text
	 * file, then writes to the file. If a directory, then goes through the directory 
	 * to find its text files or directories. 
	 * 
	 * @param path path that is being checked 
	 * @param index inverted index that contains the stemmed words, their files, and their positions
	 * @throws IOException if unable to read to write to file 
	 */
	public static void filesInPath(Path path, InvertedIndex index) throws IOException {
		if (Files.isDirectory(path)) {
			try (DirectoryStream<Path> filePathStream = Files.newDirectoryStream(path)) {
				for (Path file: filePathStream) {
					if (Files.isRegularFile(file)) {
						path = Paths.get(file.toString());
						if (file.toString().toLowerCase().endsWith(".txt") || file.toString().toLowerCase().endsWith(".text")) {
							TextFileStemmer.stemFile(path, index);
						}
					} else if (Files.isDirectory(file)) {
						path = Paths.get(file.toString());
						filesInPath(path, index);
					}
				}
			} catch (NullPointerException e) {
				System.out.println("There was an issue fiding the directory: " + path);
			}
		} else if (Files.isRegularFile(path)) {
			TextFileStemmer.stemFile(path, index);
		}
	}
	
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
		String[] words = parse(line);
		List<String> stemmedWords = new ArrayList<>();
		for (String word : words) {
			stemmedWords.add(stemmer.stem(word).toString());
		}
		return stemmedWords;
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
						if (index.containsPath(word ,path.toString())) {
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
}

