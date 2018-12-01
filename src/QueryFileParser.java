import java.nio.file.Path;

public interface QueryFileParser {

	/**
	 * Interface methods for reading query files and performing search on them
	 * @param path path of the file to read the queries from
	 * @param exact whether exact or partial search should be performed
	 * @param threads how many threads to run on
	 */
	public void stemQueryFile(Path path, boolean exact, int threads);
	public void searchLine(String line, boolean exact);

}
