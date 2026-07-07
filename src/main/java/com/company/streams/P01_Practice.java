package com.company.streams;

import java.util.*;
import java.util.stream.*;

/**
 * LESSON 1 — PRACTICE (EASY)
 * Fill in each method body, run this file, turn PASS/FAIL green.
 * Rule: use STREAMS (no manual for-loops) unless a question says otherwise.
 */
public class P01_Practice {

    // Q1. Return only the EVEN numbers, in the same order.  [1,2,3,4,5,6] -> [2,4,6]
    static List<Integer> evens(List<Integer> in) {
        return in.stream().filter(x->x%2==0).collect(Collectors.toList());
    }

    // Q2. Return each number SQUARED, same order.  [1,2,3] -> [1,4,9]
    static List<Integer> squares(List<Integer> in) {
        return in.stream().map(x->x*x).collect(Collectors.toList());
    }

    // Q3. Return the names UPPERCASED.  ["ann","bob"] -> ["ANN","BOB"]
    static List<String> upper(List<String> in) {
        return in.stream().map(String::toUpperCase).collect(Collectors.toList());
    }

    // Q4. Return DISTINCT values, SORTED ascending.  [5,3,5,1,3] -> [1,3,5]
    static List<Integer> distinctSorted(List<Integer> in) {
        return in.stream().distinct().sorted().collect(Collectors.toList());
    }

    // Q5. Return the SUM of all the string lengths.  ["a","bbb","cc"] -> 6
    static int totalLength(List<String> in) {
        return in.stream().mapToInt(String::length).reduce(0, Integer::sum);
    }

    // Q6. FIRST string longer than 3 chars, or "NONE".  ["hi","yes","hello","world"] -> "hello"
    static String firstLongerThan3(List<String> in) {
        return in.stream().filter(x->x.length()>3).findFirst().orElse("NONE");
    }

    // ---------------- Test harness — do not edit below ----------------
    public static void main(String[] args) {
        int pass = 0, total = 0;
        total++; pass += check("Q1 evens", Arrays.asList(2, 4, 6), evens(Arrays.asList(1, 2, 3, 4, 5, 6)));
        total++; pass += check("Q2 squares", Arrays.asList(1, 4, 9), squares(Arrays.asList(1, 2, 3)));
        total++; pass += check("Q3 upper", Arrays.asList("ANN", "BOB"), upper(Arrays.asList("ann", "bob")));
        total++; pass += check("Q4 distinctSorted", Arrays.asList(1, 3, 5), distinctSorted(Arrays.asList(5, 3, 5, 1, 3)));
        total++; pass += check("Q5 totalLength", 6, totalLength(Arrays.asList("a", "bbb", "cc")));
        total++; pass += check("Q6 firstLongerThan3", "hello", firstLongerThan3(Arrays.asList("hi", "yes", "hello", "world")));
        System.out.println("--------------------------------------");
        System.out.println("SCORE: " + pass + " / " + total + (pass == total ? "  ALL GREEN" : "  keep going"));
    }

    static int check(String name, Object expected, Object actual) {
        boolean ok = Objects.equals(expected, actual);
        System.out.printf("[%s] %-20s expected=%s  actual=%s%n", ok ? "PASS" : "FAIL", name, expected, actual);
        return ok ? 1 : 0;
    }
}
