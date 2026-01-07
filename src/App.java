public class App {
    public static void main(String[] args) {
        /*
            Confirms structure.
        */
        Test.testTreeStructures(new CharNodeFactory());
        Test.testTreeStructures(new MapNodeFactory());

        /*
            Confirms suffix link traversal during extensions of branch points.

            Example: String s5: abcabcdeabcabfabcabcdg.

            In the first traversal we insert the 'd' at index 6: three 'traversals' to the root each time.

            In the second traversal we insert the 'f' at index 13: 5 traversals. 
            The first three use the internal nodes created previously. Then 2 more which go back to the root again.

            In the third traversal, we insert the 'g' at the final index: 7 traversals.
            The first three go through the suffix links created previously.
            The second three go through the suffix links created previously previously.
            The last one creates an internal node at root -> de... from which goes back to the root. 

            A special property of the last traversal is it reaches the root halfway through, 
            but the next branching must happen some nodes down, so it is required to skip down a few nodes. The last node that was passed through becomes the suffix link entry point for the next round back to root (see the diagram).
        */
        Test.testSuffixLinkCreationAndTraversal(new CharNodeFactory());
        Test.testSuffixLinkCreationAndTraversal(new MapNodeFactory());


        // A fun extra test for Node.edges length:
        String test = "";
        for (char c : Test.s5.toCharArray())
        {
            char next = (char) ('z' - (c - 'a'));
            test += next;
        }
        System.out.println(test);
        Node n = SuffixTreeBuilder.build(test, new CharNodeFactory(), false, null);
        StringBuilder b = new StringBuilder(); Test.suffixes(n, "", test, b, false);
        System.out.println(b.toString());
    }
}
