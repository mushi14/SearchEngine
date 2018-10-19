import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/** 
 * Data structure to store file paths and the word positions.
 */
public class InvertedIndex {

//	/** 
//	 * Stores a mapping of files to the positions the words were found in the file.
//	 */
//	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> index;
//
//	/**
//	 *  Initializes the index.
//	 */
//	public InvertedIndex() {
//		index = new TreeMap<>();
//	}
//
//	/** 
//	 * Gets the TreeMap of keys paths and values positions associated with the word
//	 * 
//	 * @param word word inside of the file
//	 * @return TreeMap containing path and positions of word 
//	 */
//	public Map<String, Set<Integer>> get(String word) {
//		if (index.containsKey(word)) {
//			return Collections.unmodifiableMap(index.get(word));
//		} else {
//			return Collections.emptyMap();
//		}
//	}
//
//	/** 
//	 * Adds the key word to with value TreeMap of key path and value positions
//	 * 
//	 * @param word word inside of the file
//	 * @param location path of the file
//	 * @param position position the word appears in the file
//	 */
//	public void addWord(String word, String location, int position) {
//		TreeMap<String, TreeSet<Integer>> value = new TreeMap<>();
//		value.put(location, new TreeSet<Integer>());
//		value.get(location).add(position);
//		index.put(word, value);
//	}
//
//	/**
//	 * Adds a key path and value position to the already existing word in the TreeMap
//	 * 
//	 * @param word word inside of the file
//	 * @param location path of the file
//	 * @param position position the word appears in the file
//	 */
//	public void addPath(String word, String location, int position) {
//		TreeSet<Integer> value = new TreeSet<>();
//		value.add(position);
//		index.get(word).put(location, value);
//	}
//	
//	/**
//	 * Adds the position in which the word appears in the path
//	 * 
//	 * @param word word inside of the file
//	 * @param location path of the file
//	 * @param position position the word appears in the file
//	 */
//	public void addPosition(String word, String location, int position) {
//		index.get(word).get(location).add(position);
//	}
//
//	/**
//	 * Shows all the words in the map
//	 *
//	 * @return Returns a set view of all the paths
//	 */
//	public Set<String> wordsKeySet() {
//		return Collections.unmodifiableSet(index.keySet());
//	}
//
//	/**
//	 * Shows all the paths associated with the word in the map
//	 *
//	 * @param word word inside of the file
//	 * @return Returns a set view of all the paths
//	 */
//	public Set<String> locationKeySet(String word) {
//		if (index.containsKey(word)) {
//			return Collections.unmodifiableSet(index.get(word).keySet());
//		} else {
//			return Collections.emptySet();
//		}
//	}
//	
//	/**
//	 * Shows all the positions associated with a path in the map
//	 *
//	 * @param word word inside of the file
//	 * @param location path of the file
//	 * @return Returns a set view of all the positions associated with the path
//	 */
//	public Set<Integer> positionsSet(String word, String location) {
//		if (index.containsKey(word)) {
//			return Collections.unmodifiableSet(index.get(word).get(location));
//		} else {
//			return Collections.emptySet();
//		}
//	}
//
//	/** 
//	 * Number of words in the the map
//	 * 
//	 * @return integer size of the number of words in the map
//	 */
//	public int words() {
//		return index.size();
//	}
//
//	/** 
//	 * Number of paths associated with the word in the the map
//	 * 
//	 * @param word word inside of the file
//	 * @return integer size of the number of paths associated with word in the map
//	 * 
//	 */
//	public int locations(String word) {
//		if (index.containsKey(word)) {
//			return index.get(word).size();
//		} else {
//			return 0;
//		}
//	}
//
//	/** 
//	 * Number of positions associated with the path in the the map
//	 * 
//	 * @param word word inside of the file
//	 * @param location path of the file
//	 * @return integer size of the number of paths associated with word in the map
//	 * 
//	 */
//	public int positions(String word, String location) {
//		if (index.containsKey(word)) {
//			return index.get(word).get(location).size();
//		} else {
//			return 0;
//		}
//	}
//
//	/**
//	 * Total locations of all the words and the total words they contain 
//	 * @return TreeMap of locations and their total words
//	 */
//	public Map<String, Integer> totalLocations() {
//		Map<String, Integer> locationsMap = new TreeMap<>();
//		for (String word : wordsKeySet()) {
//			for (String path : locationKeySet(word)) {
//				if (!locationsMap.containsKey(path)) {
//					locationsMap.put(path, positions(word, path));
//				} else {
//					int value = locationsMap.get(path) + positions(word, path);
//					locationsMap.replace(path, value);
//				}
//			}
//		}
//		return Collections.unmodifiableMap(locationsMap);
//	}
//
//	/** Checks to see if the map contains the word
//	 * 
//	 * @param word word inside the file
//	 * @return true if map contains the word
//	 * 
//	 */
//	public boolean containsWord(String word) {
//		return index.containsKey(word);
//	}
//
//	/** Checks to see if the word contains the path
//	 * 
//	 * @param word word inside the file
//	 * @param location path of the file
//	 * @return true if word contains the path
//	 * 
//	 */
//	public boolean containsLocation(String word, String location) {
//		if (index.containsKey(word)) {
//			return index.get(word).containsKey(location);
//		} else {
//			return false;
//		}
//	}
//
//	/**
//	 * Clears the inverted index
//	 */
//	public void clear() {
//		index.clear();
//	}
//
//	/**
//	 * Checks to see if the inverted index is empty
//	 * @return true if empty, false if not
//	 */
//	public boolean isEmpty() {
//		return index.isEmpty();
//	}
//
//	/** 
//	 * Prints in the map
//	 */
//	@Override
//	public String toString() {
//		return this.index.toString();
//	}

		/** 
		 * Stores a mapping of files to the positions the words were found in the file.
		 */
		private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> index;

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

		/**
		 * Adds the word, its location, and its position to the index. 
		 * @param word word inside of the file
		 * @param path location of the file
		 * @param position the position of the word in the file
		 */
		public void add(String word, String path, int position) {
			if (index.containsKey(word)) {
				if (index.get(word).containsKey(path)) {
					if (!index.get(word).get(path).contains(position)) {
						index.get(word).get(path).add(position);
					}
				} else {
					index.get(word).put(path, new TreeSet<Integer>());
					index.get(word).get(path).add(position);
				}
			} else {
				index.put(word, new TreeMap<String, TreeSet<Integer>>());
				index.get(word).put(path, new TreeSet<Integer>());
				index.get(word).get(path).add(position);
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
		public void writeJSON(Path path) throws IOException {
			TreeJSONWriter.asTripleNested(this.index, path);
		}

		/** 
		 * Prints in the map
		 */
		@Override
		public String toString() {
			return index.toString();
		}
	}
}