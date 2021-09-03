package com.pecawolf.charactersheet.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pecawolf.charactersheet.databinding.EmptySimpleSelectionBinding
import com.pecawolf.charactersheet.databinding.ItemSimpleSelectionBinding
import com.pecawolf.presentation.SimpleSelectionItem
import kotlin.math.max

class SimpleSelectionAdapter(
    private val onClick: (Any?) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    var items: List<SimpleSelectionItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = max(items.size, 1)

    override fun getItemViewType(position: Int) = if (items.isEmpty()) EMPTY else ITEM

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            EMPTY -> EmptySelectionViewHolder(
                EmptySimpleSelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            ITEM -> SimpleSelectionViewHolder(
                ItemSimpleSelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> throw IllegalStateException("This does not happen")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is SimpleSelectionViewHolder -> holder.bind(items[position], onClick)
        }
    }

    class EmptySelectionViewHolder(private val binding: EmptySimpleSelectionBinding) : ViewHolder(binding.root) {
    }

    class SimpleSelectionViewHolder(private val binding: ItemSimpleSelectionBinding) : ViewHolder(binding.root) {

        fun bind(item: SimpleSelectionItem, listener: (Any?) -> Unit) {
            binding.selectionCheckbox.apply {
                text = item.text
                setOnClickListener { listener.invoke(item.data) }
                isChecked = item.isChecked
                item.icon?.let { icon = ResourcesCompat.getDrawable(resources, it, null) }
            }
        }
    }

    companion object {
        private const val EMPTY = 0
        private const val ITEM = 1
    }
}
