package com.company.DSA.srivatsanDSA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 40
 *  Problem: Top K Frequent Elements
 *
 *  APPROACH (from notes):
 *    1. Count frequencies into HashMap.
 *    2. Bucket sort by frequency — bucket[f] = list of values with that frequency.
 *       Max possible frequency is nums.length, so allocate nums.length + 1 buckets.
 *    3. Walk buckets top-down picking values until we have k.
 *  Alt: PriorityQueue<int[]>{freq desc} also works (O(n log k)).
 * ============================================================ */
public class TopKFrequent {

    public int[] topKFrequent(final int[] numbers, final int kRequested) {
        final Map<Integer, Integer> frequencyByValue = countFrequencies(numbers);

        @SuppressWarnings("unchecked")
        final List<Integer>[] valuesPerFrequencyBucket = new List[numbers.length + 1];

        for (final Map.Entry<Integer, Integer> entry : frequencyByValue.entrySet()) {
            final int frequency = entry.getValue();
            if (valuesPerFrequencyBucket[frequency] == null) {
                valuesPerFrequencyBucket[frequency] = new ArrayList<>();
            }
            valuesPerFrequencyBucket[frequency].add(entry.getKey());
        }

        final int[] result = new int[kRequested];
        int writeCursor = 0;

        for (int frequency = valuesPerFrequencyBucket.length - 1; frequency >= 1 && writeCursor < kRequested; frequency--) {
            if (valuesPerFrequencyBucket[frequency] == null) {
                continue;
            }
            for (final int value : valuesPerFrequencyBucket[frequency]) {
                if (writeCursor == kRequested) {
                    break;
                }
                result[writeCursor++] = value;
            }
        }
        return result;
    }

    private Map<Integer, Integer> countFrequencies(final int[] numbers) {
        final Map<Integer, Integer> frequencyByValue = new HashMap<>();
        for (final int value : numbers) {
            frequencyByValue.merge(value, 1, Integer::sum);
        }
        return frequencyByValue;
    }
}
