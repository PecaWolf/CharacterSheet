package com.pecawolf.charactersheet.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.pecawolf.presentation.viewmodel.BaseViewModel
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

abstract class BaseFragment<VIEWMODEL : BaseViewModel, BINDING : ViewBinding> : Fragment() {

    protected val viewModel: VIEWMODEL by lazy { createViewModel() }

    protected lateinit var binding: BINDING

    val dialogHelper: DialogHelper by inject { parametersOf(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(binding, viewModel)
        observeViewModel(binding, viewModel)
    }

    override fun onResume() {
        super.onResume()

        viewModel.onRefresh()
    }

    protected abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): BINDING
    protected abstract fun createViewModel(): VIEWMODEL
    protected abstract fun bindView(binding: BINDING, viewModel: VIEWMODEL)
    protected abstract fun observeViewModel(binding: BINDING, viewModel: VIEWMODEL)
}