import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {

    private int currentHeight;
    private int currentWidth;
    private final double[][] energies;
    private final int[][] pixels;
    private static final String WIDTH_DIMENSION = "width";
    private static final String HEIGHT_DIMENSION = "height";

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("Null picture");
        }
        currentHeight = picture.height();
        currentWidth = picture.width();
        pixels = new int[currentWidth][currentHeight];
        for (int x = 0; x < currentWidth; x++) {
            for (int y = 0; y < currentHeight; y++) {
                pixels[x][y] = picture.getRGB(x, y);
            }
        }
        // All pixels must be stored before calculating energies
        energies = new double[currentWidth][currentHeight];
        for (int x = 0; x < currentWidth; x++) {
            for (int y = 0; y < currentHeight; y++) {
                energies[x][y] = calculateEnergy(x, y);
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
        return energies[x][y];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int seamSize = currentWidth;
        int idRange = currentHeight;

        double[][] distTo = new double[currentWidth][currentHeight];
        int[][] pathFrom = new int[currentWidth][currentHeight];

        for (int y = 0; y < idRange; y++) {
            pathFrom[0][y] = y;
            distTo[0][y] = 0d;
        }

        for (int x = 1; x < currentWidth; x++) {
            for (int y = 0; y < currentHeight; y++) {
                pathFrom[x][y] = -1;
                distTo[x][y] = Double.POSITIVE_INFINITY;
            }
        }

        for (int x = 1; x < seamSize; x++) {
            for (int y = 0; y < idRange; y++) {
                for (int i = y - 1; i < y + 2; i++) {
                    if (i >= 0 && i < idRange) {
                        double dist = distTo[x - 1][y] + energy(x, i);
                        if (dist < distTo[x][i]) {
                            distTo[x][i] = dist;
                            pathFrom[x][i] = y;
                        }
                    }
                }
            }
        }
        double minPathDist = Double.POSITIVE_INFINITY;
        int unsavedSeamEndpoint = -1;
        for (int y = 0; y < idRange; y++) {
            double dist = distTo[seamSize - 1][y];
            if (dist < minPathDist) {
                minPathDist = dist;
                unsavedSeamEndpoint = y;
            }
        }

        int[] seam = new int[seamSize];
        seam[seamSize - 1] = unsavedSeamEndpoint;
        for (int x = seamSize - 1; x > 0; x--) {
            unsavedSeamEndpoint = pathFrom[x][unsavedSeamEndpoint];
            seam[x - 1] = unsavedSeamEndpoint;
        }

        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int seamSize = currentHeight;
        int idRange = currentWidth;

        double[][] distTo = new double[currentWidth][currentHeight];
        int[][] pathFrom = new int[currentWidth][currentHeight];
        for (int x = 0; x < idRange; x++) {
            pathFrom[x][0] = x;
            distTo[x][0] = 0d;
        }

        for (int x = 0; x < currentWidth; x++) {
            for (int y = 1; y < currentHeight; y++) {
                pathFrom[x][y] = -1;
                distTo[x][y] = Double.POSITIVE_INFINITY;
            }
        }

        for (int y = 1; y < seamSize; y++) {
            for (int x = 0; x < idRange; x++) {
                for (int i = x - 1; i < x + 2; i++) {
                    if (i >= 0 && i < idRange) {
                        double dist = distTo[x][y - 1] + energy(i, y);
                        if (dist < distTo[i][y]) {
                            distTo[i][y] = dist;
                            pathFrom[i][y] = x;
                        }
                    }
                }
            }
        }
        double minPathDist = Double.POSITIVE_INFINITY;
        int unsavedSeamEndpoint = -1;
        for (int x = 0; x < idRange; x++) {
            double dist = distTo[x][seamSize - 1];
            if (dist < minPathDist) {
                minPathDist = dist;
                unsavedSeamEndpoint = x;
            }
        }

        int[] seam = new int[seamSize];
        seam[seamSize - 1] = unsavedSeamEndpoint;
        for (int y = seamSize - 1; y > 0; y--) {
            unsavedSeamEndpoint = pathFrom[unsavedSeamEndpoint][y];
            seam[y - 1] = unsavedSeamEndpoint;
        }

        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        checkSeam(seam, WIDTH_DIMENSION);
        for (int x = 0; x < currentWidth; x++) {
            int y = seam[x];
            for (int i = y; i < currentHeight - 1; i++) {
                pixels[x][i] = pixels[x][i + 1];
                energies[x][i] = energies[x][i + 1];
            }
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
                pixels[i][y] = pixels[i + 1][y];
                energies[i][y] = energies[i + 1][y];
            }
        }
        currentWidth--;

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

    private Color getPixel(int x, int y) {
        return new Color(pixels[x][y]);
    }

    private void updateEnergy(int x, int y) {
        energies[x][y] = calculateEnergy(x, y);
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

}