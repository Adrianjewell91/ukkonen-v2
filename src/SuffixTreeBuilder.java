import java.util.*;

/*
* An implementation of Ukkonen's Algorithm.
* 
* Overview: Build a suffix tree in O(N) time by optimizing a naive O(N^^3)
* algorithm.
*
* The naive O(N^^3) algorithm:
* 
* for each substring (0..i) in string:
* build suffix tree:
* for each char in substring:
* insert into suffix tree;
* 
* 
* Optimizations:
* 
* 1. Extending each suffix: a) Maintain a global pointer to the last index and
* update the pointer instead.
* 2. Paths that branch : a) Maintain a pointer of how far down the suffix path
* already traversed.
* b) Point internal branch points to one another, then:
* c) Traverse the branch points to avoid dfs path traversals during branch
* creation.
*/

/*
Space complexity of GST:

m + n vs m then n 
m+n == O((n+m)^2)
m then n == O(n^2 + m^2)
which is theoretically equivalent 
*/
public class SuffixTreeBuilder {
    /*
     * nb. isDebug: will insert a certain logs if set.
     */
    public static Node build(String s, NodeFactory factory, boolean isDebug, List<String> logs) {
        /*
         * Create the root node.
         */
        Node root = factory.createNode();
        root.setSuffixLink(root);
        root.setIsRoot(true);

        /*
         * All terminal edges point to this gloabal end
         */
        End globalEnd = new End(0);

        /*
         * The peg indicates the last non repeating character,
         * so where are no repeats, then peg and i are the same.
         */
        int peg = 0;

        Node currentNode = root;
        Edge currentEdge = null;

        // counter
        int counter = 0;

        /*
         * It is required to understand that at each iteration, the
         * implicit suffix tree exists for 0..i.
         */
        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);

            if (currentNode.getIsRoot() == false && currentEdge == null) {
                System.out.println("bug");
                break;
            }

            if (isDebug == true) {
                StringBuilder b = new StringBuilder();
                try {
                    Util.suffixes(root, "", s, b, false);
                    System.out.println(b.toString());

                } catch (Exception e) {
                    System.out.println("ERROR");
                    throw e;
                }
            }

            if (isDebug == true) {
                logs.add("Inserting char " + c + ".");
            }

            /*
             * Two cases:
             * 1. Not traversing an edge (guaranteed to be at the root node).
             * 2. Traversing an edge (happens during repeats in the string).
             */

            /*
             * Case 1. No active edge.
             */
            if (currentEdge == null) {
                /*
                 * Option 1:
                 * 
                 * There is no edge for the character. aka this character hasn't been seen
                 * before.
                 */
                if (currentNode.getEdge(c) == null) {
                    Edge e = new Edge(i, globalEnd);
                    currentNode.setEdge(c, e);

                    peg++;
                }
                /*
                 * Option 2:
                 * 
                 * Start traversing the edge for that character since it is there already.
                 */
                else {
                    currentEdge = currentNode.getEdge(c);
                    counter++;
                }

                i++;
            }
            /*
             * Case 2. Traversing an edge...
             */
            else if (c != s.charAt(currentEdge.start + counter)) {
                // System.out.println(currentEdge.start);
                /*
                 * Option 1:
                 * 
                 * The next character already exists while traversing the string (due to
                 * repeats).
                 * 
                 * AND reached the end of edge, because otherwise i++ == move down the current
                 * edge.
                 */
                Node lastCreatedInternalNode = null;
                /*
                 * counter is the distance between the current node and the branch point to
                 * be created.
                 * 
                 * It works because the start-end indexes of the the edges being traversed are
                 * guaranteed to be contiguous.
                 * That is because when a set of edges are traversed, the iteration continues
                 * from idx 0
                 * until it reaches a non-matching character.
                 */
                // int localCounter = counter;

                boolean resetEdgeAndCounter = true;

                while (peg < i) {
                    /*
                     * For when the suffix link traversal returns to the root but requires skipping
                     * down to a deeper branch node.
                     * 
                     * Skip-jump down the string and nodes, if necessary.
                     * It happens when the counter indicates that to continue the branching, one
                     * much skip down the string to a lower node.
                     * 
                     */
                    if (currentNode.getIsRoot()) {
                        counter = i - peg;
                    }

                    /*
                     * but i-counter also works because: counter is the distance from the
                     * currentNode,
                     * and since i is the current character, it is guaranteed that i-counter
                     * will be the first
                     * character in the desired edge, and therefore in all the edges to be
                     * manipulated during the suffix link traversal.
                     */
                    currentEdge = currentNode.getEdge(s.charAt(i - counter));

                    while (counter > (currentEdge.end.end - currentEdge.start)) {
                        counter -= currentEdge.end.end - currentEdge.start;

                        // if this happens, then there is a bug
                        // if (currentEdge.child.getEdge(s.charAt(i - counter)) == null) {
                        // same issue looks like nodes are being re assigned which is not good.
                        // break;
                        // }
                        currentNode = currentEdge.child;
                        currentEdge = currentNode.getEdge(s.charAt(i - counter));
                    }

                    /*
                     * Check if one is simply at the end of a edge,
                     * even if the 'c' != next char, another branch might contain it, in this case
                     * continue down that edge..
                     */
                    if (currentEdge.start + counter == currentEdge.end.end
                            && currentEdge.child != null
                            && currentEdge.child.getEdge(c) != null) {

                        resetEdgeAndCounter = false;

                        currentNode = currentEdge.child;
                        currentEdge = currentNode.getEdge(c);
                        counter = 1;
                        i++;
                        break;
                    } else if (currentEdge.start + counter == currentEdge.end.end
                            && currentEdge.child != null) {

                        Edge newEdge = new Edge(i, globalEnd);
                        currentEdge.child.setEdge(c, newEdge);
                    }
                    /*
                     * Create the new branch point, which == a new internal node, and copying over
                     * children from the existing edge.
                     */
                    else {
                        Node internalNode = factory.createNode();

                        // if (currentEdge.start + counter >= currentEdge.end.end)
                        // {
                        // System.out.print("found");
                        // }
                        Edge split = new Edge(currentEdge.start + counter, currentEdge.end);
                        Edge newEdge = new Edge(i, globalEnd);
                        
                        // Insert the node.
                        split.child = currentEdge.child;

                        // Update CurrentEdge: create a hard end, and set child to the new node.
                        currentEdge.child = internalNode;
                        currentEdge.end = new End(currentEdge.start + counter);

                        /*
                         * Set edges and suffix link.
                         * 
                         * Internal nodes be default point to the root unless otherwise modified later.
                         */
                        internalNode.setSuffixLink(root);
                        internalNode.setEdge(s.charAt(currentEdge.start + counter), split);
                        internalNode.setEdge(c, newEdge);



                        /*
                         * Create the suffix link.
                         */
                        if (lastCreatedInternalNode == null) {
                            lastCreatedInternalNode = internalNode;
                        } else {
                            lastCreatedInternalNode.setSuffixLink(internalNode);
                            lastCreatedInternalNode = internalNode;
                        }
                        /*
                         * 
                         * Reset the last created internal node if the traversal reaches the root,
                         * because each link of suffixes must terminate at the root, which means
                         * traversing down from the root starts a "new" set of links.
                         */
                        if (currentNode.getSuffixLink().getIsRoot() && !currentNode.getIsRoot()) {
                            lastCreatedInternalNode = null;
                        }
                    }

                    /*
                     * Traverse the suffix link.
                     */
                    currentNode = currentNode.getSuffixLink();

                    if (isDebug == true) {
                        logs.add("Traversed to a Suffix Link. Is it to the root?");
                        logs.add(String.valueOf(currentNode.getIsRoot()));
                    }

                    peg++;
                }

                /*
                 * Reset the edge.
                 * 
                 * This is an implementation complexity.
                 */
                if (resetEdgeAndCounter) {
                    currentEdge = null;
                    counter = 0;
                }

                /*
                 * Continue to next loop if a branch was created.
                 * Essentially the same character but now it's fresh start,
                 * Therefore also do not increment global end.
                 */
                if (resetEdgeAndCounter) {
                    continue;
                }
            }
            /*
             * Option 3: Where the characters equal.
             */
            else {
                i++;
                counter++;
            }

            /*
             * Update the global end, which adds the character to all existing terminal
             * edges.
             */
            globalEnd.end++;
        }

        return root;
    }
}
