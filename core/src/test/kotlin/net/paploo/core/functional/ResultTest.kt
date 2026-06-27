package net.paploo.core.functional

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe

class ResultTest : DescribeSpec({

    describe("flatMap") {

        val testException = Exception("failure made in test")

        it("should transform a success to a success") {
            val result = Result.success("hello").flatMap { Result.success(it.length) }
            result.shouldBeSuccess {
                it shouldBe 5
            }
        }

        it("should transform a success to a failure") {
            val result: Result<Int> = Result.success("hello").flatMap { Result.failure(testException) }
            result.shouldBeFailure(testException)
        }

        it("should preserve a failure") {
            val result: Result<Int> = Result.failure<String>(testException).flatMap { Result.success(it.length) }
            result.shouldBeFailure(testException)
        }
    }

})