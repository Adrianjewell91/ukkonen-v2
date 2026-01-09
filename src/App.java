import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        // A fun extra test for Node.edges length == 26:
        String test = "";
        for (char c : Test.s5.toCharArray()) {
            char next = (char) ('z' - (c - 'a'));
            test += next;
        }
        System.out.println(test);
        Node root = SuffixTreeBuilder.build(test, 1, new CharNodeFactory(), false, null);
        StringBuilder b = new StringBuilder();
        Test.suffixes(root, "", Map.of(1, test), b, false);

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

        boolean result = b.toString().equals(expected);
        System.out.println(result);

        results.add(result);

        /*
         * Generalized suffix tree:
         */

        String gst1 = "abcabc$";
        String gst2 = "defdef%";

        root = SuffixTreeBuilder.build(gst1, 1, new MapNodeFactory(), false, null);
        root = SuffixTreeBuilder.build(gst2, 2, new MapNodeFactory(), false, null, root);

        b = new StringBuilder();
        Test.suffixes(root, "", Map.of(1, gst1, 2, gst2), b, false);

        String gst1and2Expected = """
                /abc/abc$
                /abc/$
                /bc/abc$
                /bc/$
                /c/abc$
                /c/$
                /$
                /def/def%
                /def/%
                /ef/def%
                /ef/%
                /%
                /f/def%
                /f/%
                """;

        result = b.toString().equals(gst1and2Expected);
        System.out.println(result);

        results.add(result);

        /*
         * Do all tests pass?
         */
        System.out.println("Do all tests pass: " + !results.contains(false));
    }
}
