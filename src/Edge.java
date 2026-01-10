
/*
 * Represents a substring using a start and end index.
 */
public class Edge {
    public int start;
    public End end;
    public Node child;

    public Edge(
            int start,
            End end) {
        this.start = start;
        this.end = end;
    }
}