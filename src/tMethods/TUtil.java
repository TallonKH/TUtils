package tMethods;

import java.util.ArrayList;
import java.util.Random;
import java.lang.reflect.*;

/**
 * This class contains miscellaneous static methods
 * @author Tallon Hodge
 *
 */
public class TUtil {
	static Random rand = new Random();
	/**
	 * Shuffles the items in an arraylist in-place.
	 * <p>Implements Fisher-Yates shuffle.
	 * @param items
	 */
	public static void shuffle(ArrayList<Object> items){
		for(int i=items.size()-1; i>1; i--){
			Object c = items.get(i);
			int r = rand.nextInt(i+1);
			items.set(i, items.get(r));
			items.set(r, c);
		}
	}

	/**
	 * Shuffles the items in an array in-place.
	 * <p>Implements Fisher-Yates shuffle.
	 * @param items
	 */
	public static void shuffle(Object[] items){
		for(int i=items.length-1; i>1; i--){
			Object c = items[i];
			int r = rand.nextInt(i+1);
			items[i] = items[r];
			items[r] = c;
		}
	}

	/**
	 *
	 * @return
	 * @return an array of the values of a field from an array of objects
	 */
	public Object[] fieldArray(Class<?> objType, Object[] array, String fieldName){
		int l=array.length;
		Object[] vals = new Object[l];
		try {
			Field f = objType.getField(fieldName);
			for(int i=0; i<l; i++){
				vals[i] = (f.get(array[i]));
			}
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return vals;
	}

	/**
	 *
	 * @return an arraylist of the values of a field from an arraylist of objects
	 */
	public ArrayList<?> fieldArrayList(Class<?> objType, ArrayList<?> array, String fieldName){
		int l = array.size();
		ArrayList<Object> vals = new ArrayList<Object>();
		try {
			Field f = objType.getField(fieldName);
			for(int i=0; i<l; i++){
				vals.add(f.get(array.get(i)));
			}
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return vals;
	}
}
