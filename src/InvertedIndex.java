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
	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> index;
	private final Map<String, Integer> locationsMap;

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

		if (locationsMap.containsKey(path)) {
			locationsMap.put(path, locationsMap.get(path) + 1);
		} else {
			locationsMap.put(path, 1);
		}
	}

	/**
	 * Adds a list of words to the index given they all have the same path
	 * @param words list of words to add
	 * @param location path of the file
	 */
	public void addAll(String[] words, String location) {
		int position = 0;
		for (String word : words) {
			position++;
			this.add(word, location, position);
		}
	}

	/**
	 * Shows all the words in the map
	 *
	 * @return Returns a set view of all the paths
	 */
	public Set<String> getWords() {
		return Collections.unmodifiableSet(index.keySet());
	}

	/**
	 * Shows all the paths associated with the word in the map
	 *
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
	 *
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
		if (containsWord(word)) {
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
		if (containsPath(word, path)) {
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
	public void writeLocationsJSON(Path path) throws IOException {
		TreeJSONWriter.asLocations(locationsMap, path);
	}

	/**
	 * Writes the search results to the file path in pretty json format
	 * @param path path to the file to write to
	 * @throws IOException in case there's any problem finding the file
	 */
	public void writeSearchResultsJSON(Map<String, List<Search>> results, Path path) throws IOException {
		TreeJSONWriter.asSearchResult(results, path);
	}

	/*
	 * TODO
	 * public List<Search> exactSearch(Set<String> queries)
	 */
	
	/**
	 * Performs exact search on a line from the query file. Stores the results to results map
	 * @param results map containing key-line and value-Search to refer from
	 * @param queries line of queries to compare
	 */
	public void exactSearch(Map<String, List<Search>> results, Set<String> queries) {
		String line = String.join(" ", queries);
		int totalMatches = 0;
		int totalWords = 0;
		double rawScore = 0;
		String score = "";

		Map<String, Search> locationsList = new HashMap<>();
		List<Search> resultsList = new ArrayList<>();

		if (!results.containsKey(line)) {
			results.put(line, new ArrayList<>());
			for (String query : queries) {
				for (String word : getWords()) {
					if (word.equals(query)) {
						for (String loc : getPaths(word)) {
							if (locationsList.containsKey(loc)) {
								totalMatches = locationsList.get(loc).getMatches();
								totalMatches += positions(word, loc);
								totalWords = locationsMap.get(loc);
								rawScore = totalMatches / totalWords;
//								score = FORMATTER.format(totalMatches / totalWords);
								Search newQuery = new Search(loc, totalMatches, totalWords, rawScore);
								locationsList.put(loc, newQuery);

							} else {
								totalMatches = positions(word, loc);
								totalWords = locationsMap.get(loc);
								rawScore = totalMatches / totalWords;
//								score = FORMATTER.format(totalMatches / totalWords);
								Search newQuery = new Search(loc, totalMatches, totalWords, rawScore);
								locationsList.put(loc, newQuery);

								// TODO resultList.add(q);
								resultsList.add(locationsList.get(loc));
							}
//							if (!resultsList.contains(locationsList.get(loc))) {
//								resultsList.add(locationsList.get(loc));
//							}
						}
					}
				}
			}
			
			// TODO separate loop... can embed (see above)
//			for (String loc : locationsList.keySet()) {
//				resultsList.add(locationsList.get(loc));
//			}
			Collections.sort(resultsList, new Search.Comparison());

			for (Search query : resultsList) {
				results.get(line).add(query);
			}
		}
	}

	/**
	 * Performs partial search on a line from the query file. Stores the results to results map
	 * @param results map containing key-line and value-Search to refer from
	 * @param queries line of queries to compare
	 */
	public void partialSearch(Map<String, List<Search>> results, Set<String> queries) {
		String line = String.join(" ", queries);
		int totalMatches = 0;
		int totalWords = 0;
		double rawScore = 0;
		String score = "";

		Map<String, Search> locationsList = new HashMap<>();
		if (!results.containsKey(line)) {
			results.put(line, new ArrayList<>());
			for (String query : queries) {
				for (String word : getWords()) {
					if (word.startsWith(query)) {
						for (String loc : getPaths(word)) {
							if (locationsList.containsKey(loc)) {
								totalMatches = locationsList.get(loc).getMatches();
								totalMatches += positions(word, loc);
								totalWords = locationsMap.get(loc);
								rawScore = totalMatches / totalWords;
//								score = FORMATTER.format(totalMatches / totalWords);

								Search newQuery = new Search(loc, totalMatches, totalWords, rawScore);
								locationsList.put(loc, newQuery);
							} else {
								totalMatches = positions(word, loc);
								totalWords = locationsMap.get(loc);
								rawScore = totalMatches / totalWords;
//								score = FORMATTER.format(totalMatches / totalWords);

								Search newQuery = new Search(loc, totalMatches, totalWords, rawScore);
								locationsList.put(loc, newQuery);
							}
						}
					}
				}
			}
		}

		List<Search> tempList = new ArrayList<>();
		for (String loc : locationsList.keySet()) {
			tempList.add(locationsList.get(loc));
		}
		Collections.sort(tempList, new Search.Comparison());

		for (Search query : tempList) {
			results.get(line).add(query);
		}
	}

	/** 
	 * Prints in the inverted index
	 */
	@Override
	public String toString() {
		return index.toString();
	}
}