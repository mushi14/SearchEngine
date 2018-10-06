import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class Search {
	
//	private static final Map<String, Double> scoresMap = new TreeMap<>();
//	private static final Hashtable<Double, List<String>> scores = new Hashtable<>();
//	private static final Map<String, Hashtable<Double, List<String>>> map = new TreeMap<>();
//	private static final List<String> seen = new ArrayList<>();
	
	static Map<String, Map<Double, List<String>>> map = new TreeMap<>();
	
	static double totalMatches = 0;
	static double totalWords = 0;
	static double score = 0;
	
	
	
	public static Map<String, Map<Double, List<String>>> score(InvertedIndex index, Set<String> queries) {
		DecimalFormat FORMATTER = new DecimalFormat("0.000000");
		boolean contains = false;
		Map<Double, List<String>> scores = new TreeMap<>(Collections.reverseOrder());

		
		for (String file : index.totalLocations().keySet()) {
			for (String query : queries) {
				if (index.get(query).containsKey(file)) {
					contains = true;
					totalWords = index.totalLocations().get(file);
					totalMatches += index.get(query, file).size();
					score = Double.valueOf(FORMATTER.format(totalMatches / totalWords));
				}
			}
			if (contains == true) {
				if (scores.containsKey(score)) {
					scores.get(score).add(file);
				} else {
					scores.put(score, new ArrayList<>());
					scores.get(score).add(file);
				}
			}
			contains = false;
			totalMatches = 0;
			totalWords = 0;
			score = 0;
		}

		Map<Double, List<String>> newScores = new TreeMap<>(scores);
		for (Double score : scores.keySet()) {
			if (scores.get(score).size() > 1) {
				newScores.put(score ,compareTo(scores.get(score), index));
			}
		}
		newScores.putAll(scores);

		String temp = "";
		for (String query : queries) {
			temp += query + " ";
		}
		map.put(temp, scores);
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
	

	public static void printMap() {
		System.out.println(map.toString());
	}



	
	
	
	
//	public static void printScoresMap() {
//		System.out.println(scoresMap.toString());
//	}
//	
//	public static void printMap() {
//		System.out.println(map.toString());
//	}
	
//	public static Map<Double, List<String>> score(InvertedIndex index, TreeSet<String> queries) {
//		double totalWords = 0;
//		double totalMatches = 0;
//		double score = 0;
//
//
//		for (String location : index.totalLocations().keySet()) {
//			totalWords = index.totalLocations().get(location);
//			List<String> temp = new ArrayList<>();
//
//			for (String query : queries) {
//				if (index.containsWord(query)) {
//					if (index.get(query).containsKey(location)) {
//						temp.add(query);
//					}
//				}
//			}
//			
//			for (String query : temp) {
//				if (!seen.contains(query)) {
//					totalMatches += index.get(query, location).size();
//					score = Double.valueOf(FORMATTER.format(totalMatches / totalWords));
//					seen.add(query);
//				} 
//			}
//			
//			if (score != 0) {
//				if (scores.containsKey(score)) {
//					scores.get(score).add(location);
//				} else {
//					scores.put(score, new ArrayList<String>());
//					scores.get(score).add(location);
//				}
//			}
//			totalMatches = 0;
//		}
//
//		for (Double s : scores.keySet()) {
//			if (scores.get(s).size() > 1) {
//				scores.replace(s, compareScores(index, scores.get(s)));
//			}
//		}
//		return scores;
//	}
//
//	public static List<String> compareScores(InvertedIndex index, List<String> duplicateScores) {
//		TreeSet<Integer> totalWords = new TreeSet<>(Collections.reverseOrder());
//		List<String> sorted = new ArrayList<>();
//		List<String> sortedCopy = new ArrayList<>();
//
//		for (String location : duplicateScores) {
//			totalWords.add(index.totalLocations().get(location));
//		}
//
//		int range = totalWords.first();
//		boolean[] seen = new boolean[range + 1];
//
//		for (Integer wordCount : totalWords) {
//			for (int i = 0; i < duplicateScores.size(); i++) {
//				if (index.totalLocations().get(duplicateScores.get(i)) == wordCount) {
//					if (seen[wordCount] == true) {
//						for (int j = 0; j < sorted.size(); j++) {
//							if (index.totalLocations().get(sorted.get(j)) == wordCount) {
//								String temp = sortedCopy.get(j);
//								int a = sortedCopy.get(j).toLowerCase().compareTo(duplicateScores.get(i).toLowerCase());
//								if (a > 0) {
//									sortedCopy.set(j, duplicateScores.get(i));
//									sortedCopy.add(temp);
//								} else {
//									sortedCopy.add(duplicateScores.get(i));
//								}
//							}
//						}
//						sorted.clear();
//						sorted.addAll(sortedCopy);
//					} else {
//						sorted.add(duplicateScores.get(i));
//						sortedCopy.add(duplicateScores.get(i));
//						seen[wordCount] = true;
//					}
//				}
//			}
//		}
//		return sorted;
//	}
//	
//	
//	public static double get(String location) {
//		return countsMap.get(location); 
//	}
//	
//	
//	public static void exactSearch(InvertedIndex index, TreeSet<String> queries) {
//		score(index, queries);
//	}
	
//
//	public static void PartialSearch() {
//
//	}
	
	
	
	
	
	
	
	
//	public static void score(InvertedIndex index, TreeSet<String> queries) {
//
//		DecimalFormat FORMATTER = new DecimalFormat("0.000000");
//		double totalWords = 0;
//		double totalMatches = 0;
//		double score = 0;
//		
//		boolean contains = false;
//
//		double temp = 0;
//		for (String loc : index.totalLocations().keySet()) {
//			for (String q : queries) {
//				if (index.get(q).containsKey(loc)) {
//					contains = true;
//					map.put(q, null);
//					if (!scoresMap.containsKey(q)) {
//						totalMatches += index.get(q, loc).size();
//						totalWords = index.totalLocations().get(loc);
//						score = Double.valueOf(FORMATTER.format(totalMatches / totalWords));
//					} else {
//						score = scoresMap.get(q);
//					}
//					temp = score;
//				} 
//			}
//			
//			for (String word : map.keySet()) {
//				
//			}
//			if (contains == true) {
//				if (scores.containsKey(score)) {
//					if (!scores.get(score).contains(loc)) {
//						scores.get(score).add(loc);
//					}
//				} else {
//					scores.put(score, new ArrayList<>());
//					scores.get(score).add(loc);
//				}
//				
//				for (String q : queries) {
//					scoresMap.put(q, score);
//				}
//			}
//			contains = false;
//		}
//	}
}