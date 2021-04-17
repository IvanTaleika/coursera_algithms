package uf.quiz;

public class CanonicalElement {

  public int n;
  public int[] elements;
  public int[] treeSizes;
  public int[] canonicalElements;

  public CanonicalElement(int size) {
    n = size;
    elements = new int[n];
    treeSizes = new int[n];
    canonicalElements = new int[n];
    for (int i = 0; i < n; i++) {
      elements[i] = i;
      treeSizes[i] = 1;
      canonicalElements[i] = i;
    }
  }

  public void union(int id1, int id2) {
    int root1 = findRoot(id1);
    int root2 = findRoot(id2);
    if (root1 != root2) {
      int maxCanonical = Math.max(canonicalElements[root1], canonicalElements[root2]);
      int treeSize1 = treeSizes[root1];
      int treeSize2 = treeSizes[root2];
      if (treeSize1 > treeSize2) {
        elements[root2] = root1;
        canonicalElements[root2] = maxCanonical;
        treeSizes[root1] += treeSizes[root2];
      } else {
        elements[root1] = root2;
        canonicalElements[root1] = maxCanonical;
        treeSizes[root2] += treeSizes[root1];
      }

    }
  }

  public int findRoot(int id) {
    int parent = elements[id];
    if (parent == elements[parent]) {
      return parent;
    } else {
      int newParent = findRoot(parent);
      elements[id] = newParent;
      return newParent;
    }
  }

  public int canonical(int id) {
    return canonicalElements[findRoot(id)];
  }
}
