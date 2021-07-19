package com.pecawolf.charactersheet.ui

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.DialogMultiChoiceBinding
import com.pecawolf.charactersheet.databinding.DialogSingleChoiceBinding
import com.pecawolf.charactersheet.databinding.DialogTextInputBinding
import com.pecawolf.charactersheet.databinding.DialogThreeChoiceBinding
import com.pecawolf.charactersheet.databinding.DialogTwoChoiceBinding
import com.pecawolf.charactersheet.ui.view.showKeyboard
import com.pecawolf.presentation.SimpleSelectionItem
import kotlin.math.min

class DialogHelper(private val context: Context) {

    fun showSingleChoiceDialog(
        title: String,
        message: String,
        positiveButton: String,
        positive: () -> Unit
    ) {
        val binding = DialogSingleChoiceBinding.inflate(LayoutInflater.from(context))
        val dialog: AlertDialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .show()
            .apply {
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }

        binding.singleChoiceHeader.text = title
        binding.singleChoiceMessage.text = message
        binding.singleChoiceButton.apply {
            text = positiveButton
            setOnClickListener {
                positive.invoke()
                dialog.cancel()
            }
        }
    }

    fun showTwoChoiceDialog(
        title: String,
        message: String,
        positiveButton: String,
        positive: () -> Unit
    ) {
        val binding = DialogTwoChoiceBinding.inflate(LayoutInflater.from(context))
        val dialog: AlertDialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .show()
            .apply {
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }

        binding.twoChoiceHeader.text = title
        binding.twoChoiceMessage.text = message
        binding.twoChoiceButtonPositive.apply {
            text = positiveButton
            setOnClickListener {
                positive.invoke()
                dialog.cancel()
            }
        }
        binding.twoChoiceButtonNegative.apply {
            setText(R.string.generic_cancel)
            setOnClickListener { dialog.cancel() }
        }
    }

    fun showThreeChoiceDialog(
        title: String,
        message: String,
        positiveButton: String,
        positive: () -> Unit,
        negativeButton: String,
        negative: () -> Unit
    ) {
        val binding = DialogThreeChoiceBinding.inflate(LayoutInflater.from(context))
        val dialog: AlertDialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .show()
            .apply {
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }

        binding.threeChoiceHeader.text = title
        binding.threeChoiceMessage.text = message
        binding.threeChoiceButtonPositive.apply {
            text = positiveButton
            setOnClickListener {
                positive.invoke()
                dialog.cancel()
            }
        }
        binding.threeChoiceButtonNegative.apply {
            text = negativeButton
            setOnClickListener {
                negative.invoke()
                dialog.cancel()
            }
        }
        binding.threeChoiceButtonNeutral.apply {
            setText(R.string.generic_cancel)
            setOnClickListener { dialog.cancel() }
        }
    }

    fun <T> showListChoiceDialog(
        title: String,
        isSingleChoice: Boolean,
        items: List<SimpleSelectionItem>,
        positive: (List<T>) -> Unit,
    ) {
        val binding = DialogMultiChoiceBinding.inflate(LayoutInflater.from(context))

        val dialog: AlertDialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .show()
            .apply {
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }

        binding.multiChoiceRecycler.apply {
            layoutParams.height = min(items.size, 5)
                .times(resources.getDimensionPixelSize(R.dimen.slection_item_height))

            post {
                requestLayout()

                adapter = SimpleSelectionAdapter {
                    (adapter as SimpleSelectionAdapter).items =
                        if (isSingleChoice) items.map { item ->
                            item.copy(isChecked = item.data == it)
                        }
                        else items.map { item: SimpleSelectionItem ->
                            if (item.data == it) item.copy(isChecked = !item.isChecked)
                            else item
                        }
                }
                layoutManager = LinearLayoutManager(context)
                (adapter as SimpleSelectionAdapter).items = items
            }
        }

        binding.multiChoiceHeader.text = title
        binding.multiChoiceButtonPositive.apply {
            setText(R.string.generic_ok)
            setOnClickListener {
                (binding.multiChoiceRecycler.adapter as SimpleSelectionAdapter).items
                    .filter { it.isChecked }
                    .map { it.data as T }
                    .let { list ->
                        positive.invoke(list)
                        dialog.cancel()
                    }
            }
        }
        binding.multiChoiceButtonNegative.apply {
            setText(R.string.generic_cancel)
            setOnClickListener { dialog.cancel() }
        }
    }

    fun showTextInputDialog(
        title: String,
        message: String?,
        inputType: Int,
        lineCount: Int,
        defaultInput: String?,
        hint: String,
        positiveButton: String,
        positive: (String) -> Unit
    ) {

        val binding = DialogTextInputBinding.inflate(LayoutInflater.from(context))

        val dialog: AlertDialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .show()
            .apply {
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }

        binding.textInputHeader.text = title
        binding.textInputMessage.apply {
            text = message ?: ""
            isVisible = message?.isNotBlank() ?: false
        }
        binding.textInputInput.apply {
            this.inputType = inputType
            setLines(lineCount)
            maxLines = lineCount
            gravity =
                if (lineCount == 1) {
                    Gravity.START or Gravity.CENTER_VERTICAL
                } else {
                    setHorizontallyScrolling(false)
                    isHorizontalScrollBarEnabled = false
                    Gravity.START or Gravity.TOP
                }

            setText(defaultInput)
            setSelection(defaultInput?.length ?: 0)
            showKeyboard()
            addTextChangedListener {
                binding.textInputButtonPositive.isEnabled = it?.isNotBlank() ?: false
            }
        }
        binding.textInputInputLayout.hint = hint

        binding.textInputButtonPositive.apply {
            text = positiveButton
            setOnClickListener {
                positive.invoke(binding.textInputInput.text?.toString() ?: "")
                dialog.cancel()
            }
        }
        binding.textInputButtonNegative.apply {
            setText(R.string.generic_cancel)
            setOnClickListener {
                dialog.cancel()
            }
        }
    }
}
