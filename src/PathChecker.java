import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathChecker {

	/** 
	 * Checks to see if the path provided is a text file or a directory. If a valid text
	 * file, then writes to the file. If a directory, then goes through the directory 
	 * to find its text files or directories. 
	 * 
	 * @param path path that is being checked 
	 * @param index inverted index that contains the stemmed words, their files, and their positions
	 * @throws IOException if unable to read to write to file 
	 */
	public static void filesInPath(Path path, InvertedIndex index) throws IOException {
		if (Files.isDirectory(path)) {
			try (DirectoryStream<Path> filePathStream = Files.newDirectoryStream(path)) {
				for (Path file: filePathStream) {
					
					// TODO Just always call filesInPath(file)
					/*
					 * In the recursive call will check if file was a directory and if so open the stream
					 * or a text file and call stemFile
					 */
					
					if (Files.isRegularFile(file)) {
						path = Paths.get(file.toString()); // TODO Remove? Use file directly
						// TODO String name = file.toString().toLowerCase(); and then use name.endsWith(...)
						if (file.toString().toLowerCase().endsWith(".txt") || file.toString().toLowerCase().endsWith(".text")) {
							TextFileStemmer.stemFile(path, index);
						}
					} else if (Files.isDirectory(file)) {
						path = Paths.get(file.toString()); // TODO Remove?
						filesInPath(path, index);
					}
				}
			} catch (NullPointerException e) { // TODO Remove this catch
				System.out.println("There was an issue fiding the directory: " + path);
			}
		} else if (Files.isRegularFile(path)) {
			
			// TODO Check if a text file and call stemFile
			
			TextFileStemmer.stemFile(path, index);
		}
	}
}
