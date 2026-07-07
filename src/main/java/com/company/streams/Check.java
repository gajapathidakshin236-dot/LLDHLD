package com.company.streams;
import java.util.*;

/** Shared test harness for the practice files. */
public class Check {
    static int pass = 0, total = 0, review = 0;

    /** Auto-graded: compares your answer to the expected value. */
    static void eq(String name, Object expected, Object actual) {
        total++;
        boolean ok = Objects.equals(expected, actual);
        if (ok) pass++;
        System.out.printf("[%s] %-26s expected=%s  actual=%s%n",
                ok ? "PASS" : "FAIL", name, expected, actual);
    }

    /** Theory answer: printed for review, not auto-graded. */
    static void say(String name, String answer) {
        review++;
        System.out.printf("[SELF] %-26s %s%n", name,
                (answer == null || answer.isEmpty()) ? "(blank - write your answer)" : answer);
    }

    static void summary() {
        System.out.println("------------------------------------------------------------");
        System.out.println("AUTO-GRADED: " + pass + "/" + total
                + "   |   THEORY answers to review: " + review);
        pass = 0; total = 0; review = 0;
    }
}
