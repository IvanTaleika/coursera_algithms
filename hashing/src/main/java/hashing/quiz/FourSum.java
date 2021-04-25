package hashing.quiz;

import it.utils.ArrayUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FourSum {

  static class Pair {

    final int _1;
    final int _2;

    Pair(int a, int b) {
      _1 = a;
      _2 = b;
    }

    @Override
    public String toString() {
      return "(" + _1 + "," + _2 + ')';
    }
  }

  public static void main(String[] args) {
    int boundMultiplier = 20;
    int n = 10;
    int[] a = ArrayUtils.randomDistinctIntArray(n, boundMultiplier * n, -boundMultiplier * n / 2);
    System.out.println(Arrays.toString(a));
    Map<Integer, List<Pair>> fourSum = new HashMap<>();
    for (int i = 0; i < n; i++) {
      for (int j = i + 1; j < n; j++) {
        int sum = a[i] + a[j];
        Pair pair = new Pair(i, j);
        fourSum.computeIfAbsent(sum, k -> new ArrayList<>()).add(pair);
      }
    }
    System.out.println("Same sum indices:");
    for (Map.Entry<Integer, List<Pair>> e : fourSum.entrySet()) {
      if (e.getValue().size() > 1) {
        System.out.printf("%s -> %s%n", e.getKey(), e.getValue());
      }
    }

  }

}
