package com.pecawolf.charactersheet.ui.view

import android.content.Context
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pecawolf.common.extensions.postDelayed

fun View.showKeyboard() =
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)

fun RecyclerView.initialize(adapter: RecyclerView.Adapter<*>, manager: RecyclerView.LayoutManager? = null) {
    this.adapter = adapter
    layoutManager = manager ?: LinearLayoutManager(context)
}

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
}

class DebouncedTextChangeListener(val afterTextChanged: (String) -> Unit) : TextWatcher {

    var debounce: Long = 500
    private val handler = Handler()
    private var runnable: Runnable? = null

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        runnable?.let { handler.removeCallbacks(it) }

        runnable = Runnable {
            afterTextChanged.invoke(s?.toString() ?: "")
        }

        handler.postDelayed(runnable!!, debounce)
    }
}