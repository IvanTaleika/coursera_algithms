package uf.quiz;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

class SocialNetwork {

  public int n;
  public int[] people;
  public int[] relationTreeSizes;
  public int nRelationTrees;

  public SocialNetwork(int size) {
    n = size;
    people = new int[n];
    nRelationTrees = n;
    relationTreeSizes = new int[n];
    for (int i = 0; i < n; i++) {
      people[i] = i;
      relationTreeSizes[i] = 1;
    }
  }

  public void connect(int id1, int id2) {
    int root1 = findRelationTreeRoot(id1);
    int root2 = findRelationTreeRoot(id2);
    if (root1 != root2) {
      int treeSize1 = relationTreeSizes[root1];
      int treeSize2 = relationTreeSizes[root2];
      if (treeSize1 > treeSize2) {
        people[root2] = root1;
        relationTreeSizes[root1] += relationTreeSizes[root2];
      } else {
        people[root1] = root2;
        relationTreeSizes[root2] += relationTreeSizes[root1];
      }
      nRelationTrees--;
    }
  }

  public int findRelationTreeRoot(int id) {
    while (people[id] != id) {
      id = people[id];
    }
    return id;
  }

  public static void main(String[] args) {
    int networkSize = StdIn.readInt();
    if (networkSize < 1) {
      StdOut.println("invalid network size");
    } else if (networkSize == 1) {
      StdOut.println("Single user network");
    } else {
      SocialNetwork socialNetwork = new SocialNetwork(networkSize);
      long time = -1L;
      while (!StdIn.isEmpty() && socialNetwork.nRelationTrees != 1) {
        time = StdIn.readLong();
        int id1 = StdIn.readInt();
        int id2 = StdIn.readInt();
        socialNetwork.connect(id1, id2);
      }
      if (socialNetwork.nRelationTrees == 1) {
        StdOut.printf("Network connected in %f%n", time);
      } else {
        StdOut.println("Network never connects");
      }
    }
  }
}