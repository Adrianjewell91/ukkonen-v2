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

        //counter
        int counter = 0;

        /*
         * It is required to understand that at each iteration, the
         * implicit suffix tree exists for 0..i.
         */
        for (int i = 0; i < s.length(); i++) {

            char c = s.charAt(i);

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
            }
            /*
             * Case 2. Traversing an edge...
             */
            else {
                /*
                 * Option 1:
                 * 
                 * The next character already exists while traversing the string (due to
                 * repeats).
                 * 
                 * AND reached the end of edg, because otherwise i++ == move down the current
                 * edge.
                 */
                if (c == s.charAt(currentEdge.start + counter) && (currentEdge.start + counter == currentEdge.end.end)) {
                // if (c == s.charAt(i - peg) && (i - peg) == currentEdge.end.end) {
                    currentNode = currentEdge.child;
                    currentEdge = currentNode.getEdge(c);
                    counter = 1;
                }
                /*
                 * Option 2:
                 * 
                 * The next character in the suffix != the next character in the string,
                 * Therefore this is a branch point.
                 */
                else if (c != s.charAt(currentEdge.start + counter)) {
                // else if (c != s.charAt(i - peg)) {
                    Node lastCreatedInternalNode = null;
                    /*
                     * LocalCounter is the distance between the current node and the branch point to
                     * be created.
                     * 
                     * It works because the start-end indexes of the the edges being traversed are
                     * guaranteed to be contiguous.
                     * That is because when a set of edges are traversed, it continues from idx 0
                     * until it reaches a non-matching character.
                     */
                    int localCounter = (i - peg) - currentEdge.start;

                    while (peg < i) {
                        /*
                         * Skip-jump down the string and nodes, if necessary.
                         * It happens when the gblCounter indicates that to continue the branching, one
                         * much skip down the string to a lower node.
                         */
                        if (currentNode.getIsRoot()) {
                            localCounter = i - peg;
                            // not sure about these either...
                            currentEdge = currentNode.getEdge(s.charAt(i - localCounter));

                            while (localCounter > (currentEdge.end.end - currentEdge.start)) {
                                localCounter -= currentEdge.end.end - currentEdge.start;
                                currentNode = currentEdge.child;
                                currentEdge = currentNode.getEdge(s.charAt(i - localCounter));
                            }
                        } else {
                            // current edge.start would work here too,
                            /*
                             * but i-localCounter also works because: localCounter is the distance from the
                             * currentNode,
                             * and since i is the current character, it is guaranteed that i-localCounter
                             * will be the first
                             * character in the desired edge, and therefore in all the edges to be
                             * manipulated during the suffix link traversal.
                             */
                            currentEdge = currentNode.getEdge(s.charAt(i - localCounter));
                        }

                        /*
                         * Create the new branch point, which == a new internal node, and copying over
                         * children from the existing edge.
                         */
                        Node internalNode = factory.createNode();
                        Edge split = new Edge(currentEdge.start + localCounter, currentEdge.end);
                        Edge newEdge = new Edge(i, globalEnd);

                        internalNode.setEdge(s.charAt(currentEdge.start + localCounter), split);
                        internalNode.setEdge(c, newEdge);

                        /*
                         * Internal nodes be default point to the root unless otherwise modified later.
                         */
                        internalNode.setSuffixLink(root);

                        split.child = currentEdge.child;

                        currentEdge.child = internalNode;
                        currentEdge.end = new End(currentEdge.start + localCounter);

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
                         * nb.
                         * Reset the last created internal node if the traversal reaches the root,
                         * because each link of suffixes must terminate at the root, which means
                         * traversing down from the root starts a "new" set of links.
                         */
                        if (currentNode.getSuffixLink().getIsRoot() && !currentNode.getIsRoot()) {
                            lastCreatedInternalNode = null;
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
                     * Create the new node at the root for the new character.
                     */
                    // technically duplicate of line ~88
                    Edge e = new Edge(i, globalEnd);
                    currentNode.setEdge(c, e);

                    /*
                     * Essentially: the next possible peg will be i+1 because at i+1 there might be
                     * a repeat and so the peg indeed gets held at i+1.
                     */
                    peg++;

                    /*
                     * Reset the edge.
                     * 
                     * This is an implementation complexity.
                     */
                    currentEdge = null;
                    counter = 0;
                }
                else { counter++; }
                /*
                 * There is an Option 3: Where the character are simply equal but this in
                 * implicit
                 * because i++ == an increment down the edge.
                 */
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
