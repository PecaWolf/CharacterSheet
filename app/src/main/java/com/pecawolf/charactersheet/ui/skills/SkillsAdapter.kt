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
import com.pecawolf.model.Rollable
import com.pecawolf.model.Rollable.Skill
import com.pecawolf.model.Rollable.Stat

class SkillsAdapter(
    private val onRollClicked: (Rollable) -> Unit,
) : RecyclerView.Adapter<SkillsViewHolder<SkillAdapterItem, ViewBinding>>() {

    var items: List<List<Skill>> = listOf()
        set(value) {
            field = value
            itemsInternal = value.flatMap { list ->
                listOf(
                    listOf(SkillAdapterHeader(list.first().stat)),
                    list.map {
                        SkillAdapterSkill(it)
                    }
                ).flatten()
            }
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
        when (getItemViewType(position)) {
            HEADER -> (holder as HeaderViewHolder).bind(itemsInternal[position] as SkillAdapterHeader)
            SKILL -> (holder as SkillViewHolder).bind(
                itemsInternal[position] as SkillAdapterSkill,
                onRollClicked
            )
        }
    }

    sealed class SkillAdapterItem(val itemType: Int) {
        data class SkillAdapterHeader(val stat: Stat) : SkillAdapterItem(HEADER)
        data class SkillAdapterSkill(val skill: Skill) : SkillAdapterItem(SKILL)
    }

    abstract class SkillsViewHolder<T : SkillAdapterItem, B : ViewBinding>(open val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    class HeaderViewHolder(override val binding: ItemSkillHeaderBinding) :
        SkillsViewHolder<SkillAdapterHeader, ItemSkillHeaderBinding>(binding) {

        fun bind(item: SkillAdapterHeader) {
            binding.itemSkillHeader.apply {
                text = resources.getString(R.string.skill_group_header,
                    item.stat.getLocalizedName(context),
                    item.stat.value)
            }
        }
    }

    class SkillViewHolder(override val binding: ItemSkillSkillBinding) :
        SkillsViewHolder<SkillAdapterSkill, ItemSkillSkillBinding>(binding) {

        fun bind(item: SkillAdapterSkill, onRollClicked: (Rollable) -> Unit) {
            binding.itemSkillStatView.apply {
                data = item.skill
                setOnRollClickListener(onRollClicked)
            }
        }
    }

    companion object {
        const val HEADER = 0
        const val SKILL = 1
    }
}
