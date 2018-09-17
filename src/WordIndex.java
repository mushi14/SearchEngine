import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/** Data structure to store file paths and the word positions.
 * 
 */
public class WordIndex {

	/** Stores a mapping of files to the positions the words were found in the file.
	 * 
	 */
	private TreeMap<String, TreeSet<Integer>> pathsMap;

	/** Initializes the index.
	 * 
	 */
	public WordIndex() {
		this.pathsMap = new TreeMap<>();
	}
	
	/** Gets the TreeSet of positions associated with the path
	 * 
	 * @param path path of the file
	 * @return TreeSet of positions of the words in the file
	 * 
	 */
	public TreeSet<Integer> get(String path) {
		return pathsMap.get(path);
	}
	
	/** Adds key, value pair to the map
	 * 
	 * @param path path of the file as key
	 * @param val TreeMap as values
	 * 
	 */
	public void put(String path, TreeSet<Integer> val) {
		pathsMap.put(path, val);
	}
	
	/** Shows all the paths in the map
	 * 
	 * @return Returns a set view of all the paths
	 * 
	 */
	public Set<String> keySet() {
		return pathsMap.keySet();
	}

	/** Size of the map
	 * 
	 * @return integer size of the the map
	 * 
	 */
	public int paths() {
		return pathsMap.size();
	}

	/** Checks to see if the map contains the path
	 * 
	 * @param path path to the file
	 * @return true if map contains the path
	 * 
	 */
	public boolean contains(String path) {
		return pathsMap.containsKey(path);
	}


	/** Checks to see if a position exists in the path key
	 * 
	 * @param path path to the file
	 * @param position position to look for
	 * @return true if the position is present in the value set of the path
	 * 
	 */
	public boolean contains(String path, int position) {
		if (pathsMap.containsKey(path)) {
			if (pathsMap.get(path).contains(position)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/** Prints in the map
	 * 
	 */
	@Override
	public String toString() {
		return this.pathsMap.toString();
	}
}
