import java.text.DecimalFormat;
import java.util.Comparator;

public class Search implements Comparable<Search> {

	private DecimalFormat FORMATTER;
	private final String location;
	private int totalMatches;
	private final int totalWords;
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
	public Search(String loc, int matches, int words) {
		FORMATTER = new DecimalFormat("0.000000"); 
		this.location = loc;
		this.totalMatches = matches;
		this.totalWords = words;
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
	public int getMatches() {
		return totalMatches;
	}

	/**
	 * Gets the total words of the location
	 * @return total words in the location
	 */
	public int getWords() {
		return totalWords;
	}

	public void calculate(int matches) {
		this.totalMatches = matches;
		this.rawScore = Double.valueOf(this.totalMatches) / Double.valueOf(this.totalWords);
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
		this.score = FORMATTER.format(this.rawScore);
		return score;
	}

//	// TODO Remove...
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
	
	@Override
	public int compareTo(Search o) {
		if (this.getRawScore() > o.getRawScore()) {
			return -1;
		} else if (this.getRawScore() < o.getRawScore()) {
			return 1;
		} else {
			if (this.getWords() > o.getWords()) {
				return -1;
			} else if (this.getWords() < o.getWords()) {
				return 1;
			} else {
				return this.getLocation().compareToIgnoreCase(o.getLocation());
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