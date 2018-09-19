import java.io.IOException;
import java.nio.file.Paths;
import java.util.TreeMap;

public class Driver {

	// TODO The only method that cannot throw exceptions is Driver.main
	// TODO Make sure the user always sees user-friendly output
	/**
	 * Parses the command-line arguments to build and use an in-memory search
	 * engine from files or the web.
	 *
	 * @param args the command-line arguments to parse
	 * @return 0 if everything went well
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Create an inverted index data structure class
		// TODO Include a triple nested data structure and don't use WordIndex directly
		// TODO But, try to have the same kind of methods as WordIndex
		// TODO TreeMap<String, WordIndex> --> TreeMap<String, TreeMap<String, TreeSet<Integer>>>
		
		/*
		 * TODO
		 * 
		 * if (have the -path flag)
		 * 		trigger building your inverted index
		 * 
		 * if (have the -index flag)
		 * 		trigger write your index
		 */
		
		TreeMap<String, WordIndex> index = new TreeMap<String, WordIndex>();
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-path")) {
				try {
					if ((i + 1) < args.length && ArgumentParser.isValidPath(args[i + 1])) {
						ArgumentParser.isPath(args[i], Paths.get(args[i + 1]), index);
					} 
				} catch (NullPointerException | IOException e) {
//					e.printStackTrace();
					System.out.println("There was an issue finding the direcotry or file: " + args[i + 1]);
				}
				i++;
			} else if (args[i].equals("-index")) {
				
			}
		}
//		TreeMap<String, WordIndex> index = new TreeMap<String, WordIndex>();
//		ArgumentParser.parse(args, index);
	}
}
