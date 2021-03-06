package cz.pecawolf.charactersheet.ui

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<BINDING: ViewDataBinding>: Fragment() {
    protected lateinit var binding: BINDING

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        bindView(binding)
        observeViewModel()
    }

    protected abstract fun bindView(binding: BINDING)
    abstract fun observeViewModel()
}
