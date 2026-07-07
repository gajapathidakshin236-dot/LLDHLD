package com.company.streams;
import java.util.*;

/** Sample domain classes + data used by the practice harnesses. */
public class PracticeModels {

    public static class Person {
        public final String name; public final int age;
        public final String city; public final String country;
        public Person(String name, int age, String city, String country) {
            this.name = name; this.age = age; this.city = city; this.country = country;
        }
        public String getName() { return name; }
        public int getAge() { return age; }
        public String getCity() { return city; }
        public String getCountry() { return country; }
        public String toString() { return name; }
        public boolean equals(Object o) {
            if (!(o instanceof Person)) return false;
            Person p = (Person) o;
            return age == p.age && Objects.equals(name, p.name)
                    && Objects.equals(city, p.city) && Objects.equals(country, p.country);
        }
        public int hashCode() { return Objects.hash(name, age, city, country); }
    }

    public static class Employee {
        public final String name; public final String dept;
        public final int salary; public final String title;
        public Employee(String name, String dept, int salary, String title) {
            this.name = name; this.dept = dept; this.salary = salary; this.title = title;
        }
        public String getName() { return name; }
        public String getDept() { return dept; }
        public int getSalary() { return salary; }
        public String getTitle() { return title; }
        public String toString() { return name; }
    }

    public static List<Person> people() {
        return Arrays.asList(
            new Person("Ann", 30, "Paris", "FR"),
            new Person("Bob", 25, "Lyon", "FR"),
            new Person("Cara", 30, "Berlin", "DE"),
            new Person("Dan", 40, "Berlin", "DE"));
    }

    public static List<Employee> employees() {
        return Arrays.asList(
            new Employee("Ann", "Eng", 100, "Dev"),
            new Employee("Bob", "Eng", 120, "Dev"),
            new Employee("Cara", "Sales", 90, "Rep"),
            new Employee("Dan", "Sales", 95, "Lead"));
    }
}
