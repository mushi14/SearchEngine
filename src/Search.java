import java.text.DecimalFormat;

public class Search implements Comparable<Search> {

	private static final DecimalFormat FORMATTER = new DecimalFormat("0.000000");
	private final String location;
	private int totalMatches;
	private final int totalWords;
	private double rawScore;

	/**
	 * Constructor for the Query class
	 * @param loc location that the query word is found in
	 * @param matches number matches in the location
	 * @param words total number of words in the location
	 * @param rs raw score of the location
	 * @param sc rounded score of the location
	 */
	public Search(String loc, int matches, int words) {
		this.location = loc;
		this.totalMatches = matches;
		this.totalWords = words;
		this.rawScore = Double.valueOf(this.totalMatches) / Double.valueOf(this.totalWords);
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

	/**
	 * Calculates or updates the raw score of the search object
	 * @param matches updated number of matches
	 */
	public void calculate(int matches) {
		this.totalMatches += matches;
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
		return FORMATTER.format(this.rawScore);
	}

	/**
	 * Compares the search results by their scores. If scores are the same, then compares by the total number of words
	 * in the file. If that's the same as well, sorts alphabetically 
	 */
	@Override
	public int compareTo(Search o) {
		int result = Double.compare(o.rawScore, this.rawScore);

		if (result == 0) {
			result = Double.compare(o.totalWords, this.totalWords);

			if (result == 0) {
				result = this.location.compareToIgnoreCase(o.location);
			}
		}

		return result;
	}

	/**
	 * Overridden toString method
	 */
	@Override
	public String toString() {
		return "Location: " + this.getLocation() + " Score: " + this.getScore() + 
				" Matches: " + this.getMatches() + " Words: " + this.getWords();
	}
}