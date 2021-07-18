package com.pecawolf.charactersheet.ui

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.WidgetDialogEditTextBinding
import com.pecawolf.charactersheet.databinding.WidgetDialogRecyclerBinding
import com.pecawolf.presentation.SimpleSelectionItem
import com.pecawolf.presentation.viewmodel.BaseViewModel

abstract class BaseFragment<VIEWMODEL : BaseViewModel, BINDING : ViewBinding> : DialogFragment() {

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

        dialog?.apply {
            val params: WindowManager.LayoutParams = window!!.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            params.horizontalMargin = resources.getDimension(R.dimen.spacing_2)
            params.verticalMargin = resources.getDimension(R.dimen.spacing_4)
            window!!.attributes = params
        }

        viewModel.onRefresh()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = getBinding(LayoutInflater.from(requireContext()), null)
        return Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
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
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButton) { dialog, _ -> positive.invoke() }
            .show()
    }

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

    protected fun <T> showSingleChoiceListDialog(
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

    protected fun <T> showMultiChoiceListDialog(
        title: String,
        items: List<T>,
        itemMapper: (T) -> String,
        positiveButton: String,
        positive: (List<T>) -> Unit
    ) {
        val initialItems = items.map {
            SimpleSelectionItem(itemMapper.invoke(it), false, it)
        }
        val adapter = SimpleSelectionAdapter(initialItems) {}

        val layout = WidgetDialogRecyclerBinding.inflate(LayoutInflater.from(requireContext()))
            .also { binding ->
                binding.dialogRecycler.adapter = adapter
                binding.dialogRecycler.layoutManager = LinearLayoutManager(requireContext())
            }
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(layout.root)
            .setPositiveButton(positiveButton) { dialog, _ ->
                positive.invoke(adapter.items.filter { it.isChecked }.mapNotNull { it.data as T })
            }
            .setNegativeButton(R.string.generic_cancel) { dialog, _ -> dialog.cancel() }
            .show()
    }

    protected fun showTextInputDialog(
        title: String,
        inputType: Int,
        lineCount: Int,
        defaultInput: String?,
        hint: String,
        positiveButton: String,
        positive: (String) -> Unit
    ) {
        val layout = WidgetDialogEditTextBinding.inflate(LayoutInflater.from(requireContext()))
            .also { binding ->
                binding.dialogEditText.apply {
                    this.inputType = inputType
                    setLines(lineCount)

                    setText(defaultInput)
                    setSelection(defaultInput?.length ?: 0)
                }
                binding.dialogEditLayout.hint = hint
            }
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(layout.root)
            .setPositiveButton(positiveButton) { dialog, _ ->
                positive.invoke(
                    layout.dialogEditText.text?.toString() ?: ""
                )
            }
            .setNegativeButton(R.string.generic_cancel) { dialog, _ -> dialog.cancel() }
            .show()
    }
}