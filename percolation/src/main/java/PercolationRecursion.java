import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class PercolationRecursion {

  private WeightedQuickUnionUF wqu;
  private int height;
  private int topId;
  private int bottomId;
  private int nOpen;
  private boolean[] openSites;
  private boolean[] fullSites;

  // creates n-by-n grid, with all sites initially blocked
  public PercolationRecursion(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException(String.format("Invalid grid size %d", n));
    }
    height = n;
    int gridSize = height * height;
    wqu = new WeightedQuickUnionUF(gridSize + 2);
    openSites = new boolean[gridSize + 2];
    fullSites = new boolean[gridSize + 1];
    topId = 0;
    bottomId = gridSize + 1;
    openSites[topId] = true;
    fullSites[topId] = true;
    openSites[bottomId] = true;
    nOpen = 0;
  }

  // opens the site (row, col) if it is not open already
  public void open(int row, int col) {
    int pos = calcPosition(row, col);
    if (!isOpen(pos)) {
      openSites[pos] = true;
      nOpen++;
      int[] neighbours = getNeighbours(pos);
      for (int i = 0; i < 4 && neighbours[i] != -1; i++) {
        int neighbour = neighbours[i];
        if (isOpen(neighbour)) {
          wqu.union(pos, neighbours[i]);
        }
      }
      for (int i = 0; i < 4 && neighbours[i] != -1; i++) {
        if (neighbours[i] != bottomId && isFull(neighbours[i])) {
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
    int i = 0;
    int left = pos - 1;
    int right = pos + 1;
    int top = pos - height;
    int bottom = pos + height;
    if (pos % height != 1) {
      ns[i] = left;
      i++;
    }
    ns[i] = Math.max(top, topId);
    i++;
    if (pos % height != 0) {
      ns[i] = right;
      i++;
    }
    ns[i] = Math.min(bottom, bottomId);
    return ns;
  }

  private void propagateFull(int pos) {
    if (pos >= topId && pos < bottomId && isOpen(pos) && !isFull(pos)) {
      fullSites[pos] = true;
      int[] neighbours = getNeighbours(pos);
      for (int i = 0; i < 4 && neighbours[i] != -1; i++) {
        propagateFull(neighbours[i]);
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
    PercolationRecursion p = new PercolationRecursion(n);
    p.open(1, 2);
    p.open(2, 1);
    p.open(3, 1);
    p.open(1, 3);
    p.open(3, 3);
    p.open(2, 3);
    System.out.println("horray");
  }
}