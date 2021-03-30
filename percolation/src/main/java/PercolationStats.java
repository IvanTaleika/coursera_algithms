import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

  private double cf = 1.96;
  private double m;
  private double s;
  private double cl;
  private double ch;

  // perform independent trials on an n-by-n grid
  public PercolationStats(int n, int trials) {
    if (n <= 0 || trials <= 0) {
      throw new IllegalArgumentException("Invalid experiment");
    }
    int size = n * n;
    double[] thresholds = new double[trials];
    int row;
    int col;
    for (int i = 0; i < trials; i++) {
      Percolation p = new Percolation(n);
      while (!p.percolates()) {
        do {
          row = StdRandom.uniform(1, n + 1);
          col = StdRandom.uniform(1, n + 1);
        } while (p.isOpen(row, col));
        p.open(row, col);
      }
      thresholds[i] = 1d * p.numberOfOpenSites() / size;
    }
    s = StdStats.stddev(thresholds);
    m = StdStats.mean(thresholds);
    cl = m - cf * s / Math.sqrt(trials);
    ch = m + cf * s / Math.sqrt(trials);
  }

  // sample mean of percolation threshold
  public double mean() {
    return m;
  }

  // sample standard deviation of percolation threshold
  public double stddev() {
    return s;
  }

  // low endpoint of 95% confidence interval
  public double confidenceLo() {
    return cl;
  }

  // high endpoint of 95% confidence interval
  public double confidenceHi() {
    return ch;
  }

  // test client (see below)
  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);
    int t = Integer.parseInt(args[1]);
    PercolationStats ps = new PercolationStats(n, t);
    System.out.printf("mean                    = %f%n", ps.mean());
    System.out.printf("stddev                  = %f%n", ps.stddev());
    System.out
        .printf("95%% confidence interval = [%f, %f]%n", ps.confidenceLo(), ps.confidenceHi());
  }

}