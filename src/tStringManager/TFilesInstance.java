package tStringManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TFilesInstance {
	public final char pair;
	public final char sep;
	public final char open;
	public final char close;
	public final char quote;

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

	public String getKey(String pair) {
		return pair.substring(0, pair.indexOf(this.pair));
	}

	public String getVal(String pair) {
		String str = pair.substring(pair.indexOf(this.pair));
		if (str.length() > 1) {
			return str.substring(1);
		}
		return "";

	}

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

	public String composeList(Object... infos) {
		return composeList(Arrays.asList(infos));
	}

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

	public List<String> parseList(String arr) {
		return parseList(arr, 1);
	}

	private List<String> parseList(String arr, int maxDepth) {
		List<String> vals = new ArrayList<String>();
		StringBuilder currentVal = new StringBuilder();
		int depth = 0;
		boolean inString = false;
		char chars[] = arr.toCharArray();
		for (int i = 0; i < arr.length(); i++) {
			char c = chars[i];
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

	public String toPair(Object key, Object info) {
		return key.toString() + pair + info.toString();
	}

	public String wrap(Object obj) {
		return open + obj.toString() + close;
	}
}