package com.gd.intership.hdziedziura.androidshopingapp.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.databinding.BottomSheetSizeBinding
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ext.parentFragmentViewModels
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.details.ProductDetailsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SizeBottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        fun show(fragmentManager: FragmentManager, tag: String?) {
            SizeBottomSheetFragment().show(fragmentManager, tag)
        }
    }

    private lateinit var binding: BottomSheetSizeBinding
    private val viewModel: ProductDetailsViewModel by parentFragmentViewModels()
    private val layoutManager = GridLayoutManager(context, 3)

    override fun getTheme() = R.style.NoBackgroundDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.bottom_sheet_size, container, false
        )
        view?.setBackgroundResource(R.drawable.rounded_dialog)

        binding.recyclerGrid.layoutManager = layoutManager

        binding.vm = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.executePendingBindings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerGrid.layoutManager = null
    }
}
