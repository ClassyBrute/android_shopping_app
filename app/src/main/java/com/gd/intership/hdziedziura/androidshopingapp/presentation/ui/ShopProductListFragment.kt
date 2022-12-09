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
import com.gd.intership.hdziedziura.androidshopingapp.databinding.FragmentShopProductListBinding
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ext.navigate
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ext.viewModels
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.ShopProductListViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch

class ShopProductListFragment : BaseFragment() {
    private val viewModel: ShopProductListViewModel by viewModels()
    private lateinit var binding: FragmentShopProductListBinding
    override val showToolbar: Boolean
        get() = true
    override val showBottomNavBar: Boolean
        get() = true
    override val toolbarTitle: String
        get() = arguments?.getString("itemTitle") ?: ""
    override val toolbarIcon: Int
        get() = R.drawable.ic_baseline_search_24

    private var sizeBottomSheet: SizeBottomSheetShopFragment = SizeBottomSheetShopFragment()
    private var colorBottomSheet: ColorBottomSheetShopFragment = ColorBottomSheetShopFragment()

    private lateinit var favoriteButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_shop_product_list, container, false
        )

        lifecycle.addObserver(viewModel)

        layoutManagers()
        addChips(viewModel.chipCategories)
        sortText(viewModel.sortText)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiEvents.collect {
                        when (it) {
                            is ShopProductListViewModel.Event.FetchingErrorEvent -> Toast.makeText(
                                context, getString(R.string.error_fetching), Toast.LENGTH_SHORT
                            ).show()
                            is ShopProductListViewModel.Event.ProductClickEvent -> {
                                val bundle = bundleOf(
                                    "itemCategory" to it.item.category,
                                    "itemTitle" to it.item.title
                                )
                                navigate(R.id.action_shopProductListFragment_to_productDetailsFragment, bundle)
                            }
                            is ShopProductListViewModel.Event.LayoutClickEvent -> layoutManagers()

                            is ShopProductListViewModel.Event.SortClickEvent ->
                                SortBottomSheetFragment.show(
                                    childFragmentManager,
                                    SortBottomSheetFragment::class.simpleName
                                )
                            is ShopProductListViewModel.Event.SortClosedEvent -> sortText(it.titleResId)
                            is ShopProductListViewModel.Event.FiltersClickEvent ->
                                ShopFiltersFragment.show(
                                    childFragmentManager,
                                    ShopFiltersFragment::class.simpleName
                                )
                            is ShopProductListViewModel.Event.FavoriteClickEvent -> {
                                viewModel.favoriteClick(it.item)

                                favoriteButton = (it.view as MaterialButton)
                                favoriteButton.isChecked = false

                                if (viewModel.chosenColorFav.get() == "Color" && !it.item.isFavorite) {
                                    colorBottomSheet.show(
                                        childFragmentManager,
                                        ColorBottomSheetShopFragment::class.simpleName
                                    )
                                }
                                if (viewModel.chosenSizeFav.get() == "Size" && !it.item.isFavorite) {
                                    sizeBottomSheet.show(
                                        childFragmentManager,
                                        SizeBottomSheetShopFragment::class.simpleName
                                    )
                                }
                            }
                            is ShopProductListViewModel.Event.ColorsSheetDismiss -> {
                                viewModel.chosenColorFav.set("Color")
                                viewModel.chosenSizeFav.set("Size")
                                colorBottomSheet.dismiss()
                            }
                            is ShopProductListViewModel.Event.SizesSheetDismiss -> {
                                sizeBottomSheet.dismiss()
                            }
                            is ShopProductListViewModel.Event.AddedToFavorites -> {
                                favoriteButton.isChecked = true
                            }
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
            if (chip.isChecked) viewModel.checkChip(text)
            else viewModel.uncheckChip(text)
        }
        binding.chipGroup.addView(chip)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }
}
