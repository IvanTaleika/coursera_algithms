import java.util.HashSet;
import java.util.Set;

// Solution accept n english uppercase letters only
public class BoggleSolver {

    private TrieEntry root;
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int RADIX = ALPHABET.length();
    private static final int RADIX_SHIFT = 'A';

    private Set<String> boggleWords;
    private boolean[][] marked;
    private BoggleBoard board;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        root = new TrieEntry('\0');
        for (String word : dictionary) {
            // 2 length words give no points
            if (word.length() > 2) {
                addDictionaryWord(word);
            }
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        this.board = board;
        marked = new boolean[board.rows()][board.cols()];
        boggleWords = new HashSet<>();

        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                startSearch(root, i, j);
            }
        }
        return boggleWords;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        TrieEntry entry = root;
        for (int i = 0; i < word.length(); i++) {
            TrieEntry nextEntry = entry.children[position(word.charAt(i))];
            if (nextEntry == null) {
                return 0;
            }
            entry = nextEntry;
        }
        return entry.word != null ? points(entry.word) : 0;
    }

    private void addDictionaryWord(String word) {
        TrieEntry entry = root;
        for (int i = 0; i < word.length() - 1; i++) {
            char c = word.charAt(i);
            int rIndex = position(c);
            if (entry.children[rIndex] == null) {
                entry.children[rIndex] = new TrieEntry(c);
            }
            entry = entry.children[rIndex];
        }

        // overwrite the last one no matter if it already exis
        char c = word.charAt(word.length() - 1);
        int rIndex = position(c);
        if (entry.children[rIndex] == null) {
            entry.children[rIndex] = new TrieEntry(c, word);
        } else {
            entry.children[rIndex].word = word;
        }
    }

    private void searchAdjacent(TrieEntry entry, int i, int j) {
        if (entry.word != null) {
            boggleWords.add(entry.word);
        }
        for (int ii = -1; ii < 2; ii++) {
            for (int jj = -1; jj < 2; jj++) {
                int adjI = i + ii;
                int adjJ = j + jj;
                if (isValidBoardRow(adjI) && isValidBoardCol(adjJ) && !marked[adjI][adjJ]) {
                    startSearch(entry, adjI, adjJ);
                }
            }
        }
    }

    private void startSearch(TrieEntry parentEntry, int i, int j) {
        char letter = board.getLetter(i, j);
        int trieId = position(letter);
        TrieEntry entry = parentEntry.children[trieId];

        if (entry != null) {
            marked[i][j] = true;

            // In this task 'Q' always represents 'QU'. This mean words like `SEQ` or `SHEQALIM` won't be counted.
            if (letter == 'Q') {
                TrieEntry quEntry = entry.children[position('U')];
                if (quEntry != null) {
                    searchAdjacent(quEntry, i, j);
                }
            } else {
                searchAdjacent(entry, i, j);
            }
            marked[i][j] = false;
        }
    }

    private boolean isValidBoardRow(int i) {
        return i >= 0 && i < board.rows();
    }

    private boolean isValidBoardCol(int j) {
        return j >= 0 && j < board.cols();
    }

    private static class TrieEntry {

        final char letter;
        final TrieEntry[] children;
        String word;

        TrieEntry(char letter, String word) {
            this.letter = letter;
            this.word = word;
            this.children = new TrieEntry[RADIX];
        }

        TrieEntry(char letter) {
            this(letter, null);
        }
    }

    private int position(int c) {
        return c - RADIX_SHIFT;
    }

    private int points(String word) {
        return points(word.length());
    }

    private int points(int length) {
        if (length < 3) {
            throw new IllegalArgumentException("Word must be at least 3 letters long.");
        }
        if (length < 5) {
            return 1;
        }
        if (length < 6) {
            return 2;
        }
        if (length < 7) {
            return 3;
        }
        if (length < 8) {
            return 5;
        }
        return 11;
    }
}