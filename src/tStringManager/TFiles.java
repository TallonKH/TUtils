package tStringManager;

import java.util.*;

/**
 * Handles composing and parsing of list/map-based information to Strings
 * <p>
 * eg: <code>values{@value #pair}{@value #open}A{@value #sep} B{@value #sep} C{@value #close}</code>
 * <br>
 * eg: <code>values{@value #pair}{@value #open}a{@value #pair}alice, b{@value #pair}bob, x{@value #pair}{@value #open}A{@value #sep} B{@value #sep} C{@value #close}{@value #close}</code>
 * @author Tallon Hodgeop
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class TFiles {

	public static void main(String args[]){
		String test = "{name:Grass,opaque=true,textureFormat=single}";
		System.out.println(parseList(test));
	}
	/**
	 * A character that separates values in the String representation of a collection
	 * <p>
	 * eg: <code>KEY{@value #pair}VALUE</code>
	 */
	public static final char pair = ':';
	/**
	 * A character that separates values in the String representation of a collection
	 * <p>
	 * eg: <code>A{@value #sep} B{@value #sep} C</code>
	 */
	public static final char sep = ',';
	/**
	 * A character that marks the start of a group of values
	 * <p>
	 * eg: <code>{@value #open}values{@value #close}</code>
	 */
	public static final char open = '{';
	/**
	 * A character that marks the end of a group of values
	 * <p>
	 * eg: <code>{@value #open}values{@value #close}</code>
	 */
	public static final char close = '}';

	/**
	 * A character that marks text - formatting characters inside these will be ignored
	 * <p>
	 * eg: {@value #quote}the following open is ignored: {@value #open} {@value #quote}
	 */
	public static final char quote = '"';
	
	/**
	 * @return the key from a given pair
	 */
	public static String getKey(String pair) {
		return pair.substring(0, pair.indexOf(TFiles.pair));
	}

	/**
	 * @return the value from a given pair
	 */
	public static String getVal(String pair) {
		String str = pair.substring(pair.indexOf(TFiles.pair));
		if (str.length() > 1) {
			return str.substring(1);
		}
		return "";

	}

	/**
	 * Checks if a String contains an even number of non-quoted <code>{@value #open}</code>'s and <code>{@value #close}</code>'s
	 */
	public static boolean isBalanced(String str) {
		int depth = 0;
		boolean inQuotes = false;
		for (char c : str.toCharArray()) {
			switch (c) {
			case open:
				if (!inQuotes) {
					depth++;
				}
				break;
			case close:
				if (!inQuotes) {
					depth--;
				}
				break;
			case quote:
				inQuotes = !inQuotes;
				break;
			}
		}
		return depth == 0;
	}

	/**
	 * @return if a String appears to be a valid pair
	 * <br>
	 * Anything separated by a <code>{@value #pair}</code>
	 */
	public static boolean isPair(String str) {
		boolean inQuotes = false;
		for(char c : str.toCharArray()){
			if(c == quote){
				inQuotes = !inQuotes;
			}else{
				if(!inQuotes && c == pair){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Creates a String representation of a List of Objects
	 * <br>
	 * The inverse of {@link #parseList(String)}
	 * @see java.util.List
	 */
	public static String composeList(List<?> infos) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < infos.size(); i++) {
			result.append(infos.get(i).toString());
			if (i < infos.size() - 1) {
				result.append(sep);
			}
		}
		return wrap(result);
	}

	/**
	 * Creates a String representation of a List of Objects
	 * <br> 
	 * The inverse of {@link #parseList(String)}
	 * @see java.util.List
	 */
	public static String composeList(Object... infos) {
		return composeList(Arrays.asList(infos));
	}

	/**
	 * Creates a String representation of a Map
	 * <br> 
	 * The inverse of {@link #parseMap(String)}
	 * @see java.util.Map
	 */
	public static String composeMap(Map<?, ?> map) {
		StringBuilder result = new StringBuilder();
		Object[] keys = map.keySet().toArray();
		for (int i = 0; i < map.size(); i++) {
			Object key = keys[i];
			result.append(toPair(key, map.get(key)));
			if (i < map.size() - 1) {
				result.append(sep);
			}
		}
		return wrap(result);
	}

	/**
	 * Parses a LinkedHashMap from a String
	 * <br> 
	 * The inverse of {@link #composeMap(Map)}
	 * @see java.util.Map
	 * @see java.util.LinkedHashMap
	 */
	public static Map<String, String> parseMap(String arr) {
		return parseMap(arr, 1);
	}

	public static Map<String, String> parseMap(String arr, int maxDepth) {
		Map<String, String> map = new LinkedHashMap<>();
		List<String> ls = parseList(arr, maxDepth);
		for (String entry : ls) {
			map.put(getKey(entry), getVal(entry));
		}
		return map;
	}

	/**
	 * Parses an ArrayList from a String
	 * <br>
	 * The inverse of {@link #composeList(List)}
	 * @see java.util.List
	 * @see java.util.ArrayList
	 */
	public static List<String> parseList(String arr) {
		return parseList(arr, 1);
	}

	public static List<String> parseList(String arr, int maxDepth) {
		List<String> vals = new ArrayList<>();
		StringBuilder currentVal = new StringBuilder();
		int depth = 0;
		boolean inString = false;
		char[] strChars = arr.toCharArray();
		for (int i = 0; i < arr.length(); i++) {
			char c = strChars[i];
			switch (c) {
			case open:
				if (!inString) {
					depth++;
					if (depth > maxDepth) {
						currentVal.append(open);
					}
				} else {
					currentVal.append(open);
				}
				break;
			case close:
				if (inString) {
					currentVal.append(close);
				} else {
					if (depth > maxDepth) {
						currentVal.append(close);
					}
					depth--;
				}
				break;
			case sep:
				if (!inString) {
					if (depth > maxDepth) {
						currentVal.append(sep);
					} else {
						vals.add(currentVal.toString());
						currentVal = new StringBuilder();
					}
				} else {
					currentVal.append(sep);
				}
				break;
			case quote:
				currentVal.append(quote);
				if (arr.charAt(i - 1) != '\\') {
					inString = !inString;
				}
				break;
			case '\n':
			case '\t':
			case ' ':
				if (!inString) {
					break;
				}
			default:
				currentVal.append(c);
				break;
			}
		}
		vals.add(currentVal.toString());
		return vals;
	}

	/**
	 * Creates the String equivalant of a Map.Entry
	 * <br>
	 * Consists of a key and a value separated by a {@value #pair}
	 * @see java.util.Map
	 * @see java.util.Map.Entry
	 */
	public static String toPair(Object key, Object info) {
		return key.toString() + pair + info.toString();
	}

	/**
	 * Wraps a given Object in open and close characters
	 * <p>
	 * <br>eg: <code> "Hello World" --> {@value #open}"Hello World"{@value #close} </code>
	 */
	public static String wrap(Object obj) {
		return open + obj.toString() + close;
	}
}