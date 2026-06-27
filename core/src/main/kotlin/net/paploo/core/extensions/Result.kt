package net.paploo.core.extensions

/*
 * Construction
 */

/**
 * A version of [runCatching] for when the wrapped logic returns a [Result].
 */
inline fun <T, R> T.flatRunCatching(block: T.() -> Result<R>): Result<R> =
    try {
        block()
    } catch (e: Throwable) {
        Result.failure(e)
    }


/*
 * Core Monadic
 */

/**
 * Flattens a nested [Result]
 */
fun <T> Result<Result<T>>.flatten(): Result<T> =
    flatMap { it }

/**
 * Standard monadic flatmap!
 */
inline fun <T, R> Result<T>.flatMap(transform: (T) -> Result<R>): Result<R> =
    fold(
        onSuccess = { transform(it) },
        onFailure = { Result.failure(it) }
    )

/**
 * [flatMap] except it catches any exceptions thrown by the transform.
 */
inline fun <T, R> Result<T>.flatMapCatching(f: (T) -> Result<R>): Result<R> =
    flatRunCatching { flatMap(f) }

/**
 * Run a side-effecting finally block that is guarnateed to run regarless of success or failure.
 *
 * If an error occurs during finally, it is returned instead of the original result, otherwise
 * the original result is returned.
 *
 * This is useful for cleanup scenarios, especially when interfacing with Java libraries.
 *
 * Note: Like [map], this will *NOT* catch errors thrown in the finally block.
 * @see flatFinally
 */
inline fun <T> Result<T>.finally(block: () -> Unit): Result<T> =
    onSuccess { block() }.onFailure { block() }

/**
 * Like [finally] but works for when the block return a `Result`.
 *
 * Note: Like [flatMap], this will *NOT* catch errors thrown in the finally block.
 * @see flatFinallyCatching
 */
inline fun <T> Result<T>.flatFinally(block: () -> Result<Any>): Result<T> =
    fold(
        onSuccess = { t -> block().map { t } },
        onFailure = { e -> block().flatMap { Result.failure(e) } }
    )

/**
 * Like [finally] but catches any errors thrown in the finally block.
 */
inline fun <T> Result<T>.finallyCatching(block: () -> Unit): Result<T> =
    flatRunCatching { finally(block) }

/**
 * Like [finally] but catches any errors thrown in the finally block.
 */
inline fun <T> Result<T>.flatFinallyCatching(block: () -> Result<Any>): Result<T> =
    flatRunCatching { flatFinally(block) }

/*
 * Sequencing Functions
 */

/**
 * Given an [Iterable] of [Result], return a [Result] containing the [Iterable] of values orthe first failure.
 */
fun <T> Iterable<Result<T>>.sequenceToResult(): Result<List<T>> {
    val buffer: MutableList<T> = mutableListOf()
    for (elem in this) {
        elem.onSuccess {
            buffer += it
        }.onFailure {
            return Result.failure(it)
        }
    }
    return Result.success(buffer.toList())
}

/**
 * Extract out nullability to be outside of the [Result].
 */
fun <T : Any> Result<T?>.sequenceToNullable(): Result<T>? =
    fold(
        onSuccess = { nullable -> nullable?.let { Result.success(it) } },
        onFailure = { Result.failure(it) }
    )

/**
 * Given a nullable of a [Result], return a [Result] of a nullable.
 */
fun <T : Any> Result<T>?.sequenceToResult(): Result<T?> =
    this ?: Result.success(null)

/**
 * Given a pair of [Result], return a result containing the pair, or the first error.
 */
fun <A, B> Pair<Result<A>, Result<B>>.sequenceToResult(): Result<Pair<A, B>> =
    first.flatMap { a ->
        second.map {
            b -> Pair(a, b)
        }
    }

/**
 * Given a triple of [Result], return a result containing the triple, or the first error.
 */
fun <A, B, C> Triple<Result<A>, Result<B>, Result<C>>.sequenceToResult(): Result<Triple<A, B, C>> =
    first.flatMap { a ->
        second.flatMap { b ->
            third.map { c ->
                Triple(a, b, c)
            }
        }
    }