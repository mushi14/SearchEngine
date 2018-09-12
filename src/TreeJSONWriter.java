import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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

	
	public static String asArray(TreeSet<Integer> elements) {
		try {
			StringWriter writer = new StringWriter();
			asArray(elements, writer, 0);
			return writer.toString();
		}
		catch (IOException e) {
			return null;
		}
	}

	
	public static void asArray(TreeSet<Integer> elements, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path,
				StandardCharsets.UTF_8)) {
			asArray(elements, writer, 0);
		}
	}

	
	public static void asArray(TreeSet<Integer> elements, Writer writer, int level) throws IOException {

		writer.write('[');
		writer.write(System.lineSeparator());
		
		if (!elements.isEmpty()) {
			int last = elements.last();
			for (Integer elem : elements) {
				if (elem != last) {
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

	
	public static String asObject(TreeMap<String, Integer> elements) {
		try {
			StringWriter writer = new StringWriter();
			asObject(elements, writer, 0);
			return writer.toString();
		}
		catch (IOException e) {
			return null;
		}
	}

	
	public static void asObject(TreeMap<String, Integer> elements, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path,
				StandardCharsets.UTF_8)) {
			asObject(elements, writer, 0);
		}
	}

	
	public static void asObject(TreeMap<String, Integer> elements, Writer writer, 
			int level) throws IOException {

		writer.write("{");
		writer.write(System.lineSeparator());
		
		int size = elements.keySet().size();
		int count = 0;
		
		for (String key : elements.keySet()) {
			count++;
			if (count != size) {
				indent(level + 1, writer);
				quote(key, writer);
				writer.write(": " + elements.get(key) + "," + System.lineSeparator());
			} else {
				indent(level + 1, writer);
				quote(key, writer);
				writer.write(": " + elements.get(key) + System.lineSeparator());
			}
		}
		writer.write("}");
	}

	
	public static String asNestedObject(TreeMap<String, TreeSet<Integer>> elements) {
		try {
			StringWriter writer = new StringWriter();
			asNestedObject(elements, writer, 0);
			return writer.toString();
		}
		catch (IOException e) {
			return null;
		}
	}

	
	public static void asNestedObject(TreeMap<String, TreeSet<Integer>> elements,
			Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path,
				StandardCharsets.UTF_8)) {
			asNestedObject(elements, writer, 0);
		}
	}

	
	public static void asNestedObject(TreeMap<String, TreeSet<Integer>> elements,
			Writer writer, int level) throws IOException {

		writer.write("{");
		writer.write(System.lineSeparator());
				
		int size = elements.keySet().size();
		int count = 0;
		
		for (String key : elements.keySet()) {
			count++;
			if (count != size) {
				indent(level + 1, writer);
				quote(key, writer);
				writer.write(": ");
				asArray(elements.get(key), writer, level + 1);
				writer.write("," + System.lineSeparator());
			} else {
				indent(level + 1, writer);
				quote(key, writer);
				writer.write(": ");
				asArray(elements.get(key), writer, level + 1);
				writer.write(System.lineSeparator());
			}	
		}
		writer.write("}");
	}
}
