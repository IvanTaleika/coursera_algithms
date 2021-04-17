package queues.quiz;

import java.util.ArrayList;
import java.util.List;

public class MaxStack<E extends Comparable<E>> {

  private List<E> stack;
  private List<Integer> maxIndexes;

  public MaxStack() {
    stack = new ArrayList<>();
    maxIndexes = new ArrayList<>();
  }

  public E max() {
    if (maxIndexes.isEmpty()) {
      return null;
    }
    return stack.get(maxIndexes.get(maxIndexes.size() - 1));
  }

  public void push(E e) {
    stack.add(e);
    if (max() == null || e.compareTo(max()) > 0) {
      maxIndexes.add(stack.size() - 1);
    }
  }

  public E pop() {
    if (stack.isEmpty()) {
      return null;
    }
    E last = stack.remove(stack.size() - 1);
    if (stack.size() == maxIndexes.get(maxIndexes.size() - 1)) {
      maxIndexes.remove(maxIndexes.size() - 1);
    }
    return last;
  }

}
