package tStringManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles composing and parsing of list/map-based information to Strings
 * <p>
 * eg:
 * <code>values{@value #pair}{@value #open}A{@value #sep} B{@value #sep} C{@value #close}</code>
 * <br>
 * eg:
 * <code>values{@value #pair}{@value #open}a{@value #pair}alice, b{@value #pair}bob, x{@value #pair}{@value #open}A{@value #sep} B{@value #sep} C{@value #close}{@value #close}</code>
 * 
 * @author Tallon Hodge
 */
public class TFilesInstance {
	/**
	 * A character that separates values in the String representation of a
	 * collection
	 * <p>
	 * eg: <code>KEY{@value #pair}VALUE</code>
	 */
	public final char pair;
	/**
	 * A character that separates values in the String representation of a
	 * collection
	 * <p>
	 * eg: <code>A{@value #sep} B{@value #sep} C</code>
	 */
	public final char sep;
	/**
	 * A character that marks the start of a group of values
	 * <p>
	 * eg: <code>{@value #open}values{@value #close}</code>
	 */
	public final char open;
	/**
	 * A character that marks the end of a group of values
	 * <p>
	 * eg: <code>{@value #open}values{@value #close}</code>
	 */
	public final char close;

	/**
	 * A character that marks text - formatting characters inside these will be
	 * ignored
	 * <p>
	 * eg: {@value #quote}the following open is ignored: {@value #open}
	 * {@value #quote}
	 */
	public final char quote;

	/**
	 * If using the default parameters, it might be easier to use a static
	 * {@link #TFiles}
	 */
	public TFilesInstance() {
		pair = ':';
		sep = ',';
		open = '{';
		close = '}';
		quote = '"';
	}

	public TFilesInstance(char pair, char sep, char open, char close, char quote) {
		this.pair = pair;
		this.sep = sep;
		this.open = open;
		this.close = close;
		this.quote = quote;
	}

	/**
	 * @return the key from a given pair
	 */
	public String getKey(String pair) {
		return pair.substring(0, pair.indexOf(this.pair));
	}

	/**
	 * @return the value from a given pair
	 */
	public String getVal(String pair) {
		String str = pair.substring(pair.indexOf(this.pair));
		if (str.length() > 1) {
			return str.substring(1);
		}
		return "";

	}

	/**
	 * Checks if a String contains an even number of non-quoted
	 * <code>{@value #open}</code>'s and <code>{@value #close}</code>'s
	 */
	public boolean isBalanced(String str) {
		int depth = 0;
		boolean inQuotes = false;
		for (char c : str.toCharArray()) {
			if (c == open) {
				if (!inQuotes) {
					depth++;
				}
				break;
			} else if (c == close) {
				if (!inQuotes) {
					depth--;
				}
				break;
			} else if (c == quote) {
				inQuotes = !inQuotes;
				break;
			}
		}
		return depth == 0;
	}

	/**
	 * @return if a String appears to be a valid pair <br>
	 *         Anything separated by a <code>{@value #pair}</code>
	 */
	public boolean isPair(String str) {
		boolean inQuotes = false;
		for (char c : str.toCharArray()) {
			if (c == quote) {
				inQuotes = !inQuotes;
			} else {
				if (!inQuotes && c == pair) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Creates a String representation of an array of Objects <br>
	 * The inverse of {@link #parseArray(String)}
	 */
	public void composeArray(Object[] arr) {
		composeList(Arrays.asList(arr));
	}

	/**
	 * Creates a String representation of a List of Objects <br>
	 * The inverse of {@link #parseList(String)}
	 * 
	 * @see java.util.List
	 */
	public String composeList(List<?> infos) {
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
	 * Creates a String representation of a List of Objects <br>
	 * The inverse of {@link #parseList(String)}
	 * 
	 * @see java.util.List
	 */
	public String composeList(Object... infos) {
		return composeList(Arrays.asList(infos));
	}

	/**
	 * Creates a String representation of a Map <br>
	 * The inverse of {@link #parseMap(String)}
	 * 
	 * @see java.util.Map
	 */
	public String composeMap(Map<?, ?> map) {
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
	 * Parses a LinkedHashMap from a String <br>
	 * The inverse of {@link #composeMap(Map)}
	 * 
	 * @see java.util.Map
	 * @see java.util.LinkedHashMap
	 */
	public Map<String, String> parseMap(String arr) {
		return parseMap(arr, 1);
	}

	private Map<String, String> parseMap(String arr, int maxDepth) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		List<String> ls = parseList(arr, maxDepth);
		for (String entry : ls) {
			map.put(getKey(entry), getVal(entry));
		}
		return map;
	}

	/**
	 * Parses an ArrayList from a String <br>
	 * The inverse of {@link #composeList(List)}
	 * 
	 * @see java.util.List
	 * @see java.util.ArrayList
	 */
	public List<String> parseList(String arr) {
		return parseList(arr, 1);
	}

	/**
	 * Parses an array from a String <br>
	 * The inverse of {@link #composeArray(Object[])}
	 */
	public String[] parseArray(String arr) {
		return (String[]) parseList(arr).toArray();
	}

	private List<String> parseList(String arr, int maxDepth) {
		List<String> vals = new ArrayList<String>();
		StringBuilder currentVal = new StringBuilder();
		int depth = 0;
		boolean inString = false;
		for (int i = 0; i < arr.length(); i++) {
			char c = arr.charAt(i);
			if (c == open) {
				if (!inString) {
					depth++;
					if (depth > maxDepth) {
						currentVal.append(open);
					}
				} else {
					currentVal.append(open);
				}
			} else if (c == close) {
				if (inString) {
					currentVal.append(close);
				} else {
					if (depth > maxDepth) {
						currentVal.append(close);
					}
					depth--;
				}
			} else if (c == sep) {
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
			} else if (c == quote) {
				currentVal.append(quote);
				if (arr.charAt(i - 1) != '\\') {
					inString = !inString;
				}
			} else if (c == '\n' || c == '\t' || c == ' ') {
				if (inString) {
					currentVal.append(c);
				}
			} else {
				currentVal.append(c);
				break;
			}
		}
		vals.add(currentVal.toString());
		return vals;
	}

	/**
	 * Creates the String equivalant of a Map.Entry <br>
	 * Consists of a key and a value separated by a {@value #pair}
	 * 
	 * @see java.util.Map
	 * @see java.util.Map.Entry
	 */
	public String toPair(Object key, Object info) {
		return key.toString() + pair + info.toString();
	}

	/**
	 * Wraps a given Object in open and close characters
	 * <p>
	 * <br>
	 * eg:
	 * <code> "Hello World" --> {@value #open}"Hello World"{@value #close} </code>
	 */
	public String wrap(Object obj) {
		return open + obj.toString() + close;
	}
}