package tPoints;

import java.awt.Color;
import java.util.Comparator;

import tMethods.TMath;

/**
 * A 3 dimensional vector. <br>
 * Used for things that use 3 dimensional vectors.
 * <p>
 * <i>It's like a TVector2 except with 50% more axis and like 90% less features.</i>
 *
 * @see TVector2
 * @author Tallon Hodge
 */
public class TVector3 implements Comparator<TVector3>{
	double x;
	double y;
	double z;
	/**
	 * Forces 2 or 3 axes to be the same.
	 * <p>
	 * <b> WARNING: </b>Will not alter current axis values upon being changed.
	 * <br>
	 * eg: If x = 1 and y == 2, setting lockedAxes to XY will not change the
	 * values of x and y
	 */
	public lockModes lockedAxes = lockModes.NONE;

	public TVector3(final double x, final double y, final double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public TVector3(final TVector3 point) {
		x = point.getX();
		y = point.getY();
		z = point.getZ();
		lockedAxes = point.lockedAxes;
	}

	/**
	 * @return the difference between Vector3 a and Vector3 b
	 */
	public static TVector3 delta(final TVector3 a, final TVector3 b) {
		return new TVector3(a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ());
	}
	
	public enum lockModes {
		NONE, XY, XZ, YZ, XYZ
	}

	/**
	 * @return a Color using the XYZ values are RGB respectively - from a range
	 *         of 0-255
	 * @see Color
	 */
	final public Color asColorByte() {
		return new Color((int) TMath.clamp(x, 0, 255), (int) TMath.clamp(y, 0, 255), (int) TMath.clamp(z, 0, 255));
	}

	/**
	 * @return a Color using the XYZ values are RGB respectively - from a range
	 *         of 0-1
	 *         <p>
	 *         You should probably normalize it first.
	 * @see Color
	 */
	final public Color asColorFloat() {
		return new Color((int) TMath.clamp(x, 0, 1) * 255, (int) TMath.clamp(y, 0, 1) * 255,
				(int) TMath.clamp(z, 0, 1) * 255);
	}

	@Override
	public int compare(TVector3 o1, TVector3 o2) {
		return (int) (o1.x - o2.x);
	}

	final public void divide(final double b) {
		divide(b, b, b);
	}

	final public void divide(final double x, final double y, final double z) {
		setX(x / x);
		setY(y / y);
		setZ(z / z);
	}

	final public void divide(final TVector3 b) {
		divide(b.x, b.y, b.z);
	}

	final public boolean equals(final double x, final double y, final double z) {
		return this.x == x && this.y == y && this.z == z;
	}

	final public boolean equals(final TVector3 point) {
		return point.x == x && point.y == y && point.z == z;
	}

	final public double getMagnitude() {
		return Math.sqrt((x * x) + (y * y) + (z * z));
	}

	final public double getX() {
		return x;
	}

	final public double getY() {
		return y;
	}

	final public double getZ() {
		return z;
	}

	final public void multiply(final double b) {
		multiply(b, b, b);
	}

	final public void multiply(final double x, final double y, final double z) {
		setX(x * x);
		setY(y * y);
		setZ(z * z);
	}

	final public void multiply(final TVector3 b) {
		multiply(b.x, b.y, b.z);
	}

	final public void normalize() {
		final double norm = getMagnitude();
		x = x / norm;
		y = y / norm;
		z = z / norm;
	}

	final public void offset(final double x, final double y, final double z) {
		setX(x + x);
		setY(y + y);
		setZ(z + z);
	}

	final public void offset(final TVector3 b) {
		offset(b.x, b.y, b.z);
	}

	final public void setX(final double n) {
		x = n;
		switch (lockedAxes) {
		case XY:
			y = n;
			break;
		case XZ:
			z = n;
			break;
		case XYZ:
			y = n;
			z = n;
			break;
		default:
		}
	}

	final public void setY(final double n) {
		y = n;
		switch (lockedAxes) {
		case XY:
			x = n;
			break;
		case YZ:
			z = n;
			break;
		case XYZ:
			x = n;
			z = n;
			break;
		default:
		}
	}

	final public void setZ(final double n) {
		z = n;
		switch (lockedAxes) {
		case XZ:
			x = n;
			break;
		case YZ:
			y = n;
			break;
		case XYZ:
			x = n;
			y = n;
			break;
		default:
		}
	}
}
