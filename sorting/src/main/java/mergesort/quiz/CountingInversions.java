package mergesort.quiz;

import java.util.Arrays;
import java.util.Random;

public class CountingInversions {

  private static final Random random = new Random();

  public static void main(String[] args) {
    int n = 10;
    int[] a = new int[n];
    for (int i = 0; i < n; i++) {
      a[i] = random.nextInt(100);
    }
    System.out.println(Arrays.toString(a));

    System.out.println(countInversions(a));
  }

  public static int countInversions(int[] arr) {
    return countInversions(arr.clone(), new int[arr.length], 0, arr.length);
  }

  public static int countInversions(int[] arr, int[] secondaryArr, int from, int to) {
    if (to - from < 2) {
      return 0;
    }
    int mid = from + (to - from) / 2;
    int inversions = 0;
    inversions += countInversions(arr, secondaryArr, from, mid);
    inversions += countInversions(arr, secondaryArr, mid, to);

    System.arraycopy(arr, from, secondaryArr, from, to - from);

    int i = from;
    int k = from;
    int j = mid;
    while (i < mid && j < to) {
      if (secondaryArr[j] < secondaryArr[i]) {
        arr[k++] = arr[j++];
      } else {
        inversions += k - i;
        arr[k++] = arr[i++];
      }
    }
    while (i < mid) {
      inversions += k - i;
      arr[k++] = arr[i++];
    }
    return inversions;
  }
}
