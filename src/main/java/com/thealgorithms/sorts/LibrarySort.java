package com.thealgorithms.sorts;

import java.util.Arrays;

/**
 * Implementation of Library Sort (also known as Gapped Insertion Sort).
 * 
 * Library Sort is a variant of insertion sort that uses gaps in the array
 * to reduce the number of shifts required. It first inserts elements into
 * a temporary array with gaps, then performs a final pass to compact the
 * elements into the original array.
 * 
 * <p>
 * Time Complexity:
 * - Best case: O(n) when the array is already sorted
 * - Average case: O(n log n) due to reduced number of shifts
 * - Worst case: O(n²) in pathological cases
 * 
 * Space Complexity: O(n) for the temporary array with gaps
 * 
 * <p>
 * The algorithm works by:
 * 1. Creating a larger array with gaps between positions
 * 2. Inserting elements into their appropriate positions with binary search
 * 3. When gaps run out, rebalancing the array to create more gaps
 * 4. Finally, compacting the result back to the original array
 * 
 * @author Paulo López Núñez
 * @see SortAlgorithm
 * @see InsertionSort
 */
public class LibrarySort implements SortAlgorithm {

    private static final double GAP_FACTOR = 2.0; // Factor to determine initial gaps

    /**
     * Sorts the given array using the Library Sort algorithm.
     * 
     * @param <T> the type of elements in the array, must be Comparable
     * @param array the array to be sorted
     * @return the sorted array
     */
    @Override
    public <T extends Comparable<T>> T[] sort(T[] array) {
        if (array == null || array.length <= 1) {
            return array;
        }

        int n = array.length;
        int gappedSize = (int) (n * GAP_FACTOR);
        
        // Create temporary array with gaps
        @SuppressWarnings("unchecked")
        T[] gappedArray = (T[]) new Comparable[gappedSize];
        boolean[] occupied = new boolean[gappedSize];
        
        // Insert first element
        int firstPos = gappedSize / 2;
        gappedArray[firstPos] = array[0];
        occupied[firstPos] = true;
        int numInserted = 1;
        
        // Insert remaining elements
        for (int i = 1; i < n; i++) {
            T element = array[i];
            
            // Find insertion position using binary search on occupied positions
            int insertPos = findInsertionPosition(gappedArray, occupied, element, gappedSize);
            
            if (insertPos != -1) {
                // Insert at found position
                gappedArray[insertPos] = element;
                occupied[insertPos] = true;
                numInserted++;
            } else {
                // No gaps available, rebalance
                rebalance(gappedArray, occupied, gappedSize, numInserted);
                
                // Try insertion again after rebalancing
                insertPos = findInsertionPosition(gappedArray, occupied, element, gappedSize);
                if (insertPos != -1) {
                    gappedArray[insertPos] = element;
                    occupied[insertPos] = true;
                    numInserted++;
                } else {
                    // Fallback: use insertion sort for remaining elements
                    return fallbackInsertionSort(array, i);
                }
            }
        }
        
        // Compact back to original array
        compactArray(gappedArray, occupied, array, gappedSize);
        return array;
    }

    /**
     * Finds an appropriate insertion position for the element.
     * 
     * @param gappedArray the array with gaps
     * @param occupied boolean array indicating occupied positions
     * @param element the element to insert
     * @param size the size of the gapped array
     * @return the insertion position, or -1 if no suitable position found
     */
    private <T extends Comparable<T>> int findInsertionPosition(T[] gappedArray, boolean[] occupied, T element, int size) {
        // Find the range where the element should be inserted
        int left = 0, right = size - 1;
        
        // Binary search for the correct position
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (!occupied[mid]) {
                // Check if this gap is suitable
                if (isValidPosition(gappedArray, occupied, element, mid, size)) {
                    return mid;
                }
                // Continue searching
                if (shouldGoLeft(gappedArray, occupied, element, mid, size)) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                // Position is occupied, compare with existing element
                int cmp = element.compareTo(gappedArray[mid]);
                if (cmp < 0) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
        }
        
        // Linear search for nearby gaps if binary search didn't find one
        return findNearbyGap(gappedArray, occupied, element, left, size);
    }

    /**
     * Checks if a position is valid for inserting the element.
     */
    private <T extends Comparable<T>> boolean isValidPosition(T[] gappedArray, boolean[] occupied, T element, int pos, int size) {
        // Check left neighbor
        for (int i = pos - 1; i >= 0; i--) {
            if (occupied[i]) {
                if (element.compareTo(gappedArray[i]) < 0) {
                    return false;
                }
                break;
            }
        }
        
        // Check right neighbor
        for (int i = pos + 1; i < size; i++) {
            if (occupied[i]) {
                if (element.compareTo(gappedArray[i]) > 0) {
                    return false;
                }
                break;
            }
        }
        
        return true;
    }

    /**
     * Determines search direction during binary search.
     */
    private <T extends Comparable<T>> boolean shouldGoLeft(T[] gappedArray, boolean[] occupied, T element, int pos, int size) {
        // Look for the nearest occupied position to the right
        for (int i = pos + 1; i < size; i++) {
            if (occupied[i]) {
                return element.compareTo(gappedArray[i]) < 0;
            }
        }
        return false; // No occupied position found to the right, go right
    }

    /**
     * Finds a gap near the specified position.
     */
    private <T extends Comparable<T>> int findNearbyGap(T[] gappedArray, boolean[] occupied, T element, int center, int size) {
        // Ensure center is within bounds
        center = Math.max(0, Math.min(center, size - 1));
        
        for (int offset = 0; offset < size; offset++) {
            // Check position to the right
            int rightPos = center + offset;
            if (rightPos < size && !occupied[rightPos] && isValidPosition(gappedArray, occupied, element, rightPos, size)) {
                return rightPos;
            }
            
            // Check position to the left
            int leftPos = center - offset;
            if (leftPos >= 0 && !occupied[leftPos] && isValidPosition(gappedArray, occupied, element, leftPos, size)) {
                return leftPos;
            }
        }
        return -1; // No suitable gap found
    }

    /**
     * Rebalances the gapped array to create more gaps.
     */
    private <T extends Comparable<T>> void rebalance(T[] gappedArray, boolean[] occupied, int size, int numElements) {
        // Extract all elements
        @SuppressWarnings("unchecked")
        T[] elements = (T[]) new Comparable[numElements];
        int elementIndex = 0;
        
        for (int i = 0; i < size; i++) {
            if (occupied[i]) {
                elements[elementIndex++] = gappedArray[i];
                gappedArray[i] = null;
                occupied[i] = false;
            }
        }
        
        // Redistribute elements with gaps
        if (numElements == 0) return;
        
        int gap = size / (numElements + 1);
        if (gap < 1) gap = 1;
        
        for (int i = 0; i < numElements; i++) {
            int newPos = (i + 1) * gap;
            if (newPos >= size) {
                // Distribute remaining elements at the end
                newPos = size - numElements + i;
                if (newPos < 0) newPos = i;
            }
            
            // Ensure we don't go out of bounds
            newPos = Math.max(0, Math.min(newPos, size - 1));
            
            gappedArray[newPos] = elements[i];
            occupied[newPos] = true;
        }
    }

    /**
     * Compacts the gapped array back to the original array.
     */
    private <T extends Comparable<T>> void compactArray(T[] gappedArray, boolean[] occupied, T[] result, int size) {
        int resultIndex = 0;
        for (int i = 0; i < size; i++) {
            if (occupied[i]) {
                result[resultIndex++] = gappedArray[i];
            }
        }
    }

    /**
     * Fallback to regular insertion sort when gaps are exhausted.
     */
    private <T extends Comparable<T>> T[] fallbackInsertionSort(T[] array, int startIndex) {
        for (int i = startIndex; i < array.length; i++) {
            T key = array[i];
            int j = i - 1;
            
            while (j >= 0 && SortUtils.greater(array[j], key)) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
        return array;
    }
}