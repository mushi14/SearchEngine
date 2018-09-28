//import java.io.IOException;
//import java.nio.file.Paths;
//
//public class Driver {
//
//	/**
//	 * Parses the command-line arguments to build and use an in-memory search
//	 * engine from files or the web.
//	 *
//	 * @param args the command-line arguments to parse
//	 * @return 0 if everything went well
//	 * @throws IOException 
//	 */
//	public static void main(String[] args) throws IOException {		
//		InvertedIndex index = new InvertedIndex();
//				
//		for (int i = 0; i < args.length; i++) {
//			try {
//				if (args[i].equals("-path")) {
//					if ((i + 1) < args.length && ArgumentParser.isValidPath(args[i + 1])) {
//						ArgumentParser.addStemmedWords(ArgumentParser.filesInPath(Paths.get(args[i + 1])), index);
//					} 
//					i++;
//				} else if (args[i].equals("-index")) {
//					if ((i + 1) < args.length) {
//						ArgumentParser.isIndex(args[i], Paths.get(args[i + 1]), index);
//					} else {
//						try {
//							TreeJSONWriter.asInvertedIndex(index, Paths.get("index.json"));
//						} catch (NullPointerException | IOException e) {
//							TreeJSONWriter.asInvertedIndex(index, Paths.get("index.json"));
//							System.out.println("There was an issue finding the directory or file: " + args[i + 1]);
//						}
//					}
//					i++;
//				} else if (args[i].equals("-search")) {
//					if ((i + 1) < args.length) {
//						ArgumentParser.isSearch(args[i], Paths.get(args[i + 1]));
//					}
//					i++;
//				} else if (args[i].equals("-exact")) {
//					if ((i + 1) < args.length) {
//
//					} else {
//						
//					}
//				} else if (args[i].equals("results")) {
//					
//				} else if (args[i].equals("locations")) {
//					
//				}
//			} catch (NullPointerException | IOException e) {
//				System.out.println("There was an issue finding the directory or file: " + args[i + 1]);
//			}
//		}
//	}
//}
//






import java.io.IOException;
import java.nio.file.Paths;

public class Driver {

	/**
	 * Parses the command-line arguments to build and use an in-memory search
	 * engine from files or the web.
	 *
	 * @param args the command-line arguments to parse
	 * @return 0 if everything went well
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		
		InvertedIndex index = new InvertedIndex();
		ArgumentMap argMap = new ArgumentMap(args);
		
		try {
			if (argMap.hasFlag("-path")) {
				if (argMap.flagPath("-path")) {
					TextFileStemmer.filesInPath(Paths.get(argMap.getPath("-path")), index);
				}
			}
			if (argMap.hasFlag("-index")) {
				if (argMap.flagPath("-index")) {
					TreeJSONWriter.asInvertedIndex(index, Paths.get(argMap.getPath("-index")));
				} else {
					TreeJSONWriter.asInvertedIndex(index, Paths.get("index.json"));
				}
			}
			if (argMap.hasFlag("-search")) {
				
			}
			if (argMap.hasFlag("-exact")) {
				
			}
			if (argMap.hasFlag("-results")) {
				
			}
			if (argMap.hasFlag("-location")) {
				
			}
		} catch (IOException | NullPointerException e) {
				System.out.println("There was an issue finding the direcotry or file: ");
		}
	}
}
