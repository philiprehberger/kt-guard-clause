package com.philiprehberger.guardclause

/**
 * Fluent guard clause that validates a value and throws [GuardException] on failure.
 *
 * All validation methods return `this` for chaining:
 * ```kotlin
 * guard(email, "email").notBlank().matches(Regex(".+@.+")).maxLength(255)
 * ```
 *
 * @param T the type of the value being validated
 * @property value the value to validate
 * @property name the parameter name for error messages
 */
public class GuardClause<T>(
    public val value: T,
    public val name: String,
) {

    /**
     * Asserts that the value is not null.
     *
     * @return this guard clause for chaining
     * @throws GuardException if the value is null
     */
    public fun notNull(): GuardClause<T> {
        if (value == null) {
            throw GuardException(name, "must not be null", value)
        }
        return this
    }

    /**
     * Asserts that the value (as a [String]) is not blank.
     *
     * @return this guard clause for chaining
     * @throws GuardException if the value is blank
     */
    public fun notBlank(): GuardClause<T> {
        val str = value as? String
            ?: throw GuardException(name, "must be a String for notBlank", value)
        if (str.isBlank()) {
            throw GuardException(name, "must not be blank", value)
        }
        return this
    }

    /**
     * Asserts that the string value has at least [min] characters.
     *
     * @param min the minimum length
     * @return this guard clause for chaining
     * @throws GuardException if the string is shorter than [min]
     */
    public fun minLength(min: Int): GuardClause<T> {
        val str = value as? String
            ?: throw GuardException(name, "must be a String for minLength", value)
        if (str.length < min) {
            throw GuardException(name, "must have at least $min characters", value)
        }
        return this
    }

    /**
     * Asserts that the string value has at most [max] characters.
     *
     * @param max the maximum length
     * @return this guard clause for chaining
     * @throws GuardException if the string is longer than [max]
     */
    public fun maxLength(max: Int): GuardClause<T> {
        val str = value as? String
            ?: throw GuardException(name, "must be a String for maxLength", value)
        if (str.length > max) {
            throw GuardException(name, "must have at most $max characters", value)
        }
        return this
    }

    /**
     * Asserts that the string value matches the given [regex].
     *
     * @param regex the regular expression to match against
     * @return this guard clause for chaining
     * @throws GuardException if the string does not match
     */
    public fun matches(regex: Regex): GuardClause<T> {
        val str = value as? String
            ?: throw GuardException(name, "must be a String for matches", value)
        if (!regex.matches(str)) {
            throw GuardException(name, "must match pattern ${regex.pattern}", value)
        }
        return this
    }

    /**
     * Asserts that the numeric value is positive (greater than zero).
     *
     * @return this guard clause for chaining
     * @throws GuardException if the value is not positive
     */
    public fun positive(): GuardClause<T> {
        val num = value as? Number
            ?: throw GuardException(name, "must be a Number for positive", value)
        if (num.toDouble() <= 0) {
            throw GuardException(name, "must be positive", value)
        }
        return this
    }

    /**
     * Asserts that the numeric value is negative (less than zero).
     *
     * @return this guard clause for chaining
     * @throws GuardException if the value is not negative
     */
    public fun negative(): GuardClause<T> {
        val num = value as? Number
            ?: throw GuardException(name, "must be a Number for negative", value)
        if (num.toDouble() >= 0) {
            throw GuardException(name, "must be negative", value)
        }
        return this
    }

    /**
     * Asserts that the numeric value is within the range [min]..[max] (inclusive).
     *
     * @param min the minimum value (inclusive)
     * @param max the maximum value (inclusive)
     * @return this guard clause for chaining
     * @throws GuardException if the value is outside the range
     */
    public fun inRange(min: Double, max: Double): GuardClause<T> {
        val num = value as? Number
            ?: throw GuardException(name, "must be a Number for inRange", value)
        val d = num.toDouble()
        if (d < min || d > max) {
            throw GuardException(name, "must be in range $min..$max", value)
        }
        return this
    }

    /**
     * Asserts that the collection value is not empty.
     *
     * @return this guard clause for chaining
     * @throws GuardException if the collection is empty
     */
    public fun notEmpty(): GuardClause<T> {
        val col = value as? Collection<*>
            ?: throw GuardException(name, "must be a Collection for notEmpty", value)
        if (col.isEmpty()) {
            throw GuardException(name, "must not be empty", value)
        }
        return this
    }

    /**
     * Asserts that the collection value has at least [min] elements.
     *
     * @param min the minimum number of elements
     * @return this guard clause for chaining
     * @throws GuardException if the collection has fewer than [min] elements
     */
    public fun minSize(min: Int): GuardClause<T> {
        val col = value as? Collection<*>
            ?: throw GuardException(name, "must be a Collection for minSize", value)
        if (col.size < min) {
            throw GuardException(name, "must have at least $min elements", value)
        }
        return this
    }

    /**
     * Asserts that the collection value has at most [max] elements.
     *
     * @param max the maximum number of elements
     * @return this guard clause for chaining
     * @throws GuardException if the collection has more than [max] elements
     */
    public fun maxSize(max: Int): GuardClause<T> {
        val col = value as? Collection<*>
            ?: throw GuardException(name, "must be a Collection for maxSize", value)
        if (col.size > max) {
            throw GuardException(name, "must have at most $max elements", value)
        }
        return this
    }

    /**
     * Asserts that the value satisfies the given custom [predicate].
     *
     * @param message the error message if the predicate fails
     * @param predicate the custom validation predicate
     * @return this guard clause for chaining
     * @throws GuardException if the predicate returns false
     */
    public fun must(message: String = "must satisfy custom condition", predicate: (T) -> Boolean): GuardClause<T> {
        if (!predicate(value)) {
            throw GuardException(name, message, value)
        }
        return this
    }

    /**
     * Asserts that the string value is a valid email address.
     *
     * Uses a standard email pattern: local@domain with at least one dot in the domain.
     *
     * @return this guard clause for chaining
     * @throws GuardException if the value is not a valid email
     */
    public fun isEmail(): GuardClause<T> {
        val str = value as? String
            ?: throw GuardException(name, "must be a String for isEmail", value)
        val emailPattern = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        if (!emailPattern.matches(str)) {
            throw GuardException(name, "must be a valid email address", value)
        }
        return this
    }

    /**
     * Asserts that the string value is a valid URL.
     *
     * Accepts http and https URLs with a domain.
     *
     * @return this guard clause for chaining
     * @throws GuardException if the value is not a valid URL
     */
    public fun isUrl(): GuardClause<T> {
        val str = value as? String
            ?: throw GuardException(name, "must be a String for isUrl", value)
        val urlPattern = Regex("^https?://[A-Za-z0-9.-]+(:[0-9]+)?(/.*)?$")
        if (!urlPattern.matches(str)) {
            throw GuardException(name, "must be a valid URL", value)
        }
        return this
    }

    /**
     * Asserts that the string value starts with the given [prefix].
     *
     * @param prefix the required prefix
     * @return this guard clause for chaining
     * @throws GuardException if the value does not start with the prefix
     */
    public fun startsWith(prefix: String): GuardClause<T> {
        val str = value as? String
            ?: throw GuardException(name, "must be a String for startsWith", value)
        if (!str.startsWith(prefix)) {
            throw GuardException(name, "must start with \"$prefix\"", value)
        }
        return this
    }

    /**
     * Asserts that the string value ends with the given [suffix].
     *
     * @param suffix the required suffix
     * @return this guard clause for chaining
     * @throws GuardException if the value does not end with the suffix
     */
    public fun endsWith(suffix: String): GuardClause<T> {
        val str = value as? String
            ?: throw GuardException(name, "must be a String for endsWith", value)
        if (!str.endsWith(suffix)) {
            throw GuardException(name, "must end with \"$suffix\"", value)
        }
        return this
    }

    /**
     * Asserts that the string value contains the given [substring].
     *
     * @param substring the required substring
     * @return this guard clause for chaining
     * @throws GuardException if the value does not contain the substring
     */
    public fun contains(substring: String): GuardClause<T> {
        val str = value as? String
            ?: throw GuardException(name, "must be a String for contains", value)
        if (substring !in str) {
            throw GuardException(name, "must contain \"$substring\"", value)
        }
        return this
    }

    /**
     * Asserts that the value is a member of the given [collection].
     *
     * @param collection the allowed values
     * @return this guard clause for chaining
     * @throws GuardException if the value is not in the collection
     */
    public fun isIn(collection: Collection<T>): GuardClause<T> {
        if (value !in collection) {
            throw GuardException(name, "must be one of $collection", value)
        }
        return this
    }

    /**
     * Asserts that the value satisfies a named custom [predicate].
     *
     * Unlike [must], this provides a more descriptive validation name for error messages.
     *
     * @param validationName the name of the validation for error messages
     * @param predicate the custom validation predicate
     * @return this guard clause for chaining
     * @throws GuardException if the predicate returns false
     */
    public fun satisfies(validationName: String, predicate: (T) -> Boolean): GuardClause<T> {
        if (!predicate(value)) {
            throw GuardException(name, validationName, value)
        }
        return this
    }
}
