package tPoints;

import java.awt.Color;
import java.util.Comparator;

/**
 * The X axis is a double, and the Y axis is a Color.
 * <p>
 * <i>It sort of makes sense if you think about it</i>
 *
 * @see Color
 * @author Tallon Hodge
 */
public class TColorPoint implements Comparator<TColorPoint>{
	public double x;
	public Color y;

	public TColorPoint(final double x, final Color y) {
		this.y = y;
		this.x = x;
	}

	@Override
	public int compare(TColorPoint o1, TColorPoint o2) {
		return (int)(o1.x - o2.x);
	}

	public void delete() {
		y = null;
		x = 0;
	}
}