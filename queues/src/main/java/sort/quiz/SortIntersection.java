package sort.quiz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SortIntersection {

  public static void main(String[] args) {
    int minSize = 50;
    Random random = new Random();
    int aSize = random.nextInt(10) + minSize;
    int bSize = random.nextInt(10) + minSize;
    System.out.println(aSize);
    System.out.println(bSize);
    // the task asks for points comparison, but it works exactly the same way as for integers.

    Set<Integer> aSet = new HashSet<>(aSize);
    Set<Integer> bSet = new HashSet<>(bSize);
    while (aSet.size() != aSize) {
      aSet.add(random.nextInt(minSize * 4));
    }
    while (bSet.size() != bSize) {
      bSet.add(random.nextInt(minSize * 4));
    }
    int[] a = aSet.stream().mapToInt(Number::intValue).toArray();
    int[] b = bSet.stream().mapToInt(Number::intValue).toArray();
    System.out.println("Unsorted");
    System.out.println(Arrays.toString(a));
    System.out.println(Arrays.toString(b));
    int[] minArr = aSize < bSize ? a : b;
    int[] maxArr = minArr == a ? b : a;
    // Let's use mergesort for this task
    mergeSort(minArr);
    List<Integer> intersect = new ArrayList<>();
    for (int e : maxArr) {
      if (binarySearch(minArr, e)) {
        intersect.add(e);
      }
    }
    System.out.println("Sorted");
    System.out.println(Arrays.toString(a));
    System.out.println(Arrays.toString(b));
    System.out.println("Intersection");
    System.out.println(intersect);
  }

  public static void mergeSort(int[] arr) {
    // 4 is too little, standard implementation uses 32, but I want more merge invocations
    // on small array sizes for algorithm validation
    int insertionSortThreshold = 4;
    for (int i = 0; i < arr.length; i += insertionSortThreshold) {
      int upperBound = Math.min(i + insertionSortThreshold, arr.length);
      insertionSort(arr, i, upperBound);
    }
    int step = insertionSortThreshold;
    int[] source = arr;
    int[] target = new int[arr.length];
    while (step < arr.length) {
      int i = 0;
      while (i < arr.length) {
        int leftEnd = Math.min(i + step, arr.length);
        int rightEnd = Math.min(i + 2 * step, arr.length);
        merge(source, target, i, leftEnd, leftEnd, rightEnd);
        i += 2 * step;
      }
      int[] tmp = source;
      source = target;
      target = tmp;
      step *= 2;
    }
    if (target == arr) {
      System.arraycopy(source, 0, arr, 0, arr.length);
    }
  }

  public static void merge(int[] source, int[] target, int leftStart, int leftEnd, int rightStart,
      int rightEnd) {
    int i = leftStart;
    while (leftStart < leftEnd && rightStart < rightEnd) {
      if (source[leftStart] <= source[rightStart]) {
        target[i] = source[leftStart];
        leftStart++;
      } else {
        target[i] = source[rightStart];
        rightStart++;
      }
      i++;
    }
    while (leftStart < leftEnd) {
      target[i] = source[leftStart];
      leftStart++;
      i++;
    }
    while (rightStart < rightEnd) {
      target[i] = source[rightStart];
      rightStart++;
      i++;
    }
  }

  public static void insertionSort(int[] arr, int from, int to) {
    for (int i = from; i < to; i++) {
      int e = arr[i];
      for (int j = i - 1; j >= from && e < arr[j]; j--) {
        arr[j + 1] = arr[j];
        arr[j] = e;
      }
    }
  }

  public static boolean binarySearch(int[] arr, int key) {
    int left = 0;
    int right = arr.length - 1;
    while (left <= right) {
      int i = left + (right - left) / 2;
      if (arr[i] == key) {
        return true;
      }
      if (arr[i] > key) {
        right = i - 1;
      } else {
        left = i + 1;
      }
    }
    return false;
  }
}
