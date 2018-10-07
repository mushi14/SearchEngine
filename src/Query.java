public class Query {

	public String location;
	public double totalMatches;
	public double totalWords;
	public String score;

	public Query(String loc, double matches, double words, String sc) {
		this.location = loc;
		this.totalMatches = matches;
		this.totalWords = words;
		this.score = sc;
	}

	public String getLocation() {
		return location;
	}

	public double getMatches() {
		return totalMatches;
	}

	public double getWords() {
		return totalWords;
	}

	public String getScore() {
		return score;
	}
	
	@Override
	public String toString() {
		return "Location: " + location + " Score: " + score + " Matches: " + totalMatches + " Words: " + totalWords;
	}
}
