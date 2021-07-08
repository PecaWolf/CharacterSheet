package cz.pecawolf.charactersheet.common

fun Any.isOneOf(vararg args: Any) = args.contains(this)