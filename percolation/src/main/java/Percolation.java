import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

  private WeightedQuickUnionUF wqu;
  private int height;
  private int gridSize;
  private int topId;
  private int bottomId;
  private int nOpen;
  private boolean[] openSites;
  private boolean[] fullSites;
  private int[] newFulls;

  // creates n-by-n grid, with all sites initially blocked
  public Percolation(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException(String.format("Invalid grid size %d", n));
    }
    height = n;
    gridSize = height * height;
    wqu = new WeightedQuickUnionUF(gridSize + 2);
    openSites = new boolean[gridSize + 2];
    fullSites = new boolean[gridSize + 1];
    topId = 0;
    bottomId = gridSize + 1;
    openSites[topId] = true;
    fullSites[topId] = true;
    openSites[bottomId] = true;
    nOpen = 0;
    newFulls = new int[gridSize];

  }

  // opens the site (row, col) if it is not open already
  public void open(int row, int col) {
    int pos = calcPosition(row, col);
    if (!isOpen(pos)) {
      openSites[pos] = true;
      nOpen++;
      int[] neighbours = getNeighbours(pos);
      for (int neighbour : neighbours) {
        if (neighbour != -1) {
          wqu.union(pos, neighbour);
        }
      }
      for (int neighbour : neighbours) {
        if (neighbour != -1 && neighbour != bottomId && isFull(neighbour)) {
          propagateFull(pos);
          break;
        }
      }
    }
  }

  //  // is the site (row, col) open?
  public boolean isOpen(int row, int col) {
    return isOpen(calcPosition(row, col));
  }

  // is the site (row, col) full?
  public boolean isFull(int row, int col) {
    return isFull(calcPosition(row, col));
  }

  private boolean isFull(int pos) {
    return fullSites[pos];
  }

  // returns the number of open sites
  public int numberOfOpenSites() {
    return nOpen;
  }

  // does the system percolate?
  public boolean percolates() {
    return isConnected(topId, bottomId);
  }

  private int[] getNeighbours(int pos) {
    int[] ns = {-1, -1, -1, -1};
    int left = pos - 1;
    int right = pos + 1;
    int top = Math.max(pos - height, topId);
    int bottom = Math.min(pos + height, bottomId);
    if (pos % height != 1 && isOpen(left)) {
      ns[0] = left;
    }
    if (isOpen(top)) {
      ns[1] = top;
    }
    if (pos % height != 0 && isOpen(right)) {
      ns[2] = right;
    }
    if (isOpen(bottom)) {
      ns[3] = bottom;
    }
    return ns;
  }

  private void propagateFull(int pos) {
    fullSites[pos] = true;
    newFulls[0] = pos;
    int size = 1;
    for (int i = 0; i < size; i++) {
      int newFullPos = newFulls[i];
      int[] neighbours = getNeighbours(newFullPos);
      for (int neighbour : neighbours) {
        if (neighbour != -1 && neighbour != bottomId && !isFull(neighbour)) {
          fullSites[neighbour] = true;
          newFulls[size] = neighbour;
          size++;
        }
      }
    }
  }

  private void checkDimension(int n) {
    if (n < 1 || n > height) {
      throw new IllegalArgumentException(String.format("Invalid grid dimension %d", n));
    }
  }

  private int calcPosition(int row, int col) {
    checkDimension(row);
    checkDimension(col);
    return height * (row - 1) + col;
  }

  private boolean isOpen(int pos) {
    return openSites[pos];
  }

  private boolean isConnected(int pos1, int pos2) {
    return isOpen(pos1) && isOpen(pos2) && wqu.find(pos1) == wqu.find(pos2);
  }

  // test client (optional)
  public static void main(String[] args) {
    int n = 3;
    Percolation p = new Percolation(n);
    p.open(1, 2);
    p.open(2, 1);
    p.open(3, 1);
    p.open(1, 3);
    p.open(3, 3);
    p.open(2, 3);
    System.out.println("horray");
  }
}