package complexity.quiz;

// Suppose that you have an nn-story building (with floors 1 through n) and plenty of eggs.
// An egg breaks if it is dropped from floor T or higher and does not break otherwise.
// Your goal is to devise a strategy to determine the value of T given the following
// limitations on the number of eggs and tosses:
//
//    Version 0: 1 egg, <= T tosses.
//    Version 1: ~ 1 lg(n) eggs and  ~ 1 lg(n) tosses.
//    Version 2: ~ lg(T) eggs and  ~ 2 lg(T) tosses.
//    Version 3: 2 eggs and  ~ 2 sqrt(n) tosses.
//    Version 4: 2 eggs and  <= c * sqrt(T) tosses for some fixed constant c.
public class EggDrop {

  private int t;

  public int version0(int n) {
    int eggs = 1;
    for (int floor = 1; floor <= n; floor++) {
      if (toss(floor)) {
        return floor;
      }
    }
    return -1;
  }

  public int version1(int n) {
    int eggs = log2Ceil(n);
    int left = 1;
    int right = n;
    while (left <= right) {
      assert eggs > 0;
      int mid = left + (right - left) / 2;
      boolean broken = toss(mid);
      if (broken) {
        if (left == right) {
          return left;
        }
        left = mid + 1;
        eggs--;
      } else {
        right = mid - 1;
      }
    }
    return -1;
  }

  public int version2(int n) {
    // Need to go power of 2 up, when egg breaks do binary search between 2 last points, when you have 1 egg, go sequential.
    return -1;
  }

  public int version3(int n) {
    int eggs = 2;
    int step = (int) (Math.sqrt(1d * n) + 0.5);
    int floor = step;
    while (floor <= n) {
      if (toss(floor)) {
        eggs--;
        for (int j = floor - step + 1; j <= floor; j++) {
          if (toss(j)) {
            return j;
          }
        }
      }
      floor += step;
    }
    for (int j = floor + 1; j <= n; j++) {
      if (toss(j)) {
        return j;
      }
    }
    return -1;
    // worst - 2 * sqrt(n). Best - sqrt(n)
  }

  public int version4(int n) {
    int base = 1;
    int floor = (int) Math.pow(base, 2);
    while (floor <= n && !(toss(floor))) {
      base++;
      floor = (int) Math.pow(base, 2);
    }
    int upperBound = Math.min(floor, n);
    for (int i = (int) Math.pow(base - 1d, 2); i <= upperBound; i++) {
      if (toss(i)) {
        return i;
      }
    }
    return -1;
  }

  private int log2Floor(int n) {
    return 32 - Integer.numberOfLeadingZeros(n);
  }

  private int log2Ceil(int n) {
    return 31 - Integer.numberOfLeadingZeros(n - 1);
  }

  private boolean toss(int floor) {
    return floor >= t ? true : false;
  }
}
