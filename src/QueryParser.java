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
import java.util.TreeSet;

public class QueryParser {

	public static final Map<String, Map<Double, List<Query>>> queryMap = new TreeMap<>();

	public static void exactSearch(InvertedIndex index, Set<String> queries) {
		String temp = "";
		int size = queries.size();
		int count = 0;
		for (String query : queries) {
			count++;
			if (count != size) {
				temp += query + " ";
			} else {
				temp += query;
			}
		}
		if (!queryMap.containsKey(temp)) {
			queryMap.put(temp, new TreeMap<>(Collections.reverseOrder()));
			DecimalFormat FORMATTER = new DecimalFormat("0.000000");
			double totalMatches = 0;
			double totalWords = 0;
			double rawScore = 0;
			String score = "";

			for (String loc : index.totalLocations().keySet()) {
				boolean contains = false;
				for (String query : queries) {
					if (index.containsWord(query)) {
						if (index.get(query).containsKey(loc)) {
							contains = true;
							totalMatches += index.get(query, loc).size();
							totalWords = index.totalLocations().get(loc);
							rawScore = totalMatches / totalWords;
							rawScore = round(rawScore);
							score = FORMATTER.format(totalMatches / totalWords);
						}
					}
				}
				if (contains == true) {
					if (queryMap.get(temp).containsKey(rawScore)) {
						Query q = new Query(loc, totalMatches, totalWords, rawScore, score);
						if (!queryMap.get(temp).get(rawScore).contains(q)) {
							queryMap.get(temp).get(rawScore).add(q);
						}
					} else {
						queryMap.get(temp).put(rawScore, new ArrayList<>());
						Query q = new Query(loc, totalMatches, totalWords, rawScore, score);
						queryMap.get(temp).get(rawScore).add(q);
					}
				}
				totalMatches = 0;
				totalWords = 0;
				rawScore = 0;
				score = "";
			}
		}
		for (String l : queryMap.keySet()) {
			for (Double sc : queryMap.get(l).keySet()) {
				if (queryMap.get(l).get(sc).size() > 1) {
					Collections.sort(queryMap.get(l).get(sc), new Comparison());
				}
			}
		}
	}

	public static void partialSearch(InvertedIndex index, Set<String> queries) {
		String temp = "";
		int size = queries.size();
		int count = 0;
		for (String query : queries) {
			count++;
			if (count != size) {
				temp += query + " ";
			} else {
				temp += query;
			}
		}
		if (!queryMap.containsKey(temp)) {
			queryMap.put(temp, new TreeMap<>(Collections.reverseOrder()));
			DecimalFormat FORMATTER = new DecimalFormat("0.000000");
			double totalMatches = 0;
			double totalWords = 0;
			double rawScore = 0;
			String score = "";

			List<Query> tempList = new ArrayList<>();
			
			for (String loc : index.totalLocations().keySet()) {
				boolean contains = false;
				for (String query : queries) {
//					System.out.println(query);
					if (!index.wordStartsWith(query).isEmpty()) {
						Set<String> words = new TreeSet<>();
						words = index.wordStartsWith(query);
						for (String word : words) {
//							System.out.println(word);
							if (index.get(word).containsKey(loc)) {
//								System.out.print(word + "      ");
								contains = true;
								totalMatches += index.get(word, loc).size();
								totalWords = index.totalLocations().get(loc);
								rawScore = totalMatches / totalWords;
								rawScore = round(rawScore);
								score = FORMATTER.format(totalMatches / totalWords);
							}
						}
					}
				}
//				System.out.println();
				if (contains == true) {
					if (queryMap.get(temp).containsKey(rawScore)) {
						Query q = new Query(loc, totalMatches, totalWords, rawScore, score);
						if (!queryMap.get(temp).get(rawScore).contains(q)) {
							queryMap.get(temp).get(rawScore).add(q);
						}
					} else {
						queryMap.get(temp).put(rawScore, new ArrayList<>());
						Query q = new Query(loc, totalMatches, totalWords, rawScore, score);
						queryMap.get(temp).get(rawScore).add(q);
					}
				}
				totalMatches = 0;
				totalWords = 0;
				rawScore = 0;
				score = "";
			}
			for (String l : queryMap.keySet()) {
				for (Double sc : queryMap.get(l).keySet()) {
					if (queryMap.get(l).get(sc).size() > 1) {
						Collections.sort(queryMap.get(l).get(sc), new Comparison());
					}
				}
			}
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
			if (o1.totalWords > o2.totalWords) {
				return -1;
			} else if (o1.totalWords < o2.totalWords) {
				return 1;
			} else {
				return o1.location.toLowerCase().compareTo(o2.location.toLowerCase());
			}
		}
	}
}
