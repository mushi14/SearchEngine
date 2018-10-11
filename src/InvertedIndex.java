import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/** 
 * Data structure to store file paths and the word positions.
 */
public class InvertedIndex {

	/** 
	 * Stores a mapping of files to the positions the words were found in the file.
	 */
	private final Map<String, Map<String, TreeSet<Integer>>> index;

	/**
	 *  Initializes the index.
	 */
	public InvertedIndex() {
		index = new TreeMap<>();
	}

	/** 
	 * Gets the TreeMap of keys paths and values positions associated with the word
	 * 
	 * @param word word inside of the file
	 * @return TreeMap containing path and positions of word 
	 */
	public Map<String, Set<Integer>> get(String word) {
		if (index.containsKey(word)) {
			return Collections.unmodifiableMap(index.get(word));
		} else {
			return Collections.emptyMap();
		}
	}
	
	/* TODO To avoid the copying, maybe this approach:
	public Set<String> getLocations(String word) {
		if (index.containsKey(word)) {
			return Collections.unmodifiableSet(index.get(word).keySet());
		}
		else {
			return Collections.EMPTY_SET;
		}
	}
	
	Do this also for getWords() that returns the index keyset safely.
	*/

//	/** 
//	 * Gets the TreeSet of positions associated with the path
//	 * 
//	 * @param word word inside of the file
//	 * @param path path of the file
//	 * @return TreeSet containing positions associated with the path
//	 */
//	public TreeSet<Integer> get(String word, String path) {
//		TreeSet<Integer> temp = new TreeSet<>();
//		if (index.containsKey(word)) {
//			temp = index.get(word).get(path);
//		}
//		return new TreeSet<Integer>(temp);
//	}

	/** 
	 * Adds the key word to with value TreeMap of key path and value positions
	 * 
	 * @param word word inside of the file
	 * @param path path of the file
	 * @param position position the word appears in the file
	 */
	public void addWord(String word, String path, int position) {
		TreeMap<String, TreeSet<Integer>> value = new TreeMap<>();
		value.put(path, new TreeSet<Integer>());
		value.get(path).add(position);
		index.put(word, value);
	}

	/**
	 * Adds a key path and value position to the already existing word in the TreeMap
	 * 
	 * @param word word inside of the file
	 * @param path path of the file
	 * @param position position the word appears in the file
	 */
	public void addPath(String word, String path, int position) {
		TreeSet<Integer> value = new TreeSet<>();
		value.add(position);
		index.get(word).put(path, value);
	}

	/**
	 * Adds the position in which the word appears in the path
	 * 
	 * @param word word inside of the file
	 * @param path path of the file
	 * @param position position the word appears in the file
	 */
	public void addPosition(String word, String path, int position) {
		index.get(word).get(path).add(position);
	}
	
	/* TODO
	 * collapse addWord, addPath, addPosition into one add method....
	public void add(String word, String path, int position) {
		here we check, do we need to add the word?
		do we need to add the path?
		do we need to add the position?
	}
	*/

	/*
	public void addAll(String[] words, String location)
	public void addAll(String[] words, String location, int position)
	*
	*
	* Not sure about this one */
	/**
	 * Adds a list of words to the index given they all have the same path
	 * @param words list of words to add
	 * @param location path of the file
	 */
	public void addAll(String[] words, String location) {
		int position = 0;
		for (String word : words) {
			position++;
			this.addPath(word, location, position); // TODO Call new add method
		}
	}

	/**
	 * Shows all the words in the map
	 *
	 * @return Returns a set view of all the paths
	 */
	public Set<String> wordsKeySet() {
		return Collections.unmodifiableSet(index.keySet());
	}

	/**
	 * Shows all the paths associated with the word in the map
	 *
	 * @param word word inside of the file
	 * @return Returns a set view of all the paths
	 */
	public Map<String, Set<Integer>> pathsKeySet(String word, String path) {
		if (index.containsKey(word)) {
			if (index.get(word).containsKey(path)) {
				return Collections.unmodifiableMap(index.get(word));
			} else {
				return Collections.emptyMap();
			}
		} else {
			return Collections.emptyMap();
		}
	}

//	/**
//	 * Shows all the positions associated with a path in the map
//	 *
//	 * @param word word inside of the file
//	 * @param path path of the file
//	 * @return Returns a set view of all the positions associated with the path
//	 */
//	public TreeSet<Integer> positionsSet(String word, String path) {
//		TreeSet<Integer> temp = new TreeSet<>();
//		if (index.containsKey(word)) {
//			temp = index.get(word).get(path);
//		}
//		return new TreeSet<Integer>(temp);
//	}

	/** 
	 * Number of words in the the map
	 * 
	 * @return integer size of the number of words in the map
	 */
	public int words() {
		return index.size();
	}

	/** 
	 * Number of paths associated with the word in the the map
	 * 
	 * @param word word inside of the file
	 * @return integer size of the number of paths associated with word in the map
	 */
	public int paths(String word) {
		if (index.containsKey(word)) {
			return index.get(word).size();
		} else {
			return 0;
		}
	}

	/**
	 *  Number of positions associated with the path in the the map
	 * 
	 * @param word word inside of the file
	 * @param path path of the file
	 * @return integer size of the number of paths associated with word in the map
	 */
	public int positions(String word, String path) {
		if (index.containsKey(word)) { // TODO containsPath(String word, String path)
			return index.get(word).get(path).size();
		} else {
			return 0;
		}
	}

	/** 
	 * Checks to see if the map contains the word
	 * 
	 * @param word word inside the file
	 * @return true if map contains the word
	 */
	public boolean containsWord(String word) {
		return index.containsKey(word);
	}

	/** 
	 * Checks to see if the word contains the path
	 * 
	 * @param word word inside the file
	 * @param path path of the file
	 * @return true if word contains the path
	 */
	public boolean containsPath(String word, String path) {
		if (index.containsKey(word)) {
			return index.get(word).containsKey(path);
		} else {
			return false;
		}
	}
	
	// TODO public boolean containsPosition(String word, String path, int position) {

	/** 
	 * Prints in the map
	 */
	@Override
	public String toString() {
		return index.toString();
	}
	
	/* TODO
	public void writeJSON(Path path) throws IOException {
		TreeJSONWriter.asTripleNested(this.index);
	}
	*/
}
