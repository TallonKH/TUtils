package tMethods;

import java.awt.Color;
import java.util.ArrayList;

import tMethods.TMath;
import tMethods.TMath.LoopModes;
import tPoints.*;

/**
 * Some useful methods to go with {@link java.awt.Color}
 *
 * @author Tallon Hodge
 * @see java.awt.Color
 */
public class TColors {
	public static Color changeAlpha(Color c, int a){
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), TMath.clamp(c.getAlpha() + a, 0, 255));
	}

	public static Color changeBlue(Color c, int b){
		return new Color(c.getRed(), c.getGreen(), TMath.clamp(c.getBlue() + b, 0, 255), c.getAlpha());
	}

	public static Color changeGreen(Color c, int g){
		return new Color(c.getRed(), TMath.clamp(c.getGreen() + g, 0, 255), c.getBlue(), c.getAlpha());
	}

	public static Color changeRed(Color c, int r){
		return new Color(TMath.clamp(c.getRed() + r, 0, 255), c.getGreen(), c.getBlue(), c.getAlpha());
	}

	final public static Color clampColor(final Color a, final Color min, final Color max) {
		return new Color(TMath.clamp(a.getRed(), min.getRed(), max.getRed()),
				TMath.clamp(a.getGreen(), min.getGreen(), max.getGreen()),
				TMath.clamp(a.getBlue(), min.getBlue(), max.getBlue()),
				TMath.clamp(a.getAlpha(), min.getAlpha(), max.getAlpha()));
	}

	/**
	 * @return the Color between min and max, scaled by alpha
	 * <br>
	 * 0 <= alpha <= 1
	 * @see Color
	 */
	final public static Color colorLerp(final Color min, final Color max, final double alpha) {
		return colorLerp(min, max, alpha, LoopModes.STOP);
	}

	/** 
	 * @return the Color between min and max scaled by alpha
	 * @see Color
	 */
	final public static Color colorLerp(final Color min, final Color max, final double alpha, LoopModes loop) {
		final double r = TMath.lerp(min.getRed(), max.getRed(), alpha, loop);
		final double g = TMath.lerp(min.getGreen(), max.getGreen(), alpha, loop);
		final double b = TMath.lerp(min.getBlue(), max.getBlue(), alpha, loop);
		final double a = TMath.lerp(min.getAlpha(), max.getAlpha(), alpha, loop);
		return new Color((int) TMath.clamp(r, 0, 255), (int) TMath.clamp(g, 0, 255), (int) TMath.clamp(b, 0, 255), (int) TMath.clamp(a, 0, 255));
	}

	/**
	 * A cleaner version of Color.toString()
	 */
	final public static String colorToString(final Color color) {
		return new String("(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")");
	}

	/**
	 * @return the average of a Color's red, green, and blue values
	 * @see Color
	 */
	final public static double getAvgBrightness(final Color color) {
		return (color.getRed() + color.getGreen() + color.getBlue()) / 3;
	}
	
	/**
	 * @return The largest individual RGB values of two colors 
	 */
	public static Color max(Color a, Color b){
		return new Color(Math.max(a.getRed(), b.getRed()), Math.max(a.getGreen(), b.getGreen()), Math.max(a.getBlue(), b.getBlue()));
	}
	
	/**
	 * @return The smallest individual RGB values of two colors 
	 */
	public static Color min(Color a, Color b){
		return new Color(Math.min(a.getRed(), b.getRed()), Math.min(a.getGreen(), b.getGreen()), Math.min(a.getBlue(), b.getBlue()));
	}

	/**
	 * @return the Color value corresponding to x on a set of CPoints
	 * @see Color
	 * @see TColorPoint
	 */
	final public static Color multiLerpColor(final ArrayList<TColorPoint> points, final double x) {
		return multiLerpColor(points, x, LoopModes.STOP);
	}

	/**
	 * @return the Color value corresponding to x on a set of CPoints
	 * @see Color
	 * @see TColorPoint
	 */
	final public static Color multiLerpColor(final ArrayList<TColorPoint> points, final double x, LoopModes loop) {
		TColorPoint[] ps = new TColorPoint[points.size()];
		for (int i = 0; i < points.size(); i++) {
			ps[i] = points.get(i);
		}
		return multiLerpColor(ps, x, loop);
	}

	/**
	 * @return the Color value corresponding to x on a set of CPoints
	 * @see Color
	 * @see TColorPoint
	 */
	final public static Color multiLerpColor(TColorPoint[] points, double x) {
		return multiLerpColor(points, x, LoopModes.STOP);
	}

	final public static Color multiLerpColor(TColorPoint[] points, double x, LoopModes loop) {
		final ArrayList<TVector2> rPoints = new ArrayList<TVector2>();
		final ArrayList<TVector2> gPoints = new ArrayList<TVector2>();
		final ArrayList<TVector2> bPoints = new ArrayList<TVector2>();
		final ArrayList<TVector2> aPoints = new ArrayList<TVector2>();
		for (int i = 0; i < points.length; i++) {
			rPoints.add(new TVector2(points[i].x, points[i].y.getRed()));
			gPoints.add(new TVector2(points[i].x, points[i].y.getGreen()));
			bPoints.add(new TVector2(points[i].x, points[i].y.getBlue()));
			aPoints.add(new TVector2(points[i].x, points[i].y.getAlpha()));
		}
		return new Color((int) TMath.clamp(TMath.multiLerp(rPoints, x, loop), 0, 255),
				(int) TMath.clamp(TMath.multiLerp(gPoints, x, loop), 0, 255),
				(int) TMath.clamp(TMath.multiLerp(bPoints, x, loop), 0, 255),
				(int) TMath.clamp(TMath.multiLerp(aPoints, x, loop), 0, 255));
	}

	final public static Color multiply(Color c, Color x){
		return new Color(TMath.clamp(c.getRed() * x.getRed(), 0, 255), TMath.clamp(c.getGreen() * x.getGreen(), 0, 255), TMath.clamp(c.getBlue() * x.getBlue(), 0, 255), c.getAlpha());
	}

	final public static Color multiply(Color c, double x){
		return new Color(TMath.clamp((int)(c.getRed() * x), 0, 255), TMath.clamp((int)(c.getGreen() * x), 0, 255), TMath.clamp((int)(c.getBlue() * x), 0, 255), c.getAlpha());
	}

	final public static Color offset(Color c, Color x){
		return new Color(TMath.clamp(c.getRed() + x.getRed(), 0, 255), TMath.clamp(c.getGreen() + x.getGreen(), 0, 255), TMath.clamp(c.getBlue() + x.getBlue(), 0, 255), c.getAlpha());
	}

	final public static Color offset(Color c, int x){
		return new Color(TMath.clamp(c.getRed() + x, 0, 255), TMath.clamp(c.getGreen() + x, 0, 255), TMath.clamp(c.getBlue() + x, 0, 255), c.getAlpha());
	}

	/**
	 * @return a Color with opposite RGB values as the given Color
	 * @see Color
	 */
	final public static Color oppositeColor(final Color color) {
		return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
	}

	/**
	 * Inverse of {@link java.awt.Color#toString()}
	 */
	final public static Color parseColor(String input) {
		input = input.replace("[", "").replace("r=", "").replace("g=", "").replace("b=", "").replace("]", "");
		int r = Integer.parseInt(input.substring(0, input.indexOf(',')));
		input = input.substring(input.indexOf(',') + 1);
		int g = Integer.parseInt(input.substring(0, input.indexOf(',')));
		input = input.substring(input.indexOf(',') + 1);
		int b = Integer.parseInt(input.substring(0));
		return new Color(r, g, b);
	}

	/**
	 * @return a random Color between min and max
	 * @see Color
	 */
	final public static Color randColor(final Color min, final Color max) {
		return new Color(TMath.randInt(min.getRed(), max.getRed()), TMath.randInt(min.getGreen(), max.getGreen()),
				TMath.randInt(min.getBlue(), max.getBlue()), TMath.randInt(min.getAlpha(), max.getAlpha()));
	}

	public static Color setAlpha(Color c, int a){
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), a);
	}

	public static Color setBlue(Color c, int b){
		return new Color(c.getRed(), c.getGreen(), b, c.getAlpha());
	}

	public static Color setGreen(Color c, int g){
		return new Color(c.getRed(), g, c.getBlue(), c.getAlpha());
	}

	public static Color setRed(Color c, int r){
		return new Color(r, c.getGreen(), c.getBlue(), c.getAlpha());
	}

	final public static Color translucent(Color c, Color l){
		return new Color(translucentHelper(c.getRed(), l.getRed()), translucentHelper(c.getGreen(), l.getGreen()), translucentHelper(c.getBlue(), l.getBlue()));
	}

	final private static int translucentHelper(int c, int l){
		return (int)(c * (((double)l)/255));
	}

	/**
	 * @return variable color with all values changed randomly by amount
	 * @param lock
	 *            decides if all values of color should vary equally
	 * @see Color
	 */
	final public static Color varyColor(final Color color, final int amount, final boolean lock) {
		if (lock) {
			final int b = TMath.randInt(-amount, amount);
			return new Color(TMath.clamp(color.getRed() + b, 0, 255), TMath.clamp(color.getGreen() + b, 0, 255),
					(int) TMath.clamp(color.getBlue() + b, 0, 255), TMath.clamp(color.getAlpha() + b, 0, 255));
		} else {
			return new Color(TMath.clamp(color.getRed() + TMath.randInt(-amount, amount), 0, 255),
					TMath.clamp(color.getGreen() + TMath.randInt(-amount, amount), 0, 255),
					TMath.clamp(color.getBlue() + TMath.randInt(-amount, amount), 0, 255), color.getAlpha());
		}
	}
}
