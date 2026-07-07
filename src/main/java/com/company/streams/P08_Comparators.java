package com.company.streams;
import java.util.*;
import java.util.stream.*;
import com.company.streams.PracticeModels.Person;

/**
 * SECTION G — Comparators & sorting (Q84-Q89)
 * Run: java com.company.streams.P08_Comparators
 */
public class P08_Comparators {

    // Q84 [code] Sort people by country, then name; return names.
    static List<String> q84(List<Person> in) { return in.stream().sorted(Comparator.comparing(Person::getCountry).thenComparing(Person::getName)).map(Person::getName).collect(Collectors.toList()); }

    // Q85 [code] Sort people by age DESCENDING (use Comparator.reversed); return names.
    static List<String> q85(List<Person> in) { return in.stream().sorted(Comparator.comparing(Person::getAge).reversed()).map(Person::getName).collect(Collectors.toList()); }

    // Q86 [code] Sort strings case-insensitively.
    static List<String> q86(List<String> in) { return in.stream()
            .sorted(String.CASE_INSENSITIVE_ORDER)
            .collect(Collectors.toList()); }

    // Q87 [code] Sort a Map's entries by value DESC; return the keys.
    static List<String> q87(Map<String, Integer> in) { return in.entrySet()
            .stream().sorted(Map.Entry.<String,Integer>comparingByValue()
                    .reversed()).map(Map.Entry::getKey).collect(Collectors.toList()); }

    // Q88 [code] Sort with nulls LAST. ["b",null,"a"] -> [a,b,null]
    static List<String> q88(List<String> in) { return in.stream().sorted( Comparator.nullsLast(
            Comparator.naturalOrder()
    )).collect(Collectors.toList()); }

    // Q89 [why] Comparator.comparingInt vs comparing(...) with an Integer key - why prefer comparingInt?
    static String q89() { return ""; }

    public static void main(String[] args) {
        Check.eq ("Q84 country,name", Arrays.asList("Cara", "Dan", "Ann", "Bob"), q84(PracticeModels.people()));
        Check.eq ("Q85 age desc", Arrays.asList("Dan", "Ann", "Cara", "Bob"), q85(PracticeModels.people()));
        Check.eq ("Q86 case-insensitive", Arrays.asList("Apple", "banana", "cherry"),
                q86(Arrays.asList("banana", "Apple", "cherry")));
        Map<String, Integer> m = new LinkedHashMap<>();
        m.put("a", 3); m.put("b", 1); m.put("c", 2);
        Check.eq ("Q87 map by value desc", Arrays.asList("a", "c", "b"), q87(m));
        Check.eq ("Q88 nulls last", Arrays.asList("a", "b", null), q88(Arrays.asList("b", null, "a")));
        Check.say("Q89 comparingInt", q89());
        Check.summary();
    }
}
