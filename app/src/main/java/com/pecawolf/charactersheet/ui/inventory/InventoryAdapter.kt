package com.pecawolf.charactersheet.ui.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.pecawolf.charactersheet.BuildConfig
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.ItemInventoryBinding
import com.pecawolf.charactersheet.ext.getIcon
import com.pecawolf.charactersheet.ext.getLocalizedName
import com.pecawolf.charactersheet.ui.inventory.InventoryAdapter.InventoryViewHolder
import com.pecawolf.model.Item

class InventoryAdapter(
    private val itemEditListener: (Long) -> Unit,
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
        holder.bind(items[position], itemEditListener)
    }

    override fun getItemCount() = items.size

    class InventoryViewHolder(
        val binding: ItemInventoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            inventoryItem: Pair<Item, Item.Slot?>,
            itemEditListener: (Long) -> Unit,
        ) {
            val item = inventoryItem.first
            val slot = inventoryItem.second

            binding.itemIcon.apply {
                setImageDrawable(item.getIcon(resources))
            }
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

            binding.itemSlot.apply {
                text = slot?.getLocalizedName(context) ?: ""

                setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        if (slot == null) R.color.textDisabled else R.color.activePrimary,
                        null
                    )
                )
            }
            binding.itemClicker.setOnClickListener { itemEditListener.invoke(item.itemId) }
        }
    }
}
