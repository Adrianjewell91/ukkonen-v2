
/*
 * Represents either the root of the tree or an internal branch point.
 */
import java.util.List;

public interface Node {
    Edge getEdge(char c);
    boolean setEdge(char c, Edge edge);
    List<Edge> getAllEdges();

    boolean getIsRoot();
    boolean setIsRoot(boolean isRoot);
    Node getSuffixLink();
    boolean setSuffixLink(Node node);
}