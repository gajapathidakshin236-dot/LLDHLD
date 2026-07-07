package com.company.streams;
import java.util.*;
import java.util.stream.*;

/**
 * SECTION D — Terminal ops & Optional (Q41-Q55)
 * Run: java com.company.streams.P05_Terminal
 */
public class P05_Terminal {

    // Q41 [code] Count strings with length > 4.
    static long q41(List<String> in) { return in.stream().filter(x->x.length()>4).count(); }

    // Q42 [code] Return the max integer. (q42why: what type does Stream.max return, and why?)
    static int q42(List<Integer> in) { return in.stream().max(Comparator.comparing(Integer::intValue)).orElse(-1); }
    static String q42why() { return ""; }

    // Q43 [code] The min String by length.
    static String q43(List<String> in) { return Integer.toString(in.stream().min(Comparator.comparing(String::length)).orElse("").length()); }

    // Q44 [code] Sum three ways (all must equal 6 for [1,2,3]).
    static int q44_mapToIntSum(List<Integer> in) { return in.stream().mapToInt(Integer::intValue).sum(); }
    static int q44_reduce(List<Integer> in) { return in.stream().reduce(0, Integer::sum); }
    static int q44_summingInt(List<Integer> in) {  return in.stream()
            .collect(Collectors.summingInt(Integer::intValue)) ; }

    // Q45 [code] Product via reduce (identity 1).
    static int q45(List<Integer> in) { return in.stream().reduce(1,(a,b)->a*b); }

    // Q46 [why] The three reduce overloads; when is the combiner required?
    static String q46() { return ""; }

    // Q47 [code] anyMatch / allMatch / noneMatch for "is even".
    static boolean q47any(List<Integer> in) {   return in.stream().anyMatch(n -> n % 2 == 0); }
    static boolean q47all(List<Integer> in) {  return in.stream().anyMatch(n -> n % 2 == 0); }
    static boolean q47none(List<Integer> in) { return in.stream().noneMatch(n -> n % 2 == 0); }

    // Q48 [why] findFirst vs findAny.
    static String q48() { return ""; }

    // Q49 [why] What is Optional and what problem does it solve?
    static String q49() { return ""; }

    // Q50 [code] Optional<String> -> uppercased, or "NONE" if empty (map + orElse).
    static String q50(Optional<String> in) { return in.map(String::toUpperCase)
            .orElse("NONE"); }

    // Q51 [why] orElse vs orElseGet vs orElseThrow; when is orElseGet better?
    static String q51() { return ""; }

    // Q52 [why] Why is optional.get() without a check a smell?
    static String q52() { return ""; }

    // Q53 [code] Stream<Integer> -> int[].
    static int[] q53(List<Integer> in) { return in.stream().mapToInt(Integer::intValue).toArray(); }

    // Q54 [why] What can count() short-circuit to on a SIZED source with no filter?
    static String q54() { return ""; }

    // Q55 [code] People names joined by "; ".
    static String q55(List<PracticeModels.Person> in) { return in.stream().map(PracticeModels.Person::getName).collect(Collectors.joining("; ")); }

    public static void main(String[] args) {
        Check.eq ("Q41 count len>4", 2L, q41(Arrays.asList("hi", "hello", "hey", "world!")));
        Check.eq ("Q42 max", 9, q42(Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6)));
        Check.say("Q42 max return type", q42why());
        Check.eq ("Q43 min by length", "a", q43(Arrays.asList("ccc", "a", "bb")));
        Check.eq ("Q44 mapToInt.sum", 6, q44_mapToIntSum(Arrays.asList(1, 2, 3)));
        Check.eq ("Q44 reduce", 6, q44_reduce(Arrays.asList(1, 2, 3)));
        Check.eq ("Q44 summingInt", 6, q44_summingInt(Arrays.asList(1, 2, 3)));
        Check.eq ("Q45 product", 24, q45(Arrays.asList(1, 2, 3, 4)));
        Check.say("Q46 reduce overloads", q46());
        Check.eq ("Q47 anyMatch", true, q47any(Arrays.asList(2, 4, 6)));
        Check.eq ("Q47 allMatch", true, q47all(Arrays.asList(2, 4, 6)));
        Check.eq ("Q47 noneMatch", false, q47none(Arrays.asList(2, 4, 6)));
        Check.say("Q48 findFirst/findAny", q48());
        Check.say("Q49 optional", q49());
        Check.eq ("Q50 opt present", "HI", q50(Optional.of("hi")));
        Check.eq ("Q50 opt empty", "NONE", q50(Optional.empty()));
        Check.say("Q51 orElse variants", q51());
        Check.say("Q52 get() smell", q52());
        Check.eq ("Q53 toArray", Arrays.toString(new int[]{1, 2, 3}), Arrays.toString(q53(Arrays.asList(1, 2, 3))));
        Check.say("Q54 count short-circuit", q54());
        Check.eq ("Q55 join names", "Ann; Bob; Cara; Dan", q55(PracticeModels.people()));
        Check.summary();
    }
}
