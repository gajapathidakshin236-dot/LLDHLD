package com.company.streams;

import java.util.*;
import java.util.stream.*;

/**
 * LESSON 1 — FOUNDATIONS + STREAM INTERNALS
 *
 * Run this file and READ the printed output next to the code.
 * Every section proves one internal fact about how streams and
 * collections actually work. Don't skim — predict the output first,
 * then run and check.
 */
public class L01_Foundations {

    public static void main(String[] args) {
        section1_streamIsLazy();
        section2_terminalTriggersExecution();
        section3_elementByElement();
        section4_singleUse();
        section5_collectionOrdering();
        section6_mapOrdering();
    }

    // 1) An intermediate op (map/filter/peek) does NOTHING on its own.
    //    A Stream is a recipe, not a result. Nothing runs until a terminal op.
    static void section1_streamIsLazy() {
        System.out.println("=== 1) Streams are LAZY ===");
        Stream<Integer> pipeline = Stream.of(1, 2, 3)
                .peek(n -> System.out.println("   peek saw: " + n))   // intermediate
                .map(n -> n * 10);                                    // intermediate
        System.out.println("   built the pipeline, but no peek printed above -> nothing ran yet");
        List<Integer> result = pipeline.collect(Collectors.toList());  // terminal -> NOW it runs
        System.out.println("   after terminal, result = " + result);
        System.out.println();
    }

    // 2) The terminal op is what pulls elements through the whole chain.
    static void section2_terminalTriggersExecution() {
        System.out.println("=== 2) Terminal op triggers the work ===");
        long count = Stream.of("a", "bb", "ccc")
                .filter(s -> {
                    System.out.println("   filtering: " + s);
                    return s.length() > 1;
                })
                .count();   // terminal
        System.out.println("   count = " + count);
        System.out.println();
    }

    // 3) Processing is VERTICAL (element-by-element), not stage-by-stage.
    //    Each element flows through the entire chain before the next starts.
    //    That is why limit() can short-circuit and stop early.
    static void section3_elementByElement() {
        System.out.println("=== 3) Element-by-element + short-circuit ===");
        List<Integer> out = Stream.iterate(1, n -> n + 1) // infinite stream 1,2,3,...
                .peek(n -> System.out.println("   generated: " + n))
                .map(n -> n * n)
                .limit(3)     // short-circuits: stops after 3 pass through
                .collect(Collectors.toList());
        System.out.println("   squares = " + out + "  (note: it never generated 4,5,6...)");
        System.out.println();
    }

    // 4) A stream can be consumed ONCE. Reusing it throws IllegalStateException.
    static void section4_singleUse() {
        System.out.println("=== 4) Streams are single-use ===");
        Stream<Integer> s = Stream.of(1, 2, 3);
        System.out.println("   first terminal, sum = " + s.mapToInt(i -> i).sum());
        try {
            s.count(); // reuse -> boom
        } catch (IllegalStateException e) {
            System.out.println("   reusing threw: " + e.getMessage());
        }
        System.out.println();
    }

    // 5) Set implementations differ ONLY by iteration order + cost. Same API.
    static void section5_collectionOrdering() {
        System.out.println("=== 5) Set choice = ordering + cost ===");
        List<String> in = Arrays.asList("banana", "apple", "cherry", "apple", "banana");
        System.out.println("   HashSet       (no order):       " + new HashSet<>(in));
        System.out.println("   LinkedHashSet (insertion order):" + new LinkedHashSet<>(in));
        System.out.println("   TreeSet       (sorted order):   " + new TreeSet<>(in));
        System.out.println();
    }

    // 6) Same idea for Map: HashMap vs LinkedHashMap vs TreeMap.
    static void section6_mapOrdering() {
        System.out.println("=== 6) Map choice = ordering + cost ===");
        String[] keys = {"banana", "apple", "cherry"};
        Map<String, Integer> hash = new HashMap<>();
        Map<String, Integer> linked = new LinkedHashMap<>();
        Map<String, Integer> tree = new TreeMap<>();
        for (int i = 0; i < keys.length; i++) {
            hash.put(keys[i], i);
            linked.put(keys[i], i);
            tree.put(keys[i], i);
        }
        System.out.println("   HashMap       (no order):        " + hash);
        System.out.println("   LinkedHashMap (insertion order): " + linked);
        System.out.println("   TreeMap       (sorted by key):   " + tree);
        System.out.println();
    }
}
