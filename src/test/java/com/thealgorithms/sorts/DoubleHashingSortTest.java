package com.thealgorithms.sorts;

/**
 * Test class for DoubleHashingSort algorithm
 *
 * Tests the DoubleHashingSort implementation against the standard
 * SortingAlgorithmTest suite which includes edge cases for:
 * - Negative numbers
 * - Floating point numbers (including NaN and Infinity)
 * - Characters and special characters
 * - Strings (including empty strings)
 * - Custom objects
 * - Null value handling
 */
public class DoubleHashingSortTest extends SortingAlgorithmTest {

    @Override
    SortAlgorithm getSortAlgorithm() {
        return new DoubleHashingSort();
    }
}