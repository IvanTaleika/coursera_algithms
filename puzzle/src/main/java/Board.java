import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Board {

  private char[][] tiles;
  private int d;
  private int h;
  private int m;
  private int blankX;
  private int blankY;

  // create a board from an n-by-n array of tiles,
  // where tiles[row][col] = tile at (row, col)
  public Board(int[][] tiles) {
    this(cloneTiles(tiles));
  }

  private static char[][] cloneTiles(int[][] tiles) {
    int d = tiles.length;
    char[][] clone = new char[d][d];
    for (int i = 0; i < d; i++) {
      for (int j = 0; j < d; j++) {
        clone[i][j] = (char) tiles[i][j];
      }
    }
    return clone;
  }

  private Board(char[][] tiles) {
    d = tiles.length;
    this.tiles = tiles;
    h = 0;
    m = 0;
    for (int i = 0; i < d; i++) {
      for (int j = 0; j < d; j++) {
        char tile = tiles[i][j];
        int expectedValue = d * i + j + 1;
        if (tile == 0) {
          blankX = j;
          blankY = i;
        } else if (tile != expectedValue) {
          h++;
          int tileY = (tile - 1) / d;
          int tileX = (tile - 1) % d;
          m += Math.abs(tileY - i);
          m += Math.abs(tileX - j);
        }
      }
    }
  }

  // string representation of this board
  public String toString() {
    String padding = "%3d ";
    StringBuilder sb = new StringBuilder(String.format("%d%n", d));
    for (char[] row : tiles) {
      for (int v : row) {
        sb.append(String.format(padding, v));
      }
      sb.append("\n");
    }
    return sb.toString();
  }

  // board dimension n
  public int dimension() {
    return d;
  }

  // number of tiles out of place
  public int hamming() {
    return h;
  }

  // sum of Manhattan distances between tiles and goal
  public int manhattan() {
    return m;
  }

  // is this board the goal board?
  public boolean isGoal() {
    return h == 0;
  }

  // does this board equal y?
  public boolean equals(Object y) {
    if (this == y) {
      return true;
    }
    if (y == null || getClass() != y.getClass()) {
      return false;
    }
    Board b = ((Board) y);
    return blankX == b.blankX && blankY == b.blankY && Arrays.deepEquals(tiles, b.tiles);
  }

  // all neighboring boards
  public Iterable<Board> neighbors() {
    List<Board> l = new ArrayList<>(4);
    if (blankY != 0) {
      l.add(createNeighbor(blankY - 1, blankX));
    }
    if (blankY != d - 1) {
      l.add(createNeighbor(blankY + 1, blankX));
    }
    if (blankX != 0) {
      l.add(createNeighbor(blankY, blankX - 1));
    }
    if (blankX != d - 1) {
      l.add(createNeighbor(blankY, blankX + 1));
    }
    return l;
  }

  private Board createNeighbor(int neighborBlankY, int neighborBlankX) {
    char[][] neighborTiles = cloneTiles();
    char swapElement = neighborTiles[neighborBlankY][neighborBlankX];
    neighborTiles[neighborBlankY][neighborBlankX] = neighborTiles[blankY][blankX];
    neighborTiles[blankY][blankX] = swapElement;
    return new Board(neighborTiles);
  }

  // a board that is obtained by exchanging any pair of tiles
  public Board twin() {
    int blank = d * blankY + blankX;
    int from = -1;
    int to = -1;
    for (int i = 0; from == -1; i++) {
      if (i != blank) {
        from = i;
      }
    }
    for (int i = from + 1; to == -1; i++) {
      if (i != blank) {
        to = i;
      }
    }
    char[][] twinTiles = cloneTiles();
    twinTiles[to / d][to % d] = tiles[from / d][from % d];
    twinTiles[from / d][from % d] = tiles[to / d][to % d];
    return new Board(twinTiles);
  }

  private char[][] cloneTiles() {
    char[][] clone = new char[d][d];
    for (int i = 0; i < d; i++) {
      System.arraycopy(tiles[i], 0, clone[i], 0, d);
    }
    return clone;
  }

  // unit testing (not graded)
  public static void main(String[] args) {
    int d = 2;
    int size = d * d;
    List<Integer> shuffledTiles = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      shuffledTiles.add(i);
    }
    Collections.shuffle(shuffledTiles);
    int[][] tiles = new int[d][d];
    for (int i = 0; i < d; i++) {
      for (int j = 0; j < d; j++) {
        tiles[i][j] = shuffledTiles.get(i * d + j);
      }
    }
    Board b = new Board(tiles);
    printBoardStats(b);
    assert b.equals(b) : System.err.printf("Boards%n%s%nAnd%n%s%nMust be equal!", b, b);

    System.out.println("twin:");
    Board t = b.twin();
    printBoardStats(t);
    assert !b.equals(t) : System.err.printf("Boards%n%s%nAnd%n%s%nMust not be equal!", b, t);

    for (Board neighbor : b.neighbors()) {
      System.out.println("neighbor:");
      printBoardStats(neighbor);
      assert
          !b.equals(neighbor) : System.err
          .printf("Boards%n%s%nAnd%n%s%nMust not be equal!", b, neighbor);
    }
  }

  private static void printBoardStats(Board b) {
    System.out.printf("The board size %dx%d%n", b.dimension(), b.dimension());
    System.out.println(b.toString());
    System.out.printf("hamming = %d%n", b.hamming());
    System.out.printf("manhattan = %d%n", b.manhattan());
    System.out.printf("goal? %b%n", b.isGoal());
    System.out.println();
  }

}