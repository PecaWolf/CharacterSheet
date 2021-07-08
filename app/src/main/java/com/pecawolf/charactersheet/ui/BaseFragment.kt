package com.pecawolf.charactersheet.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VIEWMODEL : ViewModel, BINDING : ViewBinding> : Fragment() {

    protected val viewModel: VIEWMODEL by lazy { createViewModel() }

    protected lateinit var binding: BINDING

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

    protected abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): BINDING
    protected abstract fun createViewModel(): VIEWMODEL
    protected abstract fun bindView(binding: BINDING, viewModel: VIEWMODEL)
    protected abstract fun observeViewModel(binding: BINDING, viewModel: VIEWMODEL)
}
