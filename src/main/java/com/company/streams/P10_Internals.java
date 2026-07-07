package com.company.streams;
import java.util.*;
import java.util.stream.*;

/**
 * SECTION I — Internals, pitfalls & tricky output (Q97-Q100)
 * Run: java com.company.streams.P10_Internals
 */
public class P10_Internals {

    // Q97 [code] Stream.iterate(1, n->n+1).map(square).limit(3) -> the squares.
    //     (q97note: why doesn't it run forever even though iterate is infinite?)
    static List<Integer> q97() { return null; }
    static String q97note() {  return Stream.iterate(1, n -> n + 1)
            .map(n -> n * n)
            .limit(3)
            .collect(Collectors.toList()); }

    // Q98 [why] Reusing one Stream for two pipelines -> which exception, and the fix?
    static String q98() { return ""; }

    // Q99 [why] Removing from a list inside a for-each -> which exception, why (fail-fast), two fixes?
    static String q99() { return ""; }

    // Q100 [why] Why are side effects in map()/filter() dangerous under laziness/parallelism?
    //      State the "side-effect-free" principle + the one legit place for a side effect.
    static String q100() { return ""; }

    public static void main(String[] args) {
        Check.eq ("Q97 squares", Arrays.asList(1, 4, 9), q97());
        Check.say("Q97 not forever", q97note());
        Check.say("Q98 reuse", q98());
        Check.say("Q99 CME", q99());
        Check.say("Q100 side effects", q100());
        Check.summary();
    }
}
