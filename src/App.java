import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) {
        List<Boolean> results = new ArrayList<>();
        /*
         * Confirms structure for basic strings.
         * 
         * This covered the key functionality but were incomplete because of the
         * complexities of repeat characters
         */
        Test.testTreeStructures(new CharNodeFactory(), results,
                Arrays.copyOfRange(Test.strings, 0, Test.strings.length - 1),
                Arrays.copyOfRange(Test.tests, 0, Test.tests.length - 1));
        Test.testTreeStructures(new MapNodeFactory(), results, Test.strings, Test.tests);

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

        /*
         * Do all tests pass?
         */
        System.out.println("Do all tests pass: " + !results.contains(false));

        /*
         * Testing a gene sequence of 5000 characters.
         * 
         * All substrings should be present.
         * 
         * This test case revealed a lot of bugs in the code, at least 10.
         * 
         */
        Test.testGene(results);
    }
}