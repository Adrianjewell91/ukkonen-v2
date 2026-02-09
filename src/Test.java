import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    public static final String s1Test = """
            /abc
            /bc
            /c
            """;
    public static final String s1 = "abc"; // Simple

    // String 2: abcabc
    public static final String s2Test = """
            /abcabc
            /bcabc
            /cabc
            """;
    public static final String s2 = "abcabc"; // Check counter goes up

    // String 3: abcabcdea
    public static final String s3Test = """
            /abc/abcdea
            /abc/dea
            /bc/abcdea
            /bc/dea
            /c/abcdea
            /c/dea
            /dea
            /ea
            """;
    public static final String s3 = "abcabcdea"; // Check formation of suffix links

    // String 4: abcabcdeabcabf
    public static final String s4Test = """
            /ab/c/ab/cdeabcabf
            /ab/c/ab/f
            /ab/c/deabcabf
            /ab/f
            /b/c/ab/cdeabcabf
            /b/c/ab/f
            /b/c/deabcabf
            /b/f
            /c/ab/cdeabcabf
            /c/ab/f
            /c/deabcabf
            /deabcabf
            /eabcabf
            /f
            """;
    public static final String s4 = "abcabcdeabcabf"; // Check traversal of suffix links to do second branching.

    // String 5: abcabcdeabcabfabcabcdg
    public static final String s5Test = """
            /ab/c/ab/cd/eabcabfabcabcdg
            /ab/c/ab/cd/g
            /ab/c/ab/fabcabcdg
            /ab/c/d/eabcabfabcabcdg
            /ab/c/d/g
            /ab/fabcabcdg
            /b/c/ab/cd/eabcabfabcabcdg
            /b/c/ab/cd/g
            /b/c/ab/fabcabcdg
            /b/c/d/eabcabfabcabcdg
            /b/c/d/g
            /b/fabcabcdg
            /c/ab/cd/eabcabfabcabcdg
            /c/ab/cd/g
            /c/ab/fabcabcdg
            /c/d/eabcabfabcabcdg
            /c/d/g
            /d/eabcabfabcabcdg
            /d/g
            /eabcabfabcabcdg
            /fabcabcdg
            /g
            """;
    public static final String s5 = "abcabcdeabcabfabcabcdg";
    // Check only node "traversal" after reaching root, proves
    // correct sf
    // extensions 2x.

    /*
     * Generalized suffix tree:
     * 
     * It's not really GST but also testing that repeats not starting at the
     * beginning get handled correctly.
     */
    private static final String gst1 = "abcabc" + "$";

    private static final String gst2 = "defdef" + "#";

    public static final String gst1Andgst2 = gst1 + gst2;

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

    public static final String[] strings = new String[] { s1, s2, s3, s4, s5, s6, gst1Andgst2 };
    public static final String[] tests = new String[] { s1Test, s2Test, s3Test, s4Test, s5Test, s6Test,
            gst1and2Expected };

    // This is a real gene from a public database I forgot where.
    // For some context, this test was pivotal in getting an implementation.
    // For example, all the above test passed before adding this test,
    // Then there were probable 10 or so bugs that this test revealed. 
    // My intuition told me there were bugs, but the test proved it.
    // The process for solving them was adding debug statements and going through
    // a substring step by step.
    // The longest substring was about 200 characters long perhaps.
    // The strength in the test was the limiting number of possible characters, 
    // which created all the possible edge cases. 
    public static final String gene = """
            gatcctccatatacaacggtatctccacctcaggtttagatctcaacaacggaaccattgccgacatgaga
            cagttaggtatcgtcgagagttacaagctaaaacgagcagtagtcagctctgcatctgaagccgctgaagt
            tctactaagggtggataacatcatccgtgcaagaccaagaaccgccaatagacaacatatgtaacatattt
            aggatatacctcgaaaataataaaccgccacactgtcattattataattagaaacagaacgcaaaaattat
            ccactatataattcaaagacgcgaaaaaaaaagaacaacgcgtcatagaacttttggcaattcgcgtcaca
            aataaattttggcaacttatgtttcctcttcgagcagtactcgagccctgtctcaagaatgtaataatacc
            catcgtaggtatggttaaagatagcatctccacaacctcaaagctccttgccgagagtcgccctcctttgt
            cgagtaattttcacttttcatatgagaacttattttcttattctttactctcacatcctgtagtgattgac
            actgcaacagccaccatcactagaagaacagaacaattacttaatagaaaaattatatcttcctcgaaacg
            atttcctgcttccaacatctacgtatatcaagaagcattcacttaccatgacacagcttcagatttcatta
            ttgctgacagctactatatcactactccatctagtagtggccacgccctatgaggcatatcctatcggaaa
            acaataccccccagtggcaagagtcaatgaatcgtttacatttcaaatttccaatgatacctataaatcgt
            ctgtagacaagacagctcaaataacatacaattgcttcgacttaccgagctggctttcgtttgactctagt
            tctagaacgttctcaggtgaaccttcttctgacttactatctgatgcgaacaccacgttgtatttcaatgt
            aatactcgagggtacggactctgccgacagcacgtctttgaacaatacataccaatttgttgttacaaacc
            gtccatccatctcgctatcgtcagatttcaatctattggcgttgttaaaaaactatggttatactaacggc
            aaaaacgctctgaaactagatcctaatgaagtcttcaacgtgacttttgaccgttcaatgttcactaacga
            agaatccattgtgtcgtattacggacgttctcagttgtataatgcgccgttacccaattggctgttcttcg
            attctggcgagttgaagtttactgggacggcaccggtgataaactcggcgattgctccagaaacaagctac
            agttttgtcatcatcgctacagacattgaaggattttctgccgttgaggtagaattcgaattagtcatcgg
            ggctcaccagttaactacctctattcaaaatagtttgataatcaacgttactgacacaggtaacgtttcat
            atgacttacctctaaactatgtttatctcgatgacgatcctatttcttctgataaattgggttctataaac
            ttattggatgctccagactgggtggcattagataatgctaccatttccgggtctgtcccagatgaattact
            cggtaagaactccaatcctgccaatttttctgtgtccatttatgatacttatggtgatgtgatttatttca
            acttcgaagttgtctccacaacggatttgtttgccattagttctcttcccaatattaacgctacaaggggt
            gaatggttctcctactattttttgccttctcagtttacagactacgtgaatacaaacgtttcattagagtt
            tactaattcaagccaagaccatgactgggtgaaattccaatcatctaatttaacattagctggagaagtgc
            ccaagaatttcgacaagctttcattaggtttgaaagcgaaccaaggttcacaatctcaagagctatatttt
            aacatcattggcatggattcaaagataactcactcaaaccacagtgcgaatgcaacgtccacaagaagttc
            tcaccactccacctcaacaagttcttacacatcttctacttacactgcaaaaatttcttctacctccgctg
            ctgctacttcttctgctccagcagcgctgccagcagccaataaaacttcatctcacaataaaaaagcagta
            gcaattgcgtgcggtgttgctatcccattaggcgttatcctagtagctctcatttgcttcctaatattctg
            gagacgcagaagggaaaatccagacgatgaaaacttaccgcatgctattagtggacctgatttgaataatc
            ctgcaaataaaccaaatcaagaaaacgctacacctttgaacaacccctttgatgatgatgcttcctcgtac
            gatgatacttcaatagcaagaagattggctgctttgaacactttgaaattggataaccactctgccactga
            atctgatatttccagcgtggatgaaaagagagattctctatcaggtatgaatacatacaatgatcagttcc
            aatcccaaagtaaagaagaattattagcaaaacccccagtacagcctccagagagcccgttctttgaccca
            cagaataggtcttcttctgtgtatatggatagtgaaccagcagtaaataaatcctggcgatatactggcaa
            cctgtcaccagtctctgatattgtcagagacagttacggatcacaaaaaactgttgatacagaaaaacttt
            tcgatttagaagcaccagagaaggaaaaacgtacgtcaagggatgtcactatgtcttcactggacccttgg
            aacagcaatattagcccttctcccgtaagaaaatcagtaacaccatcaccatataacgtaacgaagcatcg
            taaccgccacttacaaaatattcaagactctcaaagcggtaaaaacggaatcactcccacaacaatgtcaa
            cttcatcttctgacgattttgttccggttaaagatggtgaaaatttttgctgggtccatagcatggaacca
            gacagaagaccaagtaagaaaaggttagtagatttttcaaataagagtaatgtcaatgttggtcaagttaa
            ggacattcacggacgcatcccagaaatgctgtgattatacgcaacgatattttgcttaattttattttcct
            gttttattttttattagtggtttacagataccctatattttatttagtttttatacttagagacatttaat
            tttaattccattcttcaaatttcatttttgcacttaaaacaaagatccaaaaatgctctcgccctcttcat
            attgagaatacactccattcaaaattttgtcgtcaccgctgattaatttttcactaaactgatgaataatc
            aaaggccccacgtcagaaccgactaaagaagtgagttttattttaggaggttgaaaaccattattgtctgg
            taaattttcatcttcttgacatttaacccagtttgaatccctttcaatttctgctttttcctccaaactat
            cgaccctcctgtttctgtccaacttatgtcctagttccaattcgatcgcattaataactgcttcaaatgtt
            attgtgtcatcgttgactttaggtaatttctccaaatgcataatcaaactatttaaggaagatcggaattc
            gtcgaacacttcagtttccgtaatgatctgatcgtctttatccacatgttgtaattcactaaaatctaaaa
            cgtatttttcaatgcataaatcgttctttttattaataatgcagatggaaaatctgtaaacgtgcgttaat
            ttagaaagaacatccagtataagttcttctatatagtcaattaaagcaggatgcctattaatgggaacgaa
            ctgcggcaagttgaatgactggtaagtagtgtagtcgaatgactgaggtgggtatacatttctataaaata
            aaatcaaattaatgtagcattttaagtataccctcagccacttctctacccatctattcataaagctgacg
            caacgattactattttttttttcttcttggatctcagtcgtcgcaaaaacgtataccttctttttccgacc
            ttttttttagctttctggaaaagtttatattagttaaacagggtctagtcttagtgtgaaagctagtggtt
            tcgattgactgatattaagaaagtggaaattaaattagtagtgtagacgtatatgcatatgtatttctcgc
            ctgtttatgtttctacgtacttttgatttatagcaaggggaaaagaaatacatactattttttggtaaagg
            tgaaagcataatgtaaaagctagaataaaatggacgaaataaagagaggcttagttcatcttttttccaaa
            aagcacccaatgataataactaaaatgaaaaggatttgccatctgtcagcaacatcagttgtgtgagcaat
            aataaaatcatcacctccgttgcctttagcgcgtttgtcgtttgtatcttccgtaattttagtcttatcaa
            tgggaatcataaattttccaatgaattagcaatttcgtccaattctttttgagcttcttcatatttgcttt
            ggaattcttcgcacttcttttcccattcatctctttcttcttccaaagcaacgatccttctacccatttgc
            tcagagttcaaatcggcctctttcagtttatccattgcttccttcagtttggcttcactgtcttctagctg
            ttgttctagatcctggtttttcttggtgtagttctcattattagatctcaagttattggagtcttcagcca
            attgctttgtatcagacaattgactctctaacttctccacttcactgtcgagttgctcgtttttagcggac
            aaagatttaatctcgttttctttttcagtgttagattgctctaattctttgagctgttctctcagctcctc
            atatttttcttgccatgactcagattctaattttaagctattcaatttctctttgatc.
                    """.replace("\n", "");

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

    public static void testTreeStructures(NodeFactory factory, List<Boolean> results, String[] strings,
            String[] tests) {
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

    public static void testGene(List<Boolean> results) {
        System.out.println("Testing Gene.");
        System.out.println("Sequence Length: " + gene.length());
        /* Debugging stuff */
        Node root;
        StringBuilder b;

        // Test a long string:
        String s = gene.substring(0, gene.length()) + "$";

        b = new StringBuilder();
        root = SuffixTreeBuilder.build(s, new MapNodeFactory(), false, new ArrayList<>());
        System.out.println(Util.countNodes(root));

        // Util.suffixes(root, "", s, b, false);
        // System.out.println(b.toString());

        // System.out.println(s);

        for (int i = 0; i <= s.length(); i++) {
            String suffix = s.substring(s.length() - i, s.length());
            // System.out.println(suffix);
            // System.out.println(Util.contains(root, s, suffix));
            // System.out.println(!Util.contains(root, s, suffix + "*"));
            results.add(Util.contains(root, s, suffix));

            if (Util.contains(root, s, suffix) == false) {
                System.out.println(suffix);
            }

            // Simple check against false positives:
            results.add(!Util.contains(root, s, suffix + "*"));
        }

        // System.out.println("All tests plus gene test: " + !results.contains(false));
    }

    public static void testSimpleGST(List<Boolean> results) {
        System.out.println("Simple GST test, 2 strings.");
        /* Debugging stuff */
        Node root;
        StringBuilder b;

        // Test a long string:
        String s = gst1Andgst2;

        b = new StringBuilder();
        root = SuffixTreeBuilder.build(s, new MapNodeFactory(), false, new ArrayList<>());
        System.out.println(Util.countNodes(root));

        // Util.suffixes(root, "", s, b, false);
        // System.out.println(b.toString());

        for (String str : List.of(gst1, gst2, s)) {
            for (int i = 0; i <= str.length(); i++) {
                String suffix = str.substring(str.length() - i, str.length());
                // System.out.println(suffix);
                // System.out.println(Util.contains(root, s, suffix));
                // System.out.println(!Util.contains(root, s, suffix + "*"));
                results.add(Util.contains(root, s, suffix));

                if (Util.contains(root, s, suffix) == false) {
                    System.out.println(suffix);
                }

                // Simple check against false positives:
                results.add(!Util.contains(root, s, suffix + "*"));
            }
        }

        // System.out.println("All tests plus gst test: " + !results.contains(false));
    }

    public static void testLargeGST(List<Boolean> results) {
        System.out.println("Large GST test, 10 strings.");
        /* Debugging stuff */
        Node root;
        StringBuilder b;

        // Test a long string:
        List<String> strings = new ArrayList<>();
        List<String> delimiters = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "0");

        for (String delimiter : delimiters) {
            strings.add(generateRandomLowercaseString(100) + delimiter);
        }

        String s = String.join("", strings);

        strings.add(s);

        b = new StringBuilder();
        root = SuffixTreeBuilder.build(s, new MapNodeFactory(), false, new ArrayList<>());
        System.out.println(Util.countNodes(root));

        // Util.suffixes(root, "", s, b, false);
        // System.out.println(b.toString());

        for (String str : strings) {
            for (int i = 0; i <= str.length(); i++) {
                String suffix = str.substring(str.length() - i, str.length());
                // System.out.println(suffix);
                // System.out.println(Util.contains(root, s, suffix));
                // System.out.println(!Util.contains(root, s, suffix + "*"));
                results.add(Util.contains(root, s, suffix));

                if (Util.contains(root, s, suffix) == false) {
                    System.out.println(suffix);
                }

                // Simple check against false positives:
                results.add(!Util.contains(root, s, suffix + "*"));
            }
        }

        // System.out.println("All tests plus gst test: " + !results.contains(false));
    }

    public static String generateRandomLowercaseString(int length) {
        // The characters from which to choose
        String characters = "abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            // Get a random index from 0 to 25 (the length of the characters string - 1)
            int randomIndex = random.nextInt(characters.length());
            // Append the character at that random index to the StringBuilder
            stringBuilder.append(characters.charAt(randomIndex));
        }

        return stringBuilder.toString();
    }
}
