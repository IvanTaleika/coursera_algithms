import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Solver {

  private List<Board> answer;

  private static class Node implements Comparable<Node> {

    Board state;
    Node fromNode;
    int stepsFromRoot;
    int manhattan;
    int priority;

    public Node(Board state, Node fromNode, int stepsFromRoot) {
      this.state = state;
      this.fromNode = fromNode;
      this.stepsFromRoot = stepsFromRoot;
      this.manhattan = state.manhattan();
      this.priority = manhattan + stepsFromRoot;
    }

    @Override
    public int compareTo(Node o) {
      int compare = Integer.compare(priority, o.priority);
      if (compare != 0) {
        return compare;
      } else {
        return Integer.compare(manhattan, o.manhattan);
      }
    }
  }

  // find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial) {
    if (null == initial) {
      throw new IllegalArgumentException("The board is null");
    }
    MinPQ<Node> searchTree = initSearchTree(initial);
    MinPQ<Node> twinSearchTree = initSearchTree(initial.twin());
    Node currentNode;
    while (true) {
      currentNode = search(searchTree);
      if (currentNode.state.isGoal()) {
        answer = initSolvable(currentNode);
        break;
      }
      currentNode = search(twinSearchTree);
      if (currentNode.state.isGoal()) {
        answer = null;
        break;
      }
    }
  }

  private List<Board> initSolvable(Node winNode) {
    List<Board> a = new LinkedList<>();
    do {
      a.add(winNode.state);
      winNode = winNode.fromNode;
    } while (winNode != null);
    Collections.reverse(a);
    return a;
  }


  private MinPQ<Node> initSearchTree(Board initial) {
    MinPQ<Node> searchTree = new MinPQ<>();
    Node root = new Node(initial, null, 0);
    searchTree.insert(root);
    return searchTree;
  }

  private Node search(MinPQ<Node> searchTree) {
    Node node = searchTree.delMin();
    for (Board neighbor : node.state.neighbors()) {
      if (node.fromNode == null || !neighbor.equals(node.fromNode.state)) {
        searchTree.insert(new Node(neighbor, node, node.stepsFromRoot + 1));
      }
    }
    return node;
  }

  // is the initial board solvable? (see below)
  public boolean isSolvable() {
    return answer != null;
  }

  // min number of moves to solve initial board; -1 if unsolvable
  public int moves() {
    return (answer != null ? answer.size() : 0) - 1;
  }

  // sequence of boards in a shortest solution; null if unsolvable
  public Iterable<Board> solution() {
    return answer != null ? Collections.unmodifiableList(answer) : null;
  }

  // test client (see below)
  public static void main(String[] args) {

    // create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] tiles = new int[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        tiles[i][j] = in.readInt();
      }
    }
    Board initial = new Board(tiles);

    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable()) {
      StdOut.println("No solution possible");
    } else {
      StdOut.println("Minimum number of moves = " + solver.moves());
      for (Board board : solver.solution()) {
        StdOut.println(board);
      }
    }
  }
}