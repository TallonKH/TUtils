package tMethods;

import java.util.*;
import java.lang.reflect.*;

/**
 * This class contains miscellaneous static methods
 *
 * @author Tallon Hodge
 */
public class TUtil {
	static Random rand = new Random();

	public static List<String> quickSplit(String str, char c) {
		List<String> vals = new ArrayList<>();
		int prev = 0;
		int next = 0;
		char[] chars = str.toCharArray();
		for (; next < str.length(); next++) {
			if (chars[next] == c) {
				vals.add(str.substring(prev, next));
				next += 1;
				prev = next;
			}
		}
		vals.add(str.substring(prev));
		return vals;
	}

	/**
	 * Shuffles the items in a list in-place.
	 * <p>Implements Fisher-Yates shuffle.
	 *
	 * @param items
	 */
	public static void shuffle(List<Object> items) {
		for (int i = items.size() - 1; i > 1; i--) {
			Object c = items.get(i);
			items.set(i, items.set(rand.nextInt(i + 1), c));
		}
	}

	/**
	 * @return a list of the values of a field from an Iterable of objects
	 */
	public <T, K> List<K> fieldList(Iterable<T> list, String fieldName, Class<K> fieldType) {
		List<K> vals = new ArrayList<>();
		try {
			for (T obj : list) {
				vals.add((K) obj.getClass().getField(fieldName).get(obj));
			}
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return vals;
	}
}
