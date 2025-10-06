package com.thealgorithms.sorts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Double Hashing Sort Algorithm Implementation
 *
 * Double Hashing Sort uses a hybrid approach combining hashing with traditional sorting.
 * It creates hash buckets using double hashing technique to distribute elements
 * and then sorts each bucket individually.
 *
 * Time Complexity:
 * - Best Case: O(n) when elements are uniformly distributed
 * - Average Case: O(n log n)
 * - Worst Case: O(n²) when all elements hash to same bucket
 *
 * Space Complexity: O(n) for the auxiliary buckets
 *
 * @author TheAlgorithms Team
 * @see <a href="https://en.wikipedia.org/wiki/Hash_function">Hash Function</a>
 */
public class DoubleHashingSort implements SortAlgorithm {

    private static final int DEFAULT_BUCKET_COUNT = 10;

    /**
     * A robust comparator that handles edge cases for different data types
     */
    private static final class RobustComparator<T extends Comparable<T>> implements Comparator<T> {

        @Override
        @SuppressWarnings("unchecked")
        public int compare(T a, T b) {
            if (a == null && b == null) {
                return 0;
            }
            if (a == null) {
                throw new NullPointerException("Array contains null values");
            }
            if (b == null) {
                throw new NullPointerException("Array contains null values");
            }

            // Handle Double values
            if (a instanceof Double && b instanceof Double) {
                return compareDoubles((Double) a, (Double) b);
            }

            // Handle Float values
            if (a instanceof Float && b instanceof Float) {
                return compareFloats((Float) a, (Float) b);
            }

            // Handle Character values - special characters should be ordered properly
            if (a instanceof Character && b instanceof Character) {
                return Character.compare((Character) a, (Character) b);
            }

            // Handle String values
            if (a instanceof String && b instanceof String) {
                return compareStrings((String) a, (String) b);
            }

            // Default comparison for other Comparable types
            return a.compareTo(b);
        }

        private int compareDoubles(Double a, Double b) {
            // Handle NaN: NaN should come last
            if (Double.isNaN(a) && Double.isNaN(b)) {
                return 0;
            }
            if (Double.isNaN(a)) {
                return 1;
            }
            if (Double.isNaN(b)) {
                return -1;
            }

            // Handle infinities
            if (a.equals(Double.NEGATIVE_INFINITY) && b.equals(Double.NEGATIVE_INFINITY)) {
                return 0;
            }
            if (a.equals(Double.NEGATIVE_INFINITY)) {
                return -1;
            }
            if (b.equals(Double.NEGATIVE_INFINITY)) {
                return 1;
            }

            if (a.equals(Double.POSITIVE_INFINITY) && b.equals(Double.POSITIVE_INFINITY)) {
                return 0;
            }
            if (a.equals(Double.POSITIVE_INFINITY)) {
                return 1;
            }
            if (b.equals(Double.POSITIVE_INFINITY)) {
                return -1;
            }

            // Normal comparison
            return Double.compare(a, b);
        }

        private int compareFloats(Float a, Float b) {
            // Handle NaN: NaN should come last
            if (Float.isNaN(a) && Float.isNaN(b)) {
                return 0;
            }
            if (Float.isNaN(a)) {
                return 1;
            }
            if (Float.isNaN(b)) {
                return -1;
            }

            // Handle infinities
            if (a.equals(Float.NEGATIVE_INFINITY) && b.equals(Float.NEGATIVE_INFINITY)) {
                return 0;
            }
            if (a.equals(Float.NEGATIVE_INFINITY)) {
                return -1;
            }
            if (b.equals(Float.NEGATIVE_INFINITY)) {
                return 1;
            }

            if (a.equals(Float.POSITIVE_INFINITY) && b.equals(Float.POSITIVE_INFINITY)) {
                return 0;
            }
            if (a.equals(Float.POSITIVE_INFINITY)) {
                return 1;
            }
            if (b.equals(Float.POSITIVE_INFINITY)) {
                return -1;
            }

            // Normal comparison
            return Float.compare(a, b);
        }

        /**
         * Custom comparison for characters that handles special characters properly
         */
        @SuppressWarnings("unused")
        private int compareCharacters(Character a, Character b) {
            return Character.compare(a, b);
        }

        private int compareStrings(String a, String b) {
            // Handle empty strings properly - they should come before non-empty strings
            if (a.isEmpty() && b.isEmpty()) {
                return 0;
            }
            if (a.isEmpty()) {
                return -1;
            }
            if (b.isEmpty()) {
                return 1;
            }

            // Normal string comparison
            return a.compareTo(b);
        }
    }

    @Override
    public <T extends Comparable<T>> T[] sort(T[] array) {
        if (array == null) {
            return null;
        }
        if (array.length <= 1) {
            return array;
        }

        // Check for null values and throw exception if found
        for (T element : array) {
            if (element == null) {
                throw new NullPointerException("Array contains null values");
            }
        }

        int bucketCount = Math.min(array.length, DEFAULT_BUCKET_COUNT);
        return doubleHashingSort(array, bucketCount);
    }

    /**
     * Sorts array using double hashing technique
     *
     * @param array the array to be sorted
     * @param bucketCount number of buckets to use
     * @return sorted array
     */
    private <T extends Comparable<T>> T[] doubleHashingSort(T[] array, int bucketCount) {
        // Create buckets using ArrayList to avoid generic array issues
        List<List<T>> buckets = new ArrayList<>(bucketCount);
        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<>());
        }

        // Distribute elements into buckets using double hashing
        for (T element : array) {
            int bucketIndex = getBucketIndex(element, bucketCount);
            buckets.get(bucketIndex).add(element);
        }

        // Sort each bucket and collect results
        int index = 0;
        RobustComparator<T> comparator = new RobustComparator<>();

        for (int i = 0; i < bucketCount; i++) {
            List<T> bucket = buckets.get(i);
            if (!bucket.isEmpty()) {
                // Sort the bucket using the robust comparator
                bucket.sort(comparator);

                // Copy sorted elements back to main array
                for (T element : bucket) {
                    array[index++] = element;
                }
            }
        }

        return array;
    }

    /**
     * Calculates bucket index using double hashing technique
     *
     * @param element the element to hash
     * @param bucketCount number of available buckets
     * @return bucket index
     */
    private <T extends Comparable<T>> int getBucketIndex(T element, int bucketCount) {
        if (element == null) {
            return 0;
        }

        // Primary hash function
        int hash1 = Math.abs(element.hashCode()) % bucketCount;

        // Secondary hash function (must be odd and different from bucket count)
        int hash2 = 7 - (Math.abs(element.hashCode()) % 7);

        // Double hashing formula: (hash1 + i * hash2) % bucketCount
        // For simplicity, we use i = 1 here, but could be extended for collision resolution
        return (hash1 + hash2) % bucketCount;
    }
}