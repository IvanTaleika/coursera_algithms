package quiz.priorityqueue;

public class MaxPq<E extends Comparable<E>> extends AbstractPq<E> {

  @Override
  protected boolean isValidParent(E parent, E e) {
    return parent.compareTo(e) > 0;
  }
}
