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

    @Test
    fun `isEmail passes for valid email`() {
        guard("user@example.com", "email").isEmail()
    }

    @Test
    fun `isEmail fails for invalid email`() {
        assertFailsWith<GuardException> {
            guard("not-an-email", "email").isEmail()
        }
    }

    @Test
    fun `isEmail fails for email without domain`() {
        assertFailsWith<GuardException> {
            guard("user@", "email").isEmail()
        }
    }

    @Test
    fun `isUrl passes for valid http URL`() {
        guard("http://example.com", "url").isUrl()
    }

    @Test
    fun `isUrl passes for valid https URL with path`() {
        guard("https://example.com/path/to/resource", "url").isUrl()
    }

    @Test
    fun `isUrl passes for URL with port`() {
        guard("http://localhost:8080/api", "url").isUrl()
    }

    @Test
    fun `isUrl fails for invalid URL`() {
        assertFailsWith<GuardException> {
            guard("not-a-url", "url").isUrl()
        }
    }

    @Test
    fun `startsWith passes when string starts with prefix`() {
        guard("hello world", "greeting").startsWith("hello")
    }

    @Test
    fun `startsWith fails when string does not start with prefix`() {
        val ex = assertFailsWith<GuardException> {
            guard("world hello", "greeting").startsWith("hello")
        }
        assertTrue(ex.message!!.contains("must start with"))
    }

    @Test
    fun `endsWith passes when string ends with suffix`() {
        guard("hello world", "greeting").endsWith("world")
    }

    @Test
    fun `endsWith fails when string does not end with suffix`() {
        assertFailsWith<GuardException> {
            guard("hello world", "greeting").endsWith("hello")
        }
    }

    @Test
    fun `contains passes when string contains substring`() {
        guard("hello world", "greeting").contains("lo wo")
    }

    @Test
    fun `contains fails when string does not contain substring`() {
        assertFailsWith<GuardException> {
            guard("hello world", "greeting").contains("xyz")
        }
    }

    @Test
    fun `isIn passes when value is in collection`() {
        guard("admin", "role").isIn(listOf("admin", "user", "moderator"))
    }

    @Test
    fun `isIn fails when value is not in collection`() {
        assertFailsWith<GuardException> {
            guard("superadmin", "role").isIn(listOf("admin", "user", "moderator"))
        }
    }

    @Test
    fun `isIn works with numbers`() {
        guard(5, "priority").isIn(listOf(1, 2, 3, 5, 8))
    }

    @Test
    fun `satisfies passes when predicate is true`() {
        guard("ABC123", "code").satisfies("must be alphanumeric") { it.all { c -> c.isLetterOrDigit() } }
    }

    @Test
    fun `satisfies fails with custom validation name`() {
        val ex = assertFailsWith<GuardException> {
            guard("abc 123", "code").satisfies("must be alphanumeric") { it.all { c -> c.isLetterOrDigit() } }
        }
        assertTrue(ex.message!!.contains("must be alphanumeric"))
    }

    @Test
    fun `new guards chain with existing guards`() {
        guard("user@example.com", "email")
            .notBlank()
            .isEmail()
            .minLength(5)
            .maxLength(100)
            .contains("@")
    }
}
