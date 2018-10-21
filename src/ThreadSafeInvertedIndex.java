import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ThreadSafeInvertedIndex extends InvertedIndex {

	private ReadWriteLock lock;

	public ThreadSafeInvertedIndex() {
		super();
		lock = new ReadWriteLock();
	}

	/** 
	 * Gets the TreeMap of keys paths and values positions associated with the word
	 * 
	 * @param word word inside of the file
	 * @return TreeMap containing path and positions of word 
	 */
	public Map<String, Set<Integer>> get(String word) {
		lock.lockReadOnly();
		try {
			return super.get(word);
		} finally {
			lock.unlockReadOnly();
		}
	}

	/**
	 * Adds the word, its location, and its position to the index. 
	 * @param word word inside of the file
	 * @param path location of the file
	 * @param position the position of the word in the file
	 */
	public void add(String word, String path, int position) {
		lock.lockReadWrite();
		try {
			super.add(word, path, position);
		} finally {
			lock.unlockReadWrite();
		}
	}

	/**
	 * Adds a list of words to the index given they all have the same path
	 * @param words list of words to add
	 * @param location path of the file
	 */
	public void addAll(String[] words, String location) {
		lock.lockReadWrite();
		try {
			super.addAll(words, location);
		} finally {
			lock.unlockReadWrite();
		}
	}

	/**
	 * Shows all the words in the map
	 *
	 * @return Returns a set view of all the paths
	 */
	public Set<String> getWords() {
		lock.lockReadOnly();
		try {
			return super.getWords();
		} finally {
			lock.unlockReadOnly();
		}
	}

	/**
	 * Shows all the paths associated with the word in the map
	 *
	 * @param word word inside of the file
	 * @return Returns a set view of all the paths
	 */
	public Set<String> getPaths(String word) {
		lock.lockReadOnly();
		try {
			return super.getPaths(word);
		} finally {
			lock.unlockReadOnly();
		}
	}

	/**
	 * Shows all the positions associated with a path in the map
	 *
	 * @param word word inside of the file
	 * @param path path of the file
	 * @return Returns a set view of all the positions associated with the path
	 */
	public Set<Integer> getPositions(String word, String path) {
		lock.lockReadOnly();
		try {
			return super.getPositions(word, path);
		} finally {
			lock.unlockReadOnly();
		}
	}

	/** 
	 * Number of words in the the map
	 * 
	 * @return integer size of the number of words in the map
	 */
	public int words() {
		lock.lockReadOnly();
		try {
			return super.words();
		} finally {
			lock.unlockReadOnly();
		}
	}

	/** 
	 * Number of paths associated with the word in the the map
	 * 
	 * @param word word inside of the file
	 * @return integer size of the number of paths associated with word in the map
	 */
	public int paths(String word) {
		lock.lockReadOnly();
		try {
			return super.paths(word);
		} finally {
			lock.unlockReadOnly();
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
		lock.lockReadOnly();
		try {
			return super.positions(word, path);
		} finally {
			lock.unlockReadOnly();
		}
	}

	/**
	 * Total locations of all the words and the total words they contain 
	 * @return TreeMap of locations and their total words
	 */
//	public Map<String, Integer> totalLocations() {
//		
//	}

	/** 
	 * Checks to see if the map contains the word
	 * 
	 * @param word word inside the file
	 * @return true if map contains the word
	 */
	public boolean containsWord(String word) {
		lock.lockReadOnly();
		try {
			return super.containsWord(word);
		} finally {
			lock.unlockReadOnly();
		}
	}

	/** 
	 * Checks to see if the word contains the path
	 * 
	 * @param word word inside the file
	 * @param path path of the file
	 * @return true if word contains the path
	 */
	public boolean containsPath(String word, String path) {
		lock.lockReadOnly();
		try {
			return super.containsPath(word, path);
		} finally {
			lock.unlockReadOnly();
		}
	}

	/**
	 * Checks to see if the specified word from the path contains the position
	 * @param word word from the file
	 * @param path file path the word is in
	 * @param position specific position the word occurs in the file path
	 * @return returns true if position exists in the path, false otherwise
	 */
	public boolean containsPosition(String word, String path, int position) {
		lock.lockReadOnly();
		try {
			return super.containsPosition(word, path, position);
		} finally {
			lock.unlockReadOnly();
		}
	}

	/**
	 * Writes the index to the file path in pretty json format
	 * @param path path to the file to write to
	 * @throws IOException in case there's any problem finding the file
	 */
	public void writeIndexJSON(Path path) throws IOException {
		lock.lockReadWrite();;
		try {
			super.writeIndexJSON(path);
		} finally {
			lock.unlockReadWrite();;
		}
	}

	/**
	 * Writes the locations to the file path in pretty json format
	 * @param path path to the file to write to
	 * @throws IOException in case there's any problem finding the file
	 */
	public void writeLocationsJSON(Path path) throws IOException {
		lock.lockReadWrite();;
		try {
			super.writeLocationsJSON(path);
		} finally {
			lock.unlockReadWrite();;
		}
	}

	/**
	 * Writes the search results to the file path in pretty json format
	 * @param path path to the file to write to
	 * @throws IOException in case there's any problem finding the file
	 */
	public void writeSearchResultsJSON(Map<String, List<Search>> results, Path path) throws IOException {
		lock.lockReadWrite();;
		try {
			super.writeSearchResultsJSON(results, path);;
		} finally {
			lock.unlockReadWrite();;
		}
	}

	/**
	 * performs exact search on a line from the query file. Stores the results to results map
	 * @param results map containing key-line and value-Search to refer from
	 * @param queries line of queries to compare
	 */
//	public void exactSearch(Map<String, List<Search>> results, Set<String> queries) {
//		
//	}

	/**
	 * performs partial search on a line from the query file. Stores the results to results map
	 * @param results map containing key-line and value-Search to refer from
	 * @param queries line of queries to compare
	 */
//	public void partialSearch(Map<String, List<Search>> results, Set<String> queries) {
//		
//	}

	/** 
	 * Prints in the inverted index
	 */
	@Override
	public String toString() {
		lock.lockReadOnly();
		try {
			return super.toString();
		} finally {
			lock.unlockReadOnly();
		}
	}
}
