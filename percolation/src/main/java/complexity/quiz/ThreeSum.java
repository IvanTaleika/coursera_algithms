package complexity.quiz;

import java.util.Arrays;

// Number of 3 elements that sum to 0 (3-SUM) n^2 implementation
public class ThreeSum {

  public static void main(String[] args) {
    int size = Integer.parseInt(args[0]);
    int[] set = new int[size];
    // n * log(n) - qsort
    Arrays.sort(set);
    // 1. binary search log(n). Total n * log(n) + n^2 * log(n)
    int answer1 = 0;
    for (int i = 0; i < size; i++) {
      for (int j = i; j < size; j++) {
        int sum2 = set[i] + set[j];
        if (sum2 > 0) {
          break;
        }
        int k = Arrays.binarySearch(set, -sum2);
        if (k != -1) {
          answer1++;
        }
      }
    }

    // 2 sides search - n^2
    int answer2 = 0;
    for (int i = 0; i < size - 2; i++) {
      int j = i;
      int k = size - 1;
      while (j < k) {
        int sum3 = set[i] + set[j] + set[k];
        if (sum3 == 0) {
          answer2++;
          j++;
          k--;
        } else if (sum3 < 0) {
          j++;
        } else {
          k--;
        }
      }
    }
  }
}
