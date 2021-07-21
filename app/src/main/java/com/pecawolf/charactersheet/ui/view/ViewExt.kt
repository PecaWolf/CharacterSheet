package com.pecawolf.charactersheet.ui.view

import android.content.Context
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.showKeyboard() =
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)

class DebouncedOnClickListener(private val onClick: (Int) -> Unit) : View.OnClickListener {

    var debounce: Long = 500
    private var handler: Handler? = null
    private var bounces: Int = 1

    override fun onClick(view: View?) {
        if (handler == null) {
            handler = Handler().apply {
                postDelayed(debounce) {
                    onClick.invoke(bounces)
                    handler = null
                    bounces = 1
                }
            }
        } else {
            bounces++
        }
    }

    fun Handler.postDelayed(delayMillis: Long, runnable: Runnable) {
        postDelayed(runnable, delayMillis)
    }
}