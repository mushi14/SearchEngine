
public class ArgumentMap {

	public void parse(String[] args) {
		
	}
	
	
	
	public static boolean isFlag(String arg) {
		try {
			arg = arg.trim();
			if (arg == "-path") {
				/* Do something */
				return true;
			} else if (arg == "-index") {
				/* Do something */
				return true;
			} else {
				return false;
			}
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	
	
}
