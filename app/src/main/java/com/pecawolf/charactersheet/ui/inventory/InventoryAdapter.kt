package com.pecawolf.charactersheet.ui.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.pecawolf.charactersheet.BuildConfig
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.ItemInventoryBinding
import com.pecawolf.charactersheet.ext.getLocalizedName
import com.pecawolf.charactersheet.ui.inventory.InventoryAdapter.InventoryViewHolder
import com.pecawolf.model.Item

class InventoryAdapter(
    private val itemEditListener: (Long) -> Unit,
    private val itemEquipListener: (Item, Item.Slot?) -> Unit,
) : RecyclerView.Adapter<InventoryViewHolder>() {

    var items: List<Pair<Item, Item.Slot?>> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        InventoryViewHolder(
            ItemInventoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        holder.bind(items[position], itemEditListener, itemEquipListener)
    }

    override fun getItemCount() = items.size

    class InventoryViewHolder(
        val binding: ItemInventoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            inventoryItem: Pair<Item, Item.Slot?>,
            itemEditListener: (Long) -> Unit,
            itemEquipListener: (Item, Item.Slot?) -> Unit
        ) {
            val item = inventoryItem.first
            val slot = inventoryItem.second

            binding.itemName.text = item.name
            binding.itemId.apply {
                isVisible = BuildConfig.DEBUG
                text = String.format("#%06d", item.itemId)
            }
            binding.itemDescription.text = item.description
            binding.itemCount.apply {
                isVisible = item.count > 1
                text = resources.getString(R.string.item_count, item.count)
            }

            binding.itemEquip.apply {
                val isEquipable =
                    item is Item.Weapon || item is Item.Armor || item is Item.Weapon.Grenade
                isVisible = isEquipable
                isEnabled = isEquipable
                isChecked = slot != null
                setOnClickListener { itemEquipListener.invoke(item, slot) }
            }
            binding.itemSlot.apply {
                text = slot?.getLocalizedName(context) ?: ""

                when (slot) {
                    Item.Slot.PRIMARY -> R.color.activePrimary
                    Item.Slot.SECONDARY -> R.color.activePrimary
                    Item.Slot.TERTIARY -> R.color.activePrimary
                    Item.Slot.ARMOR -> R.color.activePrimary
                    Item.Slot.CLOTHING -> R.color.activePrimary
                    null -> R.color.disabled
                }.let {
                    setTextColor(ResourcesCompat.getColor(resources, it, null))
                }
            }
            binding.itemClicker.setOnClickListener { itemEditListener.invoke(item.itemId) }
        }
    }
}
