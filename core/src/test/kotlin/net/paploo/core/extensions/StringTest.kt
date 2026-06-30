package net.paploo.core.extensions

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class StringTest : DescribeSpec({

    describe("case conversion") {

        val snakeCaseWords = listOf("red_blue_green", "url", "user_v1")
        val kebabCaseWords = listOf("red-blue-green", "url", "user-v1")
        val constCaseWords = listOf("RED_BLUE_GREEN", "URL", "USER_V1")
        val camelCaseWords = listOf("redBlueGreen", "url", "userV1")
        val pascalCaseWords = listOf("RedBlueGreen", "Url", "UserV1")

        val wordLists = mapOf(
            "snakeCase" to snakeCaseWords,
            "kebabCase" to kebabCaseWords,
            "constCaseWords" to constCaseWords,
            "camelCaseWords" to camelCaseWords,
            "pascalCaseWords" to pascalCaseWords
        )

        describe("snakeCase") {
            wordLists.forEach { (name, words) ->
                it("Should convert words from $name to snakeCase") {
                    words.map { it.snakeCase() } shouldBe snakeCaseWords
                }
            }
        }

        describe("kebabCase") {
            wordLists.forEach { (name, words) ->
                it("Should convert words from $name to kebabCase") {
                    words.map { it.kebabCase() } shouldBe kebabCaseWords
                }
            }
        }

        describe("constCase") {
            wordLists.forEach { (name, words) ->
                it("Should convert words from $name to constCase") {
                    words.map { it.constCase() } shouldBe constCaseWords
                }
            }
        }

        describe("camelCase") {
            wordLists.forEach { (name, words) ->
                it("Should convert words from $name to camelCase") {
                    words.map { it.camelCase() } shouldBe camelCaseWords
                }
            }
        }

        describe("pascalCase") {
            wordLists.forEach { (name, words) ->
                it("Should convert words from $name to pascalCase") {
                    words.map { it.pascalCase() } shouldBe pascalCaseWords
                }
            }
        }

    }

})