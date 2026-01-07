
/*
 * Represents either the root of the tree or an internal branch point.
 */

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapNode implements Node{
    private boolean isRoot;
    private Node suffixLink;
    private Map<Character, Edge> edges;

    public MapNode() {
        edges = new HashMap<>();
    }

    public Edge getEdge(char c) {
        return edges.get(c);
    }

    public boolean setEdge(char c, Edge edge) {
        edges.put(c, edge);
        return true;
    }

    public List<Edge> getAllEdges() {
        return new ArrayList<>(edges.values());
    }
    
    public boolean getIsRoot() {
        return isRoot;
    }

    public boolean setIsRoot(boolean isTrue) {
        isRoot = isTrue;
        return true;
    }

    public Node getSuffixLink() {
        return suffixLink;
    }

    public boolean setSuffixLink(Node node) {
        suffixLink = node;
        return true;
    }
}