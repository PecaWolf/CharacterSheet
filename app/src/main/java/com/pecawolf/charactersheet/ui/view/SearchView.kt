package com.pecawolf.charactersheet.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.WidgetSearchViewBinding

class SearchView : FrameLayout {

    private val binding: WidgetSearchViewBinding = WidgetSearchViewBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private var onSearchChangedListener: ((String) -> Unit)? = null

    var text: String
        get() = binding.skillSearchInput.text?.toString() ?: ""
        set(value) = binding.skillSearchInput.run {
            if (text?.toString() != value) setText(value)
        }

    // region constructors

    constructor(context: Context) : super(context) {
        initStyleable(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initStyleable(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, def: Int) : super(context, attrs, def) {
        initStyleable(context, attrs)
    }

    // endregion constructors

    init {
        binding.skillSearchInput.apply {
            addTextChangedListener(DebouncedTextChangeListener { onSearchChangedListener?.invoke(it) })
            addTextChangedListener { binding.skillSearchCancel.isVisible = !it.isNullOrBlank() }
        }
        binding.skillSearchCancel.setOnClickListener { binding.skillSearchInput.setText("") }
    }

    fun setOnSearchChangedListener(listener: (String) -> Unit) {
        onSearchChangedListener = listener
    }

    private fun initStyleable(context: Context, attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SearchView,
            0,
            0
        ).apply {
        }
    }
}
