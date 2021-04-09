/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdDraw;
import java.util.Comparator;

public class Point implements Comparable<Point> {

  private final int x;     // x-coordinate of this point
  private final int y;     // y-coordinate of this point

  /**
   * Initializes a new point.
   *
   * @param x the <em>x</em>-coordinate of the point
   * @param y the <em>y</em>-coordinate of the point
   */
  public Point(int x, int y) {
    /* DO NOT MODIFY */
    this.x = x;
    this.y = y;
  }

  /**
   * Draws this point to standard draw.
   */
  public void draw() {
    /* DO NOT MODIFY */
    StdDraw.point(x, y);
  }

  /**
   * Draws the line segment between this point and the specified point to standard draw.
   *
   * @param that the other point
   */
  public void drawTo(Point that) {
    /* DO NOT MODIFY */
    StdDraw.line(this.x, this.y, that.x, that.y);
  }

  /**
   * Returns the slope between this point and the specified point. Formally, if the two points are
   * (x0, y0) and (x1, y1), then the slope is (y1 - y0) / (x1 - x0). For completeness, the slope is
   * defined to be +0.0 if the line segment connecting the two points is horizontal;
   * Double.POSITIVE_INFINITY if the line segment is vertical; and Double.NEGATIVE_INFINITY if (x0,
   * y0) and (x1, y1) are equal.
   *
   * @param that the other point
   * @return the slope between this point and the specified point
   */
  public double slopeTo(Point that) {
    if (compareTo(that) == 0) {
      return Double.NEGATIVE_INFINITY;
    }
    double slope = 1d * (that.y - this.y) / (that.x - this.x);
    // vertical line is always Double.POSITIVE_INFINITY
    if (slope == Double.NEGATIVE_INFINITY) {
      return Double.POSITIVE_INFINITY;
    }
    // Convert -0.0 to +0.0
    if (slope == 0d) {
      return +0d;
    }
    return slope;
  }

  /**
   * Compares two points by y-coordinate, breaking ties by x-coordinate. Formally, the invoking
   * point (x0, y0) is less than the argument point (x1, y1) if and only if either y0 < y1 or if y0
   * = y1 and x0 < x1.
   *
   * @param that the other point
   * @return the value <tt>0</tt> if this point is equal to the argument point (x0 = x1 and y0 =
   * y1); a negative integer if this point is less than the argument point; and a positive integer
   * if this point is greater than the argument point
   */
  public int compareTo(Point that) {
    if (this.y > that.y || this.y == that.y && this.x > that.x) {
      return 1;
    } else if (this.y == that.y && this.x == that.x) {
      return 0;
    } else {
      return -1;
    }
  }

  /**
   * Compares two points by the slope they make with this point. The slope is defined as in the
   * slopeTo() method.
   *
   * @return the Comparator that defines this ordering on points
   */
  public Comparator<Point> slopeOrder() {
    return (o1, o2) -> {
      double slope1 = slopeTo(o1);
      double slope2 = slopeTo(o2);
      return Double.compare(slope1, slope2);
    };
  }


  /**
   * Returns a string representation of this point. This method is provide for debugging; your
   * program should not rely on the format of the string representation.
   *
   * @return a string representation of this point
   */
  public String toString() {
    /* DO NOT MODIFY */
    return "(" + x + ", " + y + ")";
  }

  /**
   * Unit tests the Point data type.
   */
  public static void main(String[] args) {
    Point oneOne = new Point(1, 1);
    Point oneTwo = new Point(1, 2);
    Point twoTwo = new Point(2, 2);
    Point twoOne = new Point(2, 1);
    Point twoZero = new Point(2, 0);
    Point oneZero = new Point(1, 0);
    Point zeroZero = new Point(0, 0);
    Point zeroOne = new Point(0, 1);
    Point zeroTwo = new Point(0, 2);
    System.out.printf("Slop between oneOne and oneOne should be -Infinity - %f%n",
        oneOne.slopeTo(oneOne));
    System.out.printf("Slop between oneOne and oneTwo should be +Infinity - %f%n",
        oneOne.slopeTo(oneTwo));
    System.out.printf("Slop between oneOne and twoTwo should be +1 - %f%n", oneOne.slopeTo(twoTwo));
    System.out.printf("Slop between oneOne and twoOne should be +0 - %f%n", oneOne.slopeTo(twoOne));
    System.out
        .printf("Slop between oneOne and twoZero should be -1 - %f%n", oneOne.slopeTo(twoZero));
    System.out.printf("Slop between oneOne and oneZero should be +Infinity - %f%n",
        oneOne.slopeTo(oneZero));
    System.out
        .printf("Slop between oneOne and zeroZero should be +1 - %f%n", oneOne.slopeTo(zeroZero));
    System.out
        .printf("Slop between oneOne and zeroOne should be +0 - %f%n", oneOne.slopeTo(zeroOne));
    System.out
        .printf("Slop between oneOne and zeroTwo should be -1 - %f%n", oneOne.slopeTo(zeroTwo));
    System.out.printf(
        "oneOne.compareTo should be 1 for twoZero - %d, oneZero - %d, zeroZero - %d, zeroOne - %d%n",
        oneOne.compareTo(twoZero),
        oneOne.compareTo(oneZero),
        oneOne.compareTo(zeroZero),
        oneOne.compareTo(zeroOne)
    );
    System.out.printf(
        "oneOne.compareTo should be -1 for zeroTwo - %d, oneTwo - %d, twoTwo - %d, twoOne - %d%n",
        oneOne.compareTo(zeroTwo),
        oneOne.compareTo(oneTwo),
        oneOne.compareTo(twoTwo),
        oneOne.compareTo(twoOne)
    );
    System.out.printf(
        "oneOne.compareTo should be 0 for oneOne - %d%n",
        oneOne.compareTo(oneOne)
    );
    System.out.printf(
        "oneOne.slopeOrder should be 0 points on the same line: (zeroZero, oneOne, twoTwo) - %d; (oneZero, oneOne, oneTwo) - %d; (twoZero, oneOne, zeroTwo) - %d;  (zeroOne, oneOne, twoOne) - %d%n",
        oneOne.slopeOrder().compare(zeroZero, twoTwo),
        oneOne.slopeOrder().compare(oneZero, oneTwo),
        oneOne.slopeOrder().compare(twoZero, zeroTwo),
        oneOne.slopeOrder().compare(zeroOne, twoOne)
    );
    System.out.printf(
        "Lines order should be: vertical line > 1 to 3 quoter line - %b, 1 to 3 quoter line > horizontal line - %b; horizontal line > 2 to 4 quoter line - %b%n",
        oneOne.slopeOrder().compare(oneTwo, twoTwo) > 0 && oneOne.slopeOrder().compare(oneZero, zeroZero) > 0,
        oneOne.slopeOrder().compare(twoTwo, twoOne) > 0 && oneOne.slopeOrder().compare(twoTwo, zeroOne) > 0,
        oneOne.slopeOrder().compare(twoOne, twoZero) > 0 && oneOne.slopeOrder().compare(twoOne, zeroTwo) > 0
    );
  }
}
