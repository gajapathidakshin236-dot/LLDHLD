package com.company.streams;
import java.util.*;

/**
 * SECTION A — Collections: choice & foundations (Q1-Q12)
 * [why] questions: fill the returned String with your answer (reviewed, not auto-graded).
 * [code] questions: implement so the harness prints PASS.
 * Run: java com.company.streams.P02_Collections
 */
public class P02_Collections {

    // Q1 [why] Three sub-interfaces of Collection + the one that is NOT a Collection, and why it's separate.
    static String q1() { return ""; }

    // Q2 [why] Fast random access by index + mostly append: which List, and why?
    static String q2() { return ""; }

    // Q3 [why] Preserve insertion order AND forbid duplicates: which Set?
    static String q3() { return ""; }

    // Q4 [why] Keys kept sorted + range queries (keys >= "m"): which Map, and which method?
    static String q4() { return ""; }

    // Q5 [why] ArrayList vs LinkedList Big-O for get(i), add-at-end, remove-from-middle.
    static String q5() { return ""; }

    // Q6 [why] Why ArrayDeque over Stack and over LinkedList for stack/queue use?
    static String q6() { return ""; }

    // Q7 [why] What does "HashMap iteration order is unspecified" mean; can you rely on it?
    static String q7() { return ""; }

    // Q8 [why] equals() true but hashCode() different -> what breaks in a HashSet?
    static String q8() { return ""; }

    // Q9 [code] Return an UNMODIFIABLE List of exactly these three colors, in order.
    //           (Also predict in q9note: what does calling add() on it throw?)
    static List<String> q9() {
        return null; // TODO: List.of(...) or Collections.unmodifiableList(...)
    }
    static String q9note() { return ""; } // your prediction for add()

    // Q10 [why] Mutability/resizing: Arrays.asList("a","b") vs new ArrayList<>(List.of("a","b")).
    static String q10() { return ""; }

    // Q11 [why] When choose TreeMap over HashMap despite O(log n)?
    static String q11() { return ""; }

    // Q12 [why] What is a PriorityQueue + one algorithm where it's the natural choice.
    static String q12() { return ""; }

    public static void main(String[] args) {
        Check.say("Q1 collection roots", q1());
        Check.say("Q2 index+append list", q2());
        Check.say("Q3 ordered no-dup set", q3());
        Check.say("Q4 sorted map + method", q4());
        Check.say("Q5 arraylist vs linked", q5());
        Check.say("Q6 arraydeque", q6());
        Check.say("Q7 hashmap order", q7());
        Check.say("Q8 equals/hashcode", q8());
        Check.eq ("Q9 unmodifiable list", Arrays.asList("Red", "Green", "Blue"), q9());
        Check.say("Q9 add() throws?", q9note());
        Check.say("Q10 asList vs new AL", q10());
        Check.say("Q11 treemap vs hashmap", q11());
        Check.say("Q12 priorityqueue", q12());
        Check.summary();
    }
}
