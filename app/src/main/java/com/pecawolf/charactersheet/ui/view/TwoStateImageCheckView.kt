package com.pecawolf.charactersheet.ui.view

import android.content.Context
import android.content.res.ColorStateList
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

    var icon: Drawable? = null
        set(value) {
            field = value ?: ResourcesCompat.getDrawable(resources, R.drawable.ic_checkmark, null)
            binding.icon.setImageDrawable(field)
        }

    var activeColor: Int? = null
        get() = field ?: ResourcesCompat.getColor(resources, R.color.activePrimary, null)
        set(value) {
            field = value ?: ResourcesCompat.getColor(resources, R.color.activePrimary, null)
            refresh(isChecked)
        }

    private val inactiveColor: Int by lazy {
        ResourcesCompat.getColor(resources, R.color.contentSecondary, null)
    }

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

    fun setOnCheckedChangedListener(listener: OnCheckedChangeListener?) {
        checkedChangedListener = listener
    }

    fun setOnCheckedChangedListener(listener: (view: TwoStateImageCheckView, isChecked: Boolean) -> Unit) {
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
            icon = getDrawable(R.styleable.TwoStateImageCheckView_iconSrc)
            activeColor = getColor(R.styleable.TwoStateImageCheckView_activeColor, ResourcesCompat.getColor(resources, R.color.activePrimary, null))
            text = getString(R.styleable.TwoStateImageCheckView_text)
        }

        refresh(this.isChecked)

        setOnClickListener {
            isChecked = !isChecked
            checkedChangedListener?.onCheckedChanged(this, isChecked)
        }
    }

    private fun refresh(isChecked: Boolean) {
        val color = if (isChecked) activeColor!! else inactiveColor
        binding.icon.imageTintList = ColorStateList.valueOf(color)
        binding.label.setTextColor(color)
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(view: TwoStateImageCheckView, isChecked: Boolean)
    }
}