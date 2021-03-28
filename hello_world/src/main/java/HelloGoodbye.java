public class HelloGoodbye {

  public static void main(String[] args) {
    if (args.length == 2) {
      String n1 = args[0];
      String n2 = args[1];

      System.out.printf("Hello %s and %s.%n", n1, n2);
      System.out.printf("Goodbye %s and %s.%n", n2, n1);
    } else {
      System.err.println("Exactly 2 arguments are expected");
    }
  }

}
