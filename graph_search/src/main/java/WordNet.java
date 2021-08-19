import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WordNet {

    private static final String TEST_FOLDER = "wordNet";

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
            throw new IllegalArgumentException("Cycle found");
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

    private static class TestCase {

        String synsetsFile;
        List<String> hypernymsFiles;

        TestCase(String synsetsFile, String... hypernymsFiles) {
            this.synsetsFile = fullPath(synsetsFile);
            this.hypernymsFiles = new ArrayList<>(hypernymsFiles.length);
            for (String file : hypernymsFiles) {
                this.hypernymsFiles.add(fullPath(file));
            }
        }

        private String fullPath(String name) {
            return String.format("%s/%s", TEST_FOLDER, name);
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        List<TestCase> invalidTestCases = new ArrayList<>();
        invalidTestCases.add(
                new TestCase("synsets3.txt", "hypernyms3InvalidCycle.txt",
                        "hypernyms3InvalidTwoRoots.txt"));
        invalidTestCases.add(
                new TestCase("synsets6.txt", "hypernyms6InvalidCycle+Path.txt",
                        "hypernyms6InvalidCycle.txt",
                        "hypernyms6InvalidTwoRoots.txt"));

        try {
            System.out.print("Testing null arguments: ");
            new WordNet(null, null);
            throw new RuntimeException("IllegalArgumentException expected, but not thrown");
        } catch (IllegalArgumentException e) {
            System.out.println("OK!");
        }

        for (TestCase testCase : invalidTestCases) {
            for (String hypernymsFile : testCase.hypernymsFiles) {
                try {
                    System.out.printf("Testing files (%s, %s): ", testCase.synsetsFile,
                            hypernymsFile);
                    new WordNet(testCase.synsetsFile, hypernymsFile);
                    throw new RuntimeException("IllegalArgumentException expected, but not thrown");
                } catch (IllegalArgumentException e) {
                    System.out.println("OK");
                }
            }
        }

        String synsetsFiles = "wordNet/synsets50000-subgraph.txt";
        String hypernymsFiles = "wordNet/hypernyms50000-subgraph.txt";
        WordNet wordNet = new WordNet(synsetsFiles, hypernymsFiles);

        try {
            System.out.print("distance, testing non-existing nouns: ");
            wordNet.distance("1234", "54356");
            throw new RuntimeException("IllegalArgumentException expected, but not thrown");
        } catch (IllegalArgumentException e) {
            System.out.println("OK");
        }

        try {
            System.out.print("sap, testing non-existing nouns: ");
            wordNet.sap("9573", "33333");
            throw new RuntimeException("IllegalArgumentException expected, but not thrown");
        } catch (IllegalArgumentException e) {
            System.out.println("OK");
        }

        System.out.println("DISTANCE TESTS: ");
        System.out.println(wordNet.distance("loch", "Loch_Achray"));
        System.out.println(wordNet.distance("loch", "Loch_Linnhe"));
        System.out.println(wordNet.distance("loch", "Loch_Ness"));

        System.out.println("SAP TESTS: ");
        System.out.println(wordNet.sap("loch", "Loch_Achray"));
        System.out.println(wordNet.sap("loch", "Loch_Linnhe"));
        System.out.println(wordNet.sap("loch", "Loch_Ness"));


    }

}