import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        List<Boolean> results = new ArrayList<>();
        /*
         * Confirms structure.
         */
        Test.testTreeStructures(new CharNodeFactory(), results);
        Test.testTreeStructures(new MapNodeFactory(), results);

        /*
         * Confirms suffix link traversal during extensions of branch points.
         * 
         * Example: String s5: abcabcdeabcabfabcabcdg.
         * 
         * In the first traversal we insert the 'd' at index 6: three 'traversals' to
         * the root each time.
         * 
         * In the second traversal we insert the 'f' at index 13: 5 traversals.
         * The first three use the internal nodes created previously. Then 2 more which
         * go back to the root again.
         * 
         * In the third traversal, we insert the 'g' at the final index: 7 traversals.
         * The first three go through the suffix links created previously.
         * The second three go through the suffix links created previously previously.
         * The last one creates an internal node at root -> de... from which goes back
         * to the root.
         * 
         * A special property of the last traversal is it reaches the root halfway
         * through,
         * but the next branching must happen some nodes down, so it is required to skip
         * down a few nodes. The last node that was passed through becomes the suffix
         * link entry point for the next round back to root (see the diagram).
         */
        Test.testSuffixLinkCreationAndTraversal(new CharNodeFactory(), results);
        Test.testSuffixLinkCreationAndTraversal(new MapNodeFactory(), results);

        Node root;
        StringBuilder b;
        boolean result;

        // A fun extra test for Node.edges length == 26:
        String test = "";
        for (char c : Test.s5.toCharArray()) {
            char next = (char) ('z' - (c - 'a'));
            test += next;
        }
        System.out.println(test);
        b = new StringBuilder();
        root = SuffixTreeBuilder.build(test, new CharNodeFactory(), false,
                null);
        Test.suffixes(root, "", test, b, false);

        String expected = """
                /t
                /uzyxzyxwt
                /vzyxzyuzyxzyxwt
                /w/t
                /w/vzyxzyuzyxzyxwt
                /x/w/t
                /x/w/vzyxzyuzyxzyxwt
                /x/zy/uzyxzyxwt
                /x/zy/xw/t
                /x/zy/xw/vzyxzyuzyxzyxwt
                /y/uzyxzyxwt
                /y/x/w/t
                /y/x/w/vzyxzyuzyxzyxwt
                /y/x/zy/uzyxzyxwt
                /y/x/zy/xw/t
                /y/x/zy/xw/vzyxzyuzyxzyxwt
                /zy/uzyxzyxwt
                /zy/x/w/t
                /zy/x/w/vzyxzyuzyxzyxwt
                /zy/x/zy/uzyxzyxwt
                /zy/x/zy/xw/t
                /zy/x/zy/xw/vzyxzyuzyxzyxwt
                """;

        result = b.toString().equals(expected);
        System.out.println(result);

        results.add(result);

        /*
         * Do all tests pass?
         */
        System.out.println("Do all tests pass: " + !results.contains(false));
    }
}
