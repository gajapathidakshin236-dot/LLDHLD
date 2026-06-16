package com.company.patterns.builder;

/**
 * BUILDER PATTERN
 *
 * Intent: Construct a complex object step by step. Solves the
 *         "telescoping constructor" problem when an object has many
 *         optional parameters, and produces IMMUTABLE objects.
 *
 * Use when: 4+ parameters, many optional, want immutability + validation.
 *
 * Run: javac BuilderDemo.java && java BuilderDemo
 */
public class BuilderDemo {
    public static void main(String[] args) {
        // Required field in the builder constructor; optionals chained.
        User u1 = new User.Builder("Alice")
                .email("alice@example.com")
                .age(28)
                .phone("555-0100")
                .build();

        User u2 = new User.Builder("Bob")
                .age(35)
                .build();   // email/phone left as defaults — no need to pass nulls

        System.out.println(u1);
        System.out.println(u2);

        // Validation happens in build()
        try {
            new User.Builder("").build();   // blank name -> rejected
        } catch (IllegalStateException e) {
            System.out.println("Rejected: " + e.getMessage());
        }

        try {
            new User.Builder("Carol").age(-5).build();   // bad age -> rejected
        } catch (IllegalStateException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }
}

class User {
    // All fields final -> object is immutable once built.
    private final String name;
    private final String email;
    private final int age;
    private final String phone;
    private final String address;

    // Private constructor: only the Builder can construct a User.
    private User(Builder b) {
        this.name = b.name;
        this.email = b.email;
        this.age = b.age;
        this.phone = b.phone;
        this.address = b.address;
    }

    // Static nested Builder.
    public static class Builder {
        private final String name;            // required
        private String email = "N/A";         // optional with defaults
        private int age = 0;
        private String phone = "N/A";
        private String address = "N/A";

        public Builder(String name) {         // required field in constructor
            this.name = name;
        }

        // Each setter returns `this` -> enables the fluent chain.
        public Builder email(String email)     { this.email = email; return this; }
        public Builder age(int age)            { this.age = age; return this; }
        public Builder phone(String phone)     { this.phone = phone; return this; }
        public Builder address(String address) { this.address = address; return this; }

        // build() is the single validation gate + the only place a User is born.
        public User build() {
            if (name == null || name.isEmpty()) {
                throw new IllegalStateException("name is required");
            }
            if (age < 0) {
                throw new IllegalStateException("age cannot be negative");
            }
            return new User(this);
        }
    }

    @Override
    public String toString() {
        return "User{name=" + name + ", email=" + email + ", age=" + age
                + ", phone=" + phone + ", address=" + address + "}";
    }
}
