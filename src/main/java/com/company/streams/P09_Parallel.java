package com.company.streams;
import java.util.*;
import java.util.stream.*;

/**
 * SECTION H — Parallel streams (Q90-Q96)
 * Run: java com.company.streams.P09_Parallel
 */
public class P09_Parallel {

    // Q90 [why] What is a parallel stream; two ways to create one?
    static String q90() { return ""; }

    // Q91 [why] Which thread pool by default, and why is that risky in a web server?
    static String q91() { return ""; }

    // Q92 [why] Properties a reduction needs to be correct in parallel.
    static String q92() { return ""; }

    // Q93 [why] Why is parallel forEach unordered, and what restores order?
    static String q93() { return ""; }

    // Q94 [code] Sum 1..1000 with a PARALLEL stream, correctly (no shared mutable state).
    //     (q94why: describe the race if you .forEach(list::add) into a plain ArrayList instead.)
    static int q94() {  return IntStream.rangeClosed(1, 1000)
            .parallel()
            .sum(); }
    static String q94why() { return ""; }

    // Q95 [why] When does parallel help vs hurt?
    static String q95() { return ""; }

    // Q96 [why] Are toMap/groupingBy safe in parallel? What are groupingByConcurrent + UNORDERED?
    static String q96() { return ""; }

    public static void main(String[] args) {
        Check.say("Q90 what/how", q90());
        Check.say("Q91 common pool", q91());
        Check.say("Q92 reduction props", q92());
        Check.say("Q93 ordering", q93());
        Check.eq ("Q94 parallel sum", 500500, q94());
        Check.say("Q94 the race", q94why());
        Check.say("Q95 help vs hurt", q95());
        Check.say("Q96 concurrent collectors", q96());
        Check.summary();
    }
}
