package com.pecawolf.charactersheet.ui.loadout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.pecawolf.charactersheet.databinding.ItemMagazineRoundBinding
import timber.log.Timber

class RoundsAdapter : RecyclerView.Adapter<RoundsAdapter.RoundViewHolder>() {

    var items: Int = 0
    var groupSize: Int = 1

    override fun getItemViewType(position: Int): Int {
        val x = position % (groupSize * ROWS)
        val type = if (position > groupSize && x >= (groupSize * (ROWS - 1))) SPACE else ROUND
        Timber.d("getItemViewType(): $position, $type")
        return type
    }

    override fun getItemCount() = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RoundViewHolder(
        ItemMagazineRoundBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        viewType
    )

    override fun onBindViewHolder(holder: RoundViewHolder, position: Int) {
        holder.bind()
    }

    class RoundViewHolder(val binding: ItemMagazineRoundBinding, val type: Int) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.divider.isVisible = type == SPACE
        }
    }

    companion object {
        private const val SPACE = 0
        private const val ROUND = 1

        private const val ROWS = 5
    }
}
