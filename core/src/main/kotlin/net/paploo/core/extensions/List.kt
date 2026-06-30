package net.paploo.core.extensions

/**
 * Like [zip] but keeps running until both lists are exhausted, using `null` for values if a list runs out.
 *
 * Note that this version changes the list to allow for null values within it.
 */
infix fun <T> List<T>.zipFull(other: List<T>): List<Pair<T?, T?>> =
    this.zipFull(other) { null }

/**
 * List [zip] but keeps running until both lists are exhausted, using the default value for values if a list runs out.
 *
 * This version forces the return type to be non-nullable on a non-null list.
 */
fun <T> List<T>.zipFull(other: List<T>, default: (Int) -> T): List<Pair<T, T>> =
    (0 until maxOf(this.size, other.size)).map { index ->
        this.getOrElse(index, default) to other.getOrElse(index, default)
    }