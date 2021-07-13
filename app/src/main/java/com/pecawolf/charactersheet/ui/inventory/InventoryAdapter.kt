package com.pecawolf.charactersheet.ui.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.pecawolf.charactersheet.databinding.ItemInventoryBinding
import com.pecawolf.charactersheet.ui.inventory.InventoryAdapter.InventoryViewHolder
import com.pecawolf.model.Item

class InventoryAdapter(
    private val isBackpack: Boolean,
    private val itemEditListener: (Long) -> Unit,
    private val switchClickListener: (Long, Boolean) -> Unit
) : RecyclerView.Adapter<InventoryViewHolder>() {

    var items: List<Item> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        InventoryViewHolder(
            ItemInventoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        holder.bind(items[position], isBackpack, itemEditListener, switchClickListener)
    }

    override fun getItemCount() = items.size

    class InventoryViewHolder(
        val binding: ItemInventoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Item,
            isBackpack: Boolean,
            itemEditListener: (Long) -> Unit,
            switchClickListener: (Long, Boolean) -> Unit
        ) {
            binding.itemName.text = item.name
            binding.itemDescription.text = item.description

            binding.itemSwitchToStorage.isVisible = isBackpack
            binding.itemSwitchToBackpack.isVisible = !isBackpack

            binding.itemEdit.setOnClickListener { itemEditListener.invoke(item.itemId) }
            binding.itemSwitchToStorage.setOnClickListener {
                switchClickListener.invoke(
                    item.itemId,
                    isBackpack
                )
            }
            binding.itemSwitchToBackpack.setOnClickListener {
                switchClickListener.invoke(
                    item.itemId,
                    isBackpack
                )
            }
        }
    }
}
