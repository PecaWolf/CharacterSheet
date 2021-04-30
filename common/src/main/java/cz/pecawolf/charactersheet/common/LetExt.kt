package cz.pecawolf.charactersheet.common

inline fun <T, U, R> Pair<T, U>.let2(block: (T, U) -> R): R = block(first, second)