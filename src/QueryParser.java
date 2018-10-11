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

	public static final TreeMap<String, TreeMap<Double, List<Query>>> queryMap = new TreeMap<>();
	public static final TreeMap<String, List<Query>> results = new TreeMap<>();

	public static void exactSearch(InvertedIndex index, Set<String> queries) {
		String temp = String.join(" ", queries);

		if (!queryMap.containsKey(temp)) {
			queryMap.put(temp, new TreeMap<>(Collections.reverseOrder()));
		}
		
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
		for (String l : queryMap.keySet()) {
			for (Double sc : queryMap.get(l).keySet()) {
				if (queryMap.get(l).get(sc).size() > 1) {
					Collections.sort(queryMap.get(l).get(sc), new Comparison());
				}
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

		Map<String, Integer> location = index.totalLocations();
		if (!results.containsKey(line)) {
			results.put(line, new ArrayList<>());

			for (String query : queries) {
				for (String word : index.index.keySet()) {
					if (word.startsWith(query)) {
						for (String loc : index.index.get(word).keySet()) {

							if (locationsList.containsKey(loc)) {
								totalMatches = locationsList.get(loc).totalMatches;
								totalMatches += index.index.get(word).get(loc).size();
								totalWords = location.get(loc);
								rawScore = totalMatches / totalWords;
								rawScore = round(rawScore);
								score = FORMATTER.format(totalMatches / totalWords);

								Query q = new Query(loc, totalMatches, totalWords, rawScore, score);
								locationsList.put(loc, q);
							} else {
								totalMatches = index.get(word, loc).size();
								totalWords = location.get(loc);
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



//	public static void partialSearch(InvertedIndex index, Set<String> queries) {
//		String temp = String.join(" ", queries);
//
//		if (!queryMap.containsKey(temp)) {
//			queryMap.put(temp, new TreeMap<>(Collections.reverseOrder()));
//		}
//		
//		DecimalFormat FORMATTER = new DecimalFormat("0.000000");
//		double totalMatches = 0;
//		double totalWords = 0;
//		double rawScore = 0;
//		String score = "";
//
//		Set<String> words = new TreeSet<>();
//
//		for (String query : queries) {
//			if (!index.wordStartsWith(query).isEmpty()) {
//				words.addAll(index.wordStartsWith(query));
//			}
//		}
//
//		for (String loc : index.totalLocations().keySet()) {
//			boolean contains = false;
//			for (String word : words) {
//				if (index.get(word).containsKey(loc)) {
//					contains = true;
//					totalMatches += index.get(word, loc).size();
//					totalWords = index.totalLocations().get(loc);
//					rawScore = totalMatches / totalWords;
//					rawScore = round(rawScore);
//					score = FORMATTER.format(totalMatches / totalWords);
//				}
//			}
//			if (contains == true) {
//				if (queryMap.get(temp).containsKey(rawScore)) {
//					Query q = new Query(loc, totalMatches, totalWords, rawScore, score);
//					if (!queryMap.get(temp).get(rawScore).contains(q)) {
//						queryMap.get(temp).get(rawScore).add(q);
//					}
//				} else {
//					queryMap.get(temp).put(rawScore, new ArrayList<>());
//					Query q = new Query(loc, totalMatches, totalWords, rawScore, score);
//					queryMap.get(temp).get(rawScore).add(q);
//				}
//			}
//			totalMatches = 0;
//			totalWords = 0;
//			rawScore = 0;
//			score = "";
//		}
//		for (String l : queryMap.keySet()) {
//			for (Double sc : queryMap.get(l).keySet()) {
//				if (queryMap.get(l).get(sc).size() > 1) {
//					Collections.sort(queryMap.get(l).get(sc), new Comparison());
//				}
//			}
//		}
//	}

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
//					return o1.location.toLowerCase().compareTo(o2.location.toLowerCase());
					return o1.location.compareToIgnoreCase(o2.location);
				}
			}
		}
	}
}
