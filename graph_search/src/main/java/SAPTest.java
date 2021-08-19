import static it.utils.TestUtils.testIllegalArgumentException;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SAPTest {

    public static void main(String[] args) {
        testIllegalArgumentException(() -> new SAP(null));

        In in = new In("sap/digraph-wordnet.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        testIllegalArgumentException(() -> sap.length(null, null));
        testIllegalArgumentException(() -> sap.length(Arrays.asList(1, null, 2), Arrays.asList(5, 6)));

        while (!StdIn.isEmpty()) {
            String a = StdIn.readString();
            String b = StdIn.readString();
            List<Integer> v = new ArrayList<>();
            List<Integer> w = new ArrayList<>();
            for (String p : a.split(",")) {
                v.add(Integer.parseInt(p));
            }
            for (String p : b.split(",")) {
                w.add(Integer.parseInt(p));
            }

            try {
                int length = sap.length(v, w);
                StdOut.printf("length = %d\n", length);
            } catch (IllegalArgumentException e) {
                System.out.printf(
                        "IllegalArgumentException has been thrown for v=`%s`, w=`%s` by `length` method%n",
                        v, w);
            }
            try {
                int ancestor = sap.ancestor(v, w);
                StdOut.printf("ancestor = %d\n", ancestor);
            } catch (IllegalArgumentException e) {
                System.out.printf(
                        "IllegalArgumentException has been thrown for v=`%s`, w=`%s` by `ancestor` method%n",
                        v, w);
            }
            System.out.println("");
        }
    }
}
