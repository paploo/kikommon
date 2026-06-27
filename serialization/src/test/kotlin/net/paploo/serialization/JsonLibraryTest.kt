package net.paploo.serialization

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException

class JsonLibraryTest : DescribeSpec({

    describe("application") {
        val json = JsonLibrary.application

        it("should encode json") {
            val box = Box(42)
            val encoded = json.encodeToString(box)
            encoded shouldBe """{"value":42}"""
        }

        it("should decode json") {
            val encoded = """{"value":42}"""
            val box = json.decodeFromString<Box<Int>>(encoded)
            box.value shouldBe 42
        }

        it("should decode json with an unknown key") {
            val encoded = """{"value":42,"unknownKey":-101}"""
            val box = json.decodeFromString<Box<Int>>(encoded)
            box.value shouldBe 42
        }
    }

    describe("application with config") {

        it("should allow specifying additional config") {
            val json = JsonLibrary.application {
                prettyPrint = true
            }

            val point = Point(1.0, 2.0)
            val encoded = json.encodeToString(point)
            encoded shouldBe """
            {
                "x": 1.0,
                "y": 2.0
            }
            """.trimIndent()
        }

        it("should allow overriding its set config") {
            val json = JsonLibrary.application {
                ignoreUnknownKeys = false
            }

            val encoded = """{"x":1.0,"y":2.0,"unknownKey":42}"""

            shouldThrow<SerializationException> {
                //We told it to no longer ignore unknown keys
                json.decodeFromString<Point>(encoded)
            }
        }
    }
}) {

    @Serializable
    data class Box<T>(val value: T)

    @Serializable
    data class Point(val x: Double, val y: Double)

}