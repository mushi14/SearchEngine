import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadSafeInvertedIndex extends InvertedIndex {

	private final ReadWriteLock lock;

	static final Logger logger = LogManager.getLogger();

	/**
	 * Initializes the index
	 */
	public ThreadSafeInvertedIndex() {
		super();
		lock = new ReadWriteLock();
		System.out.println();
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
	public void addAll(InvertedIndex local) {
		lock.lockReadWrite();
		try {
			super.addAll(local);
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
		lock.lockReadOnly();
		try {
			super.writeIndexJSON(path);
		} finally {
			lock.unlockReadOnly();
		}
	}

	/**
	 * Writes the locations to the file path in pretty json format
	 * @param path path to the file to write to
	 * @throws IOException in case there's any problem finding the file
	 */
	@Override
	public void writeLocJSON(Path path) throws IOException {
		lock.lockReadOnly();
		try {
			super.writeLocJSON(path);
		} finally {
			lock.unlockReadOnly();
		}
	}

	/**
	 * Performs exact search on a line from the query file. Stores the results to results map
	 * @param results map containing key-line and value-Search to refer from
	 * @param queries line of queries to compare
	 */
	@Override
	public List<Search> exactSearch(Set<String> queries) {
		lock.lockReadOnly();
		try {
			return super.exactSearch(queries);
		} finally {
			lock.unlockReadOnly();
		}
	}

	/**
	 * Performs partial search on a line from the query file. Stores the results to results map
	 * @param results map containing key-line and value-Search to refer from
	 * @param queries line of queries to compare
	 * @return 
	 */
	@Override
	public List<Search> partialSearch(Set<String> queries) {
		lock.lockReadOnly();
		try {
			return super.partialSearch(queries);
		} finally {
			lock.lockReadOnly();
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
