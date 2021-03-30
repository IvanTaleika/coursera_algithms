public class PercolationCustom {

  private int height;
  private int topId;
  private int bottomId;
  private int nOpen;
  private int[] grid;
  private int[] weight;

  // creates n-by-n grid, with all sites initially blocked
  public PercolationCustom(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException(String.format("Invalid grid size %d", n));
    }
    height = n;
    int size = height * height + 2;
    topId = 0;
    bottomId = size - 1;
    grid = new int[size];
    weight = new int[size];
    init(topId);
    init(bottomId);
    for (int i = 1; i < bottomId; i++) {
      grid[i] = -1;
      weight[i] = -1;
    }
    nOpen = 0;
  }

  // opens the site (row, col) if it is not open already
  public void open(int row, int col) {
    int pos = calcPosition(row, col);
    if (isClosed(pos)) {
      init(pos);
      connect(pos, pos - 1);
      connect(pos, pos + 1);
      connect(pos, pos - height);
      connect(pos, pos + height);
      nOpen++;
    }
  }

  //  // is the site (row, col) open?
  public boolean isOpen(int row, int col) {
    return !isClosed(calcPosition(row, col));
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

  private void init(int pos) {
    grid[pos] = pos;
    weight[pos] = 1;
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
    return !isClosed(pos);
  }

  private boolean isClosed(int pos) {
    return grid[pos] == -1;
  }

  private int root(int pos) {
    while (grid[pos] != pos) {
      pos = grid[pos];
    }
    return pos;
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
      int root1 = root(pos1);
      int root2 = root(pos2);
      if (weight[root1] > weight[root2]) {
        merge(root2, root1);
      } else {
        merge(root1, root2);
      }
    }
  }

  private void merge(int root, int toRoot) {
    grid[root] = grid[toRoot];
    weight[toRoot] += weight[root];
  }

  private boolean isConnected(int pos1, int pos2) {
    return isOpen(pos1) && isOpen(pos2) && root(pos1) == root(pos2);
  }

  // test client (optional)
  public static void main(String[] args) {
    int n = 10;
    PercolationCustom p = new PercolationCustom(n);
    p.open(11, 5);
    p.open(2, 1);
    p.open(1, 1);
    p.open(3, 3);
    p.open(3, 2);
    p.open(2, 2);
    System.out.println("horray");
  }
}