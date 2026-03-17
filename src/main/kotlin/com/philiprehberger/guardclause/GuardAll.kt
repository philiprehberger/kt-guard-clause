package com.philiprehberger.guardclause

/**
 * Represents a single guard validation error collected by [guardAll].
 *
 * @property parameterName the name of the parameter that failed validation
 * @property condition the violated condition description
 * @property actualValue the actual value that failed validation
 */
data class GuardError(
    val parameterName: String,
    val condition: String,
    val actualValue: Any?,
)

/**
 * Scope for collecting guard validations without failing fast.
 *
 * Unlike direct [guard] calls which throw on the first failure,
 * [GuardAllScope] collects all errors so they can be returned together.
 */
class GuardAllScope {
    private val errors = mutableListOf<GuardError>()

    /**
     * Validates the given [value] with [name] using the provided [block].
     * Any [GuardException] thrown inside [block] is caught and recorded as a [GuardError].
     *
     * @param T the type of the value being validated
     * @param value the value to validate
     * @param name the parameter name for error messages
     * @param block the validation logic using [GuardClause]
     */
    fun <T> check(value: T, name: String, block: GuardClause<T>.() -> Unit) {
        try {
            GuardClause(value, name).block()
        } catch (e: GuardException) {
            errors.add(GuardError(e.parameterName, e.condition, e.actualValue))
        }
    }

    internal fun toErrors(): List<GuardError> = errors.toList()
}

/**
 * Runs all guard validations inside [block], collecting errors instead of failing fast.
 *
 * Returns a list of [GuardError] for all failed validations, or an empty list if all passed.
 *
 * ```kotlin
 * val errors = guardAll {
 *     check(name, "name") { notBlank().minLength(2) }
 *     check(age, "age") { positive().inRange(0.0, 150.0) }
 * }
 * if (errors.isNotEmpty()) { /* handle errors */ }
 * ```
 *
 * @param block the validation block
 * @return a list of all [GuardError]s encountered
 */
fun guardAll(block: GuardAllScope.() -> Unit): List<GuardError> {
    val scope = GuardAllScope()
    scope.block()
    return scope.toErrors()
}
