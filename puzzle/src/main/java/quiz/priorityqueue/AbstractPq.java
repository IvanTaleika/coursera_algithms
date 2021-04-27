package quiz.priorityqueue;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPq<E extends Comparable<E>> implements PriorityQueue<E> {

  private List<E> pq;
  private int s;

  protected AbstractPq(int initialCapacity) {
    pq = new ArrayList<>(initialCapacity + 1);
    // dummy 0s element
    pq.add(null);
  }

  protected AbstractPq() {
    pq = new ArrayList<>();
    // dummy 0s element
    pq.add(null);
  }

  public int size() {
    return s;
  }

  protected List<E> getPq() {
    return pq;
  }

  public void insert(E e) {
    s++;
    pq.add(e);
    swim(s);
  }

  public E take() {
    return del(1);
  }

  public E peek() {
    return pq.get(1);
  }

  protected E del(int id) {
    swap(id, s);
    E e = pq.remove(s);
    s--;
    sink(id);
    return e;
  }

  protected void swap(int i, int j) {
    E tmp = pq.get(i);
    pq.set(i, pq.get(j));
    pq.set(j, tmp);
  }

  protected boolean isValidParentIdx(int i, int j) {
    return isValidParent(getPq().get(i), getPq().get(j));
  }

  protected abstract boolean isValidParent(E parent, E e);

  protected void swim(int i) {
    int parentId = i >> 1;
    while (parentId > 0 && isValidParentIdx(i, parentId)) {
      swap(i, parentId);
      i = parentId;
      parentId = i >> 1;
    }
  }

  protected void sink(int i) {
    // Swap child is a left child
    int swapChildId = i << 1;
    while (swapChildId <= s) {
      int rightChildId = swapChildId + 1;
      if (rightChildId <= s && isValidParentIdx(rightChildId, swapChildId)) {
        swapChildId++;
      }
      if (!isValidParentIdx(swapChildId, i)) {
        break;
      }
      swap(i, swapChildId);
      i = swapChildId;
      swapChildId = i << 1;
    }
  }

}
