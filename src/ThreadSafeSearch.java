
public class ThreadSafeSearch extends Search{

	private ReadWriteLock lock;

	/**
	 * Constructor for the Query class
	 * @param loc location that the query word is found in
	 * @param matches number matches in the location
	 * @param words total number of words in the location
	 * @param rs raw score of the location
	 * @param sc rounded score of the location
	 */
	public ThreadSafeSearch(String loc, double matches, double words, double rs, String sc) {
		super(loc, matches, words, rs, sc);
		lock = new ReadWriteLock();
	}

	/**
	 * Gets the location
	 * @return location
	 */
	public String getLocation() {
		lock.lockReadOnly();
		try {
			return super.getLocation();
		} finally {
			lock.unlockReadOnly();
		}
	}

	/**
	 * Gets the total matches of the location
	 * @return total number of matches in the location
	 */
	public double getMatches() {
		lock.lockReadOnly();
		try {
			return super.getMatches();
		} finally {
			lock.unlockReadOnly();
		}
	}

	/**
	 * Gets the total words of the location
	 * @return total words in the location
	 */
	public double getWords() {
		lock.lockReadOnly();
		try {
			return super.getWords();
		} finally {
			lock.unlockReadOnly();
		}
	}

	/**
	 * Gets the raw score of the results in the location
	 * @return raw score
	 */
	public double getRawScore() {
		lock.lockReadOnly();
		try {
			return super.getRawScore();
		} finally {
			lock.unlockReadOnly();
		}
	}

	/**
	 * Gets the rounded score of the results in the location
	 * @return rounded score
	 */
	public String getScore() {
		lock.lockReadOnly();
		try {
			return super.getScore();
		} finally {
			lock.unlockReadOnly();
		}
	}
}
