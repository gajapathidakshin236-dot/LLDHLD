package com.company.DSA;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.*;

public class collections {
    List<String> words = List.of("apple", "banana", "apple", "cherry", "banana", "apple");

    Map<String, Integer> count = words.stream().collect(Collectors.toMap(
            w -> w,
            w -> 1,
            /* fill the merge function */
            (a,b) -> a+b
    ));

    List<String> words2 = List.of("ant", "apple", "avocado", "banana", "bee", "cherry");

    Map<Character, String> longest = words2.stream().collect(Collectors.toMap(
            w -> w.charAt(0),
            w -> w,
            (a,b) -> a.length()>b.length()?a:b
            /* fill the merge function */
    ));


    @Getter
    @Setter
    public class Order2 {
        public Order2(String customer) {
            this.customer = customer;
        }

        public Order2(String customer, int amount) {
            this.amount = amount;
            this.customer = customer;
        }

        String customer;
        int amount;

    }

    List<Order2> orders = List.of(
            new Order2("Alice", 100),
            new Order2("Bob",   50),
            new Order2("Alice", 200),
            new Order2("Bob",   75),
            new Order2("Alice", 150)
    );

    Map<String, Integer> latest = orders.stream().collect(Collectors.toMap(
            Order2::getCustomer,
            Order2::getAmount,
            /* fill the merge function */
            Integer::sum
    ));


    @Getter
    @Setter
   public class employee {
       public employee(String dept, int salary) {
           this.dept = dept;
           this.salary = salary;
       }

       String dept;
       int salary;
        // constructor, getters
    }

    List<employee> emps = List.of(
            new employee("Eng",   90000),
            new employee("Sales", 60000),
            new employee("Eng",   120000),
            new employee("Sales", 75000),
            new employee("HR",    55000)
    );

    Map<String, Integer> maxSalary = emps.stream().collect(Collectors.toMap(
            employee::getDept,
            employee::getSalary,
            /* fill the merge function */
            Math::max //(a,b) -> Math.max(a,b)
    ));


    record Skill(String emp, String skill) {

    }

    List<Skill> skills = List.of(
            new Skill("Alice", "Java"),
            new Skill("Bob",   "Python"),
            new Skill("Alice", "SQL"),
            new Skill("Alice", "Kafka"),
            new Skill("Bob",   "Go")
    );

    Map<String, String> bundle = skills.stream().collect(Collectors.toMap(
            Skill::emp,
            Skill::skill,
            /* fill the merge function */
            (a,b) -> a + "," +b
    ));

    List<String> input = List.of("apple", "banana", "apple", "cherry", "banana");

    List<String> unique = new ArrayList<>(input.stream().collect(Collectors.toMap(
            s -> s,
            s -> s,
            /* fill the merge function */
            (a, b) -> a, // we choose a for de duplication
            LinkedHashMap::new
            /* fill the map type */
    )).values());


    @Getter
    @Setter
    public class User {
        public User(String id, String name) {
            this.id = id;
            this.name = name;
        }

        String id;
        String name;
        // constructor, getters
    }

    List<User> users = List.of(
            new User("U1", "Alice"),
            new User("U2", "Bob"),
            new User("U3", "Carol"),
            new User("U1", "Dave")   // ← duplicate ID!
    );

    Map<String, User> byId = users.stream().collect(Collectors.toMap(
            User::getId,
            u -> u,
            (a,b) -> {
                throw new IllegalArgumentException(
                        "Duplicate ID found: " + a.getId()
                );
            }

            /* fill the merge function */

    ));

    class Order {
        private String customer;
        private int amount;

        public Order(String customer, int amount) {
            this.customer = customer;
            this.amount = amount;
        }

        public String getCustomer() { return customer; }
        public int getAmount() { return amount; }
        public void setCustomer(String customer) { this.customer = customer; }
        public void setAmount(int amount) { this.amount = amount; }
    }

    List<Order2> orders4 = List.of(
            new Order2("Alice", 100),
            new Order2("Bob",   50),
            new Order2("Alice", 200),
            new Order2("Carol", 500),
            new Order2("Bob",   75)
    );

    // Step 1 — aggregate totals per customer
    Map<String, Integer> totals = orders4.stream().collect(Collectors.toMap(
            Order2::getCustomer,
            Order2::getAmount,
            Integer::sum
            /* fill merge function for step 1 */
    ));

    // Step 2 — sort by total descending
    Map<String, Integer> sorted = totals.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (a, b) -> a ,  // collision will never happen as
                    /* fill merge function for step 2 */
                    LinkedHashMap::new

                    /* fill map type */
            ));
    class Employee {
        private String name;
        private String dept;
        private int salary;

        public Employee(String name, String dept, int salary) {
            this.name = name;
            this.dept = dept;
            this.salary = salary;
        }

        public String getName() { return name; }
        public String getDept() { return dept; }
        public int getSalary() { return salary; }
        public void setName(String name) { this.name = name; }
        public void setDept(String dept) { this.dept = dept; }
        public void setSalary(int salary) { this.salary = salary; }
    }

    List<Employee> empss = List.of(
            new Employee("Alice", "Eng",   90000),
            new Employee("Bob",   "Sales", 60000),
            new Employee("Carol", "Eng",   120000),
            new Employee("Dave",  "Sales", 75000),
            new Employee("Eve",   "HR",    55000)
    );

    Map<String, List<Employee>> byDept = empss.stream()
            .collect(Collectors.groupingBy(Employee::getDept));

    Map<String, Long> countByDept = empss.stream()
            .collect(Collectors.groupingBy(Employee::getDept,TreeMap::new, Collectors.counting()));


String sss="ddd ddds dddd ";

    Map<String, Long> countbystrigng =
            Arrays.stream(sss.trim().split("\\s+"))
                    .collect(Collectors.groupingBy(
                            str -> str,
                            TreeMap::new, // used for sortging and keepiong
                            Collectors.counting()
                    ));


    Map<String, Integer> salaryByDept = empss.stream()
            .collect(Collectors.groupingBy(
                    Employee::getDept,
                    Collectors.summingInt(Employee::getSalary)
            ));

    Map<String, Double> salaryByDeptAvg = empss.stream()
            .collect(Collectors.groupingBy(
                    Employee::getDept,
                    Collectors.averagingInt(Employee::getSalary)
            ));

    public static void met() {
        LinkedHashMap<Character, Integer> map = new LinkedHashMap<>();
        map.put('A', 1);


        for (Map.Entry<Character,Integer> m : map.entrySet()) {
            System.out.println(m.getKey() + " " + m.getValue());
        }

    }

    public static void main(String[] args) {
        met();
    }


    
}
