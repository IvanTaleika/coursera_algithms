public class CircularSuffixArray {

    // StringBuffer can be used in real application cause it provides 0-copy subSequence implementation.
    // This task, however, does not allow classes from `java.nio` package
    private static class CharsWrapper implements CharSequence {

        private final int length;
        private final int offset;
        private final char[] backend;

        CharsWrapper(char[] cs, int offset, int length) {
            this.length = length;
            this.offset = offset;
            backend = cs;
        }

        @Override
        public int length() {
            return length;
        }

        public int offset() {
            return offset;
        }

        @Override
        public char charAt(int index) {
            return backend[offset + index];
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            throw new UnsupportedOperationException("This method is not required for the task");
        }
    }

    private final int length;
    private final int[] indices;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Source string cannot be null");
        }
        this.length = s.length();
        // Avoid pointer arithmetic
        String ss = s.concat(s);
        CharsWrapper[] suffixes = new CharsWrapper[length];
        char[] backendArray = ss.toCharArray();
        for (int i = 0; i < length; i++) {
            suffixes[i] = new CharsWrapper(backendArray, i, length);
        }
        Quick3CharSequence.sort(suffixes);
        indices = new int[length];
        for (int i = 0; i < length; i++) {
            indices[i] = suffixes[i].offset();
        }
    }

    // length of s
    public int length() {
        return length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length) {
            throw new IllegalArgumentException(
                    String.format("Invalid index `%d`. Index must be between 0 and `%d`", i, length));
        }
        return indices[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        String testString = "ABRACADABRA!";
        int[] expectedIndices = {11, 10, 7, 0, 3, 5, 8, 1, 4, 6, 9, 2};
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(testString);
        assert circularSuffixArray.length() == testString.length();
        System.out.printf("Suffix array length is `%d`%n", circularSuffixArray.length());
        System.out.println("index[i]\n--------");
        for (int i = 0; i < circularSuffixArray.length; i++) {
            assert circularSuffixArray.index(i) == expectedIndices[i];
            System.out.println(circularSuffixArray.index(i));
        }

    }

}