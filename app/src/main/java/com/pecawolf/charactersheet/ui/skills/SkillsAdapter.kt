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
    private val onEditClicked: (Rollable) -> Unit = {},
) : RecyclerView.Adapter<SkillsViewHolder<SkillAdapterItem, ViewBinding>>() {

    var isEditing: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var showGroups: Boolean = true
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var items: List<List<Skill>> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val itemsInternal: List<SkillAdapterItem>
        get() = items.mapNotNull { list ->
            if (list.isNotEmpty()) listOfNotNull(
                listOf(SkillAdapterHeader(list.first().stat)).takeIf { showGroups },
                list.map { SkillAdapterSkill(it) }
            ).flatten()
            else null
        }.flatten()

    override fun getItemCount() = itemsInternal.size

    override fun getItemViewType(position: Int) =
        itemsInternal.getOrNull(position)?.itemType ?: HEADER

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
                isEditing,
                onRollClicked,
                onEditClicked,
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

        fun bind(
            item: SkillAdapterSkill,
            isEditing: Boolean,
            onRollClicked: (Rollable) -> Unit,
            onEditClicked: (Rollable) -> Unit,
        ) {
            binding.itemSkillStatView.also { view ->
                view.data = item.skill
                view.isEditing = isEditing
                view.setOnRollClickListener(onRollClicked)
                view.setOnEditClickListener(onEditClicked)
            }
        }
    }

    companion object {
        const val HEADER = 0
        const val SKILL = 1
    }
}
