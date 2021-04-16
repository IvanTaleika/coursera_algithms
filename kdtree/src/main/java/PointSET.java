import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

public class PointSET {

  private NavigableSet<Point2D> points;

  // construct an empty set of points
  public PointSET() {
    points = new TreeSet<>();
  }

  // is the set empty?
  public boolean isEmpty() {
    return points.isEmpty();
  }

  // number of points in the set
  public int size() {
    return points.size();
  }

  // add the point to the set (if it is not already in the set)
  public void insert(Point2D p) {
    checkNull(p);
    points.add(p);
  }

  // does the set contain point p?
  public boolean contains(Point2D p) {
    checkNull(p);
    return points.contains(p);
  }

  // draw all points to standard draw
  public void draw() {
    points.forEach(Point2D::draw);
  }

  // all points that are inside the rectangle (or on the boundary)
  public Iterable<Point2D> range(RectHV rect) {
    checkNull(rect);
    // O(n)
    List<Point2D> included = new ArrayList<>();
    for (Point2D p : points) {
      if (rect.contains(p)) {
        included.add(p);
      }
    }

    return included;

//    Point2D bottomLeftCorner = new Point2D(rect.xmin(), rect.ymin());
//    Point2D upperRightCorner = new Point2D(rect.xmax(), rect.ymax());
//    Set<Point2D> slice = points.subSet(bottomLeftCorner, upperRightCorner);
//    Set<Point2D> leftSlicePart = points.floor(bottomLeftCorner, upperRightCorner);
//    new HashSet<>(slice);
  }

  // a nearest neighbor in the set to point p; null if the set is empty
  public Point2D nearest(Point2D p) {
    checkNull(p);
    // O(n)
    Point2D nearest = null;
    double minDistance = Double.MAX_VALUE;
    for (Point2D p1 : points) {
      double distance = p.distanceSquaredTo(p1);
      if (distance < minDistance) {
        nearest = p1;
        minDistance = distance;
      }
    }

    return nearest;
  }

  private void checkNull(Object o) {
    if (o == null) {
      throw new IllegalArgumentException("Nulls are not supported");
    }
  }

  // unit testing of the methods (optional)
  public static void main(String[] args) {
  }
}
