public class DigraphPath {

    public final int length;
    public final int commonAncestor;
    public final int[] vertices;

    public DigraphPath(int length, int commonAncestor, int[] vertices) {
        this.length = length;
        this.commonAncestor = commonAncestor;
        this.vertices = vertices;
    }
}