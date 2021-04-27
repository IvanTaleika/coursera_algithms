package quiz.priorityqueue;

import it.utils.ArrayUtils;
import java.util.Arrays;
import java.util.Random;

public class RandomMaxPq<E extends Comparable<E>> extends MaxPq<E> {

  private Random random;

  public RandomMaxPq(Random random) {
    super();
    this.random = random;
  }

  public RandomMaxPq() {
    this(new Random());
  }


  private E sample() {
    return getPq().get(random.nextInt(size()) + 1);
  }

  private E takeRandom() {
    return del(random.nextInt(size()) + 1);
  }

  public static void main(String[] args) {
    int n = 25;
    int bound = 50;
    RandomMaxPq<Integer> pq = new RandomMaxPq<>();
    int[] a = ArrayUtils.randomIntArray(n, bound);
    for (int e : a) {
      pq.insert(e);
    }
    System.out.printf("Array:%n%s", Arrays.toString(a));
    System.out.println("\n\nDescending");
    while (pq.size() > 0) {
      System.out.printf("%d, ", pq.take());
    }
    for (int e : a) {
      pq.insert(e);
    }
    System.out.println("\n\nRandom 1");
    while (pq.size() > 0) {
      System.out.printf("%d, ", pq.takeRandom());
    }
    for (int e : a) {
      pq.insert(e);
    }
    System.out.println("\n\nRandom 2");
    while (pq.size() > 0) {
      System.out.printf("%d, ", pq.takeRandom());
    }
  }

}
