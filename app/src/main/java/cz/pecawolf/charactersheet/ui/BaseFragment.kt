package cz.pecawolf.charactersheet.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class BaseFragment<VIEWMODEL: ViewModel, BINDING: ViewDataBinding> : Fragment() {

    protected val viewModel: VIEWMODEL by lazy { createViewModel() }
    protected lateinit var binding: BINDING

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        bindView(binding)
        observeViewModel()
    }

    protected abstract fun getLayoutResource(): Int
    protected abstract fun createViewModel(): VIEWMODEL
    protected abstract fun bindView(binding: BINDING)
    protected abstract fun observeViewModel()
}
