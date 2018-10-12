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
								totalMatches = locationsList.get(loc).totalMatches;
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
								totalMatches = locationsList.get(loc).totalMatches;
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

	public static double round(double score) {
		return BigDecimal.valueOf(score)
			.setScale(15, RoundingMode.HALF_UP)
			.doubleValue();
	}

	static class Comparison implements Comparator<Query> {
		@Override
		public int compare(Query o1, Query o2) {
			if (o1.rawScore > o2.rawScore) {
				return -1;
			} else if (o1.rawScore < o2.rawScore) {
				return 1;
			} else {
				if (o1.totalWords > o2.totalWords) {
					return -1;
				} else if (o1.totalWords < o2.totalWords) {
					return 1;
				} else {
					return o1.location.compareToIgnoreCase(o2.location);
				}
			}
		}
	}
}
