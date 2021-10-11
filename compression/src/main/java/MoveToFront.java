import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.Iterator;
import java.util.LinkedList;

// Forum advise using array copies instead of linked list. It may be a bit faster, but the order of grows is the same.
// There is no solution that will allow us to avoid visiting every character before searched char `c`
public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> encoderList = init();
        convert(c -> {
            Iterator<Character> iterator = encoderList.iterator();
            for (char i = 0; iterator.hasNext(); i++) {
                if (iterator.next() == c) {
                    // A super minor performance boost for the task. However, I'd guess it can have negative impact on CPUs out of order execution.
                    if (i != 0) {
                        iterator.remove();
                        encoderList.addFirst(c);
                    }
                    return i;
                }
            }
            // Could never happen
            throw new IllegalArgumentException(String.format("Character `%s` is not found in encoding sequence", c));
        });
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Character> encoderList = init();
        convert(c -> {
            char result = encoderList.remove(c);
            encoderList.addFirst(result);
            return result;
        });
    }

    private static void convert(CharUnaryOperator f) {
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char result = f.apply(c);
            BinaryStdOut.write(result);
        }
        BinaryStdOut.close();
    }

    private static LinkedList<Character> init() {
        LinkedList<Character> encoderList = new LinkedList<>();
        for (char i = 0; i < Constants.ALPHABET_SIZE; i++) {
            // LinkedList is a Deque O(1)
            encoderList.addLast(i);
        }
        return encoderList;
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException(
                    String.format("A single argument expected. Found `%d` arguments.", args.length));
        }
        if (args[0].equals("+")) {
            decode();
        } else if (args[0].equals("-")) {
            encode();
        } else {
            throw new IllegalArgumentException(String.format("Unknown operation `%s`", args[0]));
        }
    }

}