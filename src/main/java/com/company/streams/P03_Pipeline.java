package com.company.streams;
import java.util.*;
import java.util.stream.*;

/**
 * SECTION B — Streams: creation & the pipeline model (Q13-Q20)
 * Run: java com.company.streams.P03_Pipeline
 */
public class P03_Pipeline {

    // Q13 [why] One sentence each: intermediate op vs terminal op.
    static String q13() { return ""; }

    // Q14 [code] Return the numbers 1..5 as a List<Integer> (build it with a stream).
    static List<Integer> q14() { return IntStream.rangeClosed(1,5).boxed().collect(Collectors.toList()); }

    // Q15 [print] Stream.of(1,2,3).peek(add-to-list) with NO terminal op.
    //     Return your PREDICTION of what ends up in the list (harness runs the real thing).
    static List<Integer> q15Prediction() { return null; }
    static List<Integer> q15Real() {
        List<Integer> seen = new ArrayList<>();
        Stream.of(1, 2, 3).peek(seen::add); // no terminal on purpose
        return seen;
    }

    // Q16 [why] Two terminal ops on the same Stream reference -> which exception?
    static String q16() { return ""; }

    // Q17 [code] Return 0..9 inclusive built with IntStream.rangeClosed.
    static List<Integer> q17() {  return IntStream.rangeClosed(0,9).boxed().collect(Collectors.toList()); }

    // Q18 [why] Stream.iterate vs Stream.generate, with a use case for each.
    static String q18() { return ""; }

    // Q19 [why] Why pair an infinite stream with a short-circuiting op? Name two.
    static String q19() { return ""; }

    // Q20 [print] Order of lines for .map(print "map x") then .forEach(print "each x") over ["a","b"].
    static List<String> q20Prediction() { return null; }
    static List<String> q20Real() {
        List<String> log = new ArrayList<>();
        Stream.of("a", "b")
              .map(s -> { log.add("map " + s); return s; })
              .forEach(s -> log.add("each " + s));
        return log;
    }

    public static void main(String[] args) {
        Check.say("Q13 intermediate/terminal", q13());
        Check.eq ("Q14 1..5", Arrays.asList(1, 2, 3, 4, 5), q14());
        Check.eq ("Q15 laziness prediction", q15Real(), q15Prediction());
        Check.say("Q16 reuse exception", q16());
        Check.eq ("Q17 0..9", Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), q17());
        Check.say("Q18 iterate/generate", q18());
        Check.say("Q19 short-circuit", q19());
        Check.eq ("Q20 order prediction", q20Real(), q20Prediction());
        Check.summary();
    }
}
