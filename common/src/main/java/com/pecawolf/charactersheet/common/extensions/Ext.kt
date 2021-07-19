package com.pecawolf.charactersheet.common.extensions

fun Any.isOneOf(vararg args: Any) = args.contains(this)

fun Any.isNotOneOf(vararg args: Any) = !args.contains(this)

fun Any.isOneOf(args: Collection<Any>) = args.contains(this)

fun Any.isNotOneOf(args: Collection<Any>) = !args.contains(this)

fun <T> MutableCollection<T>.setAll(items: Collection<T>) {
    clear()
    addAll(items)
}