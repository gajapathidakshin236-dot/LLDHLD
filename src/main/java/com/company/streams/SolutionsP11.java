package com.company.streams;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.*;
import static java.util.Comparator.*;
import com.company.streams.P11_Grouping.Emp;

/**
 * ANSWER KEY for P11_Grouping. Don't open until you've tried!
 * Run this file to print worked answers for all 30 questions.
 */
public class SolutionsP11 {

    static Map<String,Integer> q1(List<Emp> d){ return d.stream().collect(toMap(Emp::getName, Emp::getSalary)); }
    static Map<String,Emp> q2(List<Emp> d){ return d.stream().collect(toMap(Emp::getName, e->e)); }
    static Map<String,String> q3(List<Emp> d){ return d.stream().collect(toMap(Emp::getCity, Emp::getName, (a,b)->a)); }
    static Map<String,Integer> q4(List<Emp> d){ return d.stream().collect(toMap(Emp::getName, Emp::getSalary, (a,b)->a, TreeMap::new)); }
    static Map<String,String> q5(List<Emp> d){ return d.stream().collect(toMap(Emp::getName, Emp::getTitle)); }

    static Map<String,List<Emp>> q6(List<Emp> d){ return d.stream().collect(groupingBy(Emp::getDept)); }
    static Map<String,List<String>> q7(List<Emp> d){ return d.stream().collect(groupingBy(Emp::getDept, mapping(Emp::getName, toList()))); }
    static Map<String,List<String>> q8(List<Emp> d){ return d.stream().collect(groupingBy(Emp::getTitle, mapping(Emp::getName, toList()))); }
    static Map<String,Long> q9(List<Emp> d){ return d.stream().collect(groupingBy(Emp::getCity, counting())); }
    static Map<String,List<String>> q10(List<Emp> d){ return d.stream().collect(groupingBy(e-> e.getSalary()>=100?"high":"low", mapping(Emp::getName, toList()))); }

    static Map<String,Long> q11(List<Emp> d){ return d.stream().collect(groupingBy(Emp::getDept, counting())); }
    static Map<String,Integer> q12(List<Emp> d){ return d.stream().collect(groupingBy(Emp::getDept, summingInt(Emp::getSalary))); }
    static Map<String,Double> q13(List<Emp> d){ return d.stream().collect(groupingBy(Emp::getDept, averagingDouble(Emp::getSalary))); }
    static Map<String,String> q14(List<Emp> d){ return d.stream().collect(groupingBy(Emp::getDept, collectingAndThen(maxBy(comparingInt(Emp::getSalary)), o->o.get().getName()))); }
    static Map<String,Integer> q15(List<Emp> d){ return d.stream().collect(groupingBy(Emp::getDept, collectingAndThen(minBy(comparingInt(Emp::getAge)), o->o.get().getAge()))); }
    static Map<String,Set<String>> q16(List<Emp> d){ return d.stream().collect(groupingBy(Emp::getDept, mapping(Emp::getTitle, toSet()))); }
    static Map<String,String> q17(List<Emp> d){ return d.stream().collect(groupingBy(Emp::getDept, mapping(Emp::getName, joining(", ")))); }
    static Map<String,String> q18(List<Emp> d){ return d.stream().collect(groupingBy(Emp::getDept, collectingAndThen(summarizingInt(Emp::getSalary),
            s-> s.getCount()+","+s.getSum()+","+s.getMin()+","+s.getMax()+","+String.format("%.1f", s.getAverage())))); }
    static Map<String,List<String>> q19(List<Emp> d){ return d.stream().collect(groupingBy(Emp::getDept, filtering(e->e.getSalary()>=100, mapping(Emp::getName, toList())))); }
    static Map<String,Integer> q20(List<Emp> d){ return d.stream().collect(groupingBy(Emp::getDept, reducing(0, Emp::getSalary, Integer::sum))); }

    static Map<String,Map<String,List<String>>> q21(List<Emp> d){ return d.stream().collect(groupingBy(Emp::getDept, groupingBy(Emp::getTitle, mapping(Emp::getName, toList())))); }
    static Map<String,Map<String,Long>> q22(List<Emp> d){ return d.stream().collect(groupingBy(Emp::getDept, groupingBy(Emp::getTitle, counting()))); }
    static Map<String,Double> q23(List<Emp> d){ return d.stream().collect(groupingBy(Emp::getCity, averagingDouble(Emp::getSalary))); }
    static Map<String,Long> q24(List<Emp> d){ return d.stream().collect(groupingBy(e-> e.getDept()+"|"+e.getTitle(), counting())); }
    static Map<String,String> q25(List<Emp> d){ return d.stream().collect(groupingBy(Emp::getDept, collectingAndThen(maxBy(comparingInt(Emp::getAge)), o->o.get().getName()))); }

    static Map<Boolean,List<String>> q26(List<Emp> d){ return d.stream().collect(partitioningBy(e->e.getSalary()>=100, mapping(Emp::getName, toList()))); }
    static Map<Boolean,Long> q27(List<Emp> d){ return d.stream().collect(partitioningBy(e->e.getSalary()>=100, counting())); }
    static Map<Boolean,Double> q28(List<Emp> d){ return d.stream().collect(partitioningBy(e->e.getAge()%2==0, averagingDouble(Emp::getSalary))); }

    static Map<String,Set<String>> q29(List<Emp> d){ return d.stream().collect(groupingBy(Emp::getDept, flatMapping(e->e.getSkills().stream(), toSet()))); }
    static List<String> q30(List<Emp> d){
        Map<String,Long> c = d.stream().collect(groupingBy(Emp::getDept, counting()));
        return c.entrySet().stream()
                .sorted(Map.Entry.<String,Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .map(Map.Entry::getKey).collect(toList());
    }

    public static void main(String[] args){
        List<Emp> d = P11_Grouping.data();
        System.out.println("Q1  " + q1(d));
        System.out.println("Q3  " + q3(d));
        System.out.println("Q7  " + q7(d));
        System.out.println("Q11 " + q11(d));
        System.out.println("Q12 " + q12(d));
        System.out.println("Q13 " + q13(d));
        System.out.println("Q14 " + q14(d));
        System.out.println("Q18 " + q18(d));
        System.out.println("Q19 " + q19(d));
        System.out.println("Q21 " + q21(d));
        System.out.println("Q22 " + q22(d));
        System.out.println("Q25 " + q25(d));
        System.out.println("Q26 " + q26(d));
        System.out.println("Q29 " + q29(d));
        System.out.println("Q30 " + q30(d));
    }
}
