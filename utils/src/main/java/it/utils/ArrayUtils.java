package it.utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public final class ArrayUtils {

  private ArrayUtils() {
  }

  public static final Random random = new Random();

  public static int[] randomIntArray(int n, int bound) {
    return randomIntArray(n, bound, 0);
  }

  public static int[] randomIntArray(int n, int bound, int shift) {
    int[] a = new int[n];
    for (int i = 0; i < n; i++) {
      a[i] = random.nextInt(bound) + shift;
    }
    return a;
  }

  public static int[] randomDistinctIntArray(int n, int bound) {
    return randomDistinctIntArray(n, bound, 0);
  }

  public static int[] randomDistinctIntArray(int n, int bound, int shift) {
    if (n > bound) {
      throw new IllegalArgumentException(String.format(
          "Random bound can't be lower then size of the array (n=%d, bound=%d", n, bound));
    }
    Set<Integer> a = new HashSet<>(n);
    while (a.size() < n) {
      a.add(random.nextInt(bound) + shift);
    }
    return a.stream().mapToInt(Number::intValue).toArray();
  }

}
