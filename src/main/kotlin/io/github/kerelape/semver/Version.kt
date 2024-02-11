package io.github.kerelape.semver

import org.cactoos.Func
import org.cactoos.Scalar
import org.cactoos.Text
import org.cactoos.func.Chained
import org.cactoos.number.NumberEnvelope
import org.cactoos.number.NumberOf
import org.cactoos.scalar.Mapped
import org.cactoos.scalar.ScalarOf
import org.cactoos.scalar.Sticky
import org.cactoos.text.Flattened
import org.cactoos.text.TextEnvelope
import org.cactoos.text.TextOfScalar
import java.util.regex.Pattern
import org.cactoos.scalar.Flattened as FlattenedScalar

@Suppress("ktlint:standard:max-line-length")
private val pattern =
    Pattern.compile(
        "^v?(?<major>0|[1-9]\\d*)\\.(?<minor>0|[1-9]\\d*)\\.(?<patch>0|[1-9]\\d*)(?:-(?<prerelease>(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\\+(?<build>[0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?\$",
    )

/**
 * Version.
 *
 * Due to the [Semantic Versioning Specification](http://semver.org) -
 * however `major`, `minor`, and `patch` are always present, `prerelease`
 * and `build` may be absent - in this case a [NoSuchElementException] is thrown.
 *
 * In case the original string does not follow semantic versioning
 * format, this will throw an [IllegalArgumentException].
 *
 * @param[origin] String, that follows semantic versioning format.
 * @since 0.1
 */
class Version(origin: Scalar<String>) : Func<String, Text> {
    private val regex =
        Sticky(
            Mapped(
                Chained(
                    pattern::matcher,
                    Func { matcher ->
                        if (matcher.matches()) {
                            matcher
                        } else {
                            throw IllegalArgumentException(
                                "version does not follow semantic versioning format",
                            )
                        }
                    },
                ),
                origin,
            ),
        )

    /**
     * Version from a [Text].
     *
     * @param[origin] [Text] in semantic versioning format.
     */
    constructor(origin: Text) : this(ScalarOf(origin::asString))

    /**
     * Version from a [String].
     *
     * @param[origin] [String] in semantic versioning format.
     */
    constructor(origin: String) : this(Text { origin })

    /**
     * Major segment of the version.
     *
     * @since 0.1
     */
    inner class Major : NumberEnvelope(
        NumberOf(
            Flattened(
                Mapped(
                    this@Version,
                    Scalar { "major" },
                ),
            ),
        ),
    )

    /**
     * Minor segment of the version.
     *
     * @since 0.1
     */
    inner class Minor : NumberEnvelope(
        NumberOf(
            Flattened(
                Mapped(
                    this@Version,
                    Scalar { "minor" },
                ),
            ),
        ),
    )

    /**
     * Patch segment of the version.
     *
     * @since 0.1
     */
    inner class Patch : NumberEnvelope(
        NumberOf(
            Flattened(
                Mapped(
                    this@Version,
                    Scalar { "patch" },
                ),
            ),
        ),
    )

    /**
     * Pre-release segment of the version.
     *
     * In case the pre-release segment is absent in the version
     * a [NoSuchElementException] is thrown.
     *
     * @since 0.1
     */
    inner class PreRelease : TextEnvelope(
        Flattened(
            Mapped(
                this@Version,
                Scalar { "prerelease" },
            ),
        ),
    )

    /**
     * Build segment of the version.
     *
     * In case the build segment is absent in version
     * a [NoSuchElementException] is thrown.
     *
     * @since 0.1
     */
    inner class Build : TextEnvelope(
        Flattened(
            Mapped(
                this@Version,
                Scalar { "build" },
            ),
        ),
    )

    /**
     * @return Segment of the version by [name][segment].
     */
    override fun apply(segment: String): Text =
        TextOfScalar(
            FlattenedScalar(
                Mapped(
                    { matcher ->
                        Mapped(
                            Chained(
                                matcher::group,
                                Func { segment ->
                                    segment ?: throw NoSuchElementException(
                                        "version segment not found",
                                    )
                                },
                            ),
                            Scalar { segment },
                        )
                    },
                    this.regex,
                ),
            ),
        )
}
