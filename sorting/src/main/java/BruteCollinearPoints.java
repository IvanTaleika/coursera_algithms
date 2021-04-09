import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

public class BruteCollinearPoints {

  private int size;
  private LineSegment[] lineSegments;

  public BruteCollinearPoints(Point[] points) {
    validatePoints(points);
    LinkedList<LineSegment> segmentsList = new LinkedList<>();
    for (int i = 0; i < points.length - 3; i++) {
      Point p1 = points[i];
      Comparator<Point> p1Lines = p1.slopeOrder();
      for (int j = i + 1; j < points.length - 2; j++) {
        Point p2 = points[j];
        for (int k = j + 1; k < points.length - 1; k++) {
          Point p3 = points[k];
          for (int m = k + 1; p1Lines.compare(p2, p3) == 0 && m < points.length; m++) {
            Point p4 = points[m];
            if (p1Lines.compare(p2, p4) == 0) {
              Point[] linePoints = new Point[]{p1, p2, p3, p4};
              Arrays.sort(linePoints);
              segmentsList.add(new LineSegment(linePoints[0], linePoints[3]));
            }
          }
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

  private void validatePoints(Point[] points) {
    if (null == points) {
      throw new IllegalArgumentException("Points array must not be null");
    }
    for (int i = 0; i < points.length; i++) {
      Point p1 = points[i];
      notNull(p1);
      for (int j = i + 1; j < points.length; j++) {
        Point p2 = points[j];
        notNull(p2);
        if (p1.compareTo(p2) == 0) {
          throw new IllegalArgumentException(String.format("Points %d and %d are equal", i, j));
        }
      }
    }
  }

  private void notNull(Point p) {
    if (null == p) {
      throw new IllegalArgumentException("Point must not be null");
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
    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }
}
