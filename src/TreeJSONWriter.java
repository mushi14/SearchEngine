import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.TreeMap;
import java.util.TreeSet;

public class TreeJSONWriter {

	/**
	 * Writes several tab <code>\t</code> symbols using the provided
	 * {@link Writer}.
	 *
	 * @param times  the number of times to write the tab symbol
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
	 * @param writer  the writer to use
	 * @throws IOException if the writer encounters any issues
	 */
	public static void quote(String element, Writer writer) throws IOException {
		writer.write('"');
		writer.write(element);
		writer.write('"');
	}

	/**
	 * Writes the set of elements formatted as a pretty JSON array of numbers to
	 * the specified file.
	 *
	 * @param elements the elements to convert to JSON
	 * @param path     the path to the file write to output
	 * @throws IOException if the writer encounters any issues
	 */
	public static void asPositionArray(TreeSet<Integer> elements, Path path) {
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
	 * @param writer   the writer to use
	 * @param level    the initial indentation level
	 * @throws IOException if the writer encounters any issues
	 *
	 * @see Writer#write(String)
	 * @see Writer#append(CharSequence)
	 *
	 * @see System#lineSeparator()
	 *
	 * @see #indent(int, Writer)
	 */
	public static void asPositionArray(TreeSet<Integer> elements, Writer writer, int level) throws IOException {

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
	 * @param path     the path to the file write to output
	 * @throws IOException if the writer encounters any issues
	 *
	 */
	public static void asPathIndex(TreeMap<String, TreeSet<Integer>> elements, Path path) {
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
	 * @param writer   the writer to use
	 * @param level    the initial indentation level
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
	public static void asPathIndex(TreeMap<String, TreeSet<Integer>> elements, Writer writer, int level) throws IOException {
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
	 * Writes the map of String keys and WordIndex values formatted as a pretty JSON object to
	 * the specified file.
	 *
	 * @param elements the elements to convert to JSON
	 * @param path     the path to the file write to output
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
	 * Writes the map of String keys and WordIndex values as a pretty JSON object using the provided
	 * {@link Writer} and indentation level.
	 *
	 * @param elements the elements to convert to JSON
	 * @param writer   the writer to use
	 * @param level    the initial indentation level
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


	public static void asLocations(InvertedIndex index, Path path) {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			
			int level = 0;
			writer.write("{" + System.lineSeparator());
			int size = index.totalLocations().size();
			int count = 0;
			
			for (String location : index.totalLocations().keySet()) {
				count++;
				if (count != size) {
					indent(level + 1, writer);
					quote(location, writer);
					writer.write(": " + index.totalLocations().get(location) + ", " + System.lineSeparator());
				} else {
					indent(level + 1, writer);
					quote(location, writer);
					writer.write(": " + index.totalLocations().get(location) + System.lineSeparator());
				}
			}
			writer.write("}");
		} catch (IOException | NullPointerException e) {
			System.out.println("There was an issue finding the direcotry or file: " + path);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	public static void asSearchResult(InvertedIndex elements, Map<String, Map<String, List<String>>> scores, 
//			Path path) {
//		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
//			asSearchResult(elements, scores, writer, 0);
//		} catch (IOException | NullPointerException e) {
//			System.out.println("There was an issue finding the direcotry or file: " + path);
//		}
//	}
//
//	public static void asSearchResult(InvertedIndex elements, Map<String, Map<String, List<String>>> scores,
//			Writer writer, int level) throws IOException {
//		
//		writer.write("[" + System.lineSeparator());
//		asNestedSearch(scores, writer, level + 1);
//		writer.write("]");
//		
//	}
//	
//	public static void asNestedSearch( Map<String, Map<String, List<String>>> scores, Writer writer, 
//			int level) throws IOException {
//
//		int size = scores.keySet().size();
//		int count = 0;
//		int temp = 0;
//		int temp2 = 0;
//		for (String word : scores.keySet()) {
//			count++;
//			indent(level, writer);
//			writer.write("{" + System.lineSeparator());
//			indent(level + 1, writer);
//			quote("queries", writer);
//			writer.write(": ");
//			quote(word, writer);
//			writer.write("," + System.lineSeparator());
//			indent(level + 1, writer);
//			quote("results", writer);
//			writer.write(": [" + System.lineSeparator());
//			
//			for (String sc : scores.get(word).keySet()) {
//				
//				temp = count;
//				size = scores.get(word).get(sc).size();
//				count++;
//				for (String file : scores.get(word).get(sc)) {
//					temp2 = count;
//					size = scores.get(word).get(sc).size();
//					count++;
//					indent(level + 2, writer);
//					writer.write("{" + System.lineSeparator());
//					indent(level + 3, writer);
//					quote("where", writer);
//					writer.write(": ");
//					quote(file, writer);
//					writer.write("," + System.lineSeparator());
//					indent(level + 3, writer);
//					quote("count", writer);
//					writer.write(": " + Search.matchesMap.get(file) + "," + System.lineSeparator());
//					indent(level + 3, writer);
//					quote("score", writer);
//					writer.write(": " + sc + System.lineSeparator());
//					indent(level + 2, writer);
//					if (count != size) {
//						writer.write("}," + System.lineSeparator());
//					} else {
//						writer.write("}" + System.lineSeparator());
//					}
//				}
//				
//				if (count != size) {
//					writer.write("}," + System.lineSeparator());
//				} else {
//					writer.write("}" + System.lineSeparator());
//				}
//				
//			}
//			size = scores.keySet().size();
//			count = temp;
//			indent(level + 1, writer);
//			writer.write("]" + System.lineSeparator());
//			indent(level, writer);
//			if (count != size) {
//				writer.write("}," + System.lineSeparator());
//			} else {
//				writer.write("}" + System.lineSeparator());
//			}
//		}
//	}
}