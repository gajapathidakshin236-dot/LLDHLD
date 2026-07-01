# Singleton (Refactoring.Guru)

This folder contains a minimal Java implementation of the **Singleton** pattern, based on Refactoring.Guru:
`https://refactoring.guru/design-patterns/singleton`

## Intent

Ensure that a class has only one instance, while providing a global access point to that instance.

## Mapping to this code

- **Singleton class**: `Singleton`
  - Private constructor
  - Lazy initialization
  - Thread-safety via double-checked locking (`volatile` + `synchronized`)
- **Client / demo**: `Main`

## Run

- **IDE**: Run `com.company.singleton.Main`
- **Maven** (requires JDK 21 + Maven):

```bash
mvn -q -DskipTests package
```

