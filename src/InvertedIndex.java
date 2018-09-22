import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/** Data structure to store file paths and the word positions.
 * 
 */
public class InvertedIndex {

	/** Stores a mapping of files to the positions the words were found in the file.
	 * 
	 */
	private TreeMap<String, TreeMap<String, TreeSet<Integer>>> index;

	/** Initializes the index.
	 * 
	 */
	public InvertedIndex() {
		this.index = new TreeMap<>();
	}
	
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

//	/** Checks to see if a position exists in the path key
//	 * 
//	 * @param path path to the file
//	 * @param position position to look for
//	 * @return true if the position is present in the value set of the path
//	 * 
//	 */
//	public boolean contains(String path, int position) {
//		if (pathsMap.containsKey(path)) {
//			if (pathsMap.get(path).contains(position)) {
//				return true;
//			} else {
//				return false;
//			}
//		} else {
//			return false;
//		}
//	}
//
	/** Prints in the map
	 * 
	 */
	@Override
	public String toString() {
		return this.index.toString();
	}
}
