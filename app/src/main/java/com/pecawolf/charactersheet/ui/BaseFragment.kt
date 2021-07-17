package com.pecawolf.charactersheet.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.pecawolf.charactersheet.R
import com.pecawolf.presentation.viewmodel.BaseViewModel

abstract class BaseFragment<VIEWMODEL : BaseViewModel, BINDING : ViewBinding> : Fragment() {

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

    override fun onResume() {
        super.onResume()
        viewModel.onRefresh()
    }

    protected abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): BINDING
    protected abstract fun createViewModel(): VIEWMODEL
    protected abstract fun bindView(binding: BINDING, viewModel: VIEWMODEL)
    protected abstract fun observeViewModel(binding: BINDING, viewModel: VIEWMODEL)

    protected fun showTwoChoiceDialog(
        title: String,
        message: String,
        positiveButton: String,
        positive: () -> Unit
    ) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButton) { dialog, _ -> positive.invoke() }
            .setNegativeButton(R.string.generic_cancel) { dialog, _ -> dialog.cancel() }
            .show()
    }

    protected fun showThreeChoiceDialog(
        title: String,
        message: String,
        positiveButton: String,
        positive: () -> Unit,
        negativeButton: String,
        negative: () -> Unit
    ) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButton) { dialog, _ -> positive.invoke() }
            .setNegativeButton(negativeButton) { dialog, _ -> negative.invoke() }
            .setNeutralButton(R.string.generic_cancel) { dialog, _ -> dialog.cancel() }
            .show()
    }

    protected fun <T> showMultiChoiceDialog(
        title: String,
        items: List<T>,
        itemMapper: (T) -> String,
        onItemSelected: (T) -> Unit
    ) {
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1
        )
        arrayAdapter.addAll(items.map { itemMapper.invoke(it) })

        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setAdapter(arrayAdapter) { _, which -> onItemSelected.invoke(items[which]) }
            .setNegativeButton(R.string.generic_cancel) { dialog, _ -> dialog.dismiss() }
            .show()
    }
}