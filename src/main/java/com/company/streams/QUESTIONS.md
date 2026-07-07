# Streams & Collections — 100 Questions (Q1–Q100)

Sequential, easy -> hard. Covers: collections choice, stream pipeline, intermediate ops,
terminal ops + Optional, Collectors, primitive streams, comparators, parallel streams,
and internals/pitfalls. **No answers here** — that's the point.

## How to use this
- Answer in whatever way you like: write code in a scratch `.java` file, or reply in chat.
- Question types are mixed on purpose (matches real interviews):
  - **[code]** = write the snippet
  - **[print]** = predict the exact output and say why
  - **[why]** = explain the concept in your own words
  - **[bug]** = spot and fix the mistake
- When you're ready, tell me e.g. **"review Q1–Q20"** (or any range) and I'll grade each,
  fix what's wrong, and add teaching notes. Then we continue.

---

## Section A — Collections: choice & foundations (Q1–Q12)

1. [why] Name the three sub-interfaces of `Collection`, and the separate interface that is **not** a `Collection`. Why is that one kept separate?
2. [why] You need fast random access by index and mostly append to the end. Which `List` implementation, and why?
3. [why] You must preserve insertion order **and** forbid duplicates. Which `Set`?
4. [why] You need keys kept in sorted order with range queries (all keys >= "m"). Which `Map`, and which method returns keys >= a value?
5. [why] Compare `ArrayList` vs `LinkedList` Big-O for: `get(i)`, add at end, remove from the middle.
6. [why] Why is `ArrayDeque` preferred over `Stack` and over `LinkedList` for stack/queue use?
7. [why] What does "HashMap iteration order is unspecified" mean, and can you ever rely on it?
8. [why] Two objects are `equals()` but return different `hashCode()`. What breaks when you put them in a `HashSet`?
9. [code] Create an unmodifiable `List` of three colors. What happens if you call `add()` on it?
10. [why] Difference in mutability/resizing between `Arrays.asList("a","b")` and `new ArrayList<>(List.of("a","b"))`.
11. [why] When would you choose `TreeMap` over `HashMap` even though it's O(log n)?
12. [why] What is a `PriorityQueue`, and name one algorithm where it's the natural choice.

## Section B — Streams: creation & the pipeline model (Q13–Q20)

13. [why] One sentence each: what is an **intermediate** op vs a **terminal** op?
14. [code] Write three different ways to create a `Stream`/`IntStream` of the numbers 1..5.
15. [print] What does this print and why: `Stream.of(1,2,3).peek(System.out::println);` (note: no terminal op).
16. [bug] What happens if you call two terminal operations on the same `Stream` reference? Name the exception.
17. [code] Build an `IntStream` of 0..9 inclusive using `rangeClosed`, and the same using `range`.
18. [why] Difference between `Stream.iterate` and `Stream.generate`, with a use case for each.
19. [why] Why must an infinite stream be paired with a short-circuiting op? Name two short-circuiting ops.
20. [print] Predict the output order:
    `Stream.of("a","b").map(s->{System.out.println("map "+s); return s;}).forEach(s->System.out.println("each "+s));`

## Section C — Intermediate operations (Q21–Q40)

21. [code] Given `List<String> words`, return only those starting with "a" (case-insensitive).
22. [code] Given `List<Integer>`, return each multiplied by 10.
23. [code] Convert `List<String>` of numeric strings ("1","2","3") into `List<Integer>`.
24. [code] Given `List<String>`, return an `int` total of their lengths using `mapToInt`.
25. [code] flatMap: given `List<List<Integer>>`, flatten into a single `List<Integer>`.
26. [code] flatMap: given `List<String>` sentences, return all words across all sentences (split on space).
27. [why] Difference between `map` and `flatMap` in one line, with a types/shape example.
28. [code] Given a list with duplicates, return distinct elements **preserving order**.
29. [code] Sort a `List<String>` by natural order; then by length ascending.
30. [code] Sort a `List<Person>` by age **descending** (assume `getAge()`).
31. [code] Return the first 3 elements of a stream; then, separately, skip the first 3.
32. [code] Using `takeWhile`, return the leading run of numbers < 100 from `[3, 50, 99, 200, 4]`.
33. [code] Using `dropWhile` on the same list, return the remainder.
34. [why] What is `peek()` for, and why should you not put business logic in it?
35. [print] On `[1,2,3,4]`: does `filter(even) -> map(+1)` give the same result as `map(+1) -> filter(even)`? Show both outputs.
36. [code] Given `List<String>`, produce a comma-separated `String` of their uppercase versions.
37. [why] For `distinct()` on `List<Person>`, what determines whether two `Person` objects are "the same"?
38. [code] Return only the 2nd and 3rd elements (combine `skip` + `limit`).
39. [code] Chain `filter + map + sorted + limit` to get the 2 smallest even squares from 1..10.
40. [why] Why is `sorted()` a **stateful** intermediate op, and what does that imply for infinite streams?

## Section D — Terminal ops & Optional (Q41–Q55)

41. [code] Count how many strings in a list have length > 4.
42. [why] Find the max integer in a list — what type is returned, and why is it wrapped?
43. [code] Find the min `String` by length using `min(Comparator...)`.
44. [code] Sum a list of integers three ways: `mapToInt().sum()`, `reduce`, and `Collectors.summingInt`.
45. [code] Use `reduce` to compute the product of a list of integers (identity = 1).
46. [why] Explain the three `reduce` overloads (identity; identity+accumulator; identity+accumulator+combiner). When is the combiner required?
47. [print] For `[2,4,6]` and predicate "is even", what do `anyMatch`, `allMatch`, `noneMatch` each return?
48. [why] `findFirst` vs `findAny` — what's the difference and when does it matter?
49. [why] What is `Optional` and what problem does it solve?
50. [code] Given `Optional<String>`, return its value uppercased, or "NONE" if empty (use `map` + `orElse`).
51. [why] Difference between `orElse`, `orElseGet`, and `orElseThrow`. When is `orElseGet` better than `orElse`?
52. [why] Why is calling `optional.get()` without checking `isPresent()` a code smell?
53. [code] Convert a `Stream<Integer>` to an `int[]` using `toArray`.
54. [why] What can `count()` short-circuit to on a SIZED source with no filter, and why can that skip iterating elements?
55. [code] Turn a `List<Person>` into their names joined by "; " (`map` + `Collectors.joining`).

## Section E — Collectors (Q56–Q75)

56. [code] Collect a stream of strings into a `Set`.
57. [code] Collect into a specific type (`TreeSet`) using `toCollection`.
58. [code] `toMap`: build a `Map<String,Integer>` from Person name -> age.
59. [bug] `toMap` throws on duplicate keys. Write the version with a merge function that keeps the **first** value.
60. [code] `groupingBy`: group a `List<String>` by first letter.
61. [code] `groupingBy` + `counting`: count how many words start with each letter.
62. [code] `groupingBy` + `mapping`: group Persons by city, collecting just their names into a `List`.
63. [code] `groupingBy` + `summingInt`: total salary per department.
64. [code] `groupingBy` + `averagingDouble`: average age per city.
65. [code] Two-level grouping: group Persons by country, then by city (`groupingBy` with a downstream `groupingBy`).
66. [why] `partitioningBy`: split numbers into even/odd. What are the exact keys of the resulting map?
67. [why] Difference between `partitioningBy` and `groupingBy` with a boolean function — which guarantees both `true` and `false` keys exist?
68. [code] `joining` with prefix/suffix: produce `"[a, b, c]"` from a list.
69. [code] `summarizingInt`: get count, sum, min, average, max of ages in a single pass.
70. [code] `collectingAndThen`: group by city, then wrap the whole result in an unmodifiable `Map`.
71. [code] `groupingBy` into a `TreeMap` (sorted keys) with a downstream collector.
72. [why] `Collectors.reducing` vs `Stream.reduce` — when would you reach for the collector form?
73. [why] To build `Map<Character, List<String>>` (letter -> words), is `toMap` or `groupingBy` the right tool? Explain.
74. [code] `mapping` nested in `groupingBy`: department -> **set** of distinct job titles.
75. [code] Build a frequency map (element -> count) two ways: `groupingBy`+`counting` and `toMap`+merge.

## Section F — Primitive streams & numeric aggregation (Q76–Q83)

76. [why] Why do `IntStream`/`LongStream`/`DoubleStream` exist — what performance problem do they solve?
77. [code] Sum of squares 1..100 using `IntStream`.
78. [why] Average of a `List<Integer>` via `mapToInt().average()` — what return type, and why that type?
79. [code] Convert `IntStream` -> `Stream<Integer>` (`boxed`) and `Stream<Integer>` -> `IntStream` (`mapToInt`).
80. [code] Count vowels in a `String` using `chars()`.
81. [code] Compute factorial of `n` using `IntStream.rangeClosed` + `reduce`.
82. [code] Generate 5 random doubles using `DoubleStream.generate` + `limit`.
83. [why] Use `IntStream` to iterate by index into a list — when is index-based iteration actually necessary vs a plain stream?

## Section G — Comparators & sorting (Q84–Q89)

84. [code] Sort `List<Person>` by lastName, then firstName (`Comparator.comparing` + `thenComparing`).
85. [code] Sort by age descending using `Comparator.reversed()`.
86. [code] Sort strings case-insensitively.
87. [code] Sort a `Map`'s entries by value descending and collect the keys into a `List`.
88. [code] While sorting, put `null` values last using `nullsLast`.
89. [why] `Comparator.comparingInt` vs `Comparator.comparing(...)` with an `Integer` key — why prefer `comparingInt`?

## Section H — Parallel streams (Q90–Q96)

90. [why] What is a parallel stream, and what are the two ways to create one?
91. [why] Which thread pool do parallel streams use by default, and why can that be a problem in a web server handling many requests?
92. [why] Name the properties a reduction must have to be correct in parallel (think: associativity, statelessness, non-interference).
93. [why] Why is `list.parallelStream().forEach(...)` order NOT guaranteed, and which operation restores order?
94. [bug] Show a broken parallel stream that adds elements to a shared `ArrayList`. Explain the race, and give the correct fix (`collect`).
95. [why] When does going parallel actually help (data size, per-element cost, source splittability), and when does it hurt (small N, `LinkedList` source, cheap ops)?
96. [why] Are `Collectors.toMap`/`groupingBy` safe in parallel? What are `groupingByConcurrent` and the UNORDERED characteristic about?

## Section I — Internals, pitfalls & tricky output (Q97–Q100)

97. [print] Over `Stream.iterate(1, n->n+1)` with a `peek` that prints, then `.map(n->n*n).limit(3)` — what gets generated and printed, and why doesn't it run forever?
98. [bug] Code reuses one `Stream` variable for two pipelines. What exception is thrown, and how do you fix it?
99. [bug] A `for-each` loop calls `list.remove(x)` while iterating. Which exception, why does it happen (fail-fast), and give two correct fixes.
100. [why] Why can side effects inside `map()`/`filter()` be dangerous under laziness and parallelism? State the "streams should be side-effect-free" principle and the one legitimate place for a side effect.

---

*Answer any range, then say "review Q1–Qn" and I'll grade + add notes.*
