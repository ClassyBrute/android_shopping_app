package com.gd.intership.hdziedziura.androidshopingapp.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.databinding.FragmentHomeBinding
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ext.navigate
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ext.viewModels
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.home.HomeViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment() {

    private val viewModel: HomeViewModel by viewModels()
    private var binding: FragmentHomeBinding? = null
    override val showToolbar: Boolean
        get() = false
    override val showBottomNavBar: Boolean
        get() = true

    private var sizeBottomSheet: SizeBottomSheetHomeFragment = SizeBottomSheetHomeFragment()
    private var colorBottomSheet: ColorBottomSheetHomeFragment = ColorBottomSheetHomeFragment()

    private lateinit var favoriteButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        // TODO works but should be fixed
        // according to the comments of the google dev https://twitter.com/ianhlake/status/1103906999064907777
        if (binding == null)
            binding = FragmentHomeBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycle.addObserver(viewModel)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiEvents.collect {
                        when (it) {
                            is HomeViewModel.Event.FetchingErrorEvent -> Toast.makeText(
                                context, getString(R.string.error_fetching), Toast.LENGTH_SHORT
                            ).show()
                            is HomeViewModel.Event.BannerClickEvent -> {
                                val bundle = bundleOf(
                                    "itemTitle" to it.item.category
                                )
                                navigate(R.id.action_homeFragment_to_shopProductListFragment, bundle)
                            }
                            is HomeViewModel.Event.BannerBigTopClickEvent -> {
                                val bundle = bundleOf(
                                    "itemTitle" to it.item.categoryTop
                                )
                                navigate(R.id.action_homeFragment_to_shopProductListFragment, bundle)
                            }
                            is HomeViewModel.Event.BannerBigRightClickEvent -> {
                                val bundle = bundleOf(
                                    "itemTitle" to it.item.categoryRight
                                )
                                navigate(R.id.action_homeFragment_to_shopProductListFragment, bundle)
                            }
                            is HomeViewModel.Event.BannerBigLeftClickEvent -> {
                                val bundle = bundleOf(
                                    "itemTitle" to it.item.categoryLeft
                                )
                                navigate(R.id.action_homeFragment_to_shopProductListFragment, bundle)
                            }
                            is HomeViewModel.Event.BannerBigBottomClickEvent -> {
                                val bundle = bundleOf(
                                    "itemTitle" to it.item.categoryBottom
                                )
                                navigate(R.id.action_homeFragment_to_shopProductListFragment, bundle)
                            }
                            is HomeViewModel.Event.CarouselClickEvent -> {
                                val bundle = bundleOf(
                                    "itemTitle" to it.item.title
                                )
                                navigate(R.id.action_homeFragment_to_shopProductListFragment, bundle)
                            }
                            is HomeViewModel.Event.ProductClickEvent -> {
                                val bundle = bundleOf(
                                    "itemCategory" to it.item.category,
                                    "itemTitle" to it.item.title
                                )
                                navigate(R.id.action_homeFragment_to_productDetailsFragment, bundle)
                            }
                            is HomeViewModel.Event.FavoriteClickEvent -> {
                                viewModel.favoriteClick(it.item)

                                favoriteButton = (it.view as MaterialButton)
                                favoriteButton.isChecked = false

                                if (viewModel.chosenColor.get() == "Color" && !it.item.isFavorite) {
                                    colorBottomSheet.show(
                                        childFragmentManager,
                                        ColorBottomSheetHomeFragment::class.simpleName
                                    )
                                }
                                if (viewModel.chosenSize.get() == "Size" && !it.item.isFavorite) {
                                    sizeBottomSheet.show(
                                        childFragmentManager,
                                        SizeBottomSheetHomeFragment::class.simpleName
                                    )
                                }
                            }
                            is HomeViewModel.Event.ColorsSheetDismiss -> {
                                viewModel.chosenColor.set("Color")
                                viewModel.chosenSize.set("Size")
                                colorBottomSheet.dismiss()
                            }
                            is HomeViewModel.Event.SizesSheetDismiss -> {
                                sizeBottomSheet.dismiss()
                            }
                            is HomeViewModel.Event.AddedToFavorites -> {
                                favoriteButton.isChecked = true
                            }
                            is HomeViewModel.Event.NavigateToAuth -> navigate(R.id.action_home_auth)
                        }
                    }
                }
            }
        }

        binding!!.vm = viewModel

        return binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        lifecycle.removeObserver(viewModel)
    }
}
