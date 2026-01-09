
/*
 * Represents a substring using a start and end index.
 */
public class Edge {
    public int start;
    public End end;
    public Node child;

    public int stringId;

    public Edge(
            int start,
            End end,
            int stringId) {
        this.start = start;
        this.end = end;
        this.stringId = stringId;
    }
}