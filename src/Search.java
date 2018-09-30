import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class Search {
	
	private final static Map<Double, List<String>> scores = new TreeMap<>(Collections.reverseOrder());

	public static Map<Double, List<String>> score(InvertedIndex index, TreeSet<String> queries) {
		DecimalFormat FORMATTER = new DecimalFormat("0.000000");
		List<Double> seen = new ArrayList<>();

		List<String> duplicateScores = new ArrayList<>();

		double totalWords = 0;
		double totalMatches = 0;
		double score = 0;

		for (String location : index.totalLocations().keySet()) {
			List<String> locations = new ArrayList<>();
			List<String> temp = new ArrayList<>();
			totalWords = index.totalLocations().get(location);
			
			for (String query : queries) {
				if (index.containsWord(query)) {
					if (index.get(query).containsKey(location)) {
						temp.add(query);
					}
				}
			}
			if (!temp.isEmpty()) {
				for (String query : temp) {
					totalMatches += index.get(query, location).size();
					System.out.println(totalMatches);
					score = Double.parseDouble(FORMATTER.format(totalMatches / totalWords));
					locations.add(location);
					scores.put(score, locations);
				}
			}
			totalMatches = 0;
		}
		
//		int  count = 0;
//		for (String location: scores.keySet()) {
//			if (count == 0) {
//				seen.add(scores.get(location));
//				duplicateScores.add(location);
//				count++;
//			} else {
//				if (seen.contains(scores.get(location))) {
//					duplicateScores.add(location);
//				} else {
//					seen.add(scores.get(location));
//				}
//			}
//		}
		
//		List<Stack<String>> stack = new Stack();
//		for (String location : compareScores(index, duplicateScores)) {
//			System.out.println(scores.get(location) + "   " + location);
//		}
		return scores;
	}


	public static List<String> compareScores(InvertedIndex index, List<String> duplicateScores) {
		Map<String, Integer> totalWords = new TreeMap<>();
		List<String> sortedWords = new ArrayList<>();
		List<Integer> temp = new ArrayList<>();

		for (String location : duplicateScores) {
			totalWords.put(location, index.totalLocations().get(location));
		}
		
		temp.addAll(totalWords.values());
		Collections.sort(temp, Collections.reverseOrder());
		
		for (Integer wordCount : temp) {
			for (String location : duplicateScores) {
				if (index.totalLocations().get(location) == wordCount) {
					if (!sortedWords.contains(location)) {
						sortedWords.add(location);
					}
				}
			}
		}

//		Stack<String> stack = new Stack();
//		for (String location : sortedWords) {
//			stack.push(location);
//		}

		return sortedWords;
	}


//	public static Stack sortedScores() {
//		Stack stack = new Stack();
//		
//		return stack;
//	}
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