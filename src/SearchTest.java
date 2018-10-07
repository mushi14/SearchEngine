import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class SearchTest {
	
	static Map<String, Map<String, List<String>>> map = new TreeMap<>();
	static Map<String, Integer> matchesMap = new TreeMap<>();

	
	static int totalMatches = 0;
	static double totalWords = 0;
	static String score = "";
	
	
	
	public static Map<String, Map<String, List<String>>> score(InvertedIndex index, Set<String> queries) {
		DecimalFormat FORMATTER = new DecimalFormat("0.000000");
		boolean contains = false;
		TreeMap<String, List<String>> scores = new TreeMap<>(Collections.reverseOrder());

		
		for (String file : index.totalLocations().keySet()) {
			for (String query : queries) {
				if (index.get(query).containsKey(file)) {
					contains = true;
					totalWords = index.totalLocations().get(file);
					totalMatches += index.get(query, file).size();
					score = FORMATTER.format(totalMatches / totalWords);
				}
			}

			if (contains == true) {
				if (scores.containsKey(score)) {
					scores.get(score).add(file);
				} else {
					scores.put(score, new ArrayList<>());
					scores.get(score).add(file);
				}
				matchesMap.put(file, totalMatches);

			}

			contains = false;
			totalMatches = 0;
			totalWords = 0;
			score = "";
		}

		Map<String, List<String>> newScores = new TreeMap<>(Collections.reverseOrder());
		newScores = (TreeMap)scores.clone();
		
		for (String score : scores.keySet()) {
			if (scores.get(score).size() > 1) {
				newScores.put(score ,compareTo(scores.get(score), index));
			}
		}

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
		map.put(temp, newScores);
		return map;
	}


	public static List<String> compareTo(List<String> files, InvertedIndex index) {
		List<String> temp = new ArrayList<>();
		Map<Double, List<String>> tempMap = new TreeMap<>(Collections.reverseOrder());

		for (String file : files) {
			totalWords = index.totalLocations().get(file);
			if (tempMap.containsKey(totalWords)) {
				tempMap.get(totalWords).add(file);
			} else {
				tempMap.put(totalWords, new ArrayList<>());
				tempMap.get(totalWords).add(file);
			}
		}
		for (Double score : tempMap.keySet()) {
			if (tempMap.get(score).size() > 1) {
				Collections.sort(tempMap.get(score));
			}
		}
		for (Double score : tempMap.keySet()) {
			for (String file : tempMap.get(score)) {
				temp.add(file);
			}
		}
		
		return temp;
	}
}