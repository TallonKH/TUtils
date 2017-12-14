package tMethods;

import java.awt.*;
import java.awt.Image;
import java.awt.image.*;
import java.util.*;
import java.io.*;
import java.util.List;
import java.util.stream.*;

import javafx.scene.image.*;
import javafx.scene.paint.Color;
import tMethods.TMath;
import tMethods.TMath.LoopModes;
import tPoints.*;

import javax.imageio.*;

/**
 * Some useful methods to go with {@link javafx.scene.paint.Color}
 *
 * @author Tallon Hodge
 * @see java.awt.Color
 */
public class TColorsFX {
	public static Color changeAlpha(Color c, double a) {
		return Color.color(c.getRed(), c.getGreen(), c.getBlue(), TMath.clamp(c.getOpacity() + a, 0, 1));
	}

	public static Color changeBlue(Color c, double b) {
		return Color.color(c.getRed(), c.getGreen(), TMath.clamp(c.getBlue() + b, 0, 1), c.getOpacity());
	}

	public static Color changeGreen(Color c, double g) {
		return Color.color(c.getRed(), TMath.clamp(c.getGreen() + g, 0, 1), c.getBlue(), c.getOpacity());
	}

	public static Color changeRed(Color c, double r) {
		return Color.color(TMath.clamp(c.getRed() + r, 0, 1), c.getGreen(), c.getBlue(), c.getOpacity());
	}

	final public static Color clampColor(final Color a, final Color min, final Color max) {
		return Color.color(TMath.clamp(a.getRed(), min.getRed(), max.getRed()),
						   TMath.clamp(a.getGreen(), min.getGreen(), max.getGreen()),
						   TMath.clamp(a.getBlue(), min.getBlue(), max.getBlue()),
						   TMath.clamp(a.getOpacity(), min.getOpacity(), max.getOpacity()));
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
		final double a = TMath.lerp(min.getOpacity(), max.getOpacity(), alpha, loop);
		return Color.color(TMath.clamp(r, 0, 1), TMath.clamp(g, 0, 1), TMath.clamp(b, 0, 1), TMath.clamp(a, 0, 1));
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
	public static Color max(Color a, Color b) {
		return Color.color(Math.max(a.getRed(), b.getRed()), Math.max(a.getGreen(), b.getGreen()), Math.max(a.getBlue(), b.getBlue()));
	}

	/**
	 * @return The smallest individual RGB values of two colors
	 */
	public static Color min(Color a, Color b) {
		return Color.color(Math.min(a.getRed(), b.getRed()), Math.min(a.getGreen(), b.getGreen()), Math.min(a.getBlue(), b.getBlue()));
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
		return Color.color(TMath.clamp(TMath.multiLerp(rPoints, x, loop), 0, 1),
						   TMath.clamp(TMath.multiLerp(gPoints, x, loop), 0, 1),
						   TMath.clamp(TMath.multiLerp(bPoints, x, loop), 0, 1),
						   TMath.clamp(TMath.multiLerp(aPoints, x, loop), 0, 1));
	}

	final public static Color multiply(Color c, Color x) {
		return Color.color(TMath.clamp(c.getRed() * x.getRed(), 0, 1), TMath.clamp(c.getGreen() * x.getGreen(), 0, 1), TMath.clamp(c.getBlue() * x.getBlue(), 0, 1), c.getOpacity());
	}

	final public static Color multiply(Color c, double x) {
		return Color.color(TMath.clamp((c.getRed() * x), 0, 1), TMath.clamp((c.getGreen() * x), 0, 1), TMath.clamp((c.getBlue() * x), 0, 1), c.getOpacity());
	}

	final public static Color offset(Color c, Color x) {
		return Color.color(TMath.clamp(c.getRed() + x.getRed(), 0, 1), TMath.clamp(c.getGreen() + x.getGreen(), 0, 1), TMath.clamp(c.getBlue() + x.getBlue(), 0, 1), c.getOpacity());
	}

	final public static Color offset(Color c, double x) {
		return Color.color(TMath.clamp(c.getRed() + x, 0, 1), TMath.clamp(c.getGreen() + x, 0, 1), TMath.clamp(c.getBlue() + x, 0, 1), c.getOpacity());
	}

	/**
	 * @return a Color with opposite RGB values as the given Color
	 * @see Color
	 */
	final public static Color oppositeColor(final Color color) {
		return Color.color(1 - color.getRed(), 1 - color.getGreen(), 1 - color.getBlue());
	}

	/**
	 * @return a random Color between min and max
	 * @see Color
	 */
	final public static Color randColor(final Color min, final Color max) {
		return Color.color(TMath.randDouble(min.getRed(), max.getRed()), TMath.randDouble(min.getGreen(), max.getGreen()),
						   TMath.randDouble(min.getBlue(), max.getBlue()), TMath.randDouble(min.getOpacity(), max.getOpacity()));
	}

	public static Color setAlpha(Color c, double a) {
		return Color.color(c.getRed(), c.getGreen(), c.getBlue(), a);
	}

	public static Color setBlue(Color c, double b) {
		return Color.color(c.getRed(), c.getGreen(), b, c.getOpacity());
	}

	public static Color setGreen(Color c, double g) {
		return Color.color(c.getRed(), g, c.getBlue(), c.getOpacity());
	}

	public static Color setRed(Color c, double r) {
		return Color.color(r, c.getGreen(), c.getBlue(), c.getOpacity());
	}

	final public static Color translucent(Color c, Color l) {
		return Color.color(c.getRed() * l.getRed(), c.getGreen() * l.getGreen(), c.getBlue() * l.getBlue());
	}

	final public static LinkedHashMap<Color, Integer> getColorMap(BufferedImage img, int precision) {
		LinkedHashMap<Color, Integer> colors = new LinkedHashMap<>();
		int w = img.getWidth();
		int h = img.getHeight();
		for (int x = 0; x < w; x += precision) {
			for (int y = 0; y < h; y += precision) {
				int rgb = img.getRGB(x, y);
				Color clr = Color.color((rgb & 0x00FF0000) >> 16, (rgb & 0x0000FF00) >> 8, rgb & 0x000000FF);
				colors.merge(clr, 1, (a, b) -> a + b);
			}
		}

		LinkedHashMap<Color, Integer> sorted = new LinkedHashMap<>();
		colors.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered((o) -> {
			sorted.put(o.getKey(), o.getValue());
		});
		return sorted;
	}

	public static void main(String args[]) {
		File file = new File("/Users/default/Downloads/colors.jpg");
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		long t = System.currentTimeMillis();
		List<Color> colors = getColorPalette(image, 0.4, 2, 32);
		System.out.println(System.currentTimeMillis() - t);
		for (Color c : colors) {
			System.out.println((c.getRed() * 1) + ", " + (c.getGreen() * 1) + ", " + (c.getBlue() * 1));
		}
	}

//	final public static float getVibrancy(Color c){
//		//TODO - colors are vibrant when 1 or 2 RGB values are high, but not the third
//		//min(r,g,b) < (150?)
//		//max(r,g,b) > (200?)
//
//		// maybe split into HSL?
//			// any Hue
//			// high Saturation ~(90:100)
//			// middle Luminance (~45:60)
//	}

	final public static List<Color> getColorPalette(BufferedImage img, double fudging, int precision, int max) {
		List<Map.Entry<Color, Integer>> colorEntries = new ArrayList(getColorMap(img, precision).entrySet());

		Map<Color, Integer> fudgedColorMap = new HashMap<>();
		int count = 0;
		while (!colorEntries.isEmpty()) {
			count++;
			if (count > max) {
				break;
			}
			Map.Entry<Color, Integer> entry = colorEntries.remove(0);
			Color entryColor = entry.getKey();
			int totalFrequency = entry.getValue();    //how often the color appears
			double totalR = entryColor.getRed() * totalFrequency;
			double totalG = entryColor.getGreen() * totalFrequency;
			double totalB = entryColor.getBlue() * totalFrequency;
			double avgR = entryColor.getRed();
			double avgG = entryColor.getGreen();
			double avgB = entryColor.getBlue();
			List<Map.Entry<Color, Integer>> trimmedColorEntries = new ArrayList<>();
			for (Map.Entry<Color, Integer> entryB : colorEntries) {
				Color colorB = entryB.getKey();
				if (Math.abs(avgR - colorB.getRed()) < fudging &&
					Math.abs(avgG - colorB.getGreen()) < fudging &&
					Math.abs(avgB - colorB.getBlue()) < fudging) {

					int colorBFreq = entryB.getValue();
					totalR += colorB.getRed() * colorBFreq;
					totalG += colorB.getGreen() * colorBFreq;
					totalB += colorB.getBlue() * colorBFreq;
					totalFrequency += colorBFreq;
					avgR = totalR / totalFrequency;
					avgG = totalG / totalFrequency;
					avgB = totalB / totalFrequency;
				} else {
					trimmedColorEntries.add(entryB);
				}
			}
			fudgedColorMap.put(Color.color(avgR, avgG, avgB), totalFrequency);
			colorEntries = trimmedColorEntries;
		}
		List<Color> result = new ArrayList<>();
		fudgedColorMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered((o) -> {
			result.add(o.getKey());
		});
		return result;
	}

	/**
	 * @param lock decides if all values of color should vary equally
	 * @return variable color with all values changed randomly by amount
	 * @see Color
	 */
	final public static Color varyColor(final Color color, final double amount, final boolean lock) {
		if (lock) {
			final double b = TMath.randDouble(-amount, amount);
			return Color.color(TMath.clamp(color.getRed() + b, 0, 1), TMath.clamp(color.getGreen() + b, 0, 1),
							   TMath.clamp(color.getBlue() + b, 0, 1), TMath.clamp(color.getOpacity() + b, 0, 1));
		} else {
			return Color.color(TMath.clamp(color.getRed() + TMath.randDouble(-amount, amount), 0, 1),
							   TMath.clamp(color.getGreen() + TMath.randDouble(-amount, amount), 0, 1),
							   TMath.clamp(color.getBlue() + TMath.randDouble(-amount, amount), 0, 1), color.getOpacity());
		}
	}
}
