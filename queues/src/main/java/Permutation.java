import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {

  public static void main(String[] args) {
    int k = Integer.parseInt(args[0]);
    if (k != 0) {
      RandomizedQueue<String> rq = new RandomizedQueue<>();
      for (int i = 1; !StdIn.isEmpty(); i++) {
        String next = StdIn.readString();
        if (i <= k) {
          rq.enqueue(next);
        } else if (StdRandom.bernoulli(1d * k / i)) {
          rq.dequeue();
          rq.enqueue(next);
        }
      }
      for (String s : rq) {
        StdOut.println(s);
      }
    }
  }
}
