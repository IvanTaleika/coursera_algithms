package quiz.priorityqueue;

import it.utils.ArrayUtils;
import java.util.Arrays;

public class MedianPq<E extends Comparable<E>> implements PriorityQueue<E> {

  private MaxPq<E> maxPq;
  private MinPq<E> minPq;

  public MedianPq() {
    maxPq = new MaxPq<>();
    minPq = new MinPq<>();
  }

  @Override
  public int size() {
    return maxPq.size() + minPq.size();
  }

  @Override
  public void insert(E e) {
    if (size() == 0) {
      maxPq.insert(e);
    } else {
      E median = peek();
      if (e.compareTo(median) <= 0) {
        maxPq.insert(e);
        if (maxPq.size() - minPq.size() > 1) {
          minPq.insert(maxPq.take());
        }
      } else {
        minPq.insert(e);
        if (minPq.size() > maxPq.size()) {
          maxPq.insert(minPq.take());
        }
      }
    }
  }

  @Override
  public E take() {
    E median = maxPq.take();
    if (maxPq.size() < minPq.size()) {
      maxPq.insert(minPq.take());
    }
    return median;
  }

  @Override
  public E peek() {
    return maxPq.peek();
  }

  public static void main(String[] args) {
    int n = 10;
    int bound = 20;
    int[] a = ArrayUtils.randomIntArray(n, bound);
    MedianPq<Integer> medianPq = new MedianPq<>();
    System.out.println(Arrays.toString(a));
    for (int e : a) {
      medianPq.insert(e);
      System.out.printf("size=%d, median=%d%n", medianPq.size(), medianPq.peek());
    }
    int[] sortedA = a.clone();
    Arrays.sort(sortedA);
    System.out.println(Arrays.toString(sortedA));
    System.out.println("Reverse:");
    while (medianPq.size() > 0) {
      System.out.printf("size=%d, median=%d%n", medianPq.size(), medianPq.take());
    }
  }

}
