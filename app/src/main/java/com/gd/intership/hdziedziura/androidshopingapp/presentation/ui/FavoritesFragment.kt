package com.gd.intership.hdziedziura.androidshopingapp.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.databinding.FragmentFavoritesBinding
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ext.navigate
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ext.viewModels
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.favorites.FavoritesViewModel
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch

class FavoritesFragment : BaseFragment() {
    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var binding: FragmentFavoritesBinding
    override val showToolbar: Boolean
        get() = true
    override val showBottomNavBar: Boolean
        get() = true
    override val toolbarTitle: String
        get() = "Favorites"
    override val toolbarIcon: Int
        get() = R.drawable.ic_baseline_search_24

    private val sortBottomSheet: SortBottomSheetFavoritesFragment = SortBottomSheetFavoritesFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_favorites, container, false
        )

        lifecycle.addObserver(viewModel)

        layoutManagers()
//        addChips(viewModel.chipCategories)
        sortText(viewModel.sortText)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiEvents.collect {
                        when (it) {
                            is FavoritesViewModel.Event.FetchingErrorEvent -> Toast.makeText(
                                context, getString(R.string.error_fetching), Toast.LENGTH_SHORT
                            ).show()
                            is FavoritesViewModel.Event.ProductClickEvent -> {
                                val bundle = bundleOf(
                                    "itemCategory" to it.item.category,
                                    "itemTitle" to it.item.title
                                )
                                navigate(
                                    R.id.action_favoritesFragment_to_productDetailsFragment,
                                    bundle
                                )
                            }
                            is FavoritesViewModel.Event.LayoutClickEvent -> layoutManagers()
                            is FavoritesViewModel.Event.DeleteEvent -> viewModel.deleteFromDb(it.item)
                            is FavoritesViewModel.Event.DeleteErrorEvent -> Toast.makeText(
                                context, getString(R.string.error_deleting), Toast.LENGTH_SHORT
                            ).show()
                            is FavoritesViewModel.Event.AddToCartEvent -> viewModel.addToCart(it.item)
                            is FavoritesViewModel.Event.AddToCartSuccess -> Toast.makeText(
                                context, getString(R.string.cart_success), Toast.LENGTH_SHORT
                            ).show()
                            is FavoritesViewModel.Event.AddToCartError -> Toast.makeText(
                                context, getString(R.string.cart_fail), Toast.LENGTH_SHORT
                            ).show()
                            is FavoritesViewModel.Event.SortClickEvent ->
                                sortBottomSheet.show(
                                    childFragmentManager,
                                    SortBottomSheetFavoritesFragment::class.simpleName
                                )
                            is FavoritesViewModel.Event.SortTypeClickEvent -> {
                                viewModel.sortText = it.item.text
                                sortText(it.item.text)
                                sortBottomSheet.dismiss()
                                viewModel.sortClosed()
                            }
//                            is FavoritesViewModel.Event.FiltersClickEvent ->
//                                ShopFiltersFragment.show(
//                                    childFragmentManager,
//                                    ShopFiltersFragment::class.simpleName
//                                )
                            else -> {}
                        }
                    }
                }
            }
        }

        binding.vm = viewModel

        return binding.root
    }

    private fun sortText(title: Int) {
        binding.sortText.setText(title)
    }

    private fun layoutManagers() {
        binding.recyclerProducts.layoutManager = null
        if (viewModel.isList) {
            binding.recyclerProducts.layoutManager = LinearLayoutManager(context)
            binding.layout.setImageResource(R.drawable.ic_baseline_view_module_24)
        } else {
            binding.recyclerProducts.layoutManager = GridLayoutManager(context, 2)
            binding.layout.setImageResource(R.drawable.ic_baseline_view_list_24)
        }
    }

    private fun addChips(category: Map<String, Boolean>) {
        category.forEach {
            newChip(it.key, it.value)
        }
    }

    private fun newChip(text: String, checked: Boolean) {
        val chip = layoutInflater.inflate(
            R.layout.chip_shop_product_list,
            binding.chipGroup,
            false
        ) as Chip

        chip.text = text
        chip.isChecked = checked

        chip.setOnClickListener {
//            if (chip.isChecked) viewModel.checkChip(text)
//            else viewModel.uncheckChip(text)
        }
        binding.chipGroup.addView(chip)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }
}
