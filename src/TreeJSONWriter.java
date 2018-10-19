import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class TreeJSONWriter {

	/**
	 * Writes several tab <code>\t</code> symbols using the provided
	 * {@link Writer}.
	 *
	 * @param times the number of times to write the tab symbol
	 * @param writer the writer to use
	 * @throws IOException if the writer encounters any issues
	 */
	public static void indent(int times, Writer writer) throws IOException {
		for (int i = 0; i < times; i++) {
			writer.write('\t');
		}
	}

	/**
	 * Writes the element surrounded by quotes using the provided {@link Writer}.
	 *
	 * @param element the element to quote
	 * @param writer the writer to use
	 * @throws IOException if the writer encounters any issues
	 */
	public static void quote(String element, Writer writer) throws IOException {
		writer.write('"');
		writer.write(element);
		writer.write('"');
	}

	/**
	 * Writes the set of ` formatted as a pretty JSON array of numbers to
	 * the specified file.
	 *
	 * @param elements the elements to convert to JSON
	 * @param path the path to the file write to output
	 * @throws IOException if the writer encounters any issues
	 */
	public static void asPositionArray(Set<Integer> elements, Path path) {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asPositionArray(elements, writer, 0);
		} catch (IOException | NullPointerException e) {
			System.out.println("There was an issue finding the direcotry or file: " + path);
		}
	}

	/**
	 * Writes the set of elements formatted as a pretty JSON array of numbers
	 * using the provided {@link Writer} and indentation level.
	 *
	 * @param elements the elements to convert to JSON
	 * @param writer the writer to use
	 * @param level the initial indentation level
	 * @throws IOException if the writer encounters any issues
	 *
	 * @see Writer#write(String)
	 * @see Writer#append(CharSequence)
	 *
	 * @see System#lineSeparator()
	 *
	 * @see #indent(int, Writer)
	 */
	public static void asPositionArray(Set<Integer> elements, Writer writer, int level) throws IOException {

		writer.write('[' + System.lineSeparator());
		
		if (!elements.isEmpty()) {
			int size = elements.size();
			int count = 0;
			for (Integer elem : elements) {
				count++;
				if (count != size) {
					indent(level + 1, writer);
					writer.write(elem.toString() + ',' + System.lineSeparator());
				} else {
					indent(level + 1, writer);
					writer.write(elem.toString() + System.lineSeparator());
				}
			}
		}
		indent(level, writer);
		writer.write(']');
	}

	/**
	 * Writes the map of elements formatted as a pretty JSON object to
	 * the specified file.
	 *
	 * @param elements the elements to convert to JSON
	 * @param path the path to the file write to output
	 * @throws IOException if the writer encounters any issues
	 *
	 */
	public static void asPathIndex(Map<String, Set<Integer>> elements, Path path) {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asPathIndex(elements, writer, 0);
		} catch (IOException | NullPointerException e) {
			System.out.println("There was an issue finding the direcotry or file: " + path);
		}
	}

	/**
	 * Writes the map of elements as a pretty JSON object using the provided
	 * {@link Writer} and indentation level.
	 *
	 * @param elements the elements to convert to JSON
	 * @param writer the writer to use
	 * @param level the initial indentation level
	 * @throws IOException if the writer encounters any issues
	 *
	 * @see Writer#write(String)
	 * @see Writer#append(CharSequence)
	 *
	 * @see System#lineSeparator()
	 *
	 * @see #indent(int, Writer)
	 * @see #quote(String, Writer)
	 */
	public static void asPathIndex(Map<String, Set<Integer>> elements, Writer writer, int level) throws IOException {
		writer.write("{" + System.lineSeparator());
				
		int size = elements.keySet().size();
		int count = 0;
		
		for (String key : elements.keySet()) {
			count++;
			if (count != size) {
				indent(level + 1, writer);
				quote(key, writer);
				writer.write(": ");
				asPositionArray(elements.get(key), writer, level + 1);
				writer.write("," + System.lineSeparator());
			} else {
				indent(level + 1, writer);
				quote(key, writer);
				writer.write(": ");
				asPositionArray(elements.get(key), writer, level + 1);
				writer.write(System.lineSeparator());
			}	
		}
		indent(level, writer);
		writer.write("}");
	}

	/**
	 * Helper method for writing the Inverted Index data structure as a pretty JSON object to
	 * the specified file.
	 *
	 * @param elements the elements to convert to JSON
	 * @param path the path to the file write to output
	 * @throws IOException if the writer encounters any issues
	 *
	 */
	public static void asInvertedIndex(InvertedIndex elements, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asInvertedIndex(elements, writer, 0);
		} catch (IOException | NullPointerException e) {
			System.out.println("There was an issue finding the direcotry or file: " + path);
		}
	}

	/**
	 * Writes the Inverted Index data structure as a pretty JSON object using the provided
	 * {@link Writer} and indentation level.
	 *
	 * @param elements the elements to convert to JSON
	 * @param writer the writer to use
	 * @param level the initial indentation level
	 * @throws IOException if the writer encounters any issues
	 *
	 * @see Writer#write(String)
	 * @see Writer#append(CharSequence)
	 *
	 * @see System#lineSeparator()
	 *
	 * @see #indent(int, Writer)
	 * @see #quote(String, Writer)
	 */
	public static void asInvertedIndex(InvertedIndex elements, Writer writer, int level) throws IOException {
		
		writer.write("{" + System.lineSeparator());
		
		int size = elements.words();
		int count = 0;
		
		for (String key : elements.wordsKeySet()) {
			count++;
			if (count != size) {
				indent(level + 1, writer);
				quote(key, writer);
				writer.write(": ");
				asPathIndex(elements.get(key), writer, level + 1);
				writer.write("," + System.lineSeparator());
			} else {
				indent(level + 1, writer);
				quote(key, writer);
				writer.write(": ");
				asPathIndex(elements.get(key), writer, level + 1);
				writer.write(System.lineSeparator());
			}
		}
		writer.write("}" + System.lineSeparator());
	}


//	/**
//	 * Writes all the locations and the total number of words they contain from the Inverted Index data 
//	 * structure as pretty JSON object using the provided
//	 * {@link Writer} and indentation level.
//	 *
//	 * @param index the inverted index to convert to JSON
//	 * @param writer the writer to use
//	 * @param level the initial indentation level
//	 * @throws IOException if the writer encounters any issues
//	 * @throws NullPointerException if the writer encounters null as the path
//	 */
//	public static void asLocations(InvertedIndex index, Path path) {
//		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
//			
//			int level = 0;
//			writer.write("{" + System.lineSeparator());
//			int size = index.totalLocations().size();
//			int count = 0;
//			
//			for (String location : index.totalLocations().keySet()) {
//				count++;
//				if (count != size) {
//					indent(level + 1, writer);
//					quote(location, writer);
//					writer.write(": " + index.totalLocations().get(location) + ", " + System.lineSeparator());
//				} else {
//					indent(level + 1, writer);
//					quote(location, writer);
//					writer.write(": " + index.totalLocations().get(location) + System.lineSeparator());
//				}
//			}
//			writer.write("}");
//		} catch (IOException | NullPointerException e) {
//			System.out.println("There was an issue finding the directory or file: " + path);
//		}
//	}

	/**
	 * Helper method for writing the map of search results formatted as a pretty JSON object to
	 * the specified file.
	 *
	 * @param results the elements to convert to JSON
	 * @param path the path to the file write to output
	 * @throws IOException if the writer encounters any issues
	 *
	 */
	public static void asSearchResult(TreeMap<String, List<Query>> results, Path path) {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asSearchResult(results, writer, 0);
		} catch (IOException | NullPointerException e) {
			System.out.println("There was an issue finding the direcotry or file: " + path);
		}
	}
	
	/**
	 * Writes the map of search results formatted as a pretty JSON object to specified file
	 * @param results the map that contains all the search results, sorted
	 * @param writer the writer to use
	 * @param level the initial indentation level
	 * @throws IOException if the writer encounters any issues
	 */
	public static void asSearchResult(TreeMap<String, List<Query>> results,
			Writer writer, int level) throws IOException {

		writer.write("[" + System.lineSeparator());
		
		Iterator<String> itr = results.keySet().iterator();
		while (itr.hasNext()) {
			String next = itr.next().toString();
			indent(level + 1, writer);
			writer.write("{" + System.lineSeparator());
			indent(level + 2, writer);
			quote("queries", writer);
			writer.write(": ");
			quote(next, writer);
			writer.write("," + System.lineSeparator());
			indent(level + 2, writer);
			quote("results", writer);
			writer.write(": [" + System.lineSeparator());

			asNestedSearch(next, results, writer, level + 3);

			indent(level + 2, writer);
			writer.write("]" + System.lineSeparator());

			if (itr.hasNext()) {
				indent(level + 1, writer);
				writer.write("}," + System.lineSeparator());
			} else {
				indent(level + 1, writer);
				writer.write("}" + System.lineSeparator());
			}
		}
		writer.write("]");
	}

	/**
	 * Writes the list of sorted queries from each line of the query file
	 * @param results the map that contains all the search results, sorted
	 * @param writer the writer to use
	 * @param level the initial indentation level
	 * @throws IOException if the writer encounters any issues
	 */
	public static void asNestedSearch(String next, TreeMap<String, List<Query>> results, Writer writer, 
			int level) throws IOException {
		
		Iterator<Query> itr = results.get(next).iterator();
		int size = results.get(next).size();
		int count = 0;

		while (itr.hasNext()) {
			count++;
			Query temp = itr.next();

			indent(level, writer);
			writer.write("{" + System.lineSeparator());
			indent(level + 1, writer);
			quote("where", writer);
			writer.write(": ");
			quote(temp.getLocation(), writer);
			writer.write("," + System.lineSeparator());
			indent(level + 1, writer);
			quote("count", writer);
			writer.write(": " + (int) temp.getMatches() + "," + System.lineSeparator());
			indent(level + 1, writer);
			quote("score", writer);
			writer.write(": " + String.valueOf(temp.getScore()) + System.lineSeparator());
			indent(level, writer);
			
			if (count != size) {
				writer.write("}," + System.lineSeparator());
			} else {
				writer.write("}" + System.lineSeparator());
			}
		}
	}
}