import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.UF;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

// TODO: to match the memory constraint we must use matrices and UF (?)
public class SeamCarver {

    private int currentHeight;
    private int currentWidth;
    private final List<List<Color>> pixels;
    private final List<List<Double>> energies;
    private final List<List<Double>> distTo;
    private final List<List<Integer>> pathFrom;
    private static final String WIDTH_DIMENSION = "width";
    private static final String HEIGHT_DIMENSION = "height";

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("Null picture");
        }
        currentHeight = picture.height();
        currentWidth = picture.width();

        pixels = new ArrayList<>(currentWidth);
        distTo = new ArrayList<>(currentWidth);
        pathFrom = new ArrayList<>(currentWidth);
        for (int x = 0; x < currentWidth; x++) {
            List<Color> pixelColumn = new ArrayList<>(currentHeight);
            List<Double> distToColumn = new ArrayList<>(currentHeight);
            List<Integer> pathFromColumn = new ArrayList<>(currentHeight);
            pixels.add(pixelColumn);
            distTo.add(distToColumn);
            pathFrom.add(pathFromColumn);
            for (int y = 0; y < currentHeight; y++) {
                pixelColumn.add(picture.get(x, y));
                distToColumn.add(Double.POSITIVE_INFINITY);
                pathFromColumn.add(-1);
            }
        }
        // All pixels must be stored before calculating energies
        energies = new ArrayList<>(currentWidth);
        for (int x = 0; x < currentWidth; x++) {
            List<Double> energyColumn = new ArrayList<>(currentHeight);
            energies.add(energyColumn);
            for (int y = 0; y < currentHeight; y++) {
                energyColumn.add(calculateEnergy(x, y));
            }
        }
    }

    // current picture
    public Picture picture() {
        Picture picture = new Picture(currentWidth, currentHeight);
        for (int x = 0; x < currentWidth; x++) {
            for (int y = 0; y < currentHeight; y++) {
                picture.set(x, y, getPixel(x, y));
            }
        }
        return picture;
    }

    // width of current picture
    public int width() {
        return currentWidth;
    }

    // height of current picture
    public int height() {
        return currentHeight;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= currentWidth || y < 0 || y >= currentHeight) {
            throw new IllegalArgumentException(String.format("Invalid coordinates (%d, %d)", x, y));
        }
        return energies.get(x).get(y);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int seamSize = currentWidth;
        int idRange = currentHeight;
        for (int y = 0; y < idRange; y++) {
            pathFrom.get(0).set(y, y);
            distTo.get(0).set(y, 0d);
        }
        for (int x = 1; x < seamSize; x++) {
            for (int y = 0; y < idRange; y++) {
                for (int i = y - 1; i < y + 2; i++) {
                    if (i >= 0 && i < idRange) {
                        double dist = distTo.get(x - 1).get(y) + energy(x, i);
                        if (dist < distTo.get(x).get(i)) {
                            distTo.get(x).set(i, dist);
                            pathFrom.get(x).set(i, y);
                        }
                    }
                }
            }
        }
        double minPathDist = Double.POSITIVE_INFINITY;
        int unsavedSeamEndpoint = -1;
        for (int y = 0; y < idRange; y++) {
            double dist = distTo.get(seamSize - 1).get(y);
            if (dist < minPathDist) {
                minPathDist = dist;
                unsavedSeamEndpoint = y;
            }
        }

        int[] seam = new int[seamSize];
        seam[seamSize - 1] = unsavedSeamEndpoint;
        for (int x = seamSize - 1; x > 0; x--) {
            unsavedSeamEndpoint = pathFrom.get(x).get(unsavedSeamEndpoint);
            seam[x - 1] = unsavedSeamEndpoint;
        }

        resetSearch();
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int seamSize = currentHeight;
        int idRange = currentWidth;
        for (int x = 0; x < idRange; x++) {
            pathFrom.get(x).set(0, x);
            distTo.get(x).set(0, 0d);
        }
        for (int y = 1; y < seamSize; y++) {
            for (int x = 0; x < idRange; x++) {
                for (int i = x - 1; i < x + 2; i++) {
                    if (i >= 0 && i < idRange) {
                        double dist = distTo.get(x).get(y - 1) + energy(i, y);
                        if (dist < distTo.get(i).get(y)) {
                            distTo.get(i).set(y, dist);
                            pathFrom.get(i).set(y, x);
                        }
                    }
                }
            }
        }
        double minPathDist = Double.POSITIVE_INFINITY;
        int unsavedSeamEndpoint = -1;
        for (int x = 0; x < idRange; x++) {
            double dist = distTo.get(x).get(seamSize - 1);
            if (dist < minPathDist) {
                minPathDist = dist;
                unsavedSeamEndpoint = x;
            }
        }

        int[] seam = new int[seamSize];
        seam[seamSize - 1] = unsavedSeamEndpoint;
        for (int y = seamSize - 1; y > 0; y--) {
            unsavedSeamEndpoint = pathFrom.get(unsavedSeamEndpoint).get(y);
            seam[y - 1] = unsavedSeamEndpoint;
        }

        resetSearch();
        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        checkSeam(seam, WIDTH_DIMENSION);
        for (int x = 0; x < currentWidth; x++) {
            int y = seam[x];
            pixels.get(x).remove(y);
            energies.get(x).remove(y);
            distTo.get(x).remove(y);
            pathFrom.get(x).remove(y);
        }
        currentHeight--;

        // pixel [x, y] removal can update energy of [x - 1, y].
        // That is why update is done after all the deletes
        for (int x = 0; x < currentWidth; x++) {
            int y = seam[x];
            if (y < currentHeight) {
                updateEnergy(x, y);
            }
            if (y > 0) {
                updateEnergy(x, y - 1);
            }
        }
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        checkSeam(seam, HEIGHT_DIMENSION);
        for (int y = 0; y < currentHeight; y++) {
            int x = seam[y];
            for (int i = x; i < currentWidth - 1; i++) {
                pixels.get(i).set(y, pixels.get(i + 1).get(y));
                energies.get(i).set(y, energies.get(i + 1).get(y));
            }
        }
        currentWidth--;
        pixels.remove(currentWidth);
        energies.remove(currentWidth);
        distTo.remove(currentWidth);
        pathFrom.remove(currentWidth);

        // pixel [x, y] removal can update energy of [x, y - 1].
        // That is why update is done after all the deletes
        for (int y = 0; y < currentHeight; y++) {
            int x = seam[y];
            if (x < currentWidth) {
                updateEnergy(x, y);
            }
            if (x > 0) {
                updateEnergy(x - 1, y);
            }
        }
    }

    private void checkSeam(int[] seam, String dimensionName) {
        int seamLengthConstraint =
                dimensionName.equals(WIDTH_DIMENSION) ? currentWidth : currentHeight;
        int indexLengthConstraint =
                dimensionName.equals(WIDTH_DIMENSION) ? currentHeight : currentWidth;
        String indexDimensionName =
                dimensionName.equals(WIDTH_DIMENSION) ? HEIGHT_DIMENSION : WIDTH_DIMENSION;

        if (seam == null) {
            throw new IllegalArgumentException("Null seam");
        }
        if (indexLengthConstraint <= 1) {
            throw new IllegalArgumentException(
                    String.format("Picture %s is too short for delete operation. Value = `%d`",
                            dimensionName, indexLengthConstraint));
        }
        if (seam.length != seamLengthConstraint) {
            throw new IllegalArgumentException(
                    String.format("Seam length (`%d`) is not equal to picture %s (`%d`)",
                            seam.length, dimensionName, seamLengthConstraint));
        }
        int i = 0;
        int pictureIndex = seam[i];
        while (true) {
            if (pictureIndex < 0 || pictureIndex >= indexLengthConstraint) {
                throw new IllegalArgumentException(
                        String.format("Seam index %s=%d, %s=%d is outside of %s range [0,%d)",
                                dimensionName, i, indexDimensionName, pictureIndex,
                                indexDimensionName, indexLengthConstraint));
            }
            i++;
            if (i >= seamLengthConstraint) {
                break;
            }
            int nextPictureIndex = seam[i];
            if (nextPictureIndex < pictureIndex - 1 || nextPictureIndex > pictureIndex + 1) {
                throw new IllegalArgumentException(
                        String.format(
                                "Seam index %s=%d, %s=%d is not adjacent to seam index %s=%d, %s=%d",
                                dimensionName, i, indexDimensionName, nextPictureIndex,
                                dimensionName, i - 1, indexDimensionName, pictureIndex));
            }
            pictureIndex = nextPictureIndex;
        }

    }

    private void resetSearch() {
        for (int x = 0; x < currentWidth; x++) {
            for (int y = 0; y < currentHeight; y++) {
                pathFrom.get(x).set(y, -1);
                distTo.get(x).set(y, Double.POSITIVE_INFINITY);
            }
        }
    }

    private Color getPixel(int x, int y) {
        return pixels.get(x).get(y);
    }

    private void updateEnergy(int x, int y) {
        List<Double> energyColumn = energies.get(x);
        energyColumn.set(y, calculateEnergy(x, y));
    }

    private double calculateEnergy(int x, int y) {
        if (x == 0 || y == 0 || x == currentWidth - 1 || y == currentHeight - 1) {
            return 1000d;
        } else {
            Color leftPixel = getPixel(x - 1, y);
            Color rightPixel = getPixel(x + 1, y);
            Color topPixel = getPixel(x, y - 1);
            Color bottomPixel = getPixel(x, y + 1);
            int redXDiff = leftPixel.getRed() - rightPixel.getRed();
            int greenXDiff = leftPixel.getGreen() - rightPixel.getGreen();
            int blueXDiff = leftPixel.getBlue() - rightPixel.getBlue();
            int redYDiff = topPixel.getRed() - bottomPixel.getRed();
            int greenYDiff = topPixel.getGreen() - bottomPixel.getGreen();
            int blueYDiff = topPixel.getBlue() - bottomPixel.getBlue();
            int xEnergySquare =
                    redXDiff * redXDiff + greenXDiff * greenXDiff + blueXDiff * blueXDiff;
            int yEnergySquare =
                    redYDiff * redYDiff + greenYDiff * greenYDiff + blueYDiff * blueYDiff;
            return Math.sqrt(xEnergySquare + yEnergySquare);
        }
    }

    //  unit testing (optional)
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

    private static void testIllegalArgumentException(Runnable testFunction) {
        try {
            testFunction.run();
            throw new RuntimeException("IllegalArgumentException expected, but not thrown");
        } catch (IllegalArgumentException e) {
        }
    }

}