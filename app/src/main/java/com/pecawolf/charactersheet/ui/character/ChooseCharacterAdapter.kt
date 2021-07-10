package com.pecawolf.charactersheet.ui.character

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.ItemCreateCharacterBinding
import com.pecawolf.charactersheet.databinding.ItemExistingCharacterBinding
import com.pecawolf.charactersheet.ext.getLocalizedName
import com.pecawolf.model.CharacterSnippet
import timber.log.Timber

class ChooseCharacterAdapter(
    private val existingCharacterClickListener: (Long) -> Unit,
    private val newCharacterClickListener: () -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    var items: List<CharacterSnippet> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int) =
        if (position < items.size) ITEM_EXISTING else ITEM_NEW

    override fun getItemCount() = items.size + 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Timber.v("onCreateViewHolder()")
        return when (viewType) {
            ITEM_EXISTING -> ExistingCharacterViewHolder(
                ItemExistingCharacterBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            ITEM_NEW -> CreateCharacterViewHolder(
                ItemCreateCharacterBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Timber.v("onBindViewHolder()")
        when (getItemViewType(position)) {
            ITEM_EXISTING -> (holder as ExistingCharacterViewHolder).bind(items[position]) {
                existingCharacterClickListener.invoke(items[position].characterId)
            }
            ITEM_NEW -> (holder as CreateCharacterViewHolder).bind(Unit) { newCharacterClickListener.invoke() }
        }
    }

    abstract class ChooseCharacterViewHolder<BIND : ViewBinding, ITEM>(
        protected open val binding: BIND
    ) : ViewHolder(binding.root) {
        abstract fun bind(item: ITEM, listener: View.OnClickListener)
    }

    class ExistingCharacterViewHolder(override val binding: ItemExistingCharacterBinding) :
        ChooseCharacterViewHolder<ItemExistingCharacterBinding, CharacterSnippet>(binding) {

        override fun bind(item: CharacterSnippet, listener: View.OnClickListener) {
            binding.characterName.text = item.name
            binding.characterSpecies.apply {
                val localizedSpecies = resources.getString(item.species.getLocalizedName())
                val localizedWorld = resources.getString(item.world.getLocalizedName())
                Timber.v("bind(): ${item.name}: $localizedSpecies in $localizedWorld")
                text = resources.getString(
                    R.string.character_selection_species_world,
                    localizedSpecies,
                    localizedWorld
                )
            }
            binding.root.setOnClickListener(listener)
        }
    }

    class CreateCharacterViewHolder(override val binding: ItemCreateCharacterBinding) :
        ChooseCharacterViewHolder<ItemCreateCharacterBinding, Unit>(binding) {

        override fun bind(item: Unit, listener: View.OnClickListener) {
            binding.root.setOnClickListener(listener)
        }
    }

    companion object {
        private const val ITEM_EXISTING = 0
        private const val ITEM_NEW = 1
    }
}
