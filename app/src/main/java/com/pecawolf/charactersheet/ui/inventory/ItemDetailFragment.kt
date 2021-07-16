package com.pecawolf.charactersheet.ui.inventory

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.FragmentItemDetailBinding
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.presentation.extensions.reObserve
import com.pecawolf.presentation.viewmodel.ItemDetailViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ItemDetailFragment : BaseFragment<ItemDetailViewModel, FragmentItemDetailBinding>() {

    override fun getBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentItemDetailBinding.inflate(inflater, container, false)

    override fun createViewModel() = viewModel<ItemDetailViewModel> {
        ItemDetailFragmentArgs.fromBundle(requireArguments()).run {
            parametersOf(itemId)
        }
    }.value

    override fun bindView(
        binding: FragmentItemDetailBinding,
        viewModel: ItemDetailViewModel
    ) {
    }

    override fun observeViewModel(
        binding: FragmentItemDetailBinding,
        viewModel: ItemDetailViewModel
    ) {
        viewModel.item.reObserve(this) {

        }

        viewModel.showNotFound.reObserve(this) {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.item_detail_error_title)
                .setMessage(R.string.item_detail_error_message)
                .setCancelable(false)
                .setOnCancelListener { findNavController().popBackStack() }
                .setPositiveButton(
                    R.string.generic_back
                ) { dialog, _ -> dialog.cancel() }
                .show()
        }
    }
}