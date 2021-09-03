package com.pecawolf.charactersheet.ui.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.WidgetRectangleFabBinding

class RectangleFab : FrameLayout {

    private val binding: WidgetRectangleFabBinding = WidgetRectangleFabBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    var icon: Drawable? = null
        set(value) {
            field = value ?: ResourcesCompat.getDrawable(resources, R.drawable.ic_checkmark, null)
            binding.icon.setImageDrawable(field)
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

    private fun initStyleable(context: Context, attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.RectangleFab,
            0,
            0
        ).apply {
            icon = getDrawable(R.styleable.RectangleFab_fabIcon)
        }
    }

    // endregion constructors
}
