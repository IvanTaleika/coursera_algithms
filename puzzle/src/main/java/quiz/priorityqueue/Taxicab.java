package quiz.priorityqueue;

import it.utils.ArrayUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Taxicab {

  public static void heapSort(long[] a) {
    buildHeap(a);
    sortHeap(a);
  }


  public static void buildHeap(long[] a) {
    int i = a.length >> 1;
    do {
      i--;
      sink(a, i, a.length);
    } while (i != 0);
  }

  public static void sink(long[] a, int i, int size) {
    int swapChildId = (i << 1) + 1;
    while (swapChildId < size) {
      int rightChildId = swapChildId + 1;
      if (rightChildId < size && a[rightChildId] > a[swapChildId]) {
        swapChildId = rightChildId;
      }
      if (a[swapChildId] <= a[i]) {
        break;
      }
      swap(a, swapChildId, i);
      i = swapChildId;
      swapChildId = (i << 1) + 1;
    }
  }

  public static void swap(long[] a, int i, int j) {
    long temp = a[j];
    a[j] = a[i];
    a[i] = temp;
  }

  public static void sortHeap(long[] a) {
    for (int i = a.length - 1; i > 0; i--) {
      swap(a, i, 0);
      sink(a, 0, i);
    }
  }

  public static void main(String[] args) {
    int n = 20;
    List<Long> values = new ArrayList<>();
    for (int i = 1; i < n; i++) {
      for (int j = i + 1; j < n; j++) {
        long aQube = (long) Math.pow(i, 3);
        long bQube = (long) Math.pow(j, 3);
        values.add(aQube + bQube);
      }
    }
    long[] a = values.stream().mapToLong(Long::longValue).toArray();
    System.out.println("Unsorted");
    System.out.println(Arrays.toString(a));
    heapSort(a);
    System.out.println("Sorted");
    System.out.println(Arrays.toString(a));
    long prevNumber = -1;
    int c = 1;
    System.out.println("Taxicabs: ");
    for (long e : a) {
      if (prevNumber != e) {
        prevNumber = e;
        c = 1;
      } else {
        c++;
        if (c == 2) {
          System.out.println(prevNumber);
        }
      }
    }
  }

}
