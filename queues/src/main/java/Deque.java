import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

  private int s;
  private Node front;
  private Node back;

  private class Node {

    Item value;
    Node prev;
    Node next;
  }

  // construct an empty deque
  public Deque() {
    s = 0;
    front = null;
    back = null;
  }

  // is the deque empty?
  public boolean isEmpty() {
    return 0 == s;
  }

  // return the number of items on the deque
  public int size() {
    return s;
  }

  // add the item to the front
  public void addFirst(Item item) {
    checkNull(item);
    Node newNode = new Node();
    newNode.value = item;
    newNode.prev = front;
    if (!isEmpty()) {
      front.next = newNode;
    } else {
      back = newNode;
    }
    front = newNode;
    s++;
  }

  // add the item to the back
  public void addLast(Item item) {
    checkNull(item);
    Node newNode = new Node();
    newNode.value = item;
    newNode.next = back;
    if (!isEmpty()) {
      back.prev = newNode;
    } else {
      front = newNode;
    }
    back = newNode;
    s++;
  }

  // remove and return the item from the front
  public Item removeFirst() {
    checkNotEmpty();
    s--;
    Node first = front;
    front = first.prev;
    first.prev = null;
    if (!isEmpty()) {
      front.next = null;
    } else {
      back = null;
    }
    return first.value;
  }

  // remove and return the item from the back
  public Item removeLast() {
    checkNotEmpty();
    s--;
    Node last = back;
    back = last.next;
    last.next = null;
    if (!isEmpty()) {
      back.prev = null;
    } else {
      front = null;
    }
    return last.value;
  }

  // return an iterator over items in order from front to back
  public Iterator<Item> iterator() {
    return new Iterator<Item>() {
      private Node iteratorFront = front;

      @Override
      public boolean hasNext() {
        return null != iteratorFront;
      }

      @Override
      public Item next() {
        if (!hasNext()) {
          throw new NoSuchElementException("next on empty iterator");
        }
        Node node = iteratorFront;
        iteratorFront = iteratorFront.prev;
        return node.value;
      }
    };
  }

  private void checkNull(Item item) {
    if (null == item) {
      throw new IllegalArgumentException("Cannot add null element to the queue");
    }
  }

  private void checkNotEmpty() {
    if (isEmpty()) {
      throw new NoSuchElementException("Cannot remove from an empty queue");
    }
  }

  // unit testing (required)
  public static void main(String[] args) {
    System.out.printf("Creating the deque%n");
    Deque<String> deque = new Deque<>();
    testSize(deque);
    try {
      deque.removeFirst();
    } catch (NoSuchElementException e) {
      System.out.println("removeFirst throws an exception");
    }
    try {
      deque.removeLast();
    } catch (NoSuchElementException e) {
      System.out.println("removeLast throws an exception");
    }

    System.out.println("Foreach on an empty deque doesn't fail");
    for (String s : deque) {
      System.out.println(s);
    }
    System.out.println("Adding 3 elements - 'first', 'second' and 'third'");
    deque.addFirst("second");
    deque.addLast("third");
    deque.addFirst("first");
    testSize(deque);
    System.out.println("Creating 2 iterators");
    Iterator<String> first = deque.iterator();
    Iterator<String> second = deque.iterator();
    try {
      first.remove();
    } catch (UnsupportedOperationException e) {
      System.out.println("remove operation on the iterator isn't supported");
    }
    System.out.println("Iterating over the first iterator");
    while (first.hasNext()) {
      System.out.println(first.next());
    }
    try {
      first.next();
    } catch (NoSuchElementException e) {
      System.out.println("First iterator has no more elements");
    }
    System.out.println("Iterating over the second iterator");
    while (second.hasNext()) {
      System.out.println(second.next());
    }

    System.out.println("Removing elements from the front and back of the deque");
    System.out.printf("First element - %s%n", deque.removeFirst());
    System.out.printf("Last element - %s%n", deque.removeLast());
    testSize(deque);

    try {
      deque.addLast(null);
    } catch (IllegalArgumentException e) {
      System.out.println("null cannot be added to the back of the deque");
    }
    try {
      deque.addFirst(null);
    } catch (IllegalArgumentException e) {
      System.out.println("null cannot be added to the front of the deque");
    }
    System.out.println("Adding nulls doesn't change the size");
    testSize(deque);

    System.out.println("Foreach loop over the remaining element");
    for (String s : deque) {
      System.out.println(s);
    }
    System.out.println("Removing the last element");
    System.out.println(deque.removeFirst());

    System.out.println("Finishing with an empty deque");
    testSize(deque);

    int expandCapacityTestNumberOfElements = 1024;
    System.out.println("Testing queue expansion");
    for (int i = 0; i < expandCapacityTestNumberOfElements; i++) {
      deque.addFirst(Integer.toString(i));
    }
    testSize(deque);

  }

  private static void testSize(Deque<?> deque) {
    System.out.printf("Is empty - %b%n", deque.isEmpty());
    System.out.printf("Size - %d%n", deque.size());
  }
}