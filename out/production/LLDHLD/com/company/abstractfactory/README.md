# Abstract Factory (Refactoring.Guru)

This folder contains a minimal Java implementation of the **Abstract Factory** pattern, based on the explanation from Refactoring.Guru:
`https://refactoring.guru/design-patterns/abstract-factory`

## Intent

Provide an interface for creating **families of related objects** (products) without specifying their concrete classes.

## Mapping to this code

- **Abstract products**: `Button`, `Checkbox`
- **Concrete products**:
  - Windows family: `WindowsButton`, `WindowsCheckbox`
  - Mac family: `MacButton`, `MacCheckbox`
- **Abstract factory**: `GUIFactory`
- **Concrete factories**: `WindowsFactory`, `MacFactory`
- **Client**: `Application` (depends only on abstractions)
- **Composition root / wiring**: `Main` (picks a factory based on `os.name`)

## How it runs

`Main` chooses a factory depending on `System.getProperty("os.name")`, then builds an `Application` with that factory and calls `render()`.

## Run

- **IDE**: Run `com.company.abstractfactory.Main`
- **Maven** (requires JDK 21 + Maven):

```bash
mvn -q -DskipTests package
```
