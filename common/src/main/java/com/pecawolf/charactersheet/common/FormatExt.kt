package com.pecawolf.charactersheet.common

import java.text.NumberFormat

fun Int.formatAmount(): String = NumberFormat.getIntegerInstance().format(this)
