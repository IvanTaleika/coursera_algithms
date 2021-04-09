import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
import java.util.LinkedList;

public class FastCollinearPoints {

  private int size;
  private LineSegment[] lineSegments;

  public FastCollinearPoints(Point[] points) {
    checkNulls(points);
    int pSize = points.length;
    Point[] pointsCopy = points.clone();
    Arrays.sort(pointsCopy);
    checkDuplicates(pointsCopy);
    LinkedList<LineSegment> segmentsList = new LinkedList<>();
    Point[] naturalSortedPoints = new Point[pSize];
    for (Point basePoint : pointsCopy) {
      // copying to keep natural order within subarrays
      System.arraycopy(pointsCopy, 0, naturalSortedPoints, 0, pSize);
      Arrays.sort(naturalSortedPoints, basePoint.slopeOrder().reversed());

      // we start from index 1, so this 2 values won't appear in the array sorted by slope
      double activeSlope = Double.NEGATIVE_INFINITY;
      int lineSegmentStart = 0;

      for (int i = 0; i < pSize; i++) {
        Point point = naturalSortedPoints[i];
        double slope = point.slopeTo(basePoint);

        // if slope have changed
        if (slope != activeSlope) {
          int lineSegmentEnd = i - 1;
          Point minPoint = naturalSortedPoints[lineSegmentStart];

          // Add line segment if we have found at least 1 point between the end and the start
          // with the same slope (basePoint is a 4th one) and basePoint is a starting point of the line segment
          if (lineSegmentEnd - lineSegmentStart > 1 && basePoint.compareTo(minPoint) < 0) {
            segmentsList.add(new LineSegment(basePoint, naturalSortedPoints[lineSegmentEnd]));
          }

          // Move to the next slope
          activeSlope = slope;
          lineSegmentStart = i;
        }
      }
    }

    size = segmentsList.size();
    lineSegments = new LineSegment[size];
    lineSegments = segmentsList.toArray(lineSegments);
  }

  public int numberOfSegments() {
    return size;
  }

  public LineSegment[] segments() {
    return lineSegments.clone();
  }

  private void checkDuplicates(Point[] points) {
    if (points.length > 0) {
      Point prevPoint = points[0];
      for (int i = 1; i < points.length; i++) {
        if (points[i].compareTo(prevPoint) == 0) {
          throw new IllegalArgumentException(String.format("Duplicated point %s", prevPoint));
        }
        prevPoint = points[i];
      }
    }
  }

  private void checkNulls(Point[] points) {
    if (null == points) {
      throw new IllegalArgumentException("Points array must not be null");
    }
    for (Point p : points) {
      if (null == p) {
        throw new IllegalArgumentException("Point is null");
      }
    }
  }

  public static void main(String[] args) {

    // read the n points from a file
    In in = new In(args[0]);
    int n = in.readInt();
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
      int x = in.readInt();
      int y = in.readInt();
      points[i] = new Point(x, y);
    }

    // draw the points
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
      p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    FastCollinearPoints collinear = new FastCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }
}