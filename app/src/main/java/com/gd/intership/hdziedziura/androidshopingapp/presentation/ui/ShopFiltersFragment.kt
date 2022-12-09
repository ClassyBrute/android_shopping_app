package com.gd.intership.hdziedziura.androidshopingapp.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.databinding.FragmentShopProductFiltersBinding
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ext.parentFragmentViewModels
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.ShopProductListViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ShopFiltersFragment : BottomSheetDialogFragment() {

    companion object {
        fun show(fragmentManager: FragmentManager, tag: String?) {
            ShopFiltersFragment().show(fragmentManager, tag)
        }
    }

    private val viewModel: ShopProductListViewModel by parentFragmentViewModels()
    private lateinit var binding: FragmentShopProductFiltersBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_shop_product_filters, container, false
        )

        binding.discardButton.setOnClickListener {
            dismiss()
        }

        binding.applyButton.setOnClickListener {
            viewModel.applyFilters()
            dismiss()
        }

        binding.rangeSlider.addOnChangeListener { _, _, _ ->
            val values = binding.rangeSlider.values
            viewModel.lowestPriceString.set("$%.2f".format(values[0]))
            viewModel.highestPriceString.set("$%.2f".format(values[1]))
        }

        binding.vm = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            isDraggable = false
            isFitToContents = false
            state = STATE_EXPANDED
        }
        binding.executePendingBindings()
    }
}
