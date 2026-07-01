package net.paploo.core.extensions

/*
 * We add to the existing `Pair` and `Triple` types defined in `Tuple.kt`
 */

object Nullary

data class Single<out A>(
    val first: A
) {
    override fun toString(): String = "($first)"
}

data class Quadruple<out A, out B, out C, out D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
) {
    override fun toString(): String = "($first, $second, $third, $fourth)"
}

data class Quintuple<out A, out B, out C, out D, out E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
) {
    override fun toString(): String = "($first, $second, $third, $fourth, $fifth)"
}

operator fun <A> Nullary.plus(value: A): Single<A> = Single(value)
operator fun <A, B> Single<A>.plus(value: B): Pair<A, B> = Pair(first, value)
operator fun <A, B, C> Pair<A, B>.plus(value: C): Triple<A, B, C> = Triple(first, second, value)
operator fun <A, B, C, D> Triple<A, B, C>.plus(value: D): Quadruple<A, B, C, D> = Quadruple(first, second, third, value)
operator fun <A, B, C, D, E> Quadruple<A, B, C, D>.plus(value: E): Quintuple<A, B, C, D, E> = Quintuple(first, second, third, fourth, value)

fun Nullary.toList(): List<Nothing> = emptyList()
fun <T> Single<T>.toList(): List<T> = listOf(first)
fun <T> Pair<T, T>.toList(): List<T> = listOf(first, second)
fun <T> Triple<T, T, T>.toList(): List<T> = listOf(first, second, third)
fun <T> Quadruple<T, T, T, T>.toList(): List<T> = listOf(first, second, third, fourth)
fun <T> Quintuple<T, T, T, T, T>.toList(): List<T> = listOf(first, second, third, fourth, fifth)

interface Tuple {
    typealias Empty = Nullary
    typealias One<A> = Single<A>
    typealias Two<A, B> = Pair<A, B>
    typealias Three<A, B, C> = Triple<A, B, C>
    typealias Four<A, B, C, D> = Quadruple<A, B, C, D>
    typealias Five<A, B, C, D, E> = Quintuple<A, B, C, D, E>

    companion object  {
        operator fun invoke(): Empty = Empty
        operator fun <A> invoke(a: A): One<A> = One(a)
        operator fun <A, B> invoke(a: A, b: B): Two<A, B> = Two(a, b)
        operator fun <A, B, C> invoke(a: A, b: B, c: C): Three<A, B, C> = Three(a, b, c)
        operator fun <A, B, C, D> invoke(a: A, b: B, c: C, d: D): Four<A, B, C, D> = Four(a, b, c, d)
        operator fun <A, B, C, D, E> invoke(a: A, b: B, c: C, d: D, e: E): Five<A, B, C, D, E> = Five(a, b, c, d, e)
    }
}

fun <A> A.tuple(): Tuple.One<A> = Tuple.One(this)