package com.pecawolf.common.extensions

import android.os.Handler

fun Any.isOneOf(vararg args: Any) = args.contains(this)

fun Any.isNotOneOf(vararg args: Any) = !args.contains(this)

fun Any.isOneOf(args: Collection<Any>) = args.contains(this)

fun Any.isNotOneOf(args: Collection<Any>) = !args.contains(this)

fun <T> MutableCollection<T>.setAll(items: Collection<T>) {
    clear()
    addAll(items)
}

fun <T, U, V> Pair<T, U>.let(lambda: (T, U) -> V): V = lambda.invoke(first, second)

fun Handler.postDelayed(delayMillis: Long, runnable: () -> Unit) = postDelayed(runnable, delayMillis)