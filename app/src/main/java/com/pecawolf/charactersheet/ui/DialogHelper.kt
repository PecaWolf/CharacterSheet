package com.pecawolf.charactersheet.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Html
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.DialogMultiChoiceBinding
import com.pecawolf.charactersheet.databinding.DialogSingleChoiceBinding
import com.pecawolf.charactersheet.databinding.DialogTextInputBinding
import com.pecawolf.charactersheet.databinding.DialogThreeChoiceBinding
import com.pecawolf.charactersheet.databinding.DialogTwoChoiceBinding
import com.pecawolf.charactersheet.ext.getLocalizedName
import com.pecawolf.charactersheet.ext.getName
import com.pecawolf.charactersheet.ui.view.showKeyboard
import com.pecawolf.model.Item
import com.pecawolf.model.RollResult
import com.pecawolf.model.Rollable
import com.pecawolf.presentation.SimpleSelectionItem
import kotlin.math.min

class DialogHelper(private val context: Context) {

    // region generic dialogs

    fun showSingleChoiceDialog(
        title: String,
        message: String,
        positiveButton: String,
        positive: (Dialog) -> Unit
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
                positive.invoke(dialog)
            }
        }
    }

    fun showTwoChoiceDialog(
        title: String,
        message: String,
        positiveButton: String,
        positive: (Dialog) -> Unit
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
                positive.invoke(dialog)
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
        positive: (Dialog) -> Unit,
        negativeButton: String,
        negative: (Dialog) -> Unit
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
                positive.invoke(dialog)
                dialog.cancel()
            }
        }
        binding.threeChoiceButtonNegative.apply {
            text = negativeButton
            setOnClickListener {
                negative.invoke(dialog)
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
        positive: (Dialog, List<T>) -> Unit,
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
                .times(resources.getDimensionPixelSize(R.dimen.bar_height))

            post {
                requestLayout()

                binding.multiChoiceButtonPositive.isEnabled =
                    items.any { it.isChecked }

                adapter = SimpleSelectionAdapter { clicked ->
                    val adapter = (adapter as SimpleSelectionAdapter)

                    if (isSingleChoice) {
                        adapter.items = adapter.items.map { item ->
                            item.copy(isChecked = item.data == clicked)
                        }
                    } else {
                        adapter.items = adapter.items.map { item ->
                            if (item.data == clicked) item.copy(isChecked = !item.isChecked)
                            else item
                        }
                    }
                    binding.multiChoiceButtonPositive.isEnabled =
                        adapter.items.any { it.isChecked }
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
                    .let { list -> positive.invoke(dialog, list) }
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
        positive: (Dialog, String) -> Unit
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
                positive.invoke(dialog, binding.textInputInput.text?.toString() ?: "")
            }
        }
        binding.textInputButtonNegative.apply {
            setText(R.string.generic_cancel)
            setOnClickListener {
                dialog.cancel()
            }
        }
    }

    // endregion generic dialogs

    // region specific dialogs

    fun showEquipConfirmDialog(
        name: String,
        allowedSlots: List<Item.Slot>,
        positive: (Dialog, List<Item.Slot>) -> Unit
    ) {
        val items = allowedSlots
            .map { SimpleSelectionItem(it.getLocalizedName(context), false, it) }
        showListChoiceDialog(
            getString(R.string.item_equip_slot_selection_description, name),
            true,
            items,
            positive
        )
    }

    fun showUnequipConfirmDialog(name: String, slot: Item.Slot, positive: (Dialog) -> Unit) {
        showTwoChoiceDialog(
            getString(R.string.item_unequip_slot_title),
            getString(
                R.string.item_unequip_slot_description,
                name,
                slot.getLocalizedName(context)
            ),
            getString(R.string.generic_continue),
            positive
        )
    }

    fun showDeleteItemDialog(
        name: String,
        onClick: (Dialog, Int) -> Unit
    ) {
        showThreeChoiceDialog(
            getString(R.string.item_delete_title),
            getString(R.string.item_delete_message, name),
            getString(R.string.item_delete_sell),
            { dialog ->
                showSellItemDialog(name, onClick)
                dialog.cancel()
            },
            getString(R.string.item_delete_discard),
            { dialog -> onClick.invoke(dialog, 0) },
        )
    }

    fun showSellItemDialog(name: String, positive: (Dialog, Int) -> Unit) {
        showTextInputDialog(
            getString(R.string.item_sell_title),
            getString(R.string.item_sell_message, name),
            InputType.TYPE_CLASS_NUMBER,
            1,
            null,
            getString(R.string.item_sell_hint),
            getString(R.string.generic_continue)
        ) { dialog, result ->
            val price = result.toInt()
            if (price != 0) positive.invoke(dialog, price) // TODO: Error case
        }
    }

    fun showRollModifierDialog(rollable: Rollable, onPositive: (Dialog, String) -> Unit) {
        showTextInputDialog(
            getString(R.string.roll_modifier_title, rollable.getName(context)),
            getString(R.string.roll_modifier_message),
            InputType.TYPE_CLASS_NUMBER,
            1,
            "0",
            getString(R.string.roll_modifier_hint),
            getString(R.string.roll_modifier_positive),
            onPositive
        )
    }

    fun showRollResultDialog(roll: Int, rollResult: RollResult) {
        showSingleChoiceDialog(
            getString(R.string.roll_result_title),
            Html.fromHtml(
                getString(
                    R.string.roll_result_message,
                    roll,
                    rollResult.getLocalizedName(context)
                )
            )
                .toString(),
            getString(R.string.generic_ok)
        ) { dialog -> dialog.cancel() }
    }

    // endregion specific dialogs

    private fun getString(@StringRes resId: Int, vararg formatArgs: Any?) =
        context.resources.getString(resId, *formatArgs)

    private fun getString(@StringRes resId: Int) = context.resources.getString(resId)
}
