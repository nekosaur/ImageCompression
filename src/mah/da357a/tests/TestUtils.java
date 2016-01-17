package mah.da357a.tests;

/**
 * @author nekosaur
 */
public class TestUtils {

    public static void assertArrays(byte[] a1, byte a2[]) {

        assert(a1.length == a2.length) : "Arrays are not equal in length";

        for (int i = 0; i < a1.length; i++) {
            assert(a1[i] == a2[i]) : "Arrays are not equal in content (index=" + i + ")";
        }
    }
}
