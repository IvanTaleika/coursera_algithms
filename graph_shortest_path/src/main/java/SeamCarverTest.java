import static it.utils.TestUtils.testIllegalArgumentException;

import edu.princeton.cs.algs4.Picture;

public class SeamCarverTest {

    public static void main(String[] args) {
        // corner-case tests
        Picture picture = new Picture("tests/3x4.png");
        SeamCarver seamCarver = new SeamCarver(picture);
        testIllegalArgumentException(() -> seamCarver.energy(-1, 0));
        testIllegalArgumentException(() -> seamCarver.energy(0, -1));
        testIllegalArgumentException(() -> seamCarver.energy(3, 0));
        testIllegalArgumentException(() -> seamCarver.energy(0, 4));
        testIllegalArgumentException(() -> seamCarver.removeVerticalSeam(null));
        testIllegalArgumentException(() -> seamCarver.removeHorizontalSeam(null));
        testIllegalArgumentException(() -> seamCarver.removeVerticalSeam(new int[]{1}));
        testIllegalArgumentException(() -> seamCarver.removeHorizontalSeam(new int[]{1}));
        testIllegalArgumentException(() -> seamCarver.removeHorizontalSeam(new int[]{0, 0, 0, 0}));
        testIllegalArgumentException(
                () -> seamCarver.removeVerticalSeam(new int[]{0, 0, 0, 0, 0}));
        testIllegalArgumentException(() -> seamCarver.removeHorizontalSeam(new int[]{0, 2, 0}));
        testIllegalArgumentException(() -> seamCarver.removeVerticalSeam(new int[]{0, 1, 3, 0}));

        for (int i = 0; i < 2; i++) {
            seamCarver.removeVerticalSeam(seamCarver.findVerticalSeam());
        }
        for (int i = 0; i < 3; i++) {
            seamCarver.removeHorizontalSeam(seamCarver.findHorizontalSeam());
        }
        testIllegalArgumentException(() -> seamCarver.removeVerticalSeam(new int[]{0}));
        testIllegalArgumentException(() -> seamCarver.removeHorizontalSeam(new int[]{0}));
    }
}
