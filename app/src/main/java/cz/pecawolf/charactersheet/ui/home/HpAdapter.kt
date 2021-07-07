package cz.pecawolf.charactersheet.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import cz.pecawolf.charactersheet.R
import cz.pecawolf.charactersheet.databinding.ItemHitPointBinding

class HpAdapter : RecyclerView.Adapter<HpAdapter.ViewHolder>() {

    var items: Pair<Int, Int> = Pair(0, 0)
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val luck: Int
        get() = items.first

    private val wounds: Int
        get() = items.second

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ItemHitPointBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            item.setCardBackgroundColor(
                ResourcesCompat.getColor(
                    parent.context.resources,
                    viewType,
                    null
                )
            )
            ViewHolder(this)
        }

    override fun getItemCount(): Int = luck + wounds

    override fun getItemViewType(position: Int) = when {
        position < luck -> R.color.luck
        position < luck + wounds -> R.color.wound
        else -> throw IndexOutOfBoundsException()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    class ViewHolder(private val binding: ItemHitPointBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}

