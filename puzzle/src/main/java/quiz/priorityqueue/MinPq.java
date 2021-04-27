package quiz.priorityqueue;

public class MinPq<E extends Comparable<E>> extends AbstractPq<E> {

  public MinPq() {
    super();
  }

  public MinPq(int initialCapacity) {
    super(initialCapacity);
  }

  @Override
  protected boolean isValidParent(E parent, E e) {
    return parent.compareTo(e) < 0;
  }
}
