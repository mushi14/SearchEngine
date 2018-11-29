import java.nio.file.Path;

public interface QueryFileParser {

	public void stemQueryFile(Path path, boolean exact, int threads, InvertedIndex index);
	public void searchLine(String line, boolean exact);

}
