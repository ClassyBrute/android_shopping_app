package com.gd.intership.hdziedziura.androidshopingapp.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.databinding.BottomSheetReviewBinding
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ext.parentFragmentViewModels
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.reviews.ProductReviewsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class ReviewBottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        fun show(fragmentManager: FragmentManager, tag: String?) {
            ReviewBottomSheetFragment().show(fragmentManager, tag)
        }
    }

    private lateinit var binding: BottomSheetReviewBinding
    private val viewModel: ProductReviewsViewModel by parentFragmentViewModels()

    override fun getTheme() = R.style.NoBackgroundDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.bottom_sheet_review, container, false
        )
        view?.setBackgroundResource(R.drawable.rounded_dialog)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiEvents.collect {
                        when (it) {
                            is ProductReviewsViewModel.Event.CloseBottomSheetEvent -> dismiss()
                            else -> {}
                        }
                    }
                }
            }
        }

        binding.vm = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.executePendingBindings()
    }
}
