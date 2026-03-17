package com.philiprehberger.guardclause

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class GuardClauseTest {

    @Test
    fun `notNull passes for non-null value`() {
        guard("hello", "param").notNull()
    }

    @Test
    fun `notNull fails for null value`() {
        val ex = assertFailsWith<GuardException> {
            guard(null, "param").notNull()
        }
        assertEquals("param", ex.parameterName)
        assertTrue(ex.message!!.contains("must not be null"))
    }

    @Test
    fun `notBlank passes for non-blank string`() {
        guard("hello", "name").notBlank()
    }

    @Test
    fun `notBlank fails for blank string`() {
        val ex = assertFailsWith<GuardException> {
            guard("  ", "name").notBlank()
        }
        assertEquals("name", ex.parameterName)
        assertTrue(ex.message!!.contains("must not be blank"))
    }

    @Test
    fun `minLength and maxLength pass for valid length`() {
        guard("hello", "name").minLength(3).maxLength(10)
    }

    @Test
    fun `minLength fails for short string`() {
        assertFailsWith<GuardException> {
            guard("ab", "name").minLength(3)
        }
    }

    @Test
    fun `maxLength fails for long string`() {
        assertFailsWith<GuardException> {
            guard("abcdefghijk", "name").maxLength(5)
        }
    }

    @Test
    fun `matches passes for matching pattern`() {
        guard("test@example.com", "email").matches(Regex(".+@.+\\..+"))
    }

    @Test
    fun `matches fails for non-matching pattern`() {
        assertFailsWith<GuardException> {
            guard("not-an-email", "email").matches(Regex(".+@.+\\..+"))
        }
    }

    @Test
    fun `positive passes for positive number`() {
        guard(42, "count").positive()
    }

    @Test
    fun `positive fails for zero`() {
        assertFailsWith<GuardException> {
            guard(0, "count").positive()
        }
    }

    @Test
    fun `negative passes for negative number`() {
        guard(-5, "offset").negative()
    }

    @Test
    fun `negative fails for positive number`() {
        assertFailsWith<GuardException> {
            guard(1, "offset").negative()
        }
    }

    @Test
    fun `inRange passes for value in range`() {
        guard(5, "score").inRange(1.0, 10.0)
    }

    @Test
    fun `inRange fails for value out of range`() {
        assertFailsWith<GuardException> {
            guard(15, "score").inRange(1.0, 10.0)
        }
    }

    @Test
    fun `notEmpty passes for non-empty collection`() {
        guard(listOf(1, 2, 3), "items").notEmpty()
    }

    @Test
    fun `notEmpty fails for empty collection`() {
        assertFailsWith<GuardException> {
            guard(emptyList<Int>(), "items").notEmpty()
        }
    }

    @Test
    fun `minSize and maxSize pass for valid collection size`() {
        guard(listOf(1, 2, 3), "items").minSize(2).maxSize(5)
    }

    @Test
    fun `minSize fails for small collection`() {
        assertFailsWith<GuardException> {
            guard(listOf(1), "items").minSize(3)
        }
    }

    @Test
    fun `maxSize fails for large collection`() {
        assertFailsWith<GuardException> {
            guard(listOf(1, 2, 3, 4, 5), "items").maxSize(3)
        }
    }

    @Test
    fun `must passes for satisfied predicate`() {
        guard("hello", "greeting").must("must start with h") { it.startsWith("h") }
    }

    @Test
    fun `must fails for unsatisfied predicate`() {
        val ex = assertFailsWith<GuardException> {
            guard("world", "greeting").must("must start with h") { it.startsWith("h") }
        }
        assertTrue(ex.message!!.contains("must start with h"))
    }

    @Test
    fun `chaining multiple guards works`() {
        guard("hello@world.com", "email")
            .notBlank()
            .minLength(5)
            .maxLength(100)
            .matches(Regex(".+@.+\\..+"))
    }

    @Test
    fun `error message format is correct`() {
        val ex = assertFailsWith<GuardException> {
            guard("", "username").notBlank()
        }
        assertEquals("username: must not be blank (was: )", ex.message)
    }

    @Test
    fun `guardAll collects multiple errors`() {
        val errors = guardAll {
            check("", "name") { notBlank() }
            check(-5, "age") { positive() }
            check(emptyList<Int>(), "items") { notEmpty() }
        }
        assertEquals(3, errors.size)
        assertEquals("name", errors[0].parameterName)
        assertEquals("age", errors[1].parameterName)
        assertEquals("items", errors[2].parameterName)
    }

    @Test
    fun `guardAll returns empty list when all pass`() {
        val errors = guardAll {
            check("hello", "name") { notBlank() }
            check(5, "age") { positive() }
        }
        assertTrue(errors.isEmpty())
    }
}
