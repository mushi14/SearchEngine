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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
	public int positions(String word, String path) {
		lock.lockReadOnly();
		try {
			return super.positions(word, path);
		} finally {
			lock.unlockReadOnly();
		}
	}

//	/**
//	 * Total locations of all the words and the total words they contain 
//	 * @return TreeMap of locations and their total words
//	 */
//	@Override
//	public Map<String, Integer> totalLocations() {
//		lock.lockReadWrite();
//		try {
//			return super.totalLocations();
//		} finally {
//			lock.unlockReadWrite();
//		}
//	}

	/** 
	 * Checks to see if the map contains the word
	 * 
	 * @param word word inside the file
	 * @return true if map contains the word
	 */
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
	public void exactSearch(Map<String, List<Search>> results, Set<String> queries) {
		lock.lockReadWrite();
		try {
			super.exactSearch(results, queries);
		} finally {
			lock.unlockReadWrite();
		}
	}

	/**
	 * performs partial search on a line from the query file. Stores the results to results map
	 * @param results map containing key-line and value-Search to refer from
	 * @param queries line of queries to compare
	 */
	@Override
	public void partialSearch(Map<String, List<Search>> results, Set<String> queries) {
		lock.lockReadWrite();
		try {
			super.partialSearch(results, queries);
		} finally {
			lock.unlockReadWrite();
		}
	}

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
