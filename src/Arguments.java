
public class Arguments {
	
	
	/* Method for checking to see if the argument is a flag */
	public static boolean isFlag(String arg) {
		try {
			arg = arg.trim();
			
			if (arg.length() >= 2) {
				if (arg.charAt(0) == '-' && arg.charAt(1) != ' ') {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	
	/* Method for checking to see if the argument is a value */
	public static boolean isValue(String arg) {
		try {
			arg = arg.trim();

			if (!arg.isEmpty() && arg.charAt(0) != '-') {
				return true;
			} else {
				return false;
			}
		} catch(NullPointerException e) {
			return false;
		}
	}

}
