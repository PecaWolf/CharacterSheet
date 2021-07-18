package com.pecawolf.presentation

data class SimpleSelectionItem(
    val text: String,
    val isChecked: Boolean,
    val data: Any? = null,
)