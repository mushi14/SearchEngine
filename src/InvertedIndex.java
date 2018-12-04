import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
	public final TreeMap<String, TreeMap<String, TreeSet<Integer>>> index;
	public final Map<String, Integer> locationsMap;

	/**
	 *  Initializes the index.
	 */
	public InvertedIndex() {
		index = new TreeMap<>();
		locationsMap = new TreeMap<>();
	}

	/**
	 * Adds the word, its location, and its position to the index. 
	 * @param word word inside of the file
	 * @param path location of the file
	 * @param position the position of the word in the file
	 */
	public void add(String word, String path, int position) {
		if (index.containsKey(word)) {
			if (index.get(word).containsKey(path)) {
				index.get(word).get(path).add(position);
			} else {
				index.get(word).putIfAbsent(path, new TreeSet<Integer>());
				index.get(word).get(path).add(position);
			}
		} else {
			index.put(word, new TreeMap<String, TreeSet<Integer>>());
			index.get(word).put(path, new TreeSet<Integer>());
			index.get(word).get(path).add(position);
		}

		locationsMap.put(path, locationsMap.getOrDefault(path, 0) + 1);
	}

	/**
	 * Adds a list of words to the index mapped to their locations and positions.
	 * @param local an inverted index data structure to add to the index
	 */
	public void addAll(InvertedIndex local) {
		for (String word : local.index.keySet()) {
			if (index.containsKey(word)) {
				index.get(word).putAll(local.index.get(word));
			} else {
				index.put(word, local.index.get(word));
			}
		}

		for (String path : local.locationsMap.keySet()) {
			locationsMap.putIfAbsent(path, 0);
			locationsMap.put(path, locationsMap.get(path) + local.locationsMap.get(path));
		}
	}

	/**
	 * Shows all the words in the map
	 * @return Returns a set view of all the paths
	 */
	public Set<String> getWords() {
		return Collections.unmodifiableSet(index.keySet());
	}

	/**
	 * Shows all the paths associated with the word in the map
	 * @param word word inside of the file
	 * @return Returns a set view of all the paths
	 */
	public Set<String> getPaths(String word) {
		if (index.containsKey(word)) {
			return Collections.unmodifiableSet(index.get(word).keySet());
		} else {
			return Collections.emptySet();
		}
	}

	/**
	 * Shows all the positions associated with a path in the map
	 * @param word word inside of the file
	 * @param path path of the file
	 * @return Returns a set view of all the positions associated with the path
	 */
	public Set<Integer> getPositions(String word, String path) {
		if (containsWord(word)) {
			if (containsPath(word, path)) {
				return Collections.unmodifiableSet(index.get(word).get(path));
			} else {
				return Collections.emptySet();
			}
		} else {
			return Collections.emptySet();
		}
	}

	/** 
	 * Number of words in the the map
	 * @return integer size of the number of words in the map
	 */
	public int words() {
		return index.size();
	}

	/** 
	 * Number of paths associated with the word in the the map
	 * @param word word inside of the file
	 * @return integer size of the number of paths associated with word in the map
	 */
	public int paths(String word) {
		if (containsWord(word)) {
			return index.get(word).size();
		} else {
			return 0;
		}
	}

	/**
	 *  Number of positions associated with the path in the the map
	 * @param word word inside of the file
	 * @param path path of the file
	 * @return integer size of the number of paths associated with word in the map
	 */
	public int positions(String word, String path) {
		if (containsPath(word, path)) {
			return index.get(word).get(path).size();
		} else {
			return 0;
		}
	}

	/** 
	 * Checks to see if the map contains the word
	 * @param word word inside the file
	 * @return true if map contains the word
	 */
	public boolean containsWord(String word) {
		return index.containsKey(word);
	}

	/** 
	 * Checks to see if the word contains the path
	 * @param word word inside the file
	 * @param path path of the file
	 * @return true if word contains the path
	 */
	public boolean containsPath(String word, String path) {
		if (containsWord(word)) {
			return index.get(word).containsKey(path);
		} else {
			return false;
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
		if (containsPath(word, path)) {
			return index.get(word).get(path).contains(position);
		} else {
			return false;
		}
	}

	/**
	 * Performs exact search on a line from the query file. Stores the results to results map
	 * @param queries line of queries to compare
	 * @return list of search results sorted
	 */
	public List<Search> exactSearch(Set<String> queries) {
		Map<String, Search> locationsList = new HashMap<>();
		List<Search> resultsList = new ArrayList<>();

		for (String query : queries) {
			if (index.containsKey(query)) {
				searchHelper(query, locationsList, resultsList);
			}
		}

		Collections.sort(resultsList);
		return resultsList;
	}

	/**
	 * Performs partial search on a line from the query file. Stores the results to results map
	 * @param queries line of queries to compare
	 * @return list of search results sorted
	 */
	public List<Search> partialSearch(Set<String> queries) {
		Map<String, Search> locationsList = new HashMap<>();
		List<Search> resultsList = new ArrayList<>();

		for (String query : queries) {
			for (String word : index.tailMap(query).keySet()) {
				if (word.startsWith(query)) {
					searchHelper(word, locationsList, resultsList);
				} else {
					break;
				}
			}
		}

		Collections.sort(resultsList);
		return resultsList;
	}

	/**
	 * Performs search and updates results for one given word that is in the query line and also
	 * in the inverted index 
	 * @param word the word in the query line that is found in the inverted index
	 * @param locationsList a map containing list of locations mapped with their search results
	 * @param resultsList a list of search results, used for adding all the results to a query line
	 * @param totalMatches total number of matches
	 * @param totalWords total number of words
	 */
	private void searchHelper(String word, Map<String, Search> locationsList, List<Search> resultsList) {
		for (String loc : index.get(word).keySet()) {
			if (locationsList.containsKey(loc)) {
				locationsList.get(loc).calculate(index.get(word).get(loc).size());
			} else {
				int totalMatches = index.get(word).get(loc).size();
				int totalWords = locationsMap.get(loc);

				Search newQuery = new Search(loc, totalMatches, totalWords);
				locationsList.put(loc, newQuery);

				resultsList.add(newQuery);
			}
		}
	}

	/**
	 * Writes the index to the file path in pretty json format
	 * @param path path to the file to write to
	 * @throws IOException in case there's any problem finding the file
	 */
	public void writeIndexJSON(Path path) throws IOException {
		TreeJSONWriter.asTripleNested(this.index, path);
	}

	/**
	 * Writes the locations to the file path in pretty json format
	 * @param path path to the file to write to
	 * @throws IOException in case there's any problem finding the file
	 */
	public void writeLocJSON(Path path) throws IOException {
		TreeJSONWriter.asLocations(locationsMap, path);
	}

	/** 
	 * Prints in the inverted index
	 */
	@Override
	public String toString() {
		return index.toString();
	}
}
