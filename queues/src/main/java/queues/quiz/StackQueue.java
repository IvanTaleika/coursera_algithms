package queues.quiz;

import java.util.Stack;

public class StackQueue<E> {

  private Stack<E> enqueueStack;
  private Stack<E> dequeueStack;
  private int s;

  public StackQueue() {
    enqueueStack = new Stack<>();
    dequeueStack = new Stack<>();
    s = 0;
  }

  public boolean isEmpty() {
    return s == 0;
  }

  public int size() {
    return s;
  }

  public void enqueue(E e) {
    enqueueStack.push(e);
    s++;
  }

  public E dequeue() {
    while (!enqueueStack.isEmpty()) {
      dequeueStack.push(enqueueStack.pop());
    }
    if (dequeueStack.isEmpty()) {
      return null;
    }
    return dequeueStack.pop();
  }
}
