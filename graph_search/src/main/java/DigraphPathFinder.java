import edu.princeton.cs.algs4.Digraph;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class DigraphPathFinder {

    private final Digraph digraph;
    private final DigraphPath shortestPath;

    private Queue<Integer> bfsQueue;
    private Map<Integer, VisitedVertex> aSubgraph = new HashMap<>();
    private Map<Integer, VisitedVertex> bSubgraph = new HashMap<>();
    private int shortestCommonAncestor = -1;
    private int shortestAncestralPath = Integer.MAX_VALUE;


    public DigraphPathFinder(Digraph digraph, int v, int w) {
        this(digraph, Collections.singletonList(v), Collections.singletonList(w));
    }

    public DigraphPathFinder(Digraph digraph, Iterable<Integer> aSet, Iterable<Integer> bSet) {
        if (aSet == null || bSet == null) {
            throw new IllegalArgumentException("Point sets could not be null");
        }
        this.digraph = digraph;
        for (Integer root : aSet) {
            checkVertex(root);
            initSearch(root, aSubgraph);
            while (!bfsQueue.isEmpty()) {
                searchLevel(aSubgraph, bfsQueue);
            }
        }

        for (Integer root : bSet) {
            checkVertex(root);
            initSearch(root, bSubgraph);
            while (!bfsQueue.isEmpty() && canBeatDistance()) {
                searchLevel(bSubgraph, bfsQueue);
            }
        }

        if (shortestCommonAncestor != -1) {
            shortestPath = buildShortestPath();
        } else {
            shortestPath = new DigraphPath(-1, -1, new int[0]);
        }
    }

    public DigraphPath getShortestPath() {
        return shortestPath;
    }

    private void checkVertex(Integer vertex) {
        if (vertex == null || vertex < -1 || vertex > digraph.V()) {
            throw new IllegalArgumentException(String.format("Invalid vertex value `%s`", vertex));
        }
    }

    private void initSearch(int root, Map<Integer, VisitedVertex> subgraph) {
        bfsQueue = new ArrayDeque<>();
        bfsQueue.add(root);
        subgraph.put(root, new VisitedVertex(-1, 0));
    }

    private void searchLevel(Map<Integer, VisitedVertex> subgraph, Queue<Integer> bfsQueue) {
        int vert = bfsQueue.remove();
        int nextLayerDist = subgraph.get(vert).distance + 1;
        for (int out : digraph.adj(vert)) {
            subgraph.compute(out, (k, v) -> {
                if (v == null || v.distance > nextLayerDist) {
                    bfsQueue.add(k);
                    return new VisitedVertex(vert, nextLayerDist);
                }
                return v;
            });
        }
    }

    private boolean canBeatDistance() {
        // searchLevel call `remove`, we must call `peek`
        int vert = bfsQueue.peek();
        int layerDist = bSubgraph.get(vert).distance;

        if (shortestAncestralPath <= layerDist) {
            return false;
        }

        if (aSubgraph.containsKey(vert)) {
            VisitedVertex aVisitedVertex = aSubgraph.get(vert);
            int pathDistance = aVisitedVertex.distance + layerDist;
            if (pathDistance < shortestAncestralPath) {
                shortestAncestralPath = pathDistance;
                shortestCommonAncestor = vert;
            }
        }
        return true;
    }

    private DigraphPath buildShortestPath() {

        Deque<Integer> vertices = new ArrayDeque<>(shortestAncestralPath + 1);
        int currVertex = shortestCommonAncestor;
        while (currVertex != -1) {
            vertices.addFirst(currVertex);
            currVertex = aSubgraph.get(currVertex).previous;
        }

        currVertex = bSubgraph.get(shortestCommonAncestor).previous;
        while (currVertex != -1) {
            vertices.addLast(currVertex);
            currVertex = bSubgraph.get(currVertex).previous;
        }
        return new DigraphPath(shortestAncestralPath, shortestCommonAncestor,
                vertices.stream().mapToInt(Integer::intValue).toArray());
    }

    private static final class VisitedVertex {

        final int previous;
        final int distance;

        VisitedVertex(int previous, int distance) {
            this.previous = previous;
            this.distance = distance;
        }
    }


}
