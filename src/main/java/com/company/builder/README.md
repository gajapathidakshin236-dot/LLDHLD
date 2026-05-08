# Builder (Refactoring.Guru)

This folder contains a minimal Java implementation of the **Builder** pattern, based on Refactoring.Guru:
`https://refactoring.guru/design-patterns/builder`

## Intent

Construct complex objects step by step. The same construction process can create different representations.

## Mapping to this code

- **Builder interface**: `Builder`
- **Concrete builders**:
  - `CarBuilder` → builds a `Car`
  - `CarManualBuilder` → builds a `Manual`
- **Director** (construction recipes): `Director`
- **Products**: `Car`, `Manual`
- **Client / demo**: `Main`

## Run

- **IDE**: Run `com.company.builder.Main`
- **Maven** (requires JDK 21 + Maven):

```bash
mvn -q -DskipTests package
```

