# Streams & Collections — Practice Track

Interview practice, easy -> hard, as **runnable Java** with an auto-grading harness.

## Files

```
com/company/streams/
  README.md              <- this index
  NOTES.md               <- teaching notes (Lesson 1 so far)
  QUESTIONS.md           <- the 100 questions in plain text (reference)
  L01_Foundations.java   <- Lesson-1 DEMO (run + read the output)
  Check.java             <- shared test harness (used by all P-files)
  PracticeModels.java    <- sample data (Person, Employee) used by harnesses
  P01_Practice.java      <- Lesson-1 warmup (6 questions)
  P02_Collections.java   <- Section A  Q1-Q12   collections choice & foundations
  P03_Pipeline.java      <- Section B  Q13-Q20  stream creation & pipeline model
  P04_Intermediate.java  <- Section C  Q21-Q40  filter/map/flatMap/sorted/limit...
  P05_Terminal.java      <- Section D  Q41-Q55  reduce/count/min/max/Optional
  P06_Collectors.java    <- Section E  Q56-Q75  groupingBy/toMap/partitioning...
  P07_Primitives.java    <- Section F  Q76-Q83  IntStream & numeric aggregation
  P08_Comparators.java   <- Section G  Q84-Q89  comparator sorting
  P09_Parallel.java      <- Section H  Q90-Q96  parallel streams
  P10_Internals.java     <- Section I  Q97-Q100 laziness, pitfalls, fail-fast
```

## How to answer
Each question is a stub method. Fill in the body. Two kinds:
- **[code]** stubs return a placeholder -> implement so the harness prints `PASS`.
- **[why]/[print]** stubs return `""` -> write your answer in the String; it shows as
  `[SELF]` (printed for review, not auto-graded).

## How to run
**IntelliJ:** open any `Pnn_*` file and click the green Run. You'll see PASS/FAIL per question
and a summary line: `AUTO-GRADED: x/y | THEORY answers to review: z`.

**Command line (from repo root):**
```
javac -d out src/main/java/com/company/streams/*.java
java -cp out com.company.streams.P04_Intermediate
```

## The loop
1. Pick a section file, fill in the stubs.
2. Run it; turn the [code] ones green.
3. Tell me "review P04" (or "review Q56-Q75") and I'll grade your [code] + [SELF]
   answers, fix what's wrong, and add teaching notes to NOTES.md.
4. Next section.

Start with **P02** and **P03** (the on-ramp), or jump to whichever concept you want to drill.
