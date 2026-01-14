import java.util.ArrayList;
import java.util.List;

public class Test {
    /*
     * TESTS
     * 
     * A sequence of strings comprise the tests, and each string derives from the
     * former according to this way:
     * 
     * abc|abc|dea|abcabf|abcabcdg.
     * 
     * String 1 tests basic construction.
     * String 2 tests basic construction with repeats.
     * String 3 tests simple branching.
     * String 4 tests branching using the suffix link technique.
     * String 5 tests additionally the skip-jump technique that must occur when a
     * suffix traversal reaches the root node, but must traverse down several nodes
     * before continuing the branching.
     */

    // String 1: abc
    // /abc
    // /bc
    // /c
    public static final String s1Test = "/abc\n/bc\n/c\n";
    public static final String s1 = "abc"; // Simple

    // String 2: abcabc
    // /abcabc
    // /bcabc
    // /cabc
    public static final String s2Test = "/abcabc\n/bcabc\n/cabc\n";
    public static final String s2 = "abcabc"; // Check counter goes up

    // String 3: abcabcdea
    // /abc/abcdea
    // /abc/dea
    // /bc/abcdea
    // /bc/dea
    // /c/abcdea
    // /c/dea
    // /dea
    // /ea
    public static final String s3Test = "/abc/abcdea\n/abc/dea\n/bc/abcdea\n/bc/dea\n/c/abcdea\n/c/dea\n/dea\n/ea\n";
    public static final String s3 = "abcabcdea"; // Check formation of suffix links

    // String 4: abcabcdeabcabf
    // /ab/c/ab/cdeabcabf
    // /ab/c/ab/f
    // /ab/c/deabcabf
    // /ab/f
    // /b/c/ab/cdeabcabf
    // /b/c/ab/f
    // /b/c/deabcabf
    // /b/f
    // /c/ab/cdeabcabf
    // /c/ab/f
    // /c/deabcabf
    // /deabcabf
    // /eabcabf
    // /f
    public static final String s4Test = "/ab/c/ab/cdeabcabf\n/ab/c/ab/f\n/ab/c/deabcabf\n/ab/f\n/b/c/ab/cdeabcabf\n/b/c/ab/f\n/b/c/deabcabf\n/b/f\n/c/ab/cdeabcabf\n/c/ab/f\n/c/deabcabf\n/deabcabf\n/eabcabf\n/f\n";
    public static final String s4 = "abcabcdeabcabf"; // Check traversal of suffix links to do second branching.

    // String 5: abcabcdeabcabfabcabcdg
    // /ab/c/ab/cd/eabcabfabcabcdg
    // /ab/c/ab/cd/g
    // /ab/c/ab/fabcabcdg
    // /ab/c/d/eabcabfabcabcdg
    // /ab/c/d/g
    // /ab/fabcabcdg
    // /b/c/ab/cd/eabcabfabcabcdg
    // /b/c/ab/cd/g
    // /b/c/ab/fabcabcdg
    // /b/c/d/eabcabfabcabcdg
    // /b/c/d/g
    // /b/fabcabcdg
    // /c/ab/cd/eabcabfabcabcdg
    // /c/ab/cd/g
    // /c/ab/fabcabcdg
    // /c/d/eabcabfabcabcdg
    // /c/d/g
    // /d/eabcabfabcabcdg
    // /d/g
    // /eabcabfabcabcdg
    // /fabcabcdg
    // /g
    public static final String s5Test = "/ab/c/ab/cd/eabcabfabcabcdg\n/ab/c/ab/cd/g\n/ab/c/ab/fabcabcdg\n/ab/c/d/eabcabfabcabcdg\n/ab/c/d/g\n/ab/fabcabcdg\n/b/c/ab/cd/eabcabfabcabcdg\n/b/c/ab/cd/g\n/b/c/ab/fabcabcdg\n/b/c/d/eabcabfabcabcdg\n/b/c/d/g\n/b/fabcabcdg\n/c/ab/cd/eabcabfabcabcdg\n/c/ab/cd/g\n/c/ab/fabcabcdg\n/c/d/eabcabfabcabcdg\n/c/d/g\n/d/eabcabfabcabcdg\n/d/g\n/eabcabfabcabcdg\n/fabcabcdg\n/g\n";
    public static final String s5 = "abcabcdeabcabfabcabcdg"; // Check only node "traversal" after reaching root, proves
                                                              // correct sf
    // extensions 2x.

    /*
     * Generalized suffix tree:
     * 
     * It's not really GST but also testing that repeats not starting at the
     * beginning get handled correctly.
     */
    public static final String gst1 = "abcabc" + "$" + "defdef" + "#";

    public static final String gst1and2Expected = """
            /abc/abc$defdef#
            /abc/$defdef#
            /bc/abc$defdef#
            /bc/$defdef#
            /c/abc$defdef#
            /c/$defdef#
            /#
            /$defdef#
            /def/#
            /def/def#
            /ef/#
            /ef/def#
            /f/#
            /f/def#
            """;

    public static final String s6 = generate();
    public static final String s6Test = """
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

    public static final String[] strings = new String[] { s1, s2, s3, s4, s5, s6, gst1 };
    public static final String[] tests = new String[] { s1Test, s2Test, s3Test, s4Test, s5Test, s6Test, gst1and2Expected };

    public static final String gene = "gatcctccatatacaacggtatctccacctcaggtttagatctcaacaacggaaccattgccgacatgagacagttaggtatcgtcgagagttacaagctaaaacgagcagtagtcagctctgcatctgaagccgctgaagttctactaagggtggataacatcatccgtgcaagaccaagaaccgccaatagacaacatatgtaacatatttaggatatacctcgaaaataataaaccgccacactgtcattattataattagaaacagaacgcaaaaattatccactatataattcaaagacgcgaaaaaaaaagaacaacgcgtcatagaacttttggcaattcgcgtcacaaataaattttggcaacttatgtttcctcttcgagcagtactcgagccctgtctcaagaatgtaataatacccatcgtaggtatggttaaagatagcatctccacaacctcaaagctccttgccgagagtcgccctcctttgtcgagtaattttcacttttcatatgagaacttattttcttattctttactctcacatcctgtagtgattgacactgcaacagccaccatcactagaagaacagaacaattacttaatagaaaaattatatcttcctcgaaacgatttcctgcttccaacatctacgtatatcaagaagcattcacttaccatgacacagcttcagatttcattattgctgacagctactatatcactactccatctagtagtggccacgccctatgaggcatatcctatcggaaaacaataccccccagtggcaagagtcaatgaatcgtttacatttcaaatttccaatgatacctataaatcgtctgtagacaagacagctcaaataacatacaattgcttcgacttaccgagctggctttcgtttgactctagttctagaacgttctcaggtgaaccttcttctgacttactatctgatgcgaacaccacgttgtatttcaatgtaatactcgagggtacggactctgccgacagcacgtctttgaacaatacataccaatttgttgttacaaaccgtccatccatctcgctatcgtcagatttcaatctattggcgttgttaaaaaactatggttatactaacggcaaaaacgctctgaaactagatcctaatgaagtcttcaacgtgacttttgaccgttcaatgttcactaacgaagaatccattgtgtcgtattacggacgttctcagttgtataatgcgccgttacccaattggctgttcttcgattctggcgagttgaagtttactgggacggcaccggtgataaactcggcgattgctccagaaacaagctacagttttgtcatcatcgctacagacattgaaggattttctgccgttgaggtagaattcgaattagtcatcggggctcaccagttaactacctctattcaaaatagtttgataatcaacgttactgacacaggtaacgtttcatatgacttacctctaaactatgtttatctcgatgacgatcctatttcttctgataaattgggttctataaacttattggatgctccagactgggtggcattagataatgctaccatttccgggtctgtcccagatgaattactcggtaagaactccaatcctgccaatttttctgtgtccatttatgatacttatggtgatgtgatttatttcaacttcgaagttgtctccacaacggatttgtttgccattagttctcttcccaatattaacgctacaaggggtgaatggttctcctactattttttgccttctcagtttacagactacgtgaatacaaacgtttcattagagtttactaattcaagccaagaccatgactgggtgaaattccaatcatctaatttaacattagctggagaagtgcccaagaatttcgacaagctttcattaggtttgaaagcgaaccaaggttcacaatctcaagagctatattttaacatcattggcatggattcaaagataactcactcaaaccacagtgcgaatgcaacgtccacaagaagttctcaccactccacctcaacaagttcttacacatcttctacttacactgcaaaaatttcttctacctccgctgctgctacttcttctgctccagcagcgctgccagcagccaataaaacttcatctcacaataaaaaagcagtagcaattgcgtgcggtgttgctatcccattaggcgttatcctagtagctctcatttgcttcctaatattctggagacgcagaagggaaaatccagacgatgaaaacttaccgcatgctattagtggacctgatttgaataatcctgcaaataaaccaaatcaagaaaacgctacacctttgaacaacccctttgatgatgatgcttcctcgtacgatgatacttcaatagcaagaagattggctgctttgaacactttgaaattggataaccactctgccactgaatctgatatttccagcgtggatgaaaagagagattctctatcaggtatgaatacatacaatgatcagttccaatcccaaagtaaagaagaattattagcaaaacccccagtacagcctccagagagcccgttctttgacccacagaataggtcttcttctgtgtatatggatagtgaaccagcagtaaataaatcctggcgatatactggcaacctgtcaccagtctctgatattgtcagagacagttacggatcacaaaaaactgttgatacagaaaaacttttcgatttagaagcaccagagaaggaaaaacgtacgtcaagggatgtcactatgtcttcactggacccttggaacagcaatattagcccttctcccgtaagaaaatcagtaacaccatcaccatataacgtaacgaagcatcgtaaccgccacttacaaaatattcaagactctcaaagcggtaaaaacggaatcactcccacaacaatgtcaacttcatcttctgacgattttgttccggttaaagatggtgaaaatttttgctgggtccatagcatggaaccagacagaagaccaagtaagaaaaggttagtagatttttcaaataagagtaatgtcaatgttggtcaagttaaggacattcacggacgcatcccagaaatgctgtgattatacgcaacgatattttgcttaattttattttcctgttttattttttattagtggtttacagataccctatattttatttagtttttatacttagagacatttaattttaattccattcttcaaatttcatttttgcacttaaaacaaagatccaaaaatgctctcgccctcttcatattgagaatacactccattcaaaattttgtcgtcaccgctgattaatttttcactaaactgatgaataatcaaaggccccacgtcagaaccgactaaagaagtgagttttattttaggaggttgaaaaccattattgtctggtaaattttcatcttcttgacatttaacccagtttgaatccctttcaatttctgctttttcctccaaactatcgaccctcctgtttctgtccaacttatgtcctagttccaattcgatcgcattaataactgcttcaaatgttattgtgtcatcgttgactttaggtaatttctccaaatgcataatcaaactatttaaggaagatcggaattcgtcgaacacttcagtttccgtaatgatctgatcgtctttatccacatgttgtaattcactaaaatctaaaacgtatttttcaatgcataaatcgttctttttattaataatgcagatggaaaatctgtaaacgtgcgttaatttagaaagaacatccagtataagttcttctatatagtcaattaaagcaggatgcctattaatgggaacgaactgcggcaagttgaatgactggtaagtagtgtagtcgaatgactgaggtgggtatacatttctataaaataaaatcaaattaatgtagcattttaagtataccctcagccacttctctacccatctattcataaagctgacgcaacgattactattttttttttcttcttggatctcagtcgtcgcaaaaacgtataccttctttttccgaccttttttttagctttctggaaaagtttatattagttaaacagggtctagtcttagtgtgaaagctagtggtttcgattgactgatattaagaaagtggaaattaaattagtagtgtagacgtatatgcatatgtatttctcgcctgtttatgtttctacgtacttttgatttatagcaaggggaaaagaaatacatactattttttggtaaaggtgaaagcataatgtaaaagctagaataaaatggacgaaataaagagaggcttagttcatcttttttccaaaaagcacccaatgataataactaaaatgaaaaggatttgccatctgtcagcaacatcagttgtgtgagcaataataaaatcatcacctccgttgcctttagcgcgtttgtcgtttgtatcttccgtaattttagtcttatcaatgggaatcataaattttccaatgaattagcaatttcgtccaattctttttgagcttcttcatatttgctttggaattcttcgcacttcttttcccattcatctctttcttcttccaaagcaacgatccttctacccatttgctcagagttcaaatcggcctctttcagtttatccattgcttccttcagtttggcttcactgtcttctagctgttgttctagatcctggtttttcttggtgtagttctcattattagatctcaagttattggagtcttcagccaattgctttgtatcagacaattgactctctaacttctccacttcactgtcgagttgctcgtttttagcggacaaagatttaatctcgttttctttttcagtgttagattgctctaattctttgagctgttctctcagctcctcatatttttcttgccatgactcagattctaattttaagctattcaatttctctttgatc";

    public static void testSuffixLinkCreationAndTraversal(NodeFactory factory, List<Boolean> results) {
        System.out.println("Logs for string, did it traverse the suffix links correctly for string:" + s5 + "?");

        try {
            List<String> logs = new ArrayList<>();

            // The string is: "abcabdeabdabfabcabcdg".
            SuffixTreeBuilder.build(s5, factory, false, logs);
            String[] traversalsToRoot = {
                    // The first traversal to extend "d". Reaches root each time
                    "true", "true", "true",
                    // The second traversal to exetnd "f", it reaches the root at the third time
                    // then reaches it each time.
                    "false", "false", "true", "true", "true",

                    // The last traveral to extend "g", it similarly reaches the root at third time,
                    // then skips down 2 two nodes...
                    "false", "false", "true",
                    // then does another round.
                    "false", "false", "true",
                    // then one last time to branch "g" from "dea..." and back to root (see
                    // drawings).
                    "true"
            };

            int j = 0;
            // System.out.println(String.join("\n", logs));
            for (String log : logs) {
                if (log.equals("true") || log.equals("false")) {
                    boolean result = traversalsToRoot[j].equals(log);
                    System.out.println(result);

                    results.add(result);

                    j++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            results.add(false);
        }
    }

    private static String generate() {
        // A fun extra test for Node.edges length == 26:
        String test = "";
        for (char c : Test.s5.toCharArray()) {
            char next = (char) ('z' - (c - 'a'));
            test += next;
        }

        return test;
    }

    public static void testTreeStructures(NodeFactory factory, List<Boolean> results, String[] strings, String[] tests) {
        System.out.println("Checking test trees' final structure:");

        for (int i = 0; i < strings.length; i++) {
            try {
                // System
                // .out
                // .println("String: " + TestCases.strings[i]);

                Node root = SuffixTreeBuilder.build(strings[i], factory, false, null);
                StringBuilder b = new StringBuilder();

                Util.suffixes(root, "", strings[i], b, false);

                boolean result = b.toString().equals(tests[i]);

                System.out
                        .println(result);

                results.add(result);

                // System.out.println(b.toString());
            } catch (Exception e) {
                // System.out.println(e.toString());

                e.printStackTrace();

                results.add(false);
            }
        }
    }
}
