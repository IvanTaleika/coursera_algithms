import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class PercolationBackwash {

  private WeightedQuickUnionUF wqu;
  private int height;
  private int topId;
  private int bottomId;
  private int nOpen;
  private boolean[] openSites;

  // creates n-by-n grid, with all sites initially blocked
  public PercolationBackwash(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException(String.format("Invalid grid size %d", n));
    }
    height = n;
    int size = height * height + 2;
    wqu = new WeightedQuickUnionUF(size);
    openSites = new boolean[size];
    topId = 0;
    bottomId = size - 1;
    openSites[topId] = true;
    openSites[bottomId] = true;
    nOpen = 0;
  }

  // opens the site (row, col) if it is not open already
  public void open(int row, int col) {
    int pos = calcPosition(row, col);
    if (!isOpen(pos)) {
      openSites[pos] = true;
      connect(pos, pos - 1);
      if (pos - 1 != topId) {
        connect(pos, pos - height);
      }
      connect(pos, pos + 1);
      if (pos + 1 != bottomId) {
        connect(pos, pos + height);
      }
      nOpen++;
    }
  }

  //  // is the site (row, col) open?
  public boolean isOpen(int row, int col) {
    return isOpen(calcPosition(row, col));
  }


  // is the site (row, col) full?
  public boolean isFull(int row, int col) {
    return isConnected(calcPosition(row, col), topId);
  }

  // returns the number of open sites
  public int numberOfOpenSites() {
    return nOpen;
  }

  // does the system percolate?
  public boolean percolates() {
    return isConnected(topId, bottomId);
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

  private void connect(int pos1, int pos2) {
    if (pos2 < topId) {
      connect(pos1, topId);
    } else if (pos2 > bottomId) {
      connect(pos1, bottomId);
    } else if (isOpen(pos2) &&
        !(pos2 % height == 0 && pos1 == pos2 + 1 && pos2 != topId) &&
        !(pos1 % height == 0 && pos2 == pos1 + 1 && pos2 != bottomId)
    ) {
      wqu.union(pos1, pos2);
    }
  }

  private boolean isConnected(int pos1, int pos2) {
    return isOpen(pos1) && isOpen(pos2) && wqu.find(pos1) == wqu.find(pos2);
  }

  // test client (optional)
  public static void main(String[] args) {
    int n = 3;
    PercolationBackwash p = new PercolationBackwash(n);
    p.open(2, 1);
    p.open(3, 1);
    p.open(1, 3);
    p.open(3, 3);
    p.open(2, 3);
    System.out.println("horray");
  }
}