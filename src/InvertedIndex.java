import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

// TODO Slight formatting fix to Javadoc comments

/** Data structure to store file paths and the word positions.
 * 
 */
public class InvertedIndex {

	/** 
	 * Stores a mapping of files to the positions the words were found in the file.
	 */
	// TODO Use the final word here!
	private TreeMap<String, TreeMap<String, TreeSet<Integer>>> index;

	/** Initializes the index.
	 * 
	 */
	public InvertedIndex() {
		this.index = new TreeMap<>();
	}
	
	// TODO Most of these get methods are breaking encapsulation
	/** Gets the TreeMap of keys paths and values positions associated with the word
	 * 
	 * @param word word inside of the file
	 * @return TreeMap containing path and positions of word 
	 * 
	 */
	public TreeMap<String, TreeSet<Integer>> get(String word) {
		return index.get(word);
	}
	
	/** Gets the TreeSet of positions associated with the path
	 * 
	 * @param word word inside of the file
	 * @param path path of the file
	 * @return TreeSet containing positions associated with the path
	 * 
	 */
	public TreeSet<Integer> get(String word, String path) {
		return index.get(word).get(path);
	}
	
	/** Adds key, value pair to the map
	 * 
	 * @param word word inside of the file as key
	 * @param val TreeMap as values
	 * 
	 */
	public void put(String word, TreeMap<String, TreeSet<Integer>> val) {
		index.put(word, val);
	}
	
	/** Adds key, value pair to the map
	 * 
	 * @param word word inside of the file
	 * @param path path to the file as key
	 * @param val TreeSet as values
	 * 
	 */
	public void put(String word, String path, TreeSet<Integer> val) {
		index.get(word).put(path, val);
	}
	
	/*
	 * TODO 
	 * 
	public void add(String word, String location, int position) {
		safely add this to your index initializing what you need
	}
	
	public void addAll(String[] words, String location)
	public void addAll(String[] words, String location, int position)
	*/
	
	/** Shows all the words in the map
	 * 
	 * @return Returns a set view of all the paths
	 * 
	 */
	public Set<String> wordsKeySet() {
		return index.keySet();
	}
	
	/** Shows all the paths associated with the word in the map
	 * 
	 * @param word word inside of the file
	 * @return Returns a set view of all the paths
	 * 
	 */
	public Set<String> pathsKeySet(String word) {
		return index.get(word).keySet();
	}
	
	/** Shows all the positions associated with a path in the map
	 * 
	 * @param word word inside of the file
	 * @param path path of the file
	 * @return Returns a set view of all the positions associated with the path
	 * 
	 */
	public TreeSet<Integer> positionsSet(String word, String path) {
		return index.get(word).get(path);
	}

	/** Number of words in the the map
	 * 
	 * @return integer size of the number of words in the map
	 * 
	 */
	public int words() {
		return index.size();
	}
	
	
	// TODO Refactor name from "words" to "locations"
	// TODO If the word doesn't exist, should return 0, but here there is a NullPointerException instead
	// TODO Make similar fixes to avoid null pointers for all remaining methods
	/** Number of paths associated with the word in the the map
	 * 
	 * @param word word inside of the file
	 * @return integer size of the number of paths associated with word in the map
	 * 
	 */
	public int words(String word) {
		return index.get(word).size();
	}

	/** Number of positions associated with the path in the the map
	 * 
	 * @param word word inside of the file
	 * @param path path of the file
	 * @return integer size of the number of paths associated with word in the map
	 * 
	 */
	public int words(String word, String path) {
		return index.get(word).get(path).size();
	}
	
	/** Checks to see if the map contains the word
	 * 
	 * @param word word inside the file
	 * @return true if map contains the word
	 * 
	 */
	public boolean containsWord(String word) {
		return index.containsKey(word);
	}
	
	/** Checks to see if the word contains the path
	 * 
	 * @param word word inside the file
	 * @param path path of the file
	 * @return true if word contains the path
	 * 
	 */
	public boolean containsPath(String word, String path) {
		return index.get(word).containsKey(path);
	}

	/** Prints in the map
	 * 
	 */
	@Override
	public String toString() {
		return this.index.toString();
	}
}
