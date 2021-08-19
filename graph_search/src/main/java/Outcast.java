import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private WordNet wordNet;

    public Outcast(WordNet wn) {
        wordNet = wn;
    }

    public String outcast(String[] nouns) {
        int nNouns = nouns.length;

        int[][] distances = new int[nNouns][nNouns];
        for (int i = 0; i < nNouns; i++) {
            int j = 0;
            while (j < i) {
                distances[i][j] = distances[j][i];
                j++;
            }
            while (j < nNouns) {
                distances[i][j] = wordNet.distance(nouns[i], nouns[j]);
                j++;
            }
        }

        String outcast = null;
        int maxDistance = -1;
        for (int i = 0; i < nNouns; i++) {
            int distance = 0;
            for (int j = 0; j < nNouns; j++) {
                distance += distances[i][j];
            }
            if (distance > maxDistance) {
                maxDistance = distance;
                outcast = nouns[i];
            }
        }
        return outcast;
    }

    public static void main(String[] args) {
        String[] files = {"wordNet/synsets.txt", "wordNet/hypernyms.txt", "outcast/outcast10.txt", "outcast/outcast10a.txt", "outcast/outcast12a.txt"};
        WordNet wordnet = new WordNet(files[0], files[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < files.length; t++) {
            In in = new In(files[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(files[t] + ": " + outcast.outcast(nouns));
        }
    }
}