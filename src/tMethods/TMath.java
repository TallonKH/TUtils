package tMethods;

import java.util.ArrayList;
import java.util.Random;

import tPoints.*;

/**
 * Some useful math-and-vector-related methods
 *
 * @author Tallon Hodge
 */
public class TMath {
	private static Random rand = new Random();

	/**
	 * Defines how interpolation should be handled if alpha < 0 or alpha > 1
	 */
	public static enum LoopModes {
		/**
		 * Allow the return value to exceed min and max
		 */
		NONE,
		/**
		 * alpha = alpha % 1
		 */
		LOOP,
		/**
		 * alpha climbs up and down between 0 and 1 like a triangle wave
		 * function
		 * <p>
		 * eg: 0, 0.5, 1, 0.5, 0
		 * 
		 * @see tMethods.TMath#triangleWave(double, double) triangleWave(alpha,
		 *      1.0)
		 */
		BOUNCE,
		/**
		 * Clamp the alpha between 0 and 1
		 */
		STOP
	}

	/**
	 * @return if the difference between two doubles is below a certain
	 *         threshold 0.00001
	 */
	public static boolean aproxEqual(double a, double b) {
		return Math.abs(a - b) <= 0.00001;
	}

	/**
	 * @return if the difference between two doubles is below a certain
	 *         threshold
	 */
	public static boolean aproxEqual(double a, double b, double maxDifference) {
		return Math.abs(a - b) <= maxDifference;
	}

	/**
	 * @return a byte from a String of bits
	 */
	public static byte binaryToByte(String bin) {
		byte a = 0;
		for (int i = 0; i < 8; i++) {
			a += bin.charAt(i) == '0' ? 0 : Math.pow(2, 7 - i);
		}
		return a;
	}

	/**
	 * @return a specific bit in a byte
	 */
	public static boolean bitAt(byte a, int i) {
		return ((a >> 7 - i) & 1) == 1;
	}

	/**
	 * @return if a > max, return max</br>
	 *         if a < min, return min </br>
	 *         returns a if min > max
	 */
	public static double clamp(final double a, final double min, final double max) {
		return min < max ? (a > max ? max : (a < min ? min : a)) : a;
	}

	/**
	 * @return if a > max, return max</br>
	 *         if a < min, return min </br>
	 *         returns a if min > max
	 */
	public static int clamp(final int a, final int min, final int max) {
		return min < max ? (a > max ? max : (a < min ? min : a)) : a;
	}

	public static byte digitAt(double number, int digit) {
		double div = Math.pow(10, digit + 1);
		return (byte) ((number % (div)) / (div / 10));
	}

	/**
	 * x climbs linearly up and down from 0 to max
	 * <p>
	 * eg: max = 10, <BLOCKQUOTE>if x = 9, return 9 <br>
	 * if x = 10, return 10<br>
	 * if x = 11, return 9<br>
	 * if x = 12, return 8</BLOCKQUOTE>
	 */
	public static double flatWave(double x, final double max) {
		return (2 * Math.abs(Math.round(0.5 * x) - 0.5 * x)) * max;
	}

	/**
	 * @return the largest double in a given array
	 */
	public static double largest(double[] nums) {
		double l = Double.MIN_VALUE;
		for (double d : nums) {
			if (d > l) {
				l = d;
			}
		}
		return l;
	}

	/**
	 * @return the largest int in a given array
	 */
	public static int largestInt(int[] nums) {
		int l = Integer.MIN_VALUE;
		for (int d : nums) {
			if (d > l) {
				l = d;
			}
		}
		return l;
	}

	/**
	 * @return a value between min and max scaled by alpha
	 */
	public static double lerp(final double min, final double max, final double alpha) {
		return lerp(min, max, alpha, LoopModes.NONE);
	}

	/**
	 * @return a value between min and max scaled by alpha
	 */
	public static double lerp(final double min, final double max, double alpha, LoopModes loop) {
		switch (loop) {
		case BOUNCE:
			alpha = flatWave(alpha, 1);
			break;
		case LOOP:
			alpha %= 1;
			break;
		case STOP:
			alpha = Math.min(alpha, 1);
			break;
		case NONE:
			break;
		}
		return alpha * (max - min) + min;
	}

	/**
	 * @return the y value corresponding to x on a set of Vector2's
	 * @see ArrayList
	 * @see TVector2
	 */
	public static double multiLerp(final ArrayList<TVector2> points, final double x) {
		return multiLerp(points, x, LoopModes.NONE);
	}

	/**
	 * @return the y value corresponding to x on a set of Vector2's
	 * @see ArrayList
	 * @see TVector2
	 */
	public static double multiLerp(final ArrayList<TVector2> points, double x, LoopModes loop) {
		TVector2[] pts = new TVector2[points.size()];
		for (int i = 0; i < points.size(); i++) {
			pts[i] = points.get(i);
		}
		return multiLerp(pts, x, loop);
	}

	/**
	 * @return the y value corresponding to x on a set of Vector2's
	 * @see TVector2
	 */
	public static double multiLerp(TVector2[] points, final double x) {
		return multiLerp(points, x, LoopModes.NONE);
	}

	/**
	 * @return the y value corresponding to x on a set of Vector2's
	 * @see TVector2
	 */
	public static double multiLerp(TVector2[] points, double x, LoopModes loop) {
		switch (loop) {
		case BOUNCE:
			x = flatWave(x, points[points.length - 1].x);
			break;
		case LOOP:
			x %= points[points.length - 1].x;
			break;
		case STOP:
			x = Math.min(x, points[points.length - 1].x);
			break;
		case NONE:
			break;
		}
		int start = 0;
		for (int i = 0; i < points.length; i++) {
			if (points[i].x < x) {
				start = i;
			}
		}
		TVector2 current = points[start];
		TVector2 next = points[start < points.length - 1 ? start + 1 : start];
		return current.y + ((next.y - current.y) * ((x - current.x) / (next.x - current.x)));
	}

	/**
	 * <i> I didn't feel like making a new class for one boolean function...
	 * maybe it counts as base 2 math? </i>
	 *
	 * @return a random boolean value
	 */
	public static boolean randBool() {
		return rand.nextBoolean();
	}

	/**
	 * @return a random byte
	 */
	public static byte randByte() {
		return (byte) rand.nextInt(256);
	}

	/**
	 * @return a random double value between min and max
	 */
	public static double randDouble(final double min, final double max) {
		return min + (max - min) * rand.nextDouble();
	}

	/**
	 * @return a random integer value between min and max
	 */
	public static int randInt(int min, int max) {
		if (min >= max) {
			final int a = max;
			max = min;
			min = a;
		}
		return rand.nextInt(max - min + 1) + min;
	}

	/**
	 * @return the smallest double in a given array
	 */
	public static double smallest(double[] nums) {
		double l = Double.MAX_VALUE;
		for (double d : nums) {
			if (d < l) {
				l = d;
			}
		}
		return l;
	}

	/**
	 * @return the smallest int in a given array
	 */
	public static int smallestInt(int[] nums) {
		int l = Integer.MAX_VALUE;
		for (int d : nums) {
			if (d < l) {
				l = d;
			}
		}
		return l;
	}

	/**
	 * x climbs linearly up and down from 0 to max
	 * <p>
	 * eg: max = 10, <BLOCKQUOTE>if x = 9, return 9 <br>
	 * if x = 10, return 10<br>
	 * if x = 11, return 9<br>
	 * if x = 12, return 8</BLOCKQUOTE>
	 */
	public static double triangleWave(double x, double max) {
		return flatWave(x, max);
	}
}
