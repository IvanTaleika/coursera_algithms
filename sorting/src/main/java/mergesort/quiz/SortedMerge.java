package mergesort.quiz;

import java.util.Arrays;
import java.util.Random;

public class SortedMerge {

  private static final Random random = new Random();

  public static void main(String[] args) {
    int n = 10;
    int[] a = new int[n];
    int[] b = new int[n];
    for (int i = 0; i < n; i++) {
      a[i] = random.nextInt(100);
      b[i] = random.nextInt(100);
    }
    Arrays.sort(a);
    Arrays.sort(b);

    int[] c = new int[2 * n];
    System.arraycopy(a, 0, c, 0, n);
    System.arraycopy(b, 0, c, n, n);
    System.out.println(Arrays.toString(c));
    System.out.println();

    // merge for sorted halfs
    int[] d = new int[n];
    System.arraycopy(c, 0, d, 0, n);
    int k = 0, i = 0, j = n;
    while (i < n && j < 2 * n) {
      if (c[j] < d[i]) {
        c[k++] = c[j++];
      } else {
        c[k++] = d[i++];
      }
    }
    while (i < n) {
      c[k++] = d[i++];
    }
    while (j < 2 * n) {
      c[k++] = c[j++];
    }
    System.out.println(Arrays.toString(c));
  }

}
