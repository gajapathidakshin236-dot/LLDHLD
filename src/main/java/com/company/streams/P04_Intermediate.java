package com.company.streams;
import java.util.*;
import java.util.stream.*;

/**
 * SECTION C — Intermediate operations (Q21-Q40)
 * Run: java com.company.streams.P04_Intermediate
 */
public class P04_Intermediate {

    // Q21 [code] Keep only strings starting with "a" (case-insensitive).
    static List<String> q21(List<String> in) { return in.stream().filter(x->x.charAt(0)=='a').collect(Collectors.toList()); }

    // Q22 [code] Multiply each by 10.
    static List<Integer> q22(List<Integer> in) { return in.stream().map(x->x*10).collect(Collectors.toList()); }

    // Q23 [code] Parse numeric strings into Integers.
    static List<Integer> q23(List<String> in) { return in.stream()
            .filter(s -> s.chars().allMatch(Character::isDigit))
            .map(Integer::parseInt).collect(Collectors.toList()); }

    // Q24 [code] Total of the string lengths, using mapToInt.
    static int q24(List<String> in) { return in.stream()
            .mapToInt(String::length)
            .sum();}
    // Q25 [code] Flatten a List<List<Integer>> into one List<Integer>.
    static List<Integer> q25(List<List<Integer>> in) { return in.stream()
            .flatMap(List::stream)
            .collect(Collectors.toList()); }

    // Q26 [code] All words across sentences (split each on a single space).
    static List<String> q26(List<String> sentences) { return sentences.stream()
            .flatMap(s -> Arrays.stream(s.split("\\s+"))).collect(Collectors.toList()); }

    // Q27 [why] map vs flatMap, one line, with a types example.
    static String q27() { return ""; }

    // Q28 [code] Distinct elements, preserving encounter order.
    static List<Integer> q28(List<Integer> in) { return in.stream().distinct().collect(Collectors.toList()); }

    // Q29 [code] Sort strings by length ascending.
    static List<String> q29(List<String> in) { return in.stream().sorted(Comparator.comparing(String::length)).collect(Collectors.toList()); }

    // Q30 [code] Sort people by age DESCENDING, return their names.
    static List<String> q30(List<PracticeModels.Person> in) { return null; }

    // Q31 [code] First 3 elements (limit) and, separately, everything after the first 3 (skip).
    static List<Integer> q31limit(List<Integer> in) { return in.stream().limit(3).collect(Collectors.toList()); }
    static List<Integer> q31skip(List<Integer> in) { return in.stream().skip(3).collect(Collectors.toList()); }

    // Q32 [code] takeWhile < 100.
    static List<Integer> q32(List<Integer> in) { return in.stream().takeWhile(x->x<100).collect(Collectors.toList()); }

    // Q33 [code] dropWhile < 100.
    static List<Integer> q33(List<Integer> in) { return in.stream().dropWhile(x->x<100).collect(Collectors.toList()); }

    // Q34 [why] What is peek() for, and why not put business logic in it?
    static String q34() { return ""; }

    // Q35 [print] Same input [1,2,3,4]: does filter(even)->map(+1) equal map(+1)->filter(even)?
    static List<Integer> q35_filterThenMap(List<Integer> in) { return in.stream().filter(x->x%2==0).map(x->x+1).collect(Collectors.toList()); } // filter even, then +1
    static List<Integer> q35_mapThenFilter(List<Integer> in) { return in.stream()
            .map(x -> x + 1)
            .filter(x -> x % 2 == 0)
            .collect(Collectors.toList()); } // +1, then filter even

    // Q36 [code] Comma-separated string of the UPPERCASE versions. ["a","b","c"] -> "A,B,C"
    static String q36(List<String> in) { return in.stream().map(String::toUpperCase).reduce((a,b)-> a+","+b).orElse(""); }

    // Q37 [why] For distinct() on Person, what decides "same"?
    static String q37() { return ""; }

    // Q38 [code] Only the 2nd and 3rd elements (skip + limit).
    static List<Integer> q38(List<Integer> in) { return in.stream().skip(1).limit(2).collect(Collectors.toList()); }

    // Q39 [code] The 2 smallest EVEN squares from 1..10.
    static List<Integer> q39() { return IntStream.rangeClosed(1, 10)
            .map(x -> x * x)
            .filter(x -> x % 2 == 0)
            .limit(2)
            .boxed()
            .collect(Collectors.toList()); }

    // Q40 [why] Why is sorted() stateful, and what does that mean for infinite streams?
    static String q40() { return ""; }

    public static void main(String[] args) {
        Check.eq ("Q21 starts-with-a", Arrays.asList("apple", "Avocado", "apricot"),
                q21(Arrays.asList("apple", "Avocado", "banana", "Cherry", "apricot")));
        Check.eq ("Q22 times10", Arrays.asList(10, 20, 30), q22(Arrays.asList(1, 2, 3)));
        Check.eq ("Q23 parse ints", Arrays.asList(1, 2, 3), q23(Arrays.asList("1", "2", "3")));
        Check.eq ("Q24 total length", 6, q24(Arrays.asList("a", "bbb", "cc")));
        Check.eq ("Q25 flatten", Arrays.asList(1, 2, 3, 4, 5),
                q25(Arrays.asList(Arrays.asList(1, 2), Arrays.asList(3), Arrays.asList(4, 5))));
        Check.eq ("Q26 words", Arrays.asList("hello", "world", "foo", "bar"),
                q26(Arrays.asList("hello world", "foo bar")));
        Check.say("Q27 map vs flatMap", q27());
        Check.eq ("Q28 distinct order", Arrays.asList(3, 1, 2), q28(Arrays.asList(3, 1, 3, 2, 1)));
        Check.eq ("Q29 by length", Arrays.asList("a", "bb", "ccc"), q29(Arrays.asList("bb", "a", "ccc")));
        Check.eq ("Q30 age desc names", Arrays.asList("Dan", "Ann", "Cara", "Bob"),
                q30(PracticeModels.people()));
        Check.eq ("Q31 limit3", Arrays.asList(1, 2, 3), q31limit(Arrays.asList(1, 2, 3, 4, 5, 6)));
        Check.eq ("Q31 skip3", Arrays.asList(4, 5, 6), q31skip(Arrays.asList(1, 2, 3, 4, 5, 6)));
        Check.eq ("Q32 takeWhile<100", Arrays.asList(3, 50, 99), q32(Arrays.asList(3, 50, 99, 200, 4)));
        Check.eq ("Q33 dropWhile<100", Arrays.asList(200, 4), q33(Arrays.asList(3, 50, 99, 200, 4)));
        Check.say("Q34 peek", q34());
        Check.eq ("Q35 filter->map", Arrays.asList(3, 5), q35_filterThenMap(Arrays.asList(1, 2, 3, 4)));
        Check.eq ("Q35 map->filter", Arrays.asList(2, 4), q35_mapThenFilter(Arrays.asList(1, 2, 3, 4)));
        Check.eq ("Q36 csv upper", "A,B,C", q36(Arrays.asList("a", "b", "c")));
        Check.say("Q37 distinct person", q37());
        Check.eq ("Q38 2nd+3rd", Arrays.asList(20, 30), q38(Arrays.asList(10, 20, 30, 40)));
        Check.eq ("Q39 2 smallest even sq", Arrays.asList(4, 16), q39());
        Check.say("Q40 sorted stateful", q40());
        Check.summary();
    }
}
