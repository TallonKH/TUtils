package tStringManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public class TStringMaker {

	public final String template;
	private List<Object> parts = new ArrayList<Object>(); 
	
	public TStringMaker(String arr) {
		this.template = arr;
	}

	public String make(Object...args){
		StringBuilder builder = new StringBuilder();
		for(@SuppressWarnings("unused") Object o : parts){
			
		}
		return builder.toString();
	}
	
	public TStringMaker(String template, String[] groups, Map<String, String> namedGroups) {
		this.template = template;
	}

	public static void main(String args[]) {
		getGroups("(a|b|c)[~?] key:(sub:(d|e|f)[?]|(g|h|@1)[?])[?]").forEach((k, v) -> System.out.println(k + "\t:\t" + v));
	}

	public static void construct(String arr){
		
	}
	
	public static Map<String, String> construct(Map<String, String> groups, String arr) {
		char[] chars = arr.toCharArray();
		String currentKey = null;
		StringBuilder currentGroup = null;
		int depth = 0;
		boolean inQuotes = false;
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			switch (c) {
			case '"':
				if (i > 0 && chars[i - 1] == '\\') {
					if (depth != 0) {
						currentGroup.append('"');
					}
				} else {
					inQuotes = !inQuotes;
				}
				break;
			case '(':
				if (inQuotes || (i > 0 && chars[i - 1] == '\\')) {
					if (depth != 0) {
						currentGroup.append('(');
					}
				} else {
					if (depth == 0) {
						currentGroup = new StringBuilder();
						currentKey = null;
						if (i > 0 && chars[i - 1] == ':') {
							for (int i2 = i - 2; i2 >= 0; i2--) {
								char c2 = chars[i2];
								if ((c2 < 'a' || c2 > 'z') && (c2 < 'A' || c2 > 'Z') && c2 != '_') {
									currentKey = arr.substring(i2 + 1, i - 1);
									break;
								}
							}
							if (currentKey == null) {
								currentKey = arr.substring(0, i - 1);
							}
						}
					} else {
						currentGroup.append('(');
					}
					depth++;
				}

				break;
			case ')':
				if (inQuotes || (i > 0 && chars[i - 1] == '\\')) {
					if (depth != 0) {
						currentGroup.append(')');
					}
				} else {
					depth--;
					if (depth == 0) {
//						String accessor = "";
//						if (i < chars.length - 1 && chars[i + 1] == '[') {
//							accessor = arr.substring(i + 2, arr.indexOf(']', i + 2));
//						}
//						
						groups.put((currentKey == null ? String.valueOf(groups.size()) : currentKey),
								currentGroup.toString());
						getGroups(groups, currentGroup.toString());
					} else {
						if (depth != 0) {
							currentGroup.append(')');
						}
					}
				}
				break;
			case '\\':
				break;
			default:
				if (depth != 0) {
					currentGroup.append(c);
				}
			}
		}
		return groups;
	}
	
	public static Map<String, String> getGroups(String arr) {
		return getGroups(new LinkedHashMap<String, String>(), arr);
	}

	public static Map<String, String> getGroups(Map<String, String> groups, String arr) {
		char[] chars = arr.toCharArray();
		String currentKey = null;
		StringBuilder currentGroup = null;
		int depth = 0;
		boolean inQuotes = false;
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			switch (c) {
			case '"':
				if (i > 0 && chars[i - 1] == '\\') {
					if (depth != 0) {
						currentGroup.append('"');
					}
				} else {
					inQuotes = !inQuotes;
				}
				break;
			case '(':
				if (inQuotes || (i > 0 && chars[i - 1] == '\\')) {
					if (depth != 0) {
						currentGroup.append('(');
					}
				} else {
					if (depth == 0) {
						currentGroup = new StringBuilder();
						currentKey = null;
						if (i > 0 && chars[i - 1] == ':') {
							for (int i2 = i - 2; i2 >= 0; i2--) {
								char c2 = chars[i2];
								if ((c2 < 'a' || c2 > 'z') && (c2 < 'A' || c2 > 'Z') && c2 != '_') {
									currentKey = arr.substring(i2 + 1, i - 1);
									break;
								}
							}
							if (currentKey == null) {
								currentKey = arr.substring(0, i - 1);
							}
						}
					} else {
						currentGroup.append('(');
					}
					depth++;
				}

				break;
			case ')':
				if (inQuotes || (i > 0 && chars[i - 1] == '\\')) {
					if (depth != 0) {
						currentGroup.append(')');
					}
				} else {
					depth--;
					if (depth == 0) {
//						String accessor = "";
//						if (i < chars.length - 1 && chars[i + 1] == '[') {
//							accessor = arr.substring(i + 2, arr.indexOf(']', i + 2));
//						}
//						
						groups.put((currentKey == null ? String.valueOf(groups.size()) : currentKey),
								currentGroup.toString());
						getGroups(groups, currentGroup.toString());
					} else {
						if (depth != 0) {
							currentGroup.append(')');
						}
					}
				}
				break;
			case '\\':
				break;
			default:
				if (depth != 0) {
					currentGroup.append(c);
				}
			}
		}
		return groups;
	}
}

// Group Creation: key:(a|b|c)type a:(a|b|c)[ ]
// Group Usage: @key @a
// End Key Call: @key+etc @a+bcde
// Random Group: (a|b|c)[?] a:(a|b|c)[?]
// Random Group Persistant: (a|b|c)[~?] a:(a|b|c)[~?]
// Conditional Group: (a|b)[#arg index]
// Switch Group: (a|b|c)[#arg index]
// Repeat Group: (a)[<number of repeats>]