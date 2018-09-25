import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class QueryParser {

	public static List<String> queryFiles(Path path) throws IOException {
		return ArgumentParser.filesInPath(path);
	}
	
	public static void queryParse(Path path, InvertedIndex index) throws IOException {
		ArgumentParser.addStemmedWords(queryFiles(path), index); 
	}
}
