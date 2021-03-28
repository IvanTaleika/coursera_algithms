import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {

  public static void main(String[] args) {
    String win = "";
    for(int n = 1; !StdIn.isEmpty(); n++) {
      String next = StdIn.readString();
      if (StdRandom.bernoulli(1d / n))
        win = next;
    }
    StdOut.println(win);

  }

}
