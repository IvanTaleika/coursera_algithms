import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WordNet {

    private int size;
    private int root;
    private Map<String, List<Integer>> nounToIds = new HashMap<>();
    private List<String> synsets = new ArrayList<>();
    private SAP sap;
    private Digraph dag;
    private final boolean[] marked;
    private final boolean[] inStack;

    // constructor takes the name of the two input files
    public WordNet(String synsetsFile, String hypernymsFile) {
        if (synsetsFile == null || hypernymsFile == null) {
            throw new IllegalArgumentException("Arguments must not be null.");
        }

        In synsetsStream = new In(synsetsFile);
        while (synsetsStream.hasNextLine()) {
            String synsetInfo = synsetsStream.readLine();
            String[] columns = synsetInfo.split(",");
            Integer id = Integer.valueOf(columns[0]);
            String synset = columns[1];
            String[] nouns = synset.split(" ");
            synsets.add(synset);
            for (String noun : nouns) {
                nounToIds.compute(noun, (k, v) -> {
                    if (v == null) {
                        ArrayList<Integer> ids = new ArrayList<>();
                        ids.add(id);
                        return ids;
                    } else {
                        v.add(id);
                        return v;
                    }
                });
            }
        }
        synsetsStream.close();
        size = synsets.size();
        dag = new Digraph(size);

        In hypernymsStream = new In(hypernymsFile);
        while (hypernymsStream.hasNextLine()) {
            String synsetRelations = hypernymsStream.readLine();
            String[] columns = synsetRelations.split(",");

            int hyponym = Integer.parseInt(columns[0]);
            for (int i = 1; i < columns.length; i++) {
                dag.addEdge(hyponym, Integer.parseInt(columns[i]));
            }
        }
        hypernymsStream.close();

        root = -1;
        for (int i = 0; i < size; i++) {
            if (dag.outdegree(i) == 0) {
                if (root == -1) {
                    root = i;
                } else {
                    throw new IllegalArgumentException(
                            String.format("Multiple roots found: (%d, %d)", root, i));
                }
            }
        }

        marked = new boolean[size];
        inStack = new boolean[size];
        for (int i = 0; i < size; i++) {
            checkCycles(dag, i);
        }
        sap = new SAP(dag);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounToIds.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("Nulls are not allowed");
        }
        return nounToIds.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        Iterable<Integer> v = getIds(nounA);
        Iterable<Integer> w = getIds(nounB);
        return sap.length(v, w);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        Iterable<Integer> v = getIds(nounA);
        Iterable<Integer> w = getIds(nounB);
        return synsets.get(sap.ancestor(v, w));
    }

    private Iterable<Integer> getIds(String noun) {
        if (!isNoun(noun)) {
            throw new IllegalArgumentException(String.format("Noun `%s` not found", noun));
        }
        return nounToIds.get(noun);
    }

    private void checkCycles(Digraph graph, int vertex) {
        if (inStack[vertex]) {
            throw new IllegalArgumentException("Cycle is found");
        }
        if (marked[vertex]) {
            return;
        }
        inStack[vertex] = true;
        marked[vertex] = true;
        for (int adj : graph.adj(vertex)) {
            checkCycles(graph, adj);
        }
        inStack[vertex] = false;
    }
}