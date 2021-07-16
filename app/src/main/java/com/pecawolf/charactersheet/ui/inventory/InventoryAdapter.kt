package com.pecawolf.charactersheet.ui.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pecawolf.charactersheet.databinding.ItemInventoryBinding
import com.pecawolf.charactersheet.ui.inventory.InventoryAdapter.InventoryViewHolder
import com.pecawolf.model.Item

class InventoryAdapter(
    private val itemEditListener: (Long) -> Unit
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
        holder.bind(items[position], itemEditListener)
    }

    override fun getItemCount() = items.size

    class InventoryViewHolder(
        val binding: ItemInventoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item, itemEditListener: (Long) -> Unit) {
            binding.itemName.text = item.name
            binding.itemDescription.text = item.description

            binding.itemEdit.setOnClickListener { itemEditListener.invoke(item.itemId) }
        }
    }
}
