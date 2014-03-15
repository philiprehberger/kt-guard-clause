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
class GuardClause<T>(
    val value: T,
    val name: String,
) {

    /**
     * Asserts that the value is not null.
     *
     * @return this guard clause for chaining
     * @throws GuardException if the value is null
     */
    fun notNull(): GuardClause<T> {
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
    fun notBlank(): GuardClause<T> {
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
    fun minLength(min: Int): GuardClause<T> {
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
    fun maxLength(max: Int): GuardClause<T> {
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
    fun matches(regex: Regex): GuardClause<T> {
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
    fun positive(): GuardClause<T> {
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
    fun negative(): GuardClause<T> {
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
    fun inRange(min: Double, max: Double): GuardClause<T> {
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
    fun notEmpty(): GuardClause<T> {
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
    fun minSize(min: Int): GuardClause<T> {
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
    fun maxSize(max: Int): GuardClause<T> {
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
    fun must(message: String = "must satisfy custom condition", predicate: (T) -> Boolean): GuardClause<T> {
        if (!predicate(value)) {
            throw GuardException(name, message, value)
        }
        return this
    }
}
