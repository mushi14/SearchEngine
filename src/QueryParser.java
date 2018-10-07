import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class QueryParser {

	public static final Map<String, Map<String, List<Query>>> queryMap = new TreeMap<>();

	public static void addQueries(InvertedIndex index, String line, Set<String> queries) {

		if (!queryMap.containsKey(line)) {
			queryMap.put(line, new TreeMap<>(Collections.reverseOrder()));
		}
		
		DecimalFormat FORMATTER = new DecimalFormat("0.000000");

		double totalMatches = 0;
		double totalWords = 0;
		String score = "";
		
		for (String loc : index.totalLocations().keySet()) {
			boolean contains = false;
			for (String query : queries) {
				if (index.containsWord(query)) {
					if (index.get(query).containsKey(loc)) {
						contains = true;
						totalMatches += index.get(query, loc).size();
						totalWords = index.totalLocations().get(loc);
						score = FORMATTER.format(totalMatches / totalWords);
					}
				}
			}
			
			if (contains == true) {
				if (queryMap.get(line).containsKey(score)) {
					Query q = new Query(loc, totalMatches, totalWords, score);
					queryMap.get(line).get(score).add(q);
				} else {
					queryMap.get(line).put(score, new ArrayList<>());
					Query q = new Query(loc, totalMatches, totalWords, score);
					queryMap.get(line).get(score).add(q);
				}
			}
			contains = false;
			totalMatches = 0;
			totalWords = 0;
			score = "";
		}
		
		for (String l : queryMap.keySet()) {
			for (String sc : queryMap.get(l).keySet()) {
				if (queryMap.get(l).get(sc).size() > 1) {
					Collections.sort(queryMap.get(l).get(sc), new Comparison());
				}
			}
		}
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
