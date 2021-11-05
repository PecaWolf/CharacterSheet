package com.pecawolf.charactersheet.ui.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.pecawolf.charactersheet.BuildConfig
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.WidgetLoadoutItemBinding
import com.pecawolf.charactersheet.ext.getIcon
import com.pecawolf.charactersheet.ui.loadout.RoundsAdapter
import com.pecawolf.model.Item
import com.pecawolf.model.Rollable

class LoadoutItem : ConstraintLayout {

    private val binding: WidgetLoadoutItemBinding = WidgetLoadoutItemBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private val roundsAdapter: RoundsAdapter by lazy { RoundsAdapter() }

    var data: Item? = null
        set(value) {
            field = value
            updateItem()
        }

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
            updateItem()
        }

    var skill: Rollable.Skill?
        set(value) {
            binding.loadoutItemRollDivider.isVisible = value != null && isActive
            binding.loadoutItemRoll.also { rollView ->
                rollView.isVisible = value != null && isActive
                rollView.data = value
            }
        }
        get() = binding.loadoutItemRoll.data as? Rollable.Skill?

    private var onDetailClicked: (() -> Unit)? = null
    private var onReloadClicked: (() -> Unit)? = null
    private var onRollClicked: ((Rollable) -> Unit)? = null

    constructor(context: Context) : super(context) {
        initStyleable(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initStyleable(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, def: Int) : super(context, attrs, def) {
        initStyleable(context, attrs)
    }

    fun setOnDetailClickedListener(onClick: () -> Unit) {
        onDetailClicked = onClick
    }

    fun setOnReloadClickedListener(onClick: () -> Unit) {
        onReloadClicked = onClick
    }

    fun setOnRollClickedListener(onClick: (Rollable) -> Unit) {
        onRollClicked = onClick.also { listener ->
            binding.loadoutItemRoll.setOnRollClickListener {
                listener.invoke(it)

                (data as? Item.Weapon.Ranged)?.also { item ->
                    when {
                        item.magazineState > 0 -> {

                        }
                        item.magazineCount > 0 -> {
                            item.magazineCount--
                            item.magazineState = item.magazineSize
                        }
                        else -> {

                        }
                    }

                    updateItem()
                }
            }
        }
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
        binding.loadoutItemClicker.setOnClickListener { onDetailClicked?.invoke() }
        binding.loadoutItemRounds.adapter = roundsAdapter
        binding.loadoutItemReload.setOnClickListener { onReloadClicked?.invoke() }
    }

    private fun updateItem() {
        // data
        icon = data?.getIcon(resources)
        value = data?.name.takeIf { !it.isNullOrBlank() } ?: resources.getString(R.string.item_equipment_nothing)
        itemId = data?.itemId

        // ranged-specific
        (data as? Item.Weapon.Ranged)?.also { ranged ->
            // data

            binding.loadoutItemMagazines.text = resources.getString(R.string.item_equipment_magazines, ranged.magazineCount)
            roundsAdapter.items = ranged.magazineState
            roundsAdapter.groupSize = ranged.rateOfFire

            binding.loadoutItemRounds.apply {
                if ((layoutManager as? GridLayoutManager)?.spanCount != ranged.rateOfFire)
                    layoutManager = GridLayoutManager(context, ranged.rateOfFire, GridLayoutManager.HORIZONTAL, false)
            }
            binding.loadoutItemReload.isVisible = (ranged.magazineState < ranged.magazineSize)
        }

        // activity
        val alpha = if (isActive) 1f else ResourcesCompat.getFloat(
            resources,
            R.dimen.inactive_item_alpha
        )
        binding.loadoutItemIconStart.alpha = alpha
        binding.loadoutItemLabel.alpha = alpha
        binding.loadoutItemValue.alpha = alpha
        binding.loadoutItemIconEnd.alpha = alpha

        binding.loadoutItemRoll.isVisible = skill != null && isActive
        binding.loadoutItemRollDivider.isVisible = skill != null && isActive

        binding.loadoutItemAmmo.isVisible = data is Item.Weapon.Ranged && isActive
        binding.loadoutItemAmmoDivider.isVisible = data is Item.Weapon.Ranged && isActive
    }
}