import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.Arrays;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String input = BinaryStdIn.readString();
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(input);
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            int index = circularSuffixArray.index(i);
            if (index != 0) {
                output.append(input.charAt(index - 1));
            } else {
                BinaryStdOut.write(i);
                output.append(input.charAt(input.length() - 1));
            }
        }
        BinaryStdOut.write(output.toString());
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        // no protection against empty input stream or malformed files
        int first = BinaryStdIn.readInt();
        char[] input = BinaryStdIn.readString().toCharArray();
        Decoder decoder = new Decoder(input);
        String message = decoder.decode(first);
        BinaryStdOut.write(message);
        BinaryStdOut.close();
    }

    private static class Decoder {

        // Message characters in sorted order
        final char[] sortedChars;
        // Positions from which each character starts in sortedChars array.
        // If several characters have the same starting positions, only one of them exists in the encoded string.
        // Should be accessed using character as a key.
        final int[] charStartingOffsets;
        // Example: next character for the 2nd 'a' literal in sorted message chars can be found as charNext['a'][1]
        // If some character does not exist in the message, it `next` array has 0 size.
        final int[][] charNext;

        private Decoder(char[] cs) {
            int[] offsets = new int[Constants.ALPHABET_SIZE + 1];
            charNext = new int[Constants.ALPHABET_SIZE][];
            sortedChars = new char[cs.length];

            for (char c : cs) {
                offsets[c + 1]++;
            }

            for (int i = 1; i < Constants.ALPHABET_SIZE + 1; i++) {
                charNext[i - 1] = new int[offsets[i]];
                offsets[i] += offsets[i - 1];
            }
            charStartingOffsets = Arrays.copyOf(offsets, offsets.length);

            for (int i = 0; i < cs.length; i++) {
                char literal = cs[i];
                int currentOffset = offsets[literal]++;
                int offset = currentOffset - charStartingOffsets[literal];
                charNext[literal][offset] = i;
                sortedChars[currentOffset] = literal;
            }
        }

        /**
         * Applies Burrow-Wheeler inverse transform.
         *
         * @param pointer The first characters of the encoded message.
         * @return decoded message
         */
        private String decode(int pointer) {
            StringBuilder message = new StringBuilder();
            for (int i = 0; i < sortedChars.length; i++) {
                char literal = sortedChars[pointer];
                int offset = pointer - charStartingOffsets[literal];
                pointer = charNext[literal][offset];
                message.append(literal);
            }
            return message.toString();
        }
    }


    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException(
                    String.format("A single argument expected. Found `%d` arguments.", args.length));
        }
        if (args[0].equals("+")) {
            inverseTransform();
        } else if (args[0].equals("-")) {
            transform();
        } else {
            throw new IllegalArgumentException(String.format("Unknown operation `%s`", args[0]));
        }
    }

}