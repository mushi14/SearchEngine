import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class Search {
	
	private final static Map<String, Double> scores = new TreeMap<>();

	public static Map<String, Double> score(InvertedIndex index, TreeSet<String> queries) {
		DecimalFormat FORMATTER = new DecimalFormat("0.000000");

		double totalWords = 0;
		double totalMatches = 0;
		double score = 0;

		for (String location : index.totalLocations().keySet()) {
			List<String> temp = new ArrayList<String>();
			for (String query : queries) {
				if (index.containsWord(query)) {
					if (index.get(query).containsKey(location)) {
						temp.add(query);
					}
				}
			}

			totalWords = index.totalLocations().get(location);
			for (String q : temp) {
				totalMatches += index.get(q).get(location).size();
				score = Double.parseDouble(FORMATTER.format(totalMatches / totalWords));
				if (index.containsWord(q)) {
					if (index.get(q).containsKey(location)) {
						scores.put(location, score);
					}
				}
			}
			totalMatches = 0;
		}
		return scores;
	}


	public static void ExactSearch() {
		

	}

	public static void PartialSearch() {

	}
}