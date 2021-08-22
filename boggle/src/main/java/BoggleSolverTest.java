import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolverTest {

    public static void main(String[] args) {
        String dictionaryFile = "tests/dictionary-yawl.txt";
        In in = new In(dictionaryFile);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);

        // There are no words that start with `KEK`
        assert solver.scoreOf("KEKW") == 0;
        // There are words that start with `KE`, but there are no FULL word `KE`
        assert solver.scoreOf("KE") == 0;

        testPrecomputedBoard(solver);
        testArbitraryBoards(solver);
    }

    private static void testPrecomputedBoard(BoggleSolver solver) {
        String boardFile = "tests/board-qwerty.txt";
        BoggleBoard board = new BoggleBoard(boardFile);
        int score = 0;
        int words = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
            words++;
        }
        StdOut.println("Words = " + words);
        StdOut.println("Score = " + score);
    }

    private static void testArbitraryBoards(BoggleSolver solver) {
        int i = 0;
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < 1000) {
            BoggleBoard board = new BoggleBoard(4, 4);
            solver.getAllValidWords(board);
            i++;
        }
        System.out.printf("%d boards was solved to 1 second! Impressive!", i);
    }
}
