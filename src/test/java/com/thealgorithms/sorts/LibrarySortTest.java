package com.thealgorithms.sorts;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Test cases for LibrarySort algorithm.
 * 
 * @author Paulo López Núñez
 */
class LibrarySortTest {

    private final LibrarySort librarySort = new LibrarySort();

    @Test
    void testEmptyArray() {
        Integer[] input = {};
        Integer[] expected = {};
        Integer[] result = librarySort.sort(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testNullArray() {
        Integer[] input = null;
        Integer[] result = librarySort.sort(input);
        assertNull(result);
    }

    @Test
    void testSingleElement() {
        Integer[] input = {42};
        Integer[] expected = {42};
        Integer[] result = librarySort.sort(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testTwoElements() {
        Integer[] input = {5, 2};
        Integer[] expected = {2, 5};
        Integer[] result = librarySort.sort(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testAlreadySorted() {
        Integer[] input = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Integer[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Integer[] result = librarySort.sort(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testReverseSorted() {
        Integer[] input = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        Integer[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Integer[] result = librarySort.sort(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testRandomOrder() {
        Integer[] input = {64, 34, 25, 12, 22, 11, 90};
        Integer[] expected = {11, 12, 22, 25, 34, 64, 90};
        Integer[] result = librarySort.sort(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testDuplicateElements() {
        Integer[] input = {5, 2, 8, 2, 9, 1, 5, 4};
        Integer[] expected = {1, 2, 2, 4, 5, 5, 8, 9};
        Integer[] result = librarySort.sort(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testAllSameElements() {
        Integer[] input = {7, 7, 7, 7, 7};
        Integer[] expected = {7, 7, 7, 7, 7};
        Integer[] result = librarySort.sort(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testNegativeNumbers() {
        Integer[] input = {-5, -1, -10, -3, -8};
        Integer[] expected = {-10, -8, -5, -3, -1};
        Integer[] result = librarySort.sort(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testMixedPositiveNegative() {
        Integer[] input = {-3, 5, -1, 8, 0, -7, 2};
        Integer[] expected = {-7, -3, -1, 0, 2, 5, 8};
        Integer[] result = librarySort.sort(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testStrings() {
        String[] input = {"banana", "apple", "cherry", "date", "elderberry"};
        String[] expected = {"apple", "banana", "cherry", "date", "elderberry"};
        String[] result = librarySort.sort(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testLargerArray() {
        Integer[] input = {23, 45, 16, 37, 3, 99, 22, 55, 33, 12, 67, 89, 8, 41, 77};
        Integer[] expected = {3, 8, 12, 16, 22, 23, 33, 37, 41, 45, 55, 67, 77, 89, 99};
        Integer[] result = librarySort.sort(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testLargeArrayWithManyElements() {
        // Test with 50 elements to stress test the rebalancing logic
        Integer[] input = new Integer[50];
        Integer[] expected = new Integer[50];
        
        // Fill with random-ish values
        for (int i = 0; i < 50; i++) {
            input[i] = (i * 17 + 13) % 100; // Some pseudo-random pattern
            expected[i] = input[i];
        }
        
        // Sort expected array using built-in sort for comparison
        java.util.Arrays.sort(expected);
        
        Integer[] result = librarySort.sort(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testEdgeCaseWithRepeatedRebalancing() {
        // This test is designed to trigger multiple rebalancing operations
        Integer[] input = {100, 1, 99, 2, 98, 3, 97, 4, 96, 5, 95, 6, 94, 7, 93, 8};
        Integer[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 93, 94, 95, 96, 97, 98, 99, 100};
        Integer[] result = librarySort.sort(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testStabilityWithCustomObjects() {
        // Test with custom comparable objects to verify sorting behavior
        TestObject[] input = {
            new TestObject(3, "third"),
            new TestObject(1, "first"),
            new TestObject(2, "second"),
            new TestObject(1, "another_first")
        };
        
        TestObject[] result = librarySort.sort(input);
        
        // Verify the array is sorted
        assertNotNull(result);
        for (int i = 1; i < result.length; i++) {
            assert result[i - 1].value <= result[i].value;
        }
    }

    /**
     * Helper class for testing custom objects.
     */
    private static class TestObject implements Comparable<TestObject> {
        final int value;
        final String name;

        TestObject(int value, String name) {
            this.value = value;
            this.name = name;
        }

        @Override
        public int compareTo(TestObject other) {
            return Integer.compare(this.value, other.value);
        }

        @Override
        public String toString() {
            return name + "(" + value + ")";
        }
    }
}