package com.pecawolf.charactersheet.ui.character

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pecawolf.charactersheet.MainActivity
import com.pecawolf.charactersheet.databinding.FragmentChooseCharacterBinding
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.presentation.extensions.reObserve
import com.pecawolf.presentation.viewmodel.ChooseCharacterViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ChooseCharacterFragment :
    BaseFragment<ChooseCharacterViewModel, FragmentChooseCharacterBinding>() {

    private val characterAdapter: ChooseCharacterAdapter by lazy {
        ChooseCharacterAdapter(
            ::onExistingCharacterClicked, ::onNewCharacterClicked
        ).also {
            binding.selectionRecycler.apply {
                adapter = it
                layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL, false
                )
            }
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentChooseCharacterBinding.inflate(inflater, container, false)

    override fun createViewModel() = viewModel<ChooseCharacterViewModel>().value

    override fun bindView(
        binding: FragmentChooseCharacterBinding,
        viewModel: ChooseCharacterViewModel
    ) {
        binding.selectionRecycler.adapter = characterAdapter
    }

    override fun observeViewModel(
        binding: FragmentChooseCharacterBinding,
        viewModel: ChooseCharacterViewModel
    ) {
        viewModel.navigateTo.reObserve(this) { destination ->
            when (destination) {
                ChooseCharacterViewModel.Destination.Home -> startActivity(
                    Intent(
                        requireContext(),
                        MainActivity::class.java
                    )
                )
                ChooseCharacterViewModel.Destination.Create -> findNavController().navigate(
                    ChooseCharacterFragmentDirections.actionChooseCharacterToCreateCharacter()
                )
            }
            requireActivity().finish()
        }
    }

    fun onExistingCharacterClicked(characterId: Long) {
        viewModel.onExistingCharacterClicked(characterId)
    }

    fun onNewCharacterClicked() {
        viewModel.onNewCharacterClicked()
    }
}