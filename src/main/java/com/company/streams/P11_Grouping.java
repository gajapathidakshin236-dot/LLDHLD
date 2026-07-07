package com.company.streams;
import java.util.*;
import java.util.stream.*;

/**
 * LESSON 4 — GROUPING & toMap OVER OBJECTS (Q1-Q30, easy -> complex)
 *
 * Fill in each stub using streams + Collectors. The harness grades your answer
 * against a correct reference (SolutionsP11). Run SolutionsP11 to see worked
 * answers -- but only AFTER you attempt.
 *
 * Data: 8 employees (see data()). Fields: name, dept, title, city, salary, age, skills.
 * Run: java com.company.streams.P11_Grouping
 */
public class P11_Grouping {

    public static class Emp {
        public final String name, dept, title, city;
        public final int salary, age;
        public final List<String> skills;
        public Emp(String name, String dept, int salary, String title, int age, String city, List<String> skills) {
            this.name = name; this.dept = dept; this.salary = salary; this.title = title;
            this.age = age; this.city = city; this.skills = skills;
        }
        public String getName() { return name; }  public String getDept() { return dept; }
        public String getTitle() { return title; } public String getCity() { return city; }
        public int getSalary() { return salary; }  public int getAge() { return age; }
        public List<String> getSkills() { return skills; }
        public String toString() { return name; }
        public boolean equals(Object o) { return (o instanceof Emp) && name.equals(((Emp) o).name); }
        public int hashCode() { return name.hashCode(); }
    }

    public static List<Emp> data() {
        return Arrays.asList(
            new Emp("Ann",  "Eng",   120, "Senior", 34, "Paris",  Arrays.asList("Java", "SQL")),
            new Emp("Bob",  "Eng",    90, "Junior", 25, "Paris",  Arrays.asList("Java", "Python")),
            new Emp("Cara", "Eng",   150, "Lead",   41, "Berlin", Arrays.asList("Java", "Go", "SQL")),
            new Emp("Dan",  "Sales",  80, "Rep",    29, "Berlin", Arrays.asList("Excel", "CRM")),
            new Emp("Eve",  "Sales", 110, "Lead",   38, "Lyon",   Arrays.asList("CRM", "SQL")),
            new Emp("Fin",  "HR",     70, "Rep",    45, "Lyon",   Arrays.asList("Excel")),
            new Emp("Gina", "HR",     95, "Lead",   33, "Paris",  Arrays.asList("Excel", "SQL")),
            new Emp("Hal",  "Eng",    90, "Junior", 27, "Berlin", Arrays.asList("Python", "SQL")));
    }

    // ---------- toMap (Q1-Q5) ----------
    // Q1 [code] name -> salary
    static Map<String,Integer> q1(List<Emp> d) { return null; }
    // Q2 [code] name -> the Emp object (identity value)
    static Map<String,Emp> q2(List<Emp> d) { return null; }
    // Q3 [code] city -> FIRST name seen in that city (merge keeps first)
    static Map<String,String> q3(List<Emp> d) { return null; }
    // Q4 [code] name -> salary, but keys SORTED (TreeMap via 4-arg toMap)
    static Map<String,Integer> q4(List<Emp> d) { return null; }
    // Q5 [code] name -> title
    static Map<String,String> q5(List<Emp> d) { return null; }

    // ---------- groupingBy basics (Q6-Q10) ----------
    // Q6 [code] dept -> list of Emps
    static Map<String,List<Emp>> q6(List<Emp> d) { return null; }
    // Q7 [code] dept -> list of names (mapping)
    static Map<String,List<String>> q7(List<Emp> d) { return null; }
    // Q8 [code] title -> list of names
    static Map<String,List<String>> q8(List<Emp> d) { return null; }
    // Q9 [code] city -> headcount (counting)
    static Map<String,Long> q9(List<Emp> d) { return null; }
    // Q10 [code] salary band ("high" if salary>=100 else "low") -> list of names
    static Map<String,List<String>> q10(List<Emp> d) { return null; }

    // ---------- groupingBy + downstream (Q11-Q20) ----------
    // Q11 [code] dept -> headcount
    static Map<String,Long> q11(List<Emp> d) { return null; }
    // Q12 [code] dept -> total salary (summingInt)
    static Map<String,Integer> q12(List<Emp> d) { return null; }
    // Q13 [code] dept -> average salary (averagingDouble)
    static Map<String,Double> q13(List<Emp> d) { return null; }
    // Q14 [code] dept -> name of the TOP earner (collectingAndThen + maxBy)
    static Map<String,String> q14(List<Emp> d) { return null; }
    // Q15 [code] dept -> MIN age (collectingAndThen + minBy)
    static Map<String,Integer> q15(List<Emp> d) { return null; }
    // Q16 [code] dept -> SET of distinct titles (mapping + toSet)
    static Map<String,Set<String>> q16(List<Emp> d) { return null; }
    // Q17 [code] dept -> names joined by ", " (mapping + joining)
    static Map<String,String> q17(List<Emp> d) { return null; }
    // Q18 [code] dept -> "count,sum,min,max,avg" of salary, avg with 1 decimal (summarizingInt)
    //     e.g. "4,450,90,150,112.5"
    static Map<String,String> q18(List<Emp> d) { return null; }
    // Q19 [code] dept -> names of high earners (salary>=100) only (filtering + mapping) [Java 9+]
    static Map<String,List<String>> q19(List<Emp> d) { return null; }
    // Q20 [code] dept -> total salary using reducing (not summingInt)
    static Map<String,Integer> q20(List<Emp> d) { return null; }

    // ---------- multi-level / composite (Q21-Q25) ----------
    // Q21 [code] dept -> (title -> list of names)
    static Map<String,Map<String,List<String>>> q21(List<Emp> d) { return null; }
    // Q22 [code] dept -> (title -> count)
    static Map<String,Map<String,Long>> q22(List<Emp> d) { return null; }
    // Q23 [code] city -> average salary
    static Map<String,Double> q23(List<Emp> d) { return null; }
    // Q24 [code] "dept|title" composite key -> count
    static Map<String,Long> q24(List<Emp> d) { return null; }
    // Q25 [code] dept -> name of the OLDEST employee
    static Map<String,String> q25(List<Emp> d) { return null; }

    // ---------- partitioning (Q26-Q28) ----------
    // Q26 [code] partition high earners (salary>=100) -> names
    static Map<Boolean,List<String>> q26(List<Emp> d) { return null; }
    // Q27 [code] partition high earners -> count
    static Map<Boolean,Long> q27(List<Emp> d) { return null; }
    // Q28 [code] partition by EVEN age -> average salary
    static Map<Boolean,Double> q28(List<Emp> d) { return null; }

    // ---------- complex (Q29-Q30) ----------
    // Q29 [code] dept -> SET of all skills across its employees (flatMapping) [Java 9+]
    static Map<String,Set<String>> q29(List<Emp> d) { return null; }
    // Q30 [code] departments ranked by headcount DESC (tie-break by name ASC) -> list of dept names
    static List<String> q30(List<Emp> d) { return null; }

    public static void main(String[] args) {
        List<Emp> d = data();
        Check.eq("Q1 name->salary",        SolutionsP11.q1(d),  q1(d));
        Check.eq("Q2 name->emp",           SolutionsP11.q2(d),  q2(d));
        Check.eq("Q3 city->first name",    SolutionsP11.q3(d),  q3(d));
        Check.eq("Q4 name->salary sorted", SolutionsP11.q4(d),  q4(d));
        Check.eq("Q5 name->title",         SolutionsP11.q5(d),  q5(d));
        Check.eq("Q6 dept->emps",          SolutionsP11.q6(d),  q6(d));
        Check.eq("Q7 dept->names",         SolutionsP11.q7(d),  q7(d));
        Check.eq("Q8 title->names",        SolutionsP11.q8(d),  q8(d));
        Check.eq("Q9 city->count",         SolutionsP11.q9(d),  q9(d));
        Check.eq("Q10 band->names",        SolutionsP11.q10(d), q10(d));
        Check.eq("Q11 dept->count",        SolutionsP11.q11(d), q11(d));
        Check.eq("Q12 dept->sum salary",   SolutionsP11.q12(d), q12(d));
        Check.eq("Q13 dept->avg salary",   SolutionsP11.q13(d), q13(d));
        Check.eq("Q14 dept->top earner",   SolutionsP11.q14(d), q14(d));
        Check.eq("Q15 dept->min age",      SolutionsP11.q15(d), q15(d));
        Check.eq("Q16 dept->titles set",   SolutionsP11.q16(d), q16(d));
        Check.eq("Q17 dept->joined names", SolutionsP11.q17(d), q17(d));
        Check.eq("Q18 dept->salary stats", SolutionsP11.q18(d), q18(d));
        Check.eq("Q19 dept->high earners", SolutionsP11.q19(d), q19(d));
        Check.eq("Q20 dept->sum(reducing)",SolutionsP11.q20(d), q20(d));
        Check.eq("Q21 dept->title->names", SolutionsP11.q21(d), q21(d));
        Check.eq("Q22 dept->title->count", SolutionsP11.q22(d), q22(d));
        Check.eq("Q23 city->avg salary",   SolutionsP11.q23(d), q23(d));
        Check.eq("Q24 dept|title->count",  SolutionsP11.q24(d), q24(d));
        Check.eq("Q25 dept->oldest",       SolutionsP11.q25(d), q25(d));
        Check.eq("Q26 partition names",    SolutionsP11.q26(d), q26(d));
        Check.eq("Q27 partition count",    SolutionsP11.q27(d), q27(d));
        Check.eq("Q28 evenAge->avg sal",   SolutionsP11.q28(d), q28(d));
        Check.eq("Q29 dept->skills",       SolutionsP11.q29(d), q29(d));
        Check.eq("Q30 depts by headcount", SolutionsP11.q30(d), q30(d));
        Check.summary();
    }
}
