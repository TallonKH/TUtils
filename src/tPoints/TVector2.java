package tPoints;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Comparator;

import tMethods.TMath;

/**
 * A mutable alternative to java.awt.Point with a bunch of weird functions
 *
 * @author Tallon Hodge
 *
 */
public class TVector2 implements Cloneable, Comparator<TVector2> {
	public double x;

	public double y;

	public TVector2(Dimension point) {
		x = point.getWidth();
		y = point.getHeight();
	}

	public TVector2(final double x, final double y) {
		this.x = x;
		this.y = y;
	}

	public TVector2(Point point) {
		x = point.getX();
		y = point.getY();
	}

	public TVector2(final TVector2 point) {
		x = point.x;
		y = point.y;
	}
	public static void abs(TVector2 point){
		point.x = Math.abs(point.x);
		point.y = Math.abs(point.y);
	}

	public static TVector2 average(TVector2[] points){
		return midpoint(points);
	}

	/**
	 * @return the closest TVector2 in array b from a
	 */
	public static TVector2 closest(TVector2 a, TVector2[] b) {
		return a.closest(b);
	}

	/**
	 * @return the distance between 2 TVector2's
	 */
	public static double distance(final TVector2 a, final TVector2 b) {
		return Math.sqrt(distanceSqr(a, b));
	}
	
	/**
	 * @return the distance between 2 TVector2's, squared
	 */
	public static double distanceSqr(final TVector2 a, final TVector2 b) {
		final double xd = (a.x - b.x);
		final double yd = (a.y - b.y);
		return xd * xd + yd * yd;
	}

	/**
	 * @return the dot product of 2 TVector2's
	 * @see TVector2
	 */
	public static double dotProduct(final TVector2 a, final TVector2 b) {
		final TVector2 a1 = a.normalized();
		final TVector2 b1 = b.normalized();
		return a1.x * b1.x + b1.y * b1.y;
	}

	/**
	 * @return the farthest TVector2 in array b from a
	 */
	public static TVector2 farthest(TVector2 a, TVector2[] b) {
		return a.farthest(b);
	}

	public static TVector2 midpoint(TVector2[] points){
		int x = 0;
		int y = 0;
		for(TVector2 p : points){
			x+=p.x;
			y+=p.y;
		}
		x/=points.length;
		y/=points.length;
		return new TVector2(x, y);
	}

	/**
	 * @return the smallest x and y value in the given set of points
	 */
	public static TVector2 negativeBound(TVector2[] points) {
		double farX = Double.MAX_VALUE;
		double farY = Double.MAX_VALUE;
		for (TVector2 p : points) {
			if (p.x < farX) {
				farX = p.x;
			}
			if (p.y < farY) {
				farY = p.y;
			}
		}
		return new TVector2(farX, farY);
	}

	public static TVector2 parse(String param) {
		return new TVector2(Double.parseDouble(param.substring(1, param.indexOf(','))),
				Double.parseDouble(param.substring(param.indexOf(',') + 1, param.indexOf(']'))));
	}

	/**
	 * @return the largest x and y value in the given set of points
	 */
	public static TVector2 positiveBound(TVector2[] points) {
		double farX = Double.MIN_VALUE;
		double farY = Double.MIN_VALUE;
		for (TVector2 p : points) {
			if (p.x > farX) {
				farX = p.x;
			}
			if (p.y > farY) {
				farY = p.y;
			}
		}
		return new TVector2(farX, farY);
	}

	public static TVector2 rand() {
		return new TVector2(TMath.randDouble(-1, 1), TMath.randDouble(-1, 1));
	}

	public static TVector2 rand(final TVector2 a, final TVector2 b) {
		return new TVector2(TMath.randDouble(a.x, b.x), TMath.randDouble(a.y, b.y));
	}

	public TVector2 abs(){
		return new TVector2(Math.abs(x), Math.abs(y));
	}

	public Dimension asDimension() {
		Dimension d = new Dimension();
		d.setSize(x, y);
		return d;
	}

	public Point asPoint() {
		return new Point((int) x, (int) y);
	}

	@Override
	public TVector2 clone() {
		return new TVector2(this);
	}

	/**
	 * @return the closest TVector2 from this
	 */
	public TVector2 closest(TVector2[] points) {
		TVector2 f = this;
		double cDist = Double.MAX_VALUE;
		for (TVector2 p : points) {
			double dist = distance(p);
			if (dist < cDist) {
				cDist = dist;
				f = p;
			}
		}
		return f;
	}

	@Override
	public int compare(TVector2 o1, TVector2 o2) {
		return (int)(o1.x - o2.x);
	}

	public TVector2 difference(double x, double y) {
		return difference(new TVector2(x, y));
	}

	/**
	 * @return the difference between this and TVector2 b
	 */
	public TVector2 difference(final TVector2 b) {
		return new TVector2(x - b.x, y - b.y);
	}

	/**
	 * @return the distance to 2 b
	 */
	public double distance(final TVector2 b) {
		final double xd = (x - b.x);
		final double yd = (y - b.y);
		return Math.sqrt(xd * xd + yd * yd);
	}

	final public void divide(final double b) {
		divide(b, b);
	}

	final public void divide(final double x, final double y) {
		this.x /= x;
		this.y /= y;
	}

	final public void divide(final TVector2 b) {
		divide(b.x, b.y);
	}

	final public TVector2 dividedBy(final double b) {
		return dividedBy(b, b);
	}

	final public TVector2 dividedBy(final double x, final double y) {
		return new TVector2(this.x / x, this.y / y);
	}

	final public TVector2 dividedBy(final TVector2 b) {
		return dividedBy(b.x, b.y);
	}

	final public boolean equals(final double x, final double y) {
		return x == this.x && y == this.y;
	}

	final public boolean equals(final TVector2 point) {
		return point.x == x && point.y == y;
	}

	/**
	 * @return the farthest TVector2 from this
	 */
	public TVector2 farthest(TVector2[] points) {
		TVector2 f = this;
		double fDist = 0;
		for (TVector2 p : points) {
			double dist = distance(p);
			if (dist > fDist) {
				fDist = dist;
				f = p;
			}
		}
		return f;
	}

	final public double getMagnitude() {
		return Math.sqrt((x * x) + (y * y));
	}

	final public TVector2 multipliedBy(final double b) {
		return multipliedBy(b, b);
	}

	final public TVector2 multipliedBy(final double x, final double y) {
		return new TVector2(this.x * x, this.y * y);
	}

	final public TVector2 multipliedBy(final TVector2 b) {
		return multipliedBy(b.x, b.y);
	}

	final public void multiply(final double b) {
		multiply(b, b);
	}

	final public void multiply(final double x, final double y) {
		this.x *= x;
		this.y *= y;
	}

	final public void multiply(final TVector2 b) {
		multiply(b.x, b.y);
	}

	final public void negate() {
		x *= -1;
		y *= -1;
	}

	/**
	 * @return a copy of the vector * -1
	 */
	final public TVector2 negated() {
		return new TVector2(x *= -1, y *= -1);
	}

	final public void normalize() {
		final double norm = getMagnitude();
		x = x / norm;
		y = y / norm;
	}

	final public TVector2 normalized() {
		final double norm = getMagnitude();
		return new TVector2(x / norm, y / norm);
	}

	final public void offset(final double x, final double y) {
		this.x += x;
		this.y += y;
	}

	final public void offset(final TVector2 b) {
		x += b.x;
		y += b.y;
	}

	final public TVector2 offsetBy(final double x, final double y) {
		return new TVector2(this.x + x, this.y + y);
	}

	final public TVector2 offsetBy(final TVector2 b) {
		return new TVector2(x + b.x, y + b.y);
	}

	/**
	 * @return the given Vector2 rotated by d degrees on axis (0, 0)
	 * @see TVector2
	 */
	final public TVector2 rotatedDeg(final double d) {
		final TVector2 b = new TVector2(this);
		b.rotateDeg(d);
		return b;
	}

	/**
	 * rotates the vector by d degrees on axis (0, 0)
	 *
	 * @see TVector2
	 */
	final public void rotateDeg(final double d) {
		rotateRad(Math.toRadians(d));
	}

	/**
	 * @return the given Vector2 rotated by r radians on axis (0, 0)
	 * @see TVector2
	 */
	final public TVector2 rotatedRad(final double r) {
		final TVector2 b = new TVector2(this);
		b.rotateRad(r);
		return b;
	}

	/**
	 * rotates the vector by r radians on axis (0, 0)
	 *
	 * @see TVector2
	 */
	final public void rotateRad(final double r) {
		final double x1 = (x * Math.cos(r) - y * Math.sin(r));
		final double y1 = (x * Math.sin(r) + y * Math.cos(r));
		x = x1;
		y = y1;
	}

	/**
	 * @return the rotation in degrees to Vector2 b
	 * @see TVector2
	 */
	public double rotationTo(final TVector2 b) {
		return Math.toDegrees(Math.atan2((b.y - y), (b.x - x)));
	}

	public TVector2 rounded(){
		return new TVector2(Math.round(x), Math.round(y));
	}

	final public void set(final double x, final double y) {
		this.x = x;
		this.y = y;
	}

	final public void set(final TVector2 point) {
		x = point.x;
		y = point.y;
	}

	/**
	 * @return offsets this by -b
	 */
	public void subtract(final TVector2 b) {
		x -= b.x;
		y -= b.y;
	}

	/**
	 * Literally the same thing as the difference(TVector2) function
	 *
	 * @see TVector2#difference(TVector2)
	 */
	public TVector2 subtractedBy(final TVector2 b) {
		return difference(b);
	}

	@Override
	public String toString(){
		return "[" + x + "," + y + "]";
	}
}
