# Prototype (Refactoring.Guru)

This folder contains a minimal Java implementation of the **Prototype** pattern, based on Refactoring.Guru:
`https://refactoring.guru/design-patterns/prototype`

## Intent

Create new objects by copying existing ones (prototypes), rather than instantiating classes directly.

## Mapping to this code

- **Prototype interface**: `Prototype<T>`
- **Base prototype**: `Shape` (copy-constructor supports common state)
- **Concrete prototypes**: `Circle`, `Rectangle`
- **Prototype registry**: `PrototypeRegistry`
- **Client / demo**: `Main`

## Run

- **IDE**: Run `com.company.prototype.Main`
- **Maven** (requires JDK 21 + Maven):

```bash
mvn -q -DskipTests package
```

