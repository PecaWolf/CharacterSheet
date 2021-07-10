package com.pecawolf.charactersheet.ui.character

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pecawolf.charactersheet.MainActivity
import com.pecawolf.charactersheet.databinding.FragmentCharacterSelectionBinding
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.presentation.extensions.reObserve
import com.pecawolf.presentation.viewmodel.character.CharacterSelectionViewModel
import com.pecawolf.presentation.viewmodel.character.CharacterSelectionViewModel.Destination
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class CharacterSelectionFragment :
    BaseFragment<CharacterSelectionViewModel, FragmentCharacterSelectionBinding>() {

    private val characterAdapter: ChooseCharacterAdapter by lazy {
        ChooseCharacterAdapter(::onExistingCharacterClicked, ::onNewCharacterClicked)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCharacterSelectionBinding.inflate(inflater, container, false)

    override fun createViewModel() = viewModel<CharacterSelectionViewModel>().value

    override fun bindView(
        binding: FragmentCharacterSelectionBinding,
        viewModel: CharacterSelectionViewModel
    ) {
        binding.selectionRecycler.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun observeViewModel(
        binding: FragmentCharacterSelectionBinding,
        viewModel: CharacterSelectionViewModel
    ) {
        viewModel.characters.reObserve(this) {
            Timber.v("onCharacters(): $it")
            characterAdapter.items = it
        }
        viewModel.navigateTo.reObserve(this) { destination ->
            when (destination) {
                Destination.Home -> startActivity(
                    Intent(requireContext(), MainActivity::class.java)
                )
                Destination.Create -> findNavController().navigate(
                    CharacterSelectionFragmentDirections.actionChooseCharacterToCreateCharacter()
                )
            }
        }
    }

    fun onExistingCharacterClicked(characterId: Long) {
        viewModel.onExistingCharacterClicked(characterId)
    }

    fun onNewCharacterClicked() {
        viewModel.onNewCharacterClicked()
    }
}