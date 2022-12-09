package com.gd.intership.hdziedziura.androidshopingapp.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.databinding.BottomSheetSortFavoritesBinding
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ext.parentFragmentViewModels
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.favorites.FavoritesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SortBottomSheetFavoritesFragment : BottomSheetDialogFragment() {

    companion object {
        fun show(fragmentManager: FragmentManager, tag: String?) {
            SortBottomSheetFavoritesFragment().show(fragmentManager, tag)
        }
    }

    private lateinit var binding: BottomSheetSortFavoritesBinding
    private val viewModel: FavoritesViewModel by parentFragmentViewModels()
    private val layoutManager = LinearLayoutManager(context)

    override fun getTheme() = R.style.NoBackgroundDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.bottom_sheet_sort_favorites, container, false
        )
        view?.setBackgroundResource(R.drawable.rounded_dialog)

        binding.recycler.layoutManager = layoutManager

        binding.vm = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.executePendingBindings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recycler.layoutManager = null
    }
}
