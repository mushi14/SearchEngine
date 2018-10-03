import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Search {
	
	private final static Map<Double, List<String>> scores = new TreeMap<>(Collections.reverseOrder());

	public static Map<Double, List<String>> score(InvertedIndex index, Set<String> queries) {
		DecimalFormat FORMATTER = new DecimalFormat("0.000000");

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

			if (!temp.isEmpty()) {
				for (String query : temp) {
					totalMatches += index.get(query, location).size();
					score = Double.parseDouble(FORMATTER.format(totalMatches / totalWords));
				}
				if (scores.containsKey(score)) {
					scores.get(score).add(location);
				} else {
					scores.put(score, new ArrayList<String>());
					scores.get(score).add(location);
				}
			}
			totalMatches = 0;
		}

		for (Double s : scores.keySet()) {
			if (scores.get(s).size() > 1) {
				scores.replace(s, compareScores(index, scores.get(s)));
			}
		}
		return scores;
	}


	public static List<String> compareScores(InvertedIndex index, List<String> duplicateScores) {
		TreeSet<Integer> totalWords = new TreeSet<>(Collections.reverseOrder());
		List<String> sorted = new ArrayList<>();
		List<String> sortedCopy = new ArrayList<>();

		for (String location : duplicateScores) {
			totalWords.add(index.totalLocations().get(location));
		}

		int range = totalWords.first();
		boolean[] seen = new boolean[range + 1];

		for (Integer wordCount : totalWords) {
			for (int i = 0; i < duplicateScores.size(); i++) {
				if (index.totalLocations().get(duplicateScores.get(i)) == wordCount) {
					if (seen[wordCount] == true) {
						for (int j = 0; j < sorted.size(); j++) {
							if (index.totalLocations().get(sorted.get(j)) == wordCount) {
								String temp = sortedCopy.get(j);
								int a = sortedCopy.get(j).toLowerCase().compareTo(duplicateScores.get(i).toLowerCase());
								if (a > 0) {
									sortedCopy.set(j, duplicateScores.get(i));
									sortedCopy.add(temp);
								} else {
									sortedCopy.add(duplicateScores.get(i));
								}
							}
						}
						sorted.clear();
						sorted.addAll(sortedCopy);
					} else {
						sorted.add(duplicateScores.get(i));
						sortedCopy.add(duplicateScores.get(i));
						seen[wordCount] = true;
					}
				}
			}
		}
		return sorted;
	}

	public static void exactSearch(InvertedIndex index, TreeSet<String> queries) {
		score(index, queries);
	}
//
//	public static void PartialSearch() {
//
//	}
}