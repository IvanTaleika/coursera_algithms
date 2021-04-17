package sort.quiz;

import java.util.Arrays;
import java.util.Random;

public class SortPermutation {

  private static Random random = new Random();

  public static void main(String[] args) {
    int n = 50;
    int[] a = new int[n];
    for (int i = 0; i < n; i++) {
      a[i] = i;
    }
    int[] b = Arrays.copyOf(a, n);

    shuffle(a);
    shuffle(b);
    qsort(a);
    qsort(b);

    for (int i = 0; i < n; i++) {
      if (a[i] != b[i]) {
        System.out.printf("Non-equal elements at %d%n", i);
        return;
      }
    }
    System.out.println("Equal arrays!");
  }

  // shuffle qsort version
  public static void qsort(int[] a) {
    shuffle(a);
    qsort(a, 0, a.length);
  }

  private static void qsort(int[] a, int from, int to) {
    // It's better to use insertion sort when number of elements is small,
    // but we have already implemented it in SortIntersection. So let's use qsort till the end
    if (to - from < 2) {
      return;
    }
    int pivotLeft = from;
    int pivotRight = to;
    int pivot = a[pivotLeft];
    int left = from + 1;
    int right = to - 1;
    while (true) {
      while (a[left] < pivot) {
        left++;
        if (left >= to) {
          break;
        }
      }
      while (a[right] > pivot) {
        right--;
      }
      if (left > right) {
        break;
      }
      if (left == right) {
        left++;
        right--;
        break;
      }
      if (a[left] == pivot) {
        swap(a, left++, ++pivotLeft);
      } else if (a[right] == pivot) {
        swap(a, right--, --pivotRight);
      } else {
        swap(a, left++, right--);
      }
    }
    while (pivotLeft >= from) {
      swap(a, right--, pivotLeft--);
    }
    while (pivotRight < to) {
      swap(a, left++, pivotRight++);
    }
    qsort(a, from, right + 1);
    qsort(a, left, to);
  }

//  private static void qsort(int[] a, int from, int to) {
//    if (from >= to) {
//      return;
//    }
//    int pivotIdx = to - 1;
//    int pivot = a[pivotIdx];
//    int i = from;
//    int firstGreater = from;
//    while (i < pivotIdx) {
//      if (a[i] < pivot) {
//        // Number of exchanges isn't optimal. Even if e < p is already on the left side of the final split point, we move it to the beginning
//        // Example: [10, 11, 2, 3, 12, 13, 0, 1, 9] will sort to [2, 3, 0, 1 | 9 | 12, 13, 10, 11], while classic qsort
//        // result will be [0, 1, 2, 3 | 9 | 12, 13, 10, 11] -> 2 less exchanges
//        swap(a, i, firstGreater);
//        i++;
//        firstGreater++;
//      } else if (a[i] == pivot) {
//        pivotIdx--;
//        swap(a, i, pivotIdx);
//      } else {
//        i++;
//      }
//    }
//    int mid = Math.min(pivotIdx - firstGreater, a.length - pivotIdx);
//    int greaterLeftIndex = a.length - mid;
//    for (int g = firstGreater, k = greaterLeftIndex; k < a.length; g++, k++) {
//      swap(a, g, k);
//    }
//    qsort(a, from, firstGreater);
//    qsort(a, greaterLeftIndex, to);
//  }

  public static void swap(int[] a, int i, int j) {
    int temp = a[j];
    a[j] = a[i];
    a[i] = temp;
  }

  public static void shuffle(int[] a) {
    for (int i = a.length - 1; i > 1; i--) {
      int j = random.nextInt(i + 1);
      swap(a, i, j);
    }
  }

}
