# Changelog

## 0.1.5 (2026-03-22)

- Fix README compliance (badge label, installation format)

## 0.1.4 (2026-03-22)

- Standardize CHANGELOG format

## 0.1.3 (2026-03-20)

- Standardize README: fix title, badges, version sync, remove Requirements section

## 0.1.2 (2026-03-18)

- Upgrade to Kotlin 2.0.21 and Gradle 8.12
- Enable explicitApi() for stricter public API surface
- Add issueManagement to POM metadata

## 0.1.1 (2026-03-18)

- Fix CI badge and gradlew permissions

## 0.1.0 (2026-03-17)

### Added
- `guard(value, name)` entry point for fluent validation
- `GuardClause` with `notNull`, `notBlank`, `minLength`, `maxLength`, `matches`, `positive`, `negative`, `inRange`, `notEmpty`, `minSize`, `maxSize`, `must` validators
- `GuardException` with formatted error messages: "$name: $condition (was: $value)"
- `guardAll { }` for collecting all validation errors instead of fail-fast
- `GuardError` data class for collected errors
