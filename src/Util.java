import java.util.LinkedList;
import java.util.Queue;

public class Util {

    // Currently for exact match of suffix not contains. 
    public static boolean contains(Node tree, String s, String suffix) {
        int i = 0;
        Node current = tree;
    
        while (i < suffix.length()) {
            if (current == null) {
                return false;
            }

            Node temp = current;
            for (Edge e : temp.getAllEdges()) {
    
                current = null;
                int edgeLength = e.end.end - e.start;
                int remaining = suffix.length() - i;
    
                if (edgeLength <= remaining &&
                        s
                        .substring(e.start, e.end.end)
                        .equals(suffix.substring(i, i + (edgeLength)))) 
                {
                    current = e.child;
                    i += edgeLength;

                    // In the case where the suffix is completed,
                    // current == null but since i == suffix.length(), the while loop ends
                    // and the function therefore returns true.
                    break;
                }

                // For contains any string not just a suffix.
                
                //When remaining < edgeLength and it is contained within the edge, AND i can be exhausted 
                // reduce i, current = null, and break. 
                if (remaining < edgeLength && 
                    s.substring(e.start, e.start + remaining).equals(suffix.substring(i, suffix.length())))
                {
                    
                    current = e.child;
                    i += remaining;
                    break;
                }
            }
        }
        // System.out.println(i);
        return true;
    }

    public static int countNodes(Node root) {
        int count = 1;
        Node current = root;
    
        Queue<Node> q = new LinkedList<>();
    
        while (current != null) {
    
            for (Edge e : current.getAllEdges()) {
                if (e.child != null) {
                    q.add(e.child);
                    count++;
                }
            }
    
            current = q.poll();
        }
    
        return count;
    }

    /*
     * Traverses the suffix tree.
     */
    // The String 's' must match the suffix tree, there is no test for this
    // currently...
    public static void suffixes(Node tree, String path, String s, StringBuilder builder, boolean verbose) {
        if (tree == null) {
            if (verbose) {
                System.out.println(path);
            }
            builder.append(path + "\n");
            return;
        }
    
        for (Edge e : tree.getAllEdges()) {
            if (e == null) {
                continue;
            }
    
            try 
            {
                suffixes(e.child, path + "/" + s.substring(e.start, e.end.end), s, builder, verbose);
            } catch (Exception err)
            {
                System.out.println("Error in traversing");
                throw err;
            }
        }
    }
    
}
