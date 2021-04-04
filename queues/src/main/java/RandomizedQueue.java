import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

  private static final int STARTING_CAPACITY = 8;
  private int nElements;
  private int capacity;
  private Item[] elements;

  private class RandomizedIterator implements Iterator<Item> {

    private int currentElement;
    private final int creationSize;
    private Item[] iteratingElements;

    RandomizedIterator() {
      currentElement = 0;
      creationSize = nElements;
      iteratingElements = (Item[]) new Object[creationSize];
      System.arraycopy(elements, 0, iteratingElements, 0, creationSize);
      StdRandom.shuffle(iteratingElements);
    }

    @Override
    public boolean hasNext() {
      return currentElement < creationSize;
    }

    @Override
    public Item next() {
      if (!hasNext()) {
        throw new NoSuchElementException("next on empty iterator");
      }
      Item elem = iteratingElements[currentElement];
      iteratingElements[currentElement] = null;
      currentElement++;
      return elem;
    }
  }

  // construct an empty deque
  public RandomizedQueue() {
    nElements = 0;
    capacity = STARTING_CAPACITY;
    elements = (Item[]) new Object[capacity];
  }

  // is the deque empty?
  public boolean isEmpty() {
    return 0 == nElements;
  }

  // return the number of items on the deque
  public int size() {
    return nElements;
  }

  // add the item to the front
  public void enqueue(Item item) {
    checkNull(item);
    checkCapacity();
    elements[nElements] = item;
    nElements++;
  }

  // remove and return the item from the front
  public Item dequeue() {
    checkNotEmpty();
    swapToEnd(StdRandom.uniform(nElements));
    Item element = elements[nElements - 1];
    elements[nElements - 1] = null;
    nElements--;
    checkCapacity();
    return element;
  }

  // remove and return the item from the back
  public Item sample() {
    checkNotEmpty();
    return elements[StdRandom.uniform(nElements)];
  }

  // return an iterator over items in order from front to back
  public Iterator<Item> iterator() {
    return new RandomizedIterator();
  }

  private void swapToEnd(int pos) {
    Item last = elements[nElements - 1];
    elements[nElements - 1] = elements[pos];
    elements[pos] = last;
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

  private void checkCapacity() {
    if (nElements == capacity) {
      changeCapacity(2 * capacity);
    } else if (nElements <= capacity / 4 && capacity / 2 >= STARTING_CAPACITY) {
      changeCapacity(capacity / 2);
    }
  }

  private void changeCapacity(int newCapacity) {
    Item[] newElements = (Item[]) new Object[newCapacity];
    System.arraycopy(elements, 0, newElements, 0, Math.min(capacity, newCapacity));
    elements = newElements;
    capacity = newCapacity;
  }

  // unit testing (required)
  public static void main(String[] args) {
    System.out.printf("Creating the RandomizedQueue%n");
    RandomizedQueue<String> deque = new RandomizedQueue<>();
    testSize(deque);
    try {
      deque.dequeue();
    } catch (NoSuchElementException e) {
      System.out.println("deque throws an exception");
    }

    System.out.println("Foreach on an empty deque doesn't fail");
    for (String s : deque) {
      System.out.println(s);
    }

    System.out.println("Adding 3 elements - 'first', 'second' and 'third'");
    deque.enqueue("second");
    deque.enqueue("third");
    deque.enqueue("first");
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

    System.out.println("Removing 2 elements randomly");
    System.out.println(deque.dequeue());
    System.out.println(deque.dequeue());
    testSize(deque);

    try {
      deque.enqueue(null);
    } catch (IllegalArgumentException e) {
      System.out.println("null cannot be enqueued");
    }
    System.out.println("Adding nulls doesn't change the size");
    testSize(deque);

    System.out.println("Foreach loop over the remaining element");
    for (String s : deque) {
      System.out.println(s);
    }
    System.out.println("Removing the last element");
    System.out.println(deque.dequeue());

    System.out.println("Empty randomized queue");
    testSize(deque);

    int expandCapacityTestNumberOfElements = 1024;
    System.out.println("Testing queue expansion");
    for (int i = 0; i < expandCapacityTestNumberOfElements; i++) {
      deque.enqueue(Integer.toString(i));
    }
    testSize(deque);

    int sampleSize = 10;
    System.out.printf("Taking a sample from %d elements %n", sampleSize);
    for (int i = 0; i < sampleSize; i++) {
      System.out.println(deque.sample());
    }
    testSize(deque);

    System.out.println("Testing queue expansion. Shrinking to a single element");
    for (int i = 0; i < expandCapacityTestNumberOfElements - 1; i++) {
      deque.dequeue();
    }
    testSize(deque);
  }

  private static void testSize(RandomizedQueue<?> deque) {
    System.out.printf("Is empty - %b%n", deque.isEmpty());
    System.out.printf("Size - %d%n", deque.size());
  }
}