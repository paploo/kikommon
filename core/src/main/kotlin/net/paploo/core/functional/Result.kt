package net.paploo.core.functional

inline fun <T, R> Result<T>.flatMap(f: (T) -> Result<R>): Result<R> =
    fold(
        onSuccess = { f(it) },
        onFailure = { Result.failure(it) }
    )