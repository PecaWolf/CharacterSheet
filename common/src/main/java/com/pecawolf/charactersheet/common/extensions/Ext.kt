package com.pecawolf.charactersheet.common.extensions

fun Any.isOneOf(vararg args: Any) = args.contains(this)

fun Any.isNotOneOf(vararg args: Any) = args.contains(this)