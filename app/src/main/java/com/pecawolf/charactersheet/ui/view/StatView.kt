package com.pecawolf.charactersheet.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.pecawolf.charactersheet.databinding.WidgetStatViewBinding
import com.pecawolf.charactersheet.ext.getLocalizedName
import com.pecawolf.model.BaseStats

class StatView : FrameLayout {

    private val binding: WidgetStatViewBinding = WidgetStatViewBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    var data: BaseStats.Stat? = null
        set(value) {
            field = value
            refreshData()
        }

    var isLoading: Boolean = false
        set(value) {
            field = value
            refreshLoading()
        }

    // region constructors

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    // endregion constructors

    init {
        setOnClickListener(onClick = null)
    }

    fun setOnClickListener(onClick: ((BaseStats.Stat) -> Unit)?) {
        binding.statWidgetRollButton.isEnabled = onClick != null
        binding.statWidgetRollButton.setOnClickListener {
            data?.let(onClick!!)
        }
    }

    private fun refreshData() {
        data?.run {
            binding.statWidgetLabel.text = getLocalizedName(context)
            binding.statWidgetValue.text = value.toString()
            binding.statWidgetTrap.text = trap.toString()
        }
    }

    private fun refreshLoading() {
        binding.statWidgetProgress.isVisible = isLoading
    }
}