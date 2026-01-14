import java.util.LinkedList;
import java.util.Queue;

public class Util {

    public static boolean contains(Node tree, String s, String query) {
        int i = 0;
        Node current = tree;
    
        while (i < query.length()) {
            if (current == null) {
                return false;
            }
            Node temp = current;
            for (Edge e : temp.getAllEdges()) {
    
                current = null;
                int d = e.end.end - e.start;
                int remaining = query.length() - i;
    
                if (d <= remaining &&
                        s
                        .substring(e.start, e.end.end)
                        .equals(query.substring(i, i + (e.end.end - e.start)))) 
                {
                    current = e.child;
                    i += e.end.end - e.start;
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
