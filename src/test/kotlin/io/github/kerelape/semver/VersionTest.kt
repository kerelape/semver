package io.github.kerelape.semver

import org.cactoos.scalar.ScalarOf
import org.junit.jupiter.api.Test
import org.llorllale.cactoos.matchers.Assertion
import org.llorllale.cactoos.matchers.IsNumber
import org.llorllale.cactoos.matchers.IsText
import org.llorllale.cactoos.matchers.Throws

/**
 * Test for [Version].
 *
 * @since 0.1
 */
class VersionTest {
    @Test
    fun `resolves major`() =
        Assertion(
            "Resolves major segment of the version",
            Version("1.2.3").Major(),
            IsNumber(1),
        ).affirm()

    @Test
    fun `resolves minor`() =
        Assertion(
            "Resolves minor segment of the version",
            Version("1.2.3").Minor(),
            IsNumber(2),
        ).affirm()

    @Test
    fun `resolves patch`() =
        Assertion(
            "Resolves patch segment of the version",
            Version("1.2.3").Patch(),
            IsNumber(3),
        ).affirm()

    @Test
    fun `resolves pre-release`() =
        Assertion(
            "Resolves pre-release segment of the version",
            Version("1.2.3-alpha.1").PreRelease(),
            IsText("alpha.1"),
        ).affirm()

    @Test
    fun `resolves build`() =
        Assertion(
            "Resolves build segment of the version",
            Version("1.2.3+build").Build(),
            IsText("build"),
        ).affirm()

    @Test
    fun `fails to resolve build`() =
        Assertion(
            "Fails to resolve build segment on version without it",
            ScalarOf(Version("1.2.3-alpha.1").Build()::asString),
            Throws(
                "version segment not found",
                NoSuchElementException::class.java,
            ),
        ).affirm()

    @Test
    fun `fails to resolve pre-release`() =
        Assertion(
            "Fails to resolve pre-release segment on version without it",
            ScalarOf(Version("1.2.3+build").PreRelease()::asString),
            Throws(
                "version segment not found",
                NoSuchElementException::class.java,
            ),
        ).affirm()
}
