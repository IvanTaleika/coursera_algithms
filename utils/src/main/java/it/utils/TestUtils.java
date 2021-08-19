package it.utils;

public final class TestUtils {

    private TestUtils() {}

    public static void testIllegalArgumentException(Runnable testFunction) {
        try {
            testFunction.run();
            throw new RuntimeException("IllegalArgumentException expected, but not thrown");
        } catch (IllegalArgumentException e) {
        }
    }

}
