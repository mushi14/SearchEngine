public class Query {

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
	public Query(String loc, double matches, double words, double rs, String sc) {
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
	 * Overridden toString method
	 */
	@Override
	public String toString() {
		return "Location: " + location + " Score: " + score + " Matches: " + totalMatches + " Words: " + totalWords;
	}
}

