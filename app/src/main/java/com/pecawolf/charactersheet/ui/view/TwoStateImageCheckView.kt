package com.pecawolf.charactersheet.ui.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.WidgetTwoStateCheckViewBinding

class TwoStateImageCheckView : ConstraintLayout {

    private val binding: WidgetTwoStateCheckViewBinding = WidgetTwoStateCheckViewBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    var isChecked: Boolean = false
        set(value) {
            field = value
            refresh(value)
        }

    var checkedSrc: Drawable? = null
    var uncheckedSrc: Drawable? = null
    var text: String? = null
        set(value) {
            field = value
            binding.label.apply {
                text = value ?: ""
                isVisible = !value.isNullOrBlank()
            }
        }

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

    fun setCheckedChangedListener(listener: (view: TwoStateImageCheckView, isChecked: Boolean) -> Unit) {
        checkedChangedListener = object : OnCheckedChangeListener {
            override fun onCheckedChanged(view: TwoStateImageCheckView, isChecked: Boolean) {
                listener.invoke(view, isChecked)
            }
        }
    }

    private fun initStyleable(context: Context, attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TwoStateImageCheckView,
            0,
            0
        ).apply {
            isChecked = getBoolean(R.styleable.TwoStateImageCheckView_isChecked, false)
            checkedSrc = getDrawable(R.styleable.TwoStateImageCheckView_checkedSrc)
            uncheckedSrc = getDrawable(R.styleable.TwoStateImageCheckView_uncheckedSrc)
            text = getString(R.styleable.TwoStateImageCheckView_text)
        }

        refresh(this.isChecked)

        setOnClickListener {
            isChecked = !isChecked
            checkedChangedListener?.onCheckedChanged(this, isChecked)
        }
    }

    private fun refresh(isChecked: Boolean) {
        binding.icon.setImageDrawable(if (isChecked) checkedSrc else uncheckedSrc)
        binding.label.setTextColor(
            ResourcesCompat.getColor(
                resources,
                if (isChecked) R.color.activePrimary else R.color.contentSecondary,
                null
            )
        )
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(view: TwoStateImageCheckView, isChecked: Boolean)
    }
}