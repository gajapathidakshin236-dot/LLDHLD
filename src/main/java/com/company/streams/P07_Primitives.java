package com.company.streams;
import java.util.*;
import java.util.stream.*;

/**
 * SECTION F — Primitive streams & numeric aggregation (Q76-Q83)
 * Run: java com.company.streams.P07_Primitives
 */
public class P07_Primitives {

    // Q76 [why] Why do IntStream/LongStream/DoubleStream exist?
    static String q76() { return ""; }

    // Q77 [code] Sum of squares 1..100.
    static int q77() { return IntStream.rangeClosed(1, 100)
            .map(n -> n * n)
            .sum(); }

    // Q78 [code] Average of a List<Integer>. (q78why: what type does average() return, and why?)
    static double q78(List<Integer> in) { return in.stream() .mapToInt(Integer::intValue)
            .average()
            .orElse(0); }
    static String q78why() { return ""; }

    // Q79 [code] boxed(): IntStream.range(0,3) -> List<Integer>.
    static List<Integer> q79() { return IntStream.range(0,3).boxed().collect(Collectors.toList()); }

    // Q80 [code] Count vowels (a,e,i,o,u) in a String using chars().
    static long q80(String s) { return s.toLowerCase()
            .chars()
            .filter(c -> "aeiou".indexOf(c) >= 0)
            .count(); }

    // Q81 [code] Factorial of n via IntStream.rangeClosed + reduce.
    static long q81(int n) { return IntStream.rangeClosed(1, n)
            .asLongStream()
            .reduce(1, (a, b) -> a * b); }

    // Q82 [code] 5 random doubles in [0,1) via DoubleStream.generate + limit.
    static List<Double> q82() { return DoubleStream.generate(Math::random)
            .limit(5)
            .boxed()
            .collect(Collectors.toList()); }

    // Q83 [why] When is index-based iteration (IntStream over indices) actually necessary?
    static String q83() { return null;}

    public static void main(String[] args) {
        Check.say("Q76 why primitive", q76());
        Check.eq ("Q77 sum of squares", 338350, q77());
        Check.eq ("Q78 average", 2.5, q78(Arrays.asList(1, 2, 3, 4)));
        Check.say("Q78 average type", q78why());
        Check.eq ("Q79 boxed 0..2", Arrays.asList(0, 1, 2), q79());
        Check.eq ("Q80 vowels", 5L, q80("education"));
        Check.eq ("Q81 factorial 5", 120L, q81(5));
        List<Double> r = q82();
        Check.eq ("Q82 size 5", 5, r == null ? -1 : r.size());
        Check.eq ("Q82 all in [0,1)", true,
                r != null && r.size() == 5 && r.stream().allMatch(d -> d >= 0.0 && d < 1.0));
        Check.say("Q83 index iteration", q83());
        Check.summary();
    }
}
