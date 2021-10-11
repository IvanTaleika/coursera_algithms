import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.Huffman;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.UnaryOperator;

public class App {

    public static void main(String[] args) throws IOException {
        String testsFolder = "tests";
        String testFile = "a10.txt";

        String expectedResultFile = testFile + ".bwt.mtf.huf";
        String actualResultFile = "actual_" + expectedResultFile;

        System.err.println("ENCODING\n---\n");

        UnaryOperator<byte[]> burrowsWheeler = s -> run("BurrowsWheeler", BurrowsWheeler::transform, s);
        UnaryOperator<byte[]> moveToFront = s -> run("MoveToFront", MoveToFront::encode, s);
        UnaryOperator<byte[]> huffman = s -> run("Huffman", Huffman::compress, s);

        byte[] testData = Files.readAllBytes(Paths.get(testsFolder, testFile));
        byte[] actual = burrowsWheeler.andThen(moveToFront).andThen(huffman).apply(testData);

        Files.write(Paths.get(testsFolder, actualResultFile), actual);

        Path expectedResultPath = Paths.get(testsFolder, expectedResultFile);
        if (Files.exists(expectedResultPath)) {
            byte[] expected = Files.readAllBytes(expectedResultPath);
            assert Arrays.equals(actual, expected);
            System.err.println("Encoding is TESTED!\n---\n");
        }

        System.err.println("DECODING\n---\n");

        UnaryOperator<byte[]> burrowsWheelerDecode = s -> run("burrowsWheelerDecode", BurrowsWheeler::inverseTransform, s);
        UnaryOperator<byte[]> moveToFrontDecode = s -> run("moveToFrontDecode", MoveToFront::decode, s);
        UnaryOperator<byte[]> huffmanDecode = s -> run("huffmanDecode", Huffman::expand, s);

        byte[] compressedData = Files.readAllBytes(Paths.get(testsFolder, actualResultFile));
        byte[] actualDecoded = huffmanDecode.andThen(moveToFrontDecode).andThen(burrowsWheelerDecode).apply(compressedData);

        Files.write(Paths.get(testsFolder, "decoded_" + actualResultFile), actualDecoded);
        assert Arrays.equals(actualDecoded, testData);
    }


    private static byte[] run(String name, Runnable runnable, byte[] input) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(input));
        System.setOut(new PrintStream(byteArrayOutputStream));
        runnable.run();
        byte[] output = byteArrayOutputStream.toByteArray();
        // Using `err` cause out is swapped to use byte array
        System.err.printf("%n%s output: %n---%n", name);
        try {
            System.err.write(output);
        } catch (IOException e) {
            // Should never happen. We need to hide the exception cause `run` method is used in lambdas.
            e.printStackTrace();
        }
        System.err.printf("%n---%n");
        // Close BinaryStdIn, so it can be reinitialized after changing the backed stream array.
        // We can't close it proactively, cause it hangs for some reason
        BinaryStdIn.close();
        return output;
    }
}
