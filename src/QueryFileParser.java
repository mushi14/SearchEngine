import java.nio.file.Path;

public interface QueryFileParser {

	/**
	 * Reads, stems, cleans, and parses the queries line by line fro mthe given path
	 * @param path the path to read the queries from
	 * @param exact whether search should be exact or partial
	 */
	public void stemQueryFile(Path path, boolean exact);

	/**
	 * Searches each line of the queries separately by parsing each word from the line and performing
	 * search on it
	 * @param line the line of the queries to search
	 * @param exact whether search should be exact or partial
	 */
	public void searchLine(String line, boolean exact);

}
