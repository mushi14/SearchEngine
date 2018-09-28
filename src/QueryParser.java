import java.io.IOException;
import java.nio.file.Path;
import java.util.TreeSet;

public class QueryParser {
	
	static TreeSet<String> queries = new TreeSet<String>();

	public static void parse(Path path) throws IOException {
		TextFileStemmer.stemQueryFile(path, queries);
	}
	
	public static TreeSet<String> getQueries() {
		return queries;
	}	
}
