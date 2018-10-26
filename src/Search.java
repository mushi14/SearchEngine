import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;

public class Search {

	private String location;
	private double totalMatches;
	private double totalWords;
	private double rawScore;
	private String score;

	/**
	 * Constructor for the Query class
	 * @param loc location that the query word is found in
	 * @param matches number matches in the location
	 * @param words total number of words in the location
	 * @param rs raw score of the location
	 * @param sc rounded score of the location
	 */
	public Search(String loc, double matches, double words, double rs, String sc) {
		this.location = loc;
		this.totalMatches = matches;
		this.totalWords = words;
		this.rawScore = rs;
		this.score = sc;
	}

	/**
	 * Gets the location
	 * @return location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Gets the total matches of the location
	 * @return total number of matches in the location
	 */
	public double getMatches() {
		return totalMatches;
	}

	/**
	 * Gets the total words of the location
	 * @return total words in the location
	 */
	public double getWords() {
		return totalWords;
	}

	/**
	 * Gets the raw score of the results in the location
	 * @return raw score
	 */
	public double getRawScore() {
		return rawScore;
	}

	/**
	 * Gets the rounded score of the results in the location
	 * @return rounded score
	 */
	public String getScore() {
		return score;
	}

	/**
	 * Converts the score to 15 decimal points in order to better the score comparison of the results
	 * @param score score to convert to 15 decimal point
	 * @return score formatted with 15 decimal points
	 */
	public static double round(double score) {
		return BigDecimal.valueOf(score)
			.setScale(15, RoundingMode.HALF_UP)
			.doubleValue();
	}

	/**
	 * Inner class that implements the comparator interface
	 * @author mushahidhassan
	 */
	static class Comparison implements Comparator<Search> {
		/**
		 * sorts a list of queries in descending order by their raw score, if score is the same, sorts by
		 * total number of words in a query location, if number of words the same, then sorts alphabetically (case insensitively)
		 */
		@Override
		public int compare(Search o1, Search o2) {
			if (o1.getRawScore() > o2.getRawScore()) {
				return -1;
			} else if (o1.getRawScore() < o2.getRawScore()) {
				return 1;
			} else {
				if (o1.getWords() > o2.getWords()) {
					return -1;
				} else if (o1.getWords() < o2.getWords()) {
					return 1;
				} else {
					return o1.getLocation().compareToIgnoreCase(o2.getLocation());
				}
			}
		}
	}

	/**
	 * Overridden toString method
	 */
	@Override
	public String toString() {
		return "Location: " + location + " Score: " + score + " Matches: " + totalMatches + " Words: " + totalWords;
	}
}