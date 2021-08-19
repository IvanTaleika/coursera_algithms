import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SAP {

    private Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph d) {
        if (d == null) {
            throw new IllegalArgumentException("Digraph cannot be null");
        }
        digraph = new Digraph(d);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return new DigraphPathFinder(digraph, v, w).getShortestPath().length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return new DigraphPathFinder(digraph, v, w).getShortestPath().commonAncestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return new DigraphPathFinder(digraph, v, w).getShortestPath().length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return new DigraphPathFinder(digraph, v, w).getShortestPath().commonAncestor;
    }


    // do unit testing of this class
    public static void main(String[] args) {
        try {
            System.out.print("`null` Digraph: ");
            new SAP(null);
        } catch (IllegalArgumentException e) {
            System.out.println("OK");
        }

        In in = new In("sap/digraph-wordnet.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        try {
            System.out.print("null Iterable: ");
            sap.length(null, null);
        } catch (IllegalArgumentException e) {
            System.out.println("OK");
        }
        try {
            System.out.print("nulls in Iterable: ");
            sap.length(Arrays.asList(1, null, 2), Arrays.asList(5, 6));
        } catch (IllegalArgumentException e) {
            System.out.println("OK");
        }


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