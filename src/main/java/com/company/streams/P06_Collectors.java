package com.company.streams;
import java.util.*;
import java.util.stream.*;
import com.company.streams.PracticeModels.Person;
import com.company.streams.PracticeModels.Employee;

/**
 * SECTION E — Collectors (Q56-Q75)
 * Run: java com.company.streams.P06_Collectors
 */
public class P06_Collectors {

    // Q56 [code] Collect strings into a Set.
    static Set<String> q56(List<String> in) { return in.stream().collect(Collectors.toSet()); }

    // Q57 [code] Collect into a TreeSet via toCollection (sorted). (stub returns empty to stay null-safe)
    static Set<String> q57(List<String> in) { return new TreeSet<>(in.stream().collect(Collectors.toSet())); }

    // Q58 [code] toMap: name -> age.
    static Map<String, Integer> q58(List<Person> in) { return in.stream().collect(Collectors.toMap(Person::getName,Person::getAge)); }

    // Q59 [code] toMap with a merge function that KEEPS THE FIRST word per first-letter key.
    static Map<Character, String> q59(List<String> in) { return in.stream().collect(Collectors.toMap(x-> x.charAt(0), s -> s,(first, second) -> first )); }

    // Q60 [code] groupingBy first letter.
    static Map<Character, List<String>> q60(List<String> in) { return in.stream()
            .collect(Collectors.groupingBy(s->s.charAt(0))); }

    // Q61 [code] groupingBy + counting (words per first letter).
    static Map<Character, Long> q61(List<String> in) { return in.stream()
            .collect(Collectors.toMap(x->x.charAt(0),x->1L, Long::sum)); }

    // Q62 [code] groupingBy city + mapping to names.
    static Map<String, List<String>> q62(List<Person> in) { return in.stream().collect(Collectors.groupingBy(Person::getCity,Collectors.mapping(Person::getName,Collectors.toList()))); }

    // Q63 [code] groupingBy dept + summingInt(salary).
    static Map<String, Integer> q63(List<Employee> in) { return in.stream().collect(Collectors.groupingBy(Employee::getDept,Collectors.summingInt(Employee::getSalary))); }

    // Q64 [code] groupingBy city + averagingDouble(age).
    static Map<String, Double> q64(List<Person> in) { return in.stream()
            .collect(Collectors.groupingBy(
                    Person::getCity,
                    Collectors.averagingDouble(Person::getAge)
            )); }

    // Q65 [code] Two-level: country -> city -> names.
    static Map<String, Map<String, List<String>>> q65(List<Person> in) { return in.stream()
            .collect(Collectors.groupingBy(Person::getCountry,Collectors.groupingBy(Person::getCity,Collectors.mapping(Person::getName,Collectors.toList())))); }

    // Q66 [code] partitioningBy even/odd. (q66keys: what are the exact keys?)
    static Map<Boolean, List<Integer>> q66(List<Integer> in) { return in.stream().collect(Collectors.partitioningBy(x->x%2==0)); }
    static String q66keys() { return ""; }

    // Q67 [why] partitioningBy vs groupingBy(boolean) - which guarantees both keys?
    static String q67() { return ""; }

    // Q68 [code] joining with prefix "[" separator ", " suffix "]" -> "[a, b, c]".
    static String q68(List<String> in) { return in.stream()
            .collect(Collectors.joining(", ", "[", "]")); }

    // Q69 [code] summarizingInt(age): return String.format("%d,%d,%d,%d,%.2f", count,sum,min,max,avg).
    static String q69(List<Person> in) { IntSummaryStatistics s =
            in.stream()
                    .collect(Collectors.summarizingInt(Person::getAge));

        return String.format("%d,%d,%d,%d,%.2f",
                s.getCount(),
                s.getSum(),
                s.getMin(),
                s.getMax(),
                s.getAverage()); }

    // Q70 [code] collectingAndThen: group by city, then wrap result unmodifiable. (contents == Q62)
    static Map<String, List<String>> q70(List<Person> in) {  return in.stream()
            .collect(Collectors.collectingAndThen(

                    Collectors.groupingBy(
                            Person::getCity,
                            Collectors.mapping(
                                    Person::getName,
                                    Collectors.toList()
                            )
                    ),

                    Collections::unmodifiableMap
            )); }

    // Q71 [code] groupingBy into a TreeMap (sorted keys) + counting. (stub empty for null-safety)
    static Map<Character, Long> q71(List<String> in) { return in.stream()
            .collect(Collectors.groupingBy(
                    s -> s.charAt(0),
                    TreeMap::new,
                    Collectors.counting()
            )); }

    // Q72 [why] Collectors.reducing vs Stream.reduce - when the collector form?
    static String q72() { return ""; }

    // Q73 [why] Map<Char,List<String>>: toMap or groupingBy? Why?
    static String q73() { return ""; }

    // Q74 [code] groupingBy dept + mapping to a SET of distinct titles.
    static Map<String, Set<String>> q74(List<Employee> in) {  return in.stream()
            .collect(Collectors.groupingBy(
                    Employee::getDept,
                    Collectors.mapping(
                            Employee::getTitle,
                            Collectors.toSet()
                    )
            )); }

    // Q75 [code] Frequency map two ways.
    static Map<String, Long> q75_grouping(List<String> in) {  return
            in.stream()
            .collect(Collectors.groupingBy(
                    s -> s,
                    Collectors.counting()
            )); }
    static Map<String, Integer> q75_toMapMerge(List<String> in) { return in.stream()
            .collect(Collectors.toMap(
                    s -> s,
                    s -> 1,
                    Integer::sum
            )); }

    public static void main(String[] args) {
        List<Person> people = PracticeModels.people();
        List<Employee> emps = PracticeModels.employees();
        List<String> fruit = Arrays.asList("apple", "avocado", "banana", "cherry");
        List<String> freq = Arrays.asList("a", "b", "a", "c", "a");

        Check.eq ("Q56 toSet", new HashSet<>(Arrays.asList("a", "b")), q56(Arrays.asList("a", "b", "a")));
        Check.eq ("Q57 treeset sorted", Arrays.asList("apple", "banana", "cherry"),
                new ArrayList<>(q57(Arrays.asList("banana", "apple", "cherry"))));
        Check.eq ("Q58 toMap name-age", mapSII("Ann", 30, "Bob", 25, "Cara", 30, "Dan", 40), q58(people));
        Check.eq ("Q59 merge keep first", mapCS('a', "apple", 'b', "banana"),
                q59(Arrays.asList("apple", "avocado", "banana")));
        Check.eq ("Q60 group by letter", mapCL('a', Arrays.asList("apple", "avocado"),
                'b', Arrays.asList("banana"), 'c', Arrays.asList("cherry")), q60(fruit));
        Check.eq ("Q61 counting", mapCLong('a', 2L, 'b', 1L, 'c', 1L), q61(fruit));
        Check.eq ("Q62 city->names", mapSL("Paris", Arrays.asList("Ann"),
                "Lyon", Arrays.asList("Bob"), "Berlin", Arrays.asList("Cara", "Dan")), q62(people));
        Check.eq ("Q63 dept salary sum", mapSI("Eng", 220, "Sales", 185), q63(emps));
        Check.eq ("Q64 city avg age", mapSD("Paris", 30.0, "Lyon", 25.0, "Berlin", 35.0), q64(people));
        Map<String, Map<String, List<String>>> exp65 = new HashMap<>();
        Map<String, List<String>> fr = new HashMap<>(); fr.put("Paris", Arrays.asList("Ann")); fr.put("Lyon", Arrays.asList("Bob"));
        Map<String, List<String>> de = new HashMap<>(); de.put("Berlin", Arrays.asList("Cara", "Dan"));
        exp65.put("FR", fr); exp65.put("DE", de);
        Check.eq ("Q65 country->city", exp65, q65(people));
        Map<Boolean, List<Integer>> exp66 = new HashMap<>();
        exp66.put(false, Arrays.asList(1, 3, 5)); exp66.put(true, Arrays.asList(2, 4, 6));
        Check.eq ("Q66 partition", exp66, q66(Arrays.asList(1, 2, 3, 4, 5, 6)));
        Check.say("Q66 keys?", q66keys());
        Check.say("Q67 partition vs group", q67());
        Check.eq ("Q68 joining", "[a, b, c]", q68(Arrays.asList("a", "b", "c")));
        Check.eq ("Q69 summarizing", "4,125,25,40,31.25", q69(people));
        Check.eq ("Q70 collectingAndThen", mapSL("Paris", Arrays.asList("Ann"),
                "Lyon", Arrays.asList("Bob"), "Berlin", Arrays.asList("Cara", "Dan")), q70(people));
        Check.eq ("Q71 treemap keys", Arrays.asList('a', 'b', 'c'), new ArrayList<>(q71(fruit).keySet()));
        Check.say("Q72 reducing", q72());
        Check.say("Q73 toMap vs group", q73());
        Map<String, Set<String>> exp74 = new HashMap<>();
        exp74.put("Eng", new HashSet<>(Arrays.asList("Dev")));
        exp74.put("Sales", new HashSet<>(Arrays.asList("Rep", "Lead")));
        Check.eq ("Q74 dept->titles", exp74, q74(emps));
        Check.eq ("Q75 grouping freq", mapSLong("a", 3L, "b", 1L, "c", 1L), q75_grouping(freq));
        Check.eq ("Q75 toMap merge freq", mapSI("a", 3, "b", 1, "c", 1), q75_toMapMerge(freq));
        Check.summary();
    }

    // ---- tiny map builders for expected values ----
    static Map<String,Integer> mapSII(Object... kv){ return mapSI(kv); }
    static Map<String,Integer> mapSI(Object... kv){ Map<String,Integer> m=new HashMap<>(); for(int i=0;i<kv.length;i+=2) m.put((String)kv[i],(Integer)kv[i+1]); return m; }
    static Map<Character,String> mapCS(Object... kv){ Map<Character,String> m=new HashMap<>(); for(int i=0;i<kv.length;i+=2) m.put((Character)kv[i],(String)kv[i+1]); return m; }
    static Map<Character,List<String>> mapCL(Object... kv){ Map<Character,List<String>> m=new HashMap<>(); for(int i=0;i<kv.length;i+=2) m.put((Character)kv[i],(List<String>)kv[i+1]); return m; }
    static Map<Character,Long> mapCLong(Object... kv){ Map<Character,Long> m=new HashMap<>(); for(int i=0;i<kv.length;i+=2) m.put((Character)kv[i],(Long)kv[i+1]); return m; }
    static Map<String,List<String>> mapSL(Object... kv){ Map<String,List<String>> m=new HashMap<>(); for(int i=0;i<kv.length;i+=2) m.put((String)kv[i],(List<String>)kv[i+1]); return m; }
    static Map<String,Double> mapSD(Object... kv){ Map<String,Double> m=new HashMap<>(); for(int i=0;i<kv.length;i+=2) m.put((String)kv[i],(Double)kv[i+1]); return m; }
    static Map<String,Long> mapSLong(Object... kv){ Map<String,Long> m=new HashMap<>(); for(int i=0;i<kv.length;i+=2) m.put((String)kv[i],(Long)kv[i+1]); return m; }
}
