package com.pecawolf.charactersheet.ui

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.pecawolf.presentation.SimpleSelectionItem
import com.pecawolf.presentation.viewmodel.BaseViewModel
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

abstract class BaseFragment<VIEWMODEL : BaseViewModel, BINDING : ViewBinding> : DialogFragment() {

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

        dialog?.apply {
            val params: WindowManager.LayoutParams = window!!.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            window!!.attributes = params
        }

        viewModel.onRefresh()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = getBinding(LayoutInflater.from(requireContext()), null)
        return AlertDialog.Builder(requireContext())
            .show()
            .apply {
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setStyle(STYLE_NORMAL, android.R.style.Theme)

                setContentView(binding.root)
            }
    }

    protected abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): BINDING
    protected abstract fun createViewModel(): VIEWMODEL
    protected abstract fun bindView(binding: BINDING, viewModel: VIEWMODEL)
    protected abstract fun observeViewModel(binding: BINDING, viewModel: VIEWMODEL)

    protected fun showSingleChoiceDialog(
        title: String,
        message: String,
        positiveButton: String,
        positive: () -> Unit
    ) {
        dialogHelper.showSingleChoiceDialog(title, message, positiveButton, positive)
    }

    protected fun showTwoChoiceDialog(
        title: String,
        message: String,
        positiveButton: String,
        positive: () -> Unit
    ) {
        dialogHelper.showTwoChoiceDialog(title, message, positiveButton, positive)
    }

    protected fun showThreeChoiceDialog(
        title: String,
        message: String,
        positiveButton: String,
        positive: () -> Unit,
        negativeButton: String,
        negative: () -> Unit
    ) {
        dialogHelper.showThreeChoiceDialog(
            title,
            message,
            positiveButton,
            positive,
            negativeButton,
            negative
        )
    }

    protected fun <T> showListChoiceDialog(
        title: String,
        isSingleChoice: Boolean,
        items: List<SimpleSelectionItem>,
        positive: (List<T>) -> Unit,
    ) {
        dialogHelper.showListChoiceDialog(title, isSingleChoice, items, positive)
    }

    protected fun showTextInputDialog(
        title: String,
        message: String?,
        inputType: Int,
        lineCount: Int,
        defaultInput: String?,
        hint: String,
        positiveButton: String,
        positive: (String) -> Unit
    ) {
        dialogHelper.showTextInputDialog(
            title,
            message,
            inputType,
            lineCount,
            defaultInput,
            hint,
            positiveButton,
            positive
        )
    }
}