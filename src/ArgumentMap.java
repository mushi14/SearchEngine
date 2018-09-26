import java.util.HashMap;
import java.util.Map;

public class ArgumentMap {

	private final Map<String, String> argMap;
	
	/**
	 * Initializes this argument map.
	 */
	public ArgumentMap() {
		argMap = new HashMap<String, String>();
	}

	/**
	 * Initializes this argument map and then parsers the arguments into
	 * flag/value pairs where possible. Some flags may not have associated values.
	 * If a flag is repeated, its value is overwritten.
	 *
	 * @param args
	 */
	public ArgumentMap(String[] args) {
		argMap = new HashMap<String, String>();
		parse(args);
	}

	/**
	 * Parses the arguments into flag/value pairs where possible. Some flags may
	 * not have associated values. If a flag is repeated, its value is
	 * overwritten.
	 *
	 * @param args the command line arguments to parse
	 */
	public void parse(String[] args) {		
		for (int i = 0; i < args.length; i++) {
			if (isFlag(args[i])) {
				if ((i + 1) < args.length) {
					if (!isFlag(args[i + 1])) {
						argMap.put(args[i], args[i + 1]);
						i++;
					} else {
						argMap.put(args[i], null);
					}
				} else {
					argMap.put(args[i], null);
				}
			} 
		}
	}

	/** Checks to see if argument passed is a valid flag or not 
	 * 
	 * @param arg argument from the command line
	 * @return true if the argument is -path or -index
	 * 
	 */
	public static boolean isFlag(String arg) {
		if (arg.isEmpty()) { 
			return false;
		} else {
			try {
				arg = arg.trim();
				if (arg.equals("-path")) {
					return true;
				} else if (arg.equals("-index")) {
					return true;
				} else {
					return false;
				}
			} catch (NullPointerException e) {
				return false;
			}
		}
	}

	/**
	 * Determines whether the specified flag exists.
	 *
	 * @param flag the flag to search for
	 * @return {@code true} if the flag exists
	 */
	public boolean hasFlag(String flag) {
		return argMap.containsKey(flag);
	}

	/**
	 * Determines whether the specified flag is mapped to a non-null value.
	 *
	 * @param flag the flag to search for
	 * @return {@code true} if the flag is mapped to a non-null value
	 */
	public boolean flagPath(String flag) {
		if (hasFlag(flag)) {
			if (getPath(flag) == null) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * Returns the value to which the specified flag is mapped as a
	 * {@link String}, or null if there is no mapping for the flag.
	 *
	 * @param flag the flag whose associated value is to be returned
	 * @return the value to which the specified flag is mapped, or {@code null} if
	 *         there is no mapping for the flag
	 */
	public String getPath(String flag) {
		return argMap.get(flag);
	}

	/**
	 * Returns the value to which the specified flag is mapped as a
	 * {@link String}, or the default value if there is no mapping for the flag.
	 *
	 * @param flag         the flag whose associated value is to be returned
	 * @param defaultValue the default value to return if there is no mapping for
	 *                     the flag
	 * @return the value to which the specified flag is mapped, or the default
	 *         value if there is no mapping for the flag
	 */
//	public String getJsonPath(String indexFlag, String defaultPath) {
//		if (argMap.get(indexFlag) != null) {
//			return argMap.get(indexFlag);
//		} else {
//			return defaultPath;
//		}
//	}

//	/**
//	 * Returns the value to which the specified flag is mapped as a {@link Path},
//	 * or {@code null} if unable to retrieve this mapping for any reason
//	 * (including being unable to convert the value to a {@link Path} or no value
//	 * existing for this flag).
//	 *
//	 * This method should not throw any exceptions!
//	 *
//	 * @param flag the flag whose associated value is to be returned
//	 * @return the value to which the specified flag is mapped, or {@code null} if
//	 *         unable to retrieve this mapping for any reason
//	 *
//	 * @see Paths#get(String, String...)
//	 */
//	public Path getFilePath(String flag) {
//		Path path;		
//		if (argMap.containsKey(flag)) {
//			if (argMap.get(flag) != null) {
//				path = Paths.get(argMap.get(flag));
//			} else {
//				path = null;
//			}
//		} else {
//			path = null;
//		}
//		return path;
//	}

//	/**
//	 * Returns the value to which the specified flag is mapped as a {@link Path},
//	 * or the default value if unable to retrieve this mapping for any reason
//	 * (including being unable to convert the value to a {@link Path} or no value
//	 * existing for this flag).
//	 *
//	 * This method should not throw any exceptions!
//	 *
//	 * @param flag         the flag whose associated value is to be returned
//	 * @param defaultValue the default value to return if there is no mapping for
//	 *                     the flag
//	 * @return the value to which the specified flag is mapped as a {@link Path},
//	 *         or the default value if there is no mapping for the flag
//	 */
//	public Path getJsonPath(String flag, Path defaultValue) {
//		Path path;	
//		if (isFlag(flag)) {
//			if (argMap.containsKey(flag)) {
//				if (argMap.get(flag) != null) {
//					path = Paths.get(argMap.get(flag));
//				} else {
//					path = defaultValue;
//				}
//			}
//		}
//		return path;
//	}
//
//	@Override
//	public String toString() {
//		return this.map.toString();
//	}
}