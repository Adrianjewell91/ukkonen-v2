
/*
 * Represents either the root of the tree or an internal branch point.
 */

import java.util.Arrays;
import java.util.List;

public class CharNode implements Node {
    public boolean isRoot;
    public Node suffixLink;
    public Edge[] edges;

    public CharNode() {
        edges = new Edge[26];
    }

    public Edge getEdge(char c) 
    {
        return edges[c - 'a'];
    }

    public boolean setEdge(char c, Edge edge)
    {
        edges[c - 'a'] = edge;
        return true;
    }

    public List<Edge> getAllEdges()
    {
        return Arrays.asList(edges);
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