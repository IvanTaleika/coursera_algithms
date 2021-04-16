import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

public class KdTree {

  private static final RectHV plot = new RectHV(0d, 0d, 1d, 1d);

  private static class Node {

    Point2D key;
    RectHV leftArea;
    RectHV rightArea;
    Node left;
    Node right;
    Color color;

    Node(Point2D key, Color color, RectHV leftArea, RectHV rightArea) {
      this.key = key;
      this.color = color;
      this.leftArea = leftArea;
      this.rightArea = rightArea;
      left = null;
      right = null;
    }

    void draw() {
      double originalRadius = StdDraw.getPenRadius();
      Color originalColor = StdDraw.getPenColor();
      StdDraw.setPenColor(color);
      StdDraw.line(rightArea.xmin(), rightArea.ymin(), leftArea.xmax(), leftArea.ymax());
      StdDraw.setPenColor(Color.black);
      StdDraw.setPenRadius(originalRadius * 5);
      key.draw();
      StdDraw.setPenColor(originalColor);
      StdDraw.setPenRadius(originalRadius);
    }

    int compareTo(Point2D p) {
      if (isGreater(p)) {
        return 1;
      } else if (key.x() == p.x() && key.y() == p.y()) {
        return 0;
      } else {
        return -1;
      }
    }

    private boolean isGreater(Point2D p) {
      if (color == Color.RED) {
        return p.x() < key.x();
      } else {
        return p.y() < key.y();
      }
    }
  }


  private Node root;
  private int s;

  // construct an empty set of points
  public KdTree() {
    root = null;
    s = 0;
  }

  // is the set empty?
  public boolean isEmpty() {
    return s == 0;
  }

  // number of points in the set
  public int size() {
    return s;
  }

  // add the point to the set (if it is not already in the set)
  public void insert(Point2D p) {
    checkNull(p);
    root = insert(root, p, plot, Color.RED);
  }

  private Node insert(Node node, Point2D p, RectHV enclosingRect, Color color) {
    if (node == null) {
      s++;
      RectHV leftArea;
      RectHV rightArea;
      if (color == Color.RED) {
        leftArea = new RectHV(enclosingRect.xmin(), enclosingRect.ymin(), p.x(),
            enclosingRect.ymax());
        rightArea = new RectHV(p.x(), enclosingRect.ymin(), enclosingRect.xmax(),
            enclosingRect.ymax());
      } else {
        leftArea = new RectHV(enclosingRect.xmin(), enclosingRect.ymin(), enclosingRect.xmax(),
            p.y());
        rightArea = new RectHV(enclosingRect.xmin(), p.y(), enclosingRect.xmax(),
            enclosingRect.ymax());
      }
      return new Node(p, color, leftArea, rightArea);
    }
    int compare = node.compareTo(p);
    if (compare < 0) {
      node.right = insert(node.right, p, node.rightArea, nextColor(node.color));
    }
    if (compare > 0) {
      node.left = insert(node.left, p, node.leftArea, nextColor(node.color));
    }
    return node;
  }

  private Color nextColor(Color color) {
    return color == Color.RED ? Color.BLUE : Color.RED;
  }

  // does the set contain point p?
  public boolean contains(Point2D p) {
    checkNull(p);
    return contains(root, p);
  }

  private boolean contains(Node node, Point2D p) {
    if (node == null) {
      return false;
    }
    int compare = node.compareTo(p);
    if (compare < 0) {
      return contains(node.right, p);
    }
    if (compare > 0) {
      return contains(node.left, p);
    }
    return true;
  }

  // draw all points to standard draw
  public void draw() {
    draw(root);
  }

  private void draw(Node node) {
    if (node != null) {
      node.draw();
      draw(node.left);
      draw(node.right);
    }
  }

  // all points that are inside the rectangle (or on the boundary)
  public Iterable<Point2D> range(RectHV rect) {
    checkNull(rect);
    return range(rect, root, new ArrayList<>());
  }

  private Iterable<Point2D> range(RectHV rect, Node node, List<Point2D> included) {
    if (node != null) {
      if (rect.contains(node.key)) {
        included.add(node.key);
      }

      if (rect.intersects(node.leftArea) &&
          (node.color != Color.RED || node.key.x() != rect.xmin()) &&
          (node.color != Color.BLUE || node.key.y() != rect.ymin())
      ) {
        range(rect, node.left, included);
      }
      if (rect.intersects(node.rightArea)) {
        range(rect, node.right, included);
      }
    }
    return included;
  }

  // a nearest neighbor in the set to point p; null if the set is empty
  public Point2D nearest(Point2D p) {
    checkNull(p);
    return nearest(p, root, new Nearest(null, Double.MAX_VALUE)).p;
  }

  private static class Nearest {

    Point2D p;
    double distance;

    Nearest(Point2D p, double distance) {
      this.p = p;
      this.distance = distance;
    }
  }

  private Nearest nearest(Point2D p, Node node, Nearest nearest) {
    if (node != null) {
      Point2D key = node.key;
      double distance = p.distanceSquaredTo(key);
      if (distance < nearest.distance) {
        nearest = new Nearest(key, distance);
      }
      int compare = node.compareTo(p);

      if (compare == 0) {
        return nearest;
      }
      RectHV sameSideRect;
      RectHV oppositeSideRect;
      Node sameSideNode;
      Node oppositeSideNode;
      if (compare < 0) {
        sameSideRect = node.rightArea;
        oppositeSideRect = node.leftArea;
        sameSideNode = node.right;
        oppositeSideNode = node.left;
      } else {
        sameSideRect = node.leftArea;
        oppositeSideRect = node.rightArea;
        sameSideNode = node.left;
        oppositeSideNode = node.right;
      }
      if (sameSideRect.distanceSquaredTo(p) < nearest.distance) {
        nearest = nearest(p, sameSideNode, nearest);
      }
      if (oppositeSideRect.distanceSquaredTo(p) < nearest.distance) {
        nearest = nearest(p, oppositeSideNode, nearest);
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
