package com.pecawolf.charactersheet.ui.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.pecawolf.charactersheet.BuildConfig
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.WidgetLoadoutItemBinding
import com.pecawolf.charactersheet.ext.getIcon
import com.pecawolf.model.Item

class LoadoutItem : ConstraintLayout {

    private val binding: WidgetLoadoutItemBinding = WidgetLoadoutItemBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    var icon: Drawable? = null
        set(value) {
            field = value ?: ResourcesCompat.getDrawable(resources, R.drawable.ic_checkmark, null)
            binding.loadoutItemIconStart.setImageDrawable(field)
        }

    var label: String? = null
        set(value) {
            field = value
            binding.loadoutItemLabel.text = value ?: ""
        }

    var value: String? = null
        set(value) {
            field = value
            binding.loadoutItemValue.text = value ?: ""
        }

    var itemId: Long? = null
        set(value) {
            field = value
            binding.loadoutItemId.text = value?.let { String.format("#%06d", it) } ?: ""
        }

    var isActive: Boolean = true
        set(value) {
            field = value
            val alpha = if (value) 1f else ResourcesCompat.getFloat(
                resources,
                R.dimen.inactive_item_alpha
            )
            binding.loadoutItemLabel.alpha = alpha
            binding.loadoutItemValue.alpha = alpha
        }

    constructor(context: Context) : super(context) {
        initStyleable(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initStyleable(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, def: Int) : super(context, attrs, def) {
        initStyleable(context, attrs)
    }

    fun setItem(item: Item) {
        icon = item.getIcon(resources)
        value = item.name
        itemId = item.itemId
    }

    private fun initStyleable(context: Context, attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LoadoutItem,
            0,
            0
        ).apply {
            label = getString(R.styleable.LoadoutItem_label)
            value = getString(R.styleable.LoadoutItem_value)
            icon = getDrawable(R.styleable.LoadoutItem_itemIcon)
            isActive = getBoolean(R.styleable.LoadoutItem_isActive, true)
        }
        binding.loadoutItemId.isVisible = BuildConfig.DEBUG
    }
}