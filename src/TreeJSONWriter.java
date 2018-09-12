import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class TreeJSONWriter {

	
	public static void indent(int times, Writer writer) throws IOException {
		for (int i = 0; i < times; i++) {
			writer.write('\t');
		}
	}

	
	public static void quote(String element, Writer writer) throws IOException {
		writer.write('"');
		writer.write(element);
		writer.write('"');
	}

	
	public static String asPositionArray(HashSet<Integer> elements) {
		try {
			StringWriter writer = new StringWriter();
			asPositionArray(elements, writer, 0);
			return writer.toString();
		}
		catch (IOException e) {
			return null;
		}
	}

	
	public static void asPositionArray(HashSet<Integer> elements, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path,
				StandardCharsets.UTF_8)) {
			asPositionArray(elements, writer, 0);
		}
	}

	
	public static void asPositionArray(HashSet<Integer> elements, Writer writer, int level) throws IOException {

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

	
	public static String asWordIndex(WordIndex elements) {
		try {
			StringWriter writer = new StringWriter();
			asWordIndex(elements, writer, 0);
			return writer.toString();
		}
		catch (IOException e) {
			return null;
		}
	}

	
	public static void asWordIndex(WordIndex elements,
			Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path,
				StandardCharsets.UTF_8)) {
			asWordIndex(elements, writer, 0);
		}
	}

	
	public static void asWordIndex(WordIndex elements,
			Writer writer, int level) throws IOException {

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
	
	
	public static String asInvertedIndex(TreeMap<String, WordIndex> elements) {
		try {
			StringWriter writer = new StringWriter();
			asInvertedIndex(elements, writer, 0);
			return writer.toString();
		}
		catch (IOException e) {
			return null;
		}
	}

	
	public static void asInvertedIndex(TreeMap<String, WordIndex> elements,
			Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path,
				StandardCharsets.UTF_8)) {
			asInvertedIndex(elements, writer, 0);
		}
	}
	
	
	public static void asInvertedIndex(TreeMap<String, WordIndex> elements,
			Writer writer, int level) throws IOException {
		
		writer.write("{" + System.lineSeparator());
		
		int size = elements.size();
		int count = 0;
		
		for (String key : elements.keySet()) {
			count++;
			if (count != size) {
				indent(level + 1, writer);
				quote(key, writer);
				writer.write(": ");
				asWordIndex(elements.get(key), writer, level + 1);
				writer.write("," + System.lineSeparator());
			} else {
				indent(level + 1, writer);
				quote(key, writer);
				writer.write(": ");
				asWordIndex(elements.get(key), writer, level + 1);
				writer.write(System.lineSeparator());
			}
		}
		writer.write("}" + System.lineSeparator());
	}

	
//	public static void main(String[] args) {
//		
//		var test = new TreeMap<String, WordIndex>();
//		
//		test.put("capybara", new WordIndex());
//		test.get("capybara").put("input/mammals.txt", new HashSet());
//		test.get("capybara").get("input/mammals.txt").add(11);
//		
//		test.put("platypus", new WordIndex());
//		test.get("platypus").put("input/mammals.txt", new HashSet());
//		test.get("platypus").get("input/mammals.txt").add(8);
//		test.get("platypus").get("input/mammals.txt").add(3);
//		
//		test.get("platypus").put("input/dangerous/venomous.txt", new HashSet());
//		test.get("platypus").get("input/dangerous/venomous.txt").add(2);
//
//		System.out.println(asInvertedIndex(test));
//	}
}
