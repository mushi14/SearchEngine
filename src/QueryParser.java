import java.io.IOException;
import java.nio.file.Path;
import java.util.TreeSet;

public class QueryParser {
	
	static TreeSet<String> queries = new TreeSet<String>();

	public static TreeSet<String> parse(Path path) throws IOException {
		PathChecker.readQueryFiles(PathChecker.filesInPath(path), queries);
		return queries;
	}
	
	public static TreeSet<String> getQueries() {
		return queries;
	}	
}
