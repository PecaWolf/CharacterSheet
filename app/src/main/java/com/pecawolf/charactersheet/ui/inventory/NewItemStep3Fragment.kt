package com.pecawolf.charactersheet.ui.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import com.pecawolf.charactersheet.databinding.FragmentNewItemStep3Binding
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.presentation.viewmodel.main.NewItemStep3ViewModel
import org.koin.android.viewmodel.ext.android.viewModel as injectVM

class NewItemStep3Fragment : BaseFragment<NewItemStep3ViewModel, FragmentNewItemStep3Binding>() {

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentNewItemStep3Binding.inflate(inflater, container, false)

    override fun createViewModel() = injectVM<NewItemStep3ViewModel>().value

    override fun bindView(binding: FragmentNewItemStep3Binding, viewModel: NewItemStep3ViewModel) {

    }

    override fun observeViewModel(
        binding: FragmentNewItemStep3Binding,
        viewModel: NewItemStep3ViewModel
    ) {

    }
}