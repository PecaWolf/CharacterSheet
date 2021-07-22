package com.pecawolf.charactersheet.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.pecawolf.charactersheet.databinding.WidgetStatViewBinding
import com.pecawolf.charactersheet.ext.getLocalizedName
import com.pecawolf.model.Rollable

class StatView : FrameLayout {

    private val binding: WidgetStatViewBinding = WidgetStatViewBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    var data: Rollable? = null
        set(value) {
            field = value
            refreshData()
        }

    var isEditing: Boolean = false
        set(value) {
            field = value
            refreshEditIcon()
        }

    private var onRollClick: ((Rollable) -> Unit)? = null
        set(value) {
            field = value
            binding.statWidgetRollButton.isEnabled = value != null
            binding.statWidgetRollButton.setOnClickListener {
                data?.let(value!!)
            }
        }

    private var onEditClick: ((Rollable) -> Unit)? = null
        set(value) {
            field = value
            binding.statWidgetEditIcon.setOnClickListener {
                data?.let(value!!)
            }
            refreshEditIcon()
        }

    // region constructors

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    // endregion constructors

    init {
        setOnRollClickListener(onClick = null)
    }

    fun setOnRollClickListener(onClick: ((Rollable) -> Unit)?) {
        onRollClick = onClick
    }

    fun setOnEditClickListener(onClick: ((Rollable) -> Unit)?) {
        onEditClick = onClick
    }

    private fun refreshData() {
        data?.run {
            binding.statWidgetLabel.text = getName(context)
            binding.statWidgetValue.text = value.toString()
            binding.statWidgetTrap.text = trap.toString()
        }
    }

    private fun refreshEditIcon() {
        binding.statWidgetEditIcon.isVisible = (isEditing && onEditClick != null)
    }
}

private fun Rollable.getName(context: Context) = when (this) {
    is Rollable.Stat -> getLocalizedName(context)
    is Rollable.Skill -> name
}
