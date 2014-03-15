package com.philiprehberger.guardclause

/**
 * Creates a [GuardClause] for the given [value] with the specified parameter [name].
 *
 * Use this as the entry point for fluent guard clause validation:
 * ```kotlin
 * guard(username, "username").notBlank().minLength(3).maxLength(50)
 * ```
 *
 * @param T the type of the value being validated
 * @param value the value to validate
 * @param name the parameter name for error messages
 * @return a [GuardClause] for fluent validation
 */
fun <T> guard(value: T, name: String): GuardClause<T> = GuardClause(value, name)
