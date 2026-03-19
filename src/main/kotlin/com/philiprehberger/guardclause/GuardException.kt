package com.philiprehberger.guardclause

/**
 * Exception thrown when a guard clause validation fails.
 *
 * The message follows the format: `"$name: $condition (was: $value)"`.
 *
 * @property parameterName the name of the parameter that failed validation
 * @property condition a description of the violated condition
 * @property actualValue the actual value that failed validation
 */
public class GuardException(
    public val parameterName: String,
    public val condition: String,
    public val actualValue: Any?,
) : IllegalArgumentException("$parameterName: $condition (was: $actualValue)")
