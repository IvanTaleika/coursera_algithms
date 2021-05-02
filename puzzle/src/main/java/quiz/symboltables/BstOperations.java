package quiz.symboltables;

public class BstOperations {

  static class Node {

    int key;
    Node left;
    Node right;

    Node(int key) {
      this.key = key;
    }
  }

  public static void main(String[] args) {
    Node n1 = new Node(50);
    Node n2 = new Node(20);
    Node n3 = new Node(75);
    Node n4 = new Node(12);
    Node n5 = new Node(25);
    Node n6 = new Node(60);
    Node n7 = new Node(80);
    Node n8 = new Node(15);
    Node n9 = new Node(24);
    Node n10 = new Node(59);
    Node n11 = new Node(14);
    Node n12 = new Node(16);
    Node n13 = new Node(23);
    Node n14 = new Node(19);
    n1.left = n2;
    n1.right = n3;
    n2.left = n4;
    n2.right = n5;
    n3.left = n6;
    n3.right = n7;
    n4.right = n8;
    n5.left = n9;
    n6.left = n10;
    n8.left = n11;
    n8.right = n12;
    n9.left = n13;
    n12.right = n14;

    morrisInorderTraversal(n1);
    System.out.println(isBst(n1));
  }

  public static boolean isBst(Node root) {
    return isBst(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  public static boolean isBst(Node root, int min, int max) {
    if (root == null) {
      return true;
    }
    int key = root.key;
    return key > min && key < max && isBst(root.left, min, key) && isBst(root.right, key, max);
  }

  public static void morrisInorderTraversal(Node root) {
    Node curr = root;
    Node prev;
    while (curr != null) {
      if (curr.left == null) {
        System.out.printf("%d, ", curr.key);
        curr = curr.right;
      } else {
        prev = curr.left;
        while (prev.right != null && prev.right != curr) {
          prev = prev.right;
        }
        if (prev.right == null) {
          prev.right = curr;
          curr = curr.left;
        } else {
          System.out.printf("%d, ", curr.key);
          prev.right = null;
          curr = curr.right;

        }
      }
    }
  }
}
