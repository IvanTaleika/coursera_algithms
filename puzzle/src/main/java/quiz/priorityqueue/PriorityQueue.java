package quiz.priorityqueue;

public interface PriorityQueue<E extends Comparable<E>> {
  int size();
  void insert(E e);
  E take();
  E peek();
}
