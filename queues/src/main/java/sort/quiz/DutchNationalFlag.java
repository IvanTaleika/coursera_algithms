package sort.quiz;

import java.awt.Color;
import java.util.Random;

public class DutchNationalFlag {

  private static final Color[] flagColors = {Color.RED, Color.BLUE, Color.WHITE};
  private static final Random random = new Random();

  private static class Bin {

    final Color color;

    Bin() {
      color = flagColors[random.nextInt(3)];
    }
  }

  public static void main(String[] args) {
    int n = 100;
    Bin[] bins = new Bin[n];
    for (int i = 0; i < n; i++) {
      bins[i] = new Bin();
    }

    int red = 0;
    int blue = n - 1;
    int i = 0;
    while (i <= blue) {
      Bin bin = bins[i];
      if (bin.color == Color.RED) {
        bins[i] = bins[red];
        bins[red] = bin;
        // we can increment while red points on red balls, but it makes code a bit ugly, so not for this test task
        i++;
        red++;
      } else if (bin.color == Color.BLUE) {
        bins[i] = bins[blue];
        bins[blue] = bin;
        // we can decrement while blue points on blue balls, but it makes code a bit ugly, so not for this test task
        blue--;
      } else {
        i++;
      }
    }
    for (Bin bin : bins) {
      System.out.println(bin.color);
    }
  }

}
