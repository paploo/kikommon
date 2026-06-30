package net.paploo.core.extensions

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ListTest : DescribeSpec({

    describe("zipFull") {

        it("should zip two lists of the same length") {
            val list1 = listOf("tsukinami", "matsuri", "shinto")
            val list2 = listOf("inari", "jinja", "tsukinami")

            val result = list1 zipFull list2

            result shouldBe listOf(
                "tsukinami" to "inari",
                "matsuri" to "jinja",
                "shinto" to "tsukinami"
            )
        }

        it("should zip a shorter first list with a longer second list, using null for missing values") {
            val list1 = listOf("tsukinami", "matsuri")
            val list2 = listOf("inari", "jinja", "shinto", "tsukinami")

            val result = list1 zipFull list2

            result shouldBe listOf(
                "tsukinami" to "inari",
                "matsuri" to "jinja",
                null to "shinto",
                null to "tsukinami"
            )
        }

        it("should zip a longer first list with a shorter second list, using null for missing values") {
            val list1 = listOf("tsukinami", "matsuri", "shinto", "inari")
            val list2 = listOf("jinja", "tsukinami")

            val result = list1 zipFull list2

            result shouldBe listOf(
                "tsukinami" to "jinja",
                "matsuri" to "tsukinami",
                "shinto" to null,
                "inari" to null
            )
        }

        it("should zip an empty list to a non-empty list resulting in pairs whose first value is always null") {
            val list1 = emptyList<String>()
            val list2 = listOf("tsukinami", "matsuri", "shinto")

            val result = list1 zipFull list2

            result shouldBe listOf(
                null to "tsukinami",
                null to "matsuri",
                null to "shinto"
            )
        }

        it("should zip a non-empty list with an empty list, resulting in pairs whose last value is always null") {
            val list1 = listOf("tsukinami", "matsuri", "shinto")
            val list2 = emptyList<String>()

            val result = list1 zipFull list2

            result shouldBe listOf(
                "tsukinami" to null,
                "matsuri" to null,
                "shinto" to null
            )
        }

        it("should zip two empty lists producing an empty list") {
            val list1 = emptyList<String>()
            val list2 = emptyList<String>()

            val result = list1 zipFull list2

            result shouldBe emptyList()
        }

        it("should zip two lists composed only of nulls into a list of null pairs") {
            val list1 = listOf<String?>(null, null, null)
            val list2 = listOf<String?>(null, null, null)

            val result = list1 zipFull list2

            result shouldBe listOf(
                null to null,
                null to null,
                null to null
            )
        }

    }

    describe("zipFull with default") {

        it("should use the default when the lists are uneven") {
            val list1 = listOf("tsukinami", "matsuri")
            val list2 = listOf("inari", "jinja", "shinto", "tsukinami")

            val result1: List<Pair<String, String>> = list1.zipFull(list2) { "祭" }
            result1 shouldBe listOf(
                "tsukinami" to "inari",
                "matsuri" to "jinja",
                "祭" to "shinto",
                "祭" to "tsukinami"
            )

            val result2: List<Pair<String, String>> = list2.zipFull(list1) { "祭" }
            result2 shouldBe listOf(
                "inari" to "tsukinami",
                "jinja" to "matsuri",
                "shinto" to "祭",
                "tsukinami" to "祭"
            )
        }

        it("should allow null as the default, transmuting a non-null type to be nullable") {
            val list1: List<String> = listOf("tsukinami", "matsuri")
            val list2: List<String> = listOf("inari", "jinja", "shinto", "tsukinami")

            val result: List<Pair<String?, String?>> = list1.zipFull(list2) { null }
            result shouldBe listOf(
                "tsukinami" to "inari",
                "matsuri" to "jinja",
                null to "shinto",
                null to "tsukinami"
            )
        }

        it("should work with lists with nulls") {
            val list1 = listOf<String?>(null, null, null)
            val list2 = listOf<String?>(null, null)

            list1.zipFull(list2) { "祭" } shouldBe listOf(
                null to null,
                null to null,
                null to "祭"
            )
        }

    }

})