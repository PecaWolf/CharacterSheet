package com.pecawolf.charactersheet.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pecawolf.charactersheet.ui.SimpleSelectionAdapter.SimpleSelectionViewHolder
import com.pecawolf.presentation.SimpleSelectionItem
import com.pecawolf.charactersheet.databinding.ItemSimpleSelectionBinding as Binding

class SimpleSelectionAdapter(
    initialItems: List<SimpleSelectionItem> = listOf(),
    private val onClick: (Any?) -> Unit
) : RecyclerView.Adapter<SimpleSelectionViewHolder>() {

    init {
        if (initialItems.isNotEmpty()) notifyDataSetChanged()
    }

    var items: List<SimpleSelectionItem> = initialItems
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SimpleSelectionViewHolder(
        Binding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: SimpleSelectionViewHolder, position: Int) {
        holder.bind(items[position], onClick)
    }

    override fun getItemCount() = items.size

    class SimpleSelectionViewHolder(private val binding: Binding) : ViewHolder(binding.root) {

        fun bind(item: SimpleSelectionItem, listener: (Any?) -> Unit) {
            binding.selectionCheckbox.apply {
                text = item.text
                setOnClickListener { listener.invoke(item.data) }
                isChecked = item.isChecked
            }
        }
    }
}
