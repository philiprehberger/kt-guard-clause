# guard-clause

[![Tests](https://github.com/philiprehberger/kt-guard-clause/actions/workflows/publish.yml/badge.svg)](https://github.com/philiprehberger/kt-guard-clause/actions/workflows/publish.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.philiprehberger/guard-clause.svg)](https://central.sonatype.com/artifact/com.philiprehberger/guard-clause)
[![Last updated](https://img.shields.io/github/last-commit/philiprehberger/kt-guard-clause)](https://github.com/philiprehberger/kt-guard-clause/commits/main)

Fluent guard clause validation for Kotlin with descriptive error messages.

## Installation

### Gradle (Kotlin DSL)

```kotlin
implementation("com.philiprehberger:guard-clause:0.2.0")
```

### Maven

```xml
<dependency>
    <groupId>com.philiprehberger</groupId>
    <artifactId>guard-clause</artifactId>
    <version>0.2.0</version>
</dependency>
```

## Usage

```kotlin
import com.philiprehberger.guardclause.*

fun createUser(name: String, age: Int, email: String) {
    guard(name, "name").notBlank().minLength(2).maxLength(50)
    guard(age, "age").positive().inRange(1.0, 150.0)
    guard(email, "email").notBlank().matches(Regex(".+@.+\\..+"))
    // proceed with valid data...
}
```

### Custom Predicates

```kotlin
guard(password, "password")
    .minLength(8)
    .must("must contain a digit") { it.any { c -> c.isDigit() } }
    .must("must contain uppercase") { it.any { c -> c.isUpperCase() } }
```

### Collect All Errors

```kotlin
val errors = guardAll {
    check(name, "name") { notBlank().minLength(2) }
    check(age, "age") { positive() }
    check(email, "email") { notBlank().matches(Regex(".+@.+")) }
}

if (errors.isNotEmpty()) {
    errors.forEach { println("${it.parameterName}: ${it.condition}") }
}
```

### Email and URL Validation

```kotlin
guard(email, "email").notBlank().isEmail()
guard(website, "website").notBlank().isUrl()
```

### String Content Guards

```kotlin
guard(filename, "filename")
    .startsWith("report_")
    .endsWith(".csv")
    .contains("2026")

guard(role, "role").isIn(listOf("admin", "user", "moderator"))
```

### Named Custom Validations

```kotlin
guard(password, "password")
    .minLength(8)
    .satisfies("must contain a digit") { it.any { c -> c.isDigit() } }
    .satisfies("must contain uppercase") { it.any { c -> c.isUpperCase() } }
```

## API

| Function / Class | Description |
|------------------|-------------|
| `guard(value, name)` | Create a fluent guard clause |
| `GuardClause.notNull()` | Assert value is not null |
| `GuardClause.notBlank()` | Assert string is not blank |
| `GuardClause.minLength(n)` | Assert minimum string length |
| `GuardClause.maxLength(n)` | Assert maximum string length |
| `GuardClause.matches(regex)` | Assert string matches pattern |
| `GuardClause.positive()` | Assert number is positive |
| `GuardClause.negative()` | Assert number is negative |
| `GuardClause.inRange(min, max)` | Assert number is in range |
| `GuardClause.notEmpty()` | Assert collection is not empty |
| `GuardClause.minSize(n)` | Assert minimum collection size |
| `GuardClause.maxSize(n)` | Assert maximum collection size |
| `GuardClause.must { }` | Custom predicate validation |
| `GuardClause.isEmail()` | Assert string is a valid email |
| `GuardClause.isUrl()` | Assert string is a valid URL |
| `GuardClause.startsWith(prefix)` | Assert string starts with prefix |
| `GuardClause.endsWith(suffix)` | Assert string ends with suffix |
| `GuardClause.contains(substring)` | Assert string contains substring |
| `GuardClause.isIn(collection)` | Assert value is in collection |
| `GuardClause.satisfies(name) { }` | Named custom predicate validation |
| `guardAll { }` | Collect all errors instead of fail-fast |
| `GuardException` | Thrown on validation failure |

## Development

```bash
./gradlew test       # Run tests
./gradlew check      # Run all checks
./gradlew build      # Build JAR
```

## Support

If you find this project useful:

⭐ [Star the repo](https://github.com/philiprehberger/kt-guard-clause)

🐛 [Report issues](https://github.com/philiprehberger/kt-guard-clause/issues?q=is%3Aissue+is%3Aopen+label%3Abug)

💡 [Suggest features](https://github.com/philiprehberger/kt-guard-clause/issues?q=is%3Aissue+is%3Aopen+label%3Aenhancement)

❤️ [Sponsor development](https://github.com/sponsors/philiprehberger)

🌐 [All Open Source Projects](https://philiprehberger.com/open-source-packages)

💻 [GitHub Profile](https://github.com/philiprehberger)

🔗 [LinkedIn Profile](https://www.linkedin.com/in/philiprehberger)

## License

[MIT](LICENSE)
