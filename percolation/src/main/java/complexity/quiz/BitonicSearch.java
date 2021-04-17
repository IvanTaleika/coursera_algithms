package complexity.quiz;

// And easy way. Find max index p -> lg(n), binary search 2 halfs -> lg(p) + lg(n - p).
// Worst case ~3lg(n) when p = n/2. lg(n) + lg(n/2) + lg(n/2) = lg(n^3/4) = 3lg(n) - 2
// Below solution ~2lg(n)
public class BitonicSearch {

  private int[] arr;

  BitonicSearch(int[] arr) {
    if (arr == null || arr.length < 3) {
      throw new IllegalArgumentException("invalid array size");
    }
    this.arr = arr.clone();
  }

  public int find(int e) {
    return bitonicSearch(e, 0, arr.length - 1);
  }

  private int bitonicSearch(int e, int left, int right) {
    if (left > right) {
      return -1;
    }
    int mid = left + (right - left) / 2;
    int midValue = arr[mid];
    if (midValue == e) {
      return mid;
    }
    if (left == right) {
      return -1;
    }
    int nextValue = arr[mid + 1];
    if (midValue < nextValue && midValue < e) {
      return bitonicSearch(e, mid + 1, right);
    } else if (midValue > nextValue && midValue < e) {
      return bitonicSearch(e, left, mid - 1);
    } else {
      int leftIndex = binarySearchAscending(e, left, mid - 1);
      if (leftIndex != -1) {
        return leftIndex;
      } else {
        return binarySearchDescending(e, mid + 1, right);
      }
    }
  }

  private int binarySearchAscending(int e, int left, int right) {
    while (left <= right) {
      int mid = right - (right - left) / 2;
      if (arr[mid] == e) {
        return mid;
      }
      if (arr[mid] < e) {
        left = mid + 1;
      } else {
        right = mid - 1;
      }
    }
    return -1;
  }

  private int binarySearchDescending(int e, int left, int right) {
    while (left <= right) {
      int mid = right - (right - left) / 2;
      if (arr[mid] == e) {
        return mid;
      }
      if (arr[mid] < e) {
        right = mid - 1;
      } else {
        left = mid + 1;
      }
    }
    return -1;
  }


  public static void main(String[] args) {
//    int[] arr = {4, 6, 1, 0, -1, -10, -11};
    int[] arr = {4, 6, 7, 8, 20, 1, 0, -1};
//    int[] arr = {4, 6, 7, 8, 1, 0, -1};
//    int[] arr = {8, 1, 0, -1};
//    int[] arr = {4, 6, 7, 8};
    BitonicSearch bs = new BitonicSearch(arr);
    System.out.println("Existing");
    for (int e : arr) {
      System.out.println(bs.find(e));
    }
    int[] arr1 = {2, 3, 5, 9, 10, -2, -3};
    System.out.println("Non-existing");
    for (int e : arr1) {
      System.out.println(bs.find(e));
    }
  }
}
