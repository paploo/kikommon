package net.paploo.core.functional.extensions

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import net.paploo.core.extensions.finally
import net.paploo.core.extensions.finallyCatching
import net.paploo.core.extensions.flatFinally
import net.paploo.core.extensions.flatFinallyCatching
import net.paploo.core.extensions.flatMap
import net.paploo.core.extensions.flatMapCatching
import net.paploo.core.extensions.flatRunCatching
import net.paploo.core.extensions.flatten
import net.paploo.core.extensions.sequenceToNullable
import net.paploo.core.extensions.sequenceToResult

class ResultTest : DescribeSpec({

    val testException = Exception("ヤバっ")

    describe("flatRunCatching") {

        it("should return a success") {
            val result = Result.flatRunCatching { Result.success("こんにちは") }
            result.shouldBeSuccess {
                it shouldBe "こんにちは"
            }
        }

        it("should return a failure") {
            val result = Result.flatRunCatching { Result.failure<String>(testException) }
            result.shouldBeFailure {
                it shouldBe testException
            }
        }

        it("should catch an exception thrown in flat run") {
            val result: Result<String> = Result.flatRunCatching { throw testException }
            result.shouldBeFailure {
                it shouldBe testException
            }
        }

    }

    describe("flatten") {

        it("should flatten a success of a success") {
            val result: Result<Result<String>> = Result.success(Result.success("こんにちは"))
            result.flatten().shouldBeSuccess {
                it shouldBe "こんにちは"
            }
        }

        it("should flatten a success of a failure") {
            val result: Result<Result<String>> = Result.success(Result.failure(testException))
            result.flatten().shouldBeFailure {
                it shouldBe testException
            }
        }

        it("should flatten a failure") {
            val result: Result<Result<String>> = Result.failure(testException)
            result.flatten().shouldBeFailure {
                it shouldBe testException
            }
        }

    }

    describe("flatMap") {

        it("should transform a success to a success") {
            val result = Result.success("こんにちは").flatMap { Result.success(it.length) }
            result.shouldBeSuccess {
                it shouldBe 5
            }
        }

        it("should transform a success to a failure") {
            val result: Result<Int> = Result.success("こんにちは").flatMap { Result.failure(testException) }
            result.shouldBeFailure(testException)
        }

        it("should preserve a failure") {
            val result: Result<Int> = Result.failure<String>(testException).flatMap { Result.success(it.length) }
            result.shouldBeFailure(testException)
        }

        it("should not trap exceptions thrown by the transform") {
            shouldThrow<Exception> {
                Result.success("こんにちは").flatMap<String, Int> { throw testException }
            } shouldBe testException
        }
    }

    describe("flatMapCatching") {

        it("should trap any exceptions thrown by the transform") {
            val result: Result<Int> = Result.success("こんにちは").flatMapCatching { throw testException }
            result.shouldBeFailure(testException)
        }

    }

    describe("finally") {

        it("should run the finally after success") {
            var isClosed = false
            val result = Result.success("こんにちは").finally {
                isClosed = true
            }

            isClosed.shouldBeTrue()
            result.shouldBeSuccess("こんにちは")
        }

        it("should run the finally after failure") {
            var isClosed = false
            val result = Result.failure<String>(testException).finally {
                isClosed = true
            }

            isClosed.shouldBeTrue()
            result.shouldBeFailure(testException)
        }

        it("should not capture the error from the block if it fails on a success") {
            shouldThrow<Exception> {
                Result.success("こんにちは").finally {
                    throw testException
                }
            } shouldBe testException
        }

        it("should not capture the error from the block if it fails on a failure") {
            shouldThrow<Exception> {
                Result.failure<String>(testException).finally {
                    throw testException
                }
            } shouldBe testException
        }

    }

    describe("flatFinally") {

        it("should run the finally after success") {
            var isClosed = false
            val result = Result.success("こんにちは").flatFinally {
                isClosed = true
                Result.success("closed")
            }

            isClosed.shouldBeTrue()
            result.shouldBeSuccess("こんにちは")
        }

        it("should run the finally after failure") {
            var isClosed = false
            val result = Result.failure<String>(testException).flatFinally {
                isClosed = true
                Result.success("closed")
            }

            isClosed.shouldBeTrue()
            result.shouldBeFailure(testException)
        }

        it("should return the error from the finally if it fails on a success") {
            val result = Result.success("こんにちは").flatFinally {
                Result.failure(testException)
            }
            result.shouldBeFailure(testException)
        }

        it("should return the error from the finally if it fails on a failure") {
            val result = Result.failure<String>(testException).flatFinally {
                Result.failure(testException)
            }
            result.shouldBeFailure(testException)
        }

    }

    describe("finallyCatching") {

        it("should run the finally after success") {
            var isClosed = false
            val result = Result.success("こんにちは").finallyCatching {
                isClosed = true
            }

            isClosed.shouldBeTrue()
            result.shouldBeSuccess("こんにちは")
        }

        it("should run the finally after failure") {
            var isClosed = false
            val result = Result.failure<String>(testException).finallyCatching {
                isClosed = true
            }

            isClosed.shouldBeTrue()
            result.shouldBeFailure(testException)
        }

        it("should capture and return the error from the finally if it fails on a success") {
            val result = Result.success("こんにちは").finallyCatching {
                throw testException
            }
            result.shouldBeFailure(testException)
        }

        it("should capture and return the error from the finally if it fails on a failure") {
            val result = Result.failure<String>(testException).finallyCatching {
                throw testException
            }
            result.shouldBeFailure(testException)
        }
    }

    describe("flatFinallyCatching") {

        it("should run the finally after success") {
            var isClosed = false
            val result = Result.success("こんにちは").flatFinallyCatching {
                isClosed = true
                Result.success("closed")
            }

            isClosed.shouldBeTrue()
            result.shouldBeSuccess("こんにちは")
        }

        it("should run the finally after failure") {
            var isClosed = false
            val result = Result.failure<String>(testException).flatFinallyCatching {
                isClosed = true
                Result.success("closed")
            }

            isClosed.shouldBeTrue()
            result.shouldBeFailure(testException)
        }

        it("should return the error from the finally if it fails on a success") {
            val result = Result.success("こんにちは").flatFinallyCatching {
                Result.failure(testException)
            }
            result.shouldBeFailure(testException)
        }

        it("should return the error from the finally if it fails on a failure") {
            val result = Result.failure<String>(testException).flatFinallyCatching {
                Result.failure(testException)
            }
            result.shouldBeFailure(testException)
        }

        it("should capture and return a thrown error from the finally if it fails on a success") {
            val result = Result.success("こんにちは").flatFinallyCatching {
                throw testException
            }
            result.shouldBeFailure(testException)
        }

        it("should capture and return ta thrown error from the finally if it fails on a failure") {
            val result = Result.failure<String>(testException).flatFinallyCatching {
                throw testException
            }
            result.shouldBeFailure(testException)
        }

    }

    describe("Iterable.sequenceToResult") {

        it("should return a success of the values if all elements are success") {
            val results: Iterable<Result<String>> = listOf(
                Result.success("猫"),
                Result.success("犬"),
                Result.success("狸"),
            )
            val result: Result<Iterable<String>> = results.sequenceToResult()
            result.shouldBeSuccess(listOf("猫", "犬", "狸"))
        }

        it("should return the first error if there are error elements") {
            val results: Iterable<Result<String>> = listOf(
                Result.success("猫"),
                Result.failure(testException),
                Result.success("狸"),
                Result.failure(Exception("other exception"))
            )
            val result: Result<Iterable<String>> = results.sequenceToResult()
            result.shouldBeFailure(testException)
        }

        it("should sequence an empty iterable") {
            val results: Iterable<Result<String>> = emptyList()
            val result: Result<Iterable<String>> = results.sequenceToResult()
            result.shouldBeSuccess(emptyList())
        }

    }

    describe("sequenceToNullable") {

        it("should keep a value") {
            val result: Result<String?> = Result.success("こんにちは")
            val nullable: Result<String>? = result.sequenceToNullable()
            nullable.shouldNotBeNull().shouldBeSuccess("こんにちは")
        }

        it("should keep a failure") {
            val result: Result<String?> = Result.failure(testException)
            val nullable: Result<String>? = result.sequenceToNullable()
            nullable.shouldNotBeNull().shouldBeFailure(testException)
        }

        it("should unwrap a wrapped null") {
            val result: Result<String?> = Result.success(null)
            val nullable: Result<String>? = result.sequenceToNullable()
            nullable.shouldBeNull()
        }

    }

    describe("T?.sequenceToResult") {

        it("should keep a value") {
            val nullable: Result<String>? = Result.success("こんにちは")
            val result: Result<String?> = nullable.sequenceToResult()
            result.shouldNotBeNull().shouldBeSuccess("こんにちは")
        }

        it("should keep a failure") {
            val nullable: Result<String>? = Result.failure(testException)
            val result: Result<String?> = nullable.sequenceToResult()
            result.shouldNotBeNull().shouldBeFailure(testException)
        }

        it("should wrap an unwrapped null") {
            val nullable: Result<String>? = null
            val result: Result<String?> = nullable.sequenceToResult()
            result.shouldNotBeNull().shouldBeSuccess(null)
        }

    }

    describe("Pair.sequenceToResult") {

        it("should return a success if all the values are success") {
            val pair: Pair<Result<String>, Result<String>> = Pair(Result.success("猫"), Result.success("犬"))
            val result: Result<Pair<String, String>> = pair.sequenceToResult()
            result.shouldNotBeNull().shouldBeSuccess(Pair("猫", "犬"))
        }

        it("should return the failure if the first value is a failure") {
            val pair: Pair<Result<String>, Result<String>> = Pair(Result.failure(testException), Result.success("犬"))
            val result: Result<Pair<String, String>> = pair.sequenceToResult()
            result.shouldNotBeNull().shouldBeFailure(testException)
        }

        it("should return the failure if the second value is a failure") {
            val pair: Pair<Result<String>, Result<String>> = Pair(Result.success("猫"), Result.failure(testException))
            val result: Result<Pair<String, String>> = pair.sequenceToResult()
            result.shouldNotBeNull().shouldBeFailure(testException)
        }

        it("should return the first failure if both of the values are a failure") {
            val pair: Pair<Result<String>, Result<String>> = Pair(Result.failure(testException), Result.failure(Exception("other failure")))
            val result: Result<Pair<String, String>> = pair.sequenceToResult()
            result.shouldNotBeNull().shouldBeFailure(testException)
        }

    }

    describe("Triple.sequenceToResult") {

        it("should return a success if all the values are a success") {
            val triple: Triple<Result<String>, Result<String>, Result<String>> = Triple(Result.success("猫"), Result.success("犬"), Result.success("鳥"))
            val result: Result<Triple<String, String, String>> = triple.sequenceToResult()
            result.shouldNotBeNull().shouldBeSuccess(Triple("猫", "犬", "鳥"))
        }

        it("should return the failure if any one value is a failure") {
            val triples: List<Triple<Result<String>, Result<String>, Result<String>>> = listOf(
                Triple(Result.failure(testException), Result.success("犬"), Result.success("鳥")),
                Triple(Result.success("猫"), Result.failure(testException), Result.success("鳥")),
                Triple(Result.success("猫"), Result.success("犬"), Result.failure(testException)),
            )
            triples.forEach { triple ->
                val result: Result<Triple<String, String, String>> = triple.sequenceToResult()
                result.shouldNotBeNull().shouldBeFailure(testException)
            }
        }

    }

})