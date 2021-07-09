package com.pecawolf.charactersheet.ui.other

import android.view.LayoutInflater
import android.view.ViewGroup
import com.pecawolf.charactersheet.databinding.FragmentOtherBinding
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.presentation.extensions.reObserve
import com.pecawolf.presentation.viewmodel.OtherViewModel
import org.koin.android.viewmodel.ext.android.viewModel as injectVM

class OtherFragment : BaseFragment<OtherViewModel, FragmentOtherBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentOtherBinding.inflate(inflater, container, false)

    override fun createViewModel() = injectVM<OtherViewModel>().value

    override fun bindView(binding: FragmentOtherBinding, viewModel: OtherViewModel) {
        binding.createCharacter.setOnClickListener { viewModel.onCreateCharacterClicked() }
    }

    override fun observeViewModel(binding: FragmentOtherBinding, viewModel: OtherViewModel) {
        viewModel.navigateTo.reObserve(this) { destination ->
//            when(destination) {
//                OtherViewModel.Destination.CreateCharacter -> findNavController().navigate(OtherFragmentDirections)
//            }
        }
    }
}