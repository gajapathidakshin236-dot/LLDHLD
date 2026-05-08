# LLDHLD (Maven + Design Patterns)

## Structure

- `pom.xml`
- `src/main/java/com/company/abstractfactory/*`
- `src/main/java/com/company/builder/*`
- `src/main/java/com/company/prototype/*`

## Entry point

- `com.company.abstractfactory.Main`
- `com.company.builder.Main`
- `com.company.prototype.Main`

## Run (requires JDK 21 + Maven)

```bash
mvn -q -DskipTests package
```

If you want to run the `main` method from Maven, you can use your IDE (recommended) or add the `exec-maven-plugin`.
