import static it.utils.TestUtils.testIllegalArgumentException;

public class WordNetTest {

    public static void main(String[] args) {

        testIllegalArgumentException(() -> new WordNet(null, null));
        testIllegalArgumentException(() -> new WordNet(fullPath("synsets3.txt"),
                fullPath("hypernyms3InvalidCycle.txt")));
        testIllegalArgumentException(() -> new WordNet(fullPath("synsets3.txt"),
                fullPath("hypernyms3InvalidTwoRoots.txt")));
        testIllegalArgumentException(() -> new WordNet(fullPath("synsets6.txt"),
                fullPath("hypernyms6InvalidCycle+Path.txt")));
        testIllegalArgumentException(() -> new WordNet(fullPath("synsets6.txt"),
                fullPath("hypernyms6InvalidCycle.txt")));
        testIllegalArgumentException(() -> new WordNet(fullPath("synsets6.txt"),
                fullPath("hypernyms6InvalidTwoRoots.txt")));

        String synsetsFiles = "wordNet/synsets50000-subgraph.txt";
        String hypernymsFiles = "wordNet/hypernyms50000-subgraph.txt";
        WordNet wordNet = new WordNet(synsetsFiles, hypernymsFiles);
        testIllegalArgumentException(() -> wordNet.distance("1234", "54356"));
        testIllegalArgumentException(() -> wordNet.sap("9573", "33333"));

        System.out.println("DISTANCE TESTS: ");
        System.out.println(wordNet.distance("loch", "Loch_Achray"));
        System.out.println(wordNet.distance("loch", "Loch_Linnhe"));
        System.out.println(wordNet.distance("loch", "Loch_Ness"));

        System.out.println("SAP TESTS: ");
        System.out.println(wordNet.sap("loch", "Loch_Achray"));
        System.out.println(wordNet.sap("loch", "Loch_Linnhe"));
        System.out.println(wordNet.sap("loch", "Loch_Ness"));
    }

    private static String fullPath(String name) {
        return String.format("%s/%s", "wordNet", name);
    }
}
