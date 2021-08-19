import edu.princeton.cs.algs4.Digraph;

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
}