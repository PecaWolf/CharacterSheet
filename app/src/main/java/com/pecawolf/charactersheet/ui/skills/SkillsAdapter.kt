package com.pecawolf.charactersheet.ui.skills

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.ItemSkillHeaderBinding
import com.pecawolf.charactersheet.databinding.ItemSkillSkillBinding
import com.pecawolf.charactersheet.ext.getLocalizedName
import com.pecawolf.charactersheet.ui.skills.SkillsAdapter.SkillAdapterItem
import com.pecawolf.charactersheet.ui.skills.SkillsAdapter.SkillAdapterItem.SkillAdapterHeader
import com.pecawolf.charactersheet.ui.skills.SkillsAdapter.SkillAdapterItem.SkillAdapterSkill
import com.pecawolf.charactersheet.ui.skills.SkillsAdapter.SkillsViewHolder
import com.pecawolf.model.Rollable.Skill
import com.pecawolf.model.Rollable.Stat

class SkillsAdapter : RecyclerView.Adapter<SkillsViewHolder<SkillAdapterItem, ViewBinding>>() {

    var items: List<Set<Skill>> = listOf()
        set(value) {
            field = value
            itemsInternal = value.map { list ->
                listOf(
                    listOf(SkillAdapterHeader(list.first().stat)),
                    list.map {
                        SkillAdapterSkill(it)
                    }
                ).flatten()
            }.flatten()
        }

    private var itemsInternal: List<SkillAdapterItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = items.map { it.size + 1 }.sum()

    override fun getItemViewType(position: Int) = itemsInternal[position].itemType

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SkillsViewHolder<SkillAdapterItem, ViewBinding> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            HEADER -> HeaderViewHolder(
                ItemSkillHeaderBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            ) as SkillsViewHolder<SkillAdapterItem, ViewBinding>
            SKILL -> SkillViewHolder(
                ItemSkillSkillBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            ) as SkillsViewHolder<SkillAdapterItem, ViewBinding>
            else -> throw IllegalArgumentException("Should not happen")
        }
    }

    override fun onBindViewHolder(
        holder: SkillsViewHolder<SkillAdapterItem, ViewBinding>,
        position: Int,
    ) {
        holder.bind(itemsInternal[position])
    }

    sealed class SkillAdapterItem(val itemType: Int) {
        data class SkillAdapterHeader(val stat: Stat) : SkillAdapterItem(HEADER)
        data class SkillAdapterSkill(val skill: Skill) : SkillAdapterItem(SKILL)
    }

    abstract class SkillsViewHolder<T : SkillAdapterItem, B : ViewBinding>(open val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: T)
    }

    class HeaderViewHolder(override val binding: ItemSkillHeaderBinding) :
        SkillsViewHolder<SkillAdapterHeader, ItemSkillHeaderBinding>(binding) {

        override fun bind(item: SkillAdapterHeader) {
            binding.itemSkillHeader.apply {
                text = resources.getString(R.string.skill_group_header,
                    item.stat.getLocalizedName(context),
                    item.stat.value)
            }
        }
    }

    class SkillViewHolder(override val binding: ItemSkillSkillBinding) :
        SkillsViewHolder<SkillAdapterSkill, ItemSkillSkillBinding>(binding) {

        override fun bind(item: SkillAdapterSkill) {
            binding.itemSkillStatView.data = item.skill
        }
    }

    companion object {
        const val HEADER = 0
        const val SKILL = 1
    }
}
