import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class QueryParser {

	public static final TreeMap<String, List<Query>> results = new TreeMap<>();

	/**
	 * performs exact search on a line from the query file. Stores the results to results map
	 * @param index inverted index to refer from
	 * @param queries line of queries to compare
	 */
	public static void exactSearch(InvertedIndex index, Set<String> queries) {
		String line = String.join(" ", queries);
		DecimalFormat FORMATTER = new DecimalFormat("0.000000");
		double totalMatches = 0;
		double totalWords = 0;
		double rawScore = 0;
		String score = "";

		Map<String, Query> locationsList = new TreeMap<>();
		Map<String, Integer> totalLocations = index.totalLocations();
		if (!results.containsKey(line)) {
			results.put(line, new ArrayList<>());
			for (String query : queries) {
				for (String word : index.wordsKeySet()) {
					if (word.equals(query)) {
						for (String loc : index.get(word).keySet()) {
							if (locationsList.containsKey(loc)) {
								totalMatches = locationsList.get(loc).getMatches();
								totalMatches += index.positions(word, loc);
								totalWords = totalLocations.get(loc);
								rawScore = totalMatches / totalWords;
								rawScore = round(rawScore);
								score = FORMATTER.format(totalMatches / totalWords);

								Query q = new Query(loc, totalMatches, totalWords, rawScore, score);
								locationsList.put(loc, q);
							} else {
								totalMatches = index.positions(word, loc);
								totalWords = totalLocations.get(loc);
								rawScore = totalMatches / totalWords;
								rawScore = round(rawScore);
								score = FORMATTER.format(totalMatches / totalWords);

								Query q = new Query(loc, totalMatches, totalWords, rawScore, score);
								locationsList.put(loc, q);
							}
						}
					}
				}
			}
			List<Query> tempList = new ArrayList<>();
			for (String loc : locationsList.keySet()) {
				tempList.add(locationsList.get(loc));
			}
			Collections.sort(tempList, new Comparison());

			for (Query query : tempList) {
				results.get(line).add(query);
			}
		}
		
	}

	/**
	 * performs partial search on a line from the query file. Stores the results to results map
	 * @param index inverted index to refer from
	 * @param queries line of queries to compare
	 */
	public static void partialSearch(InvertedIndex index, Set<String> queries) {
		String line = String.join(" ", queries);
		DecimalFormat FORMATTER = new DecimalFormat("0.000000");
		double totalMatches = 0;
		double totalWords = 0;
		double rawScore = 0;
		String score = "";

		Map<String, Query> locationsList = new TreeMap<>();
		Map<String, Integer> totalLocations = index.totalLocations();
		if (!results.containsKey(line)) {
			results.put(line, new ArrayList<>());
			for (String query : queries) {
				for (String word : index.wordsKeySet()) {
					if (word.startsWith(query)) {
						for (String loc : index.get(word).keySet()) {
							if (locationsList.containsKey(loc)) {
								totalMatches = locationsList.get(loc).getMatches();
								totalMatches += index.positions(word, loc);
								totalWords = totalLocations.get(loc);
								rawScore = totalMatches / totalWords;
								rawScore = round(rawScore);
								score = FORMATTER.format(totalMatches / totalWords);

								Query q = new Query(loc, totalMatches, totalWords, rawScore, score);
								locationsList.put(loc, q);
							} else {
								totalMatches = index.positions(word, loc);
								totalWords = totalLocations.get(loc);
								rawScore = totalMatches / totalWords;
								rawScore = round(rawScore);
								score = FORMATTER.format(totalMatches / totalWords);

								Query q = new Query(loc, totalMatches, totalWords, rawScore, score);
								locationsList.put(loc, q);
							}
						}
					}
				}
			}
		}
		List<Query> tempList = new ArrayList<>();
		for (String loc : locationsList.keySet()) {
			tempList.add(locationsList.get(loc));
		}
		Collections.sort(tempList, new Comparison());

		for (Query query : tempList) {
			results.get(line).add(query);
		}
	}

	/**
	 * Converts the score to 15 decimal points in order to better the score comparison of the results
	 * @param score score to convert to 15 decimal point
	 * @return score formatted with 15 decimal points
	 */
	public static double round(double score) {
		return BigDecimal.valueOf(score)
			.setScale(15, RoundingMode.HALF_UP)
			.doubleValue();
	}

	/**
	 * Inner class that implements the comparator interface
	 * @author mushahidhassan
	 */
	static class Comparison implements Comparator<Query> {
		/**
		 * sorts a list of queries in descending order by their raw score, if score is the same, sorts by
		 * total number of words in a query location, if number of words the same, then sorts alphabetically (case insensitively)
		 */
		@Override
		public int compare(Query o1, Query o2) {
			if (o1.getRawScore() > o2.getRawScore()) {
				return -1;
			} else if (o1.getRawScore() < o2.getRawScore()) {
				return 1;
			} else {
				if (o1.getWords() > o2.getWords()) {
					return -1;
				} else if (o1.getWords() < o2.getWords()) {
					return 1;
				} else {
					return o1.getLocation().compareToIgnoreCase(o2.getLocation());
				}
			}
		}
	}
}
