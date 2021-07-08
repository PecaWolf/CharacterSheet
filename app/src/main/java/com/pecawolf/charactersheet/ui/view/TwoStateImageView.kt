package com.pecawolf.charactersheet.ui.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.pecawolf.charactersheet.R

class TwoStateImageView : AppCompatImageView {

    var isActive: Boolean = false
        set(value) {
            field = value
            setDrawable(value)
        }

    var checkedSrc: Drawable? = null
    var uncheckedSrc: Drawable? = null

    private var checkedChangedListener: OnCheckedChangeListener? = null

    constructor(context: Context) : super(context) {
        initStyleable(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initStyleable(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, def: Int) : super(context, attrs, def) {
        initStyleable(context, attrs)
    }

    fun setCheckedChangedListener(listener: OnCheckedChangeListener?) {
        checkedChangedListener = listener
    }

    fun setCheckedChangedListener(listener: (view: TwoStateImageView, isChecked: Boolean) -> Unit) {
        checkedChangedListener = object : OnCheckedChangeListener {
            override fun onCheckedChanged(view: TwoStateImageView, isChecked: Boolean) {
                listener.invoke(view, isChecked)
            }
        }
    }

    private fun initStyleable(context: Context, attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TwoStateImageView,
            0,
            0
        ).apply {
            isActive = getBoolean(R.styleable.TwoStateImageView_isActive, false)
            checkedSrc = getDrawable(R.styleable.TwoStateImageView_checkedSrc)
            uncheckedSrc = getDrawable(R.styleable.TwoStateImageView_uncheckedSrc)
        }

        setDrawable(this.isActive)

        setOnClickListener {
            isActive = !isActive
            checkedChangedListener?.onCheckedChanged(this, isActive)
        }
    }

    private fun setDrawable(isActive: Boolean) {
        setImageDrawable(if (isActive) checkedSrc else uncheckedSrc)
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(view: TwoStateImageView, isChecked: Boolean)
    }
}