import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class Search {
	
	private final static Map<String, Double> scores = new TreeMap<>();


	public static Map<String, Double> score(InvertedIndex index, TreeSet<String> queries) {
		DecimalFormat FORMATTER = new DecimalFormat("0.000000");
		List<Double> seen = new ArrayList<>();
		List<String> duplicateScores = new ArrayList<>();

		double totalWords = 0;
		double totalMatches = 0;
		double score = 0;

		for (String location : index.totalLocations().keySet()) {
			List<String> temp = new ArrayList<>();
			totalWords = index.totalLocations().get(location);
			for (String query : queries) {
				if (index.containsWord(query)) {
					if (index.get(query).containsKey(location)) {
						temp.add(query);
					}
				}
			}
			for (String q : temp) {
				totalMatches += index.get(q).get(location).size();
				score = Double.parseDouble(FORMATTER.format(totalMatches / totalWords));
				scores.put(location, score);
			}
			totalMatches = 0;
		}
		
		int  count = 0;
		for (String location: scores.keySet()) {
			if (count == 0) {
				seen.add(scores.get(location));
				duplicateScores.add(location);
				count++;
			} else {
				if (seen.contains(scores.get(location))) {
					duplicateScores.add(location);
				} else {
					seen.add(scores.get(location));
				}
			}
		}
		
		if (duplicateScores.size() > 1) {
			compareScores(index, duplicateScores);
		}
		
		return scores;
	}


	public static Map<String, Integer> compareScores(InvertedIndex index, List<String> duplicateScores) {
		Map<String, Integer> totalWords = new TreeMap<>();
		List<Integer> temp = new ArrayList<>();

		for (String location : duplicateScores) {
			totalWords.put(location, index.totalLocations().get(location));
		}
		
		temp.addAll(totalWords.values());
		Collections.sort(temp);
		
		for (String location : duplicateScores) {
		}

		return totalWords;
	}


//	public int compareTo(Movie other) {
//		// TODO Implement this however you want.
//		// DONE
//		int a = this.title().toLowerCase().compareTo(other.title().toLowerCase());
//		if (a == 0) {
//			return Integer.compare(other.year(), this.year());
//		} else {
//			return a;
//		}
//	}


//	public static void ExactSearch(InvertedIndex index, TreeSet<String> queries) {
//		
//
//	}
//
//	public static void PartialSearch() {
//
//	}
//	
//	
//	public static List<Double> get() {
//		return ExactSearch();
//	}
}