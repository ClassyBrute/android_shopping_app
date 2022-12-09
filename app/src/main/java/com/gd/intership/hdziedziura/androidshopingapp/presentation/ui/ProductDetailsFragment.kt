package com.gd.intership.hdziedziura.androidshopingapp.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.databinding.FragmentProductDetailsBinding
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ext.navigate
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ext.viewModels
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.details.ProductDetailsViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class ProductDetailsFragment : BaseFragment() {
    private val viewModel: ProductDetailsViewModel by viewModels()
    private lateinit var binding: FragmentProductDetailsBinding
    override val showToolbar: Boolean
        get() = true
    override val showBottomNavBar: Boolean
        get() = false
    override val toolbarTitle: String
        get() = arguments?.getString("itemTitle").toString()
    override val toolbarIcon: Int
        get() = R.drawable.ic_baseline_share_24

    private var sizeBottomSheet: SizeBottomSheetFragment = SizeBottomSheetFragment()
    private var colorBottomSheet: ColorBottomSheetFragment = ColorBottomSheetFragment()
    private var sizeBottomSheetFav: SizeBottomSheetFavFragment = SizeBottomSheetFavFragment()
    private var colorBottomSheetFav: ColorBottomSheetFavFragment = ColorBottomSheetFavFragment()

    private lateinit var favoriteButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_product_details, container, false
        )

        viewModel.category = arguments?.getString("itemCategory").toString()
        viewModel.title = arguments?.getString("itemTitle").toString()

        lifecycle.addObserver(viewModel)

        (requireActivity() as MainActivity).findViewById<ImageButton>(R.id.toolbar_icon)
            .setOnClickListener {
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        with(viewModel.product.get()) {
                            "Title: ${this?.title}\n" +
                                "Brand: ${this?.brand}\n" +
                                "Price: ${this?.price}$\n" +
                                "Image: ${this?.image}"
                        }
                    )
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiEvents.collect {
                        when (it) {
                            is ProductDetailsViewModel.Event.ProductClickEvent -> {
                                val bundle = bundleOf(
                                    "itemCategory" to it.item.category,
                                    "itemTitle" to it.item.title
                                )
                                navigate(
                                    R.id.action_productDetailsFragment_to_productDetailsFragment,
                                    bundle
                                )
                            }
                            is ProductDetailsViewModel.Event.FetchingErrorEvent -> Toast.makeText(
                                context, getString(R.string.error_fetching), Toast.LENGTH_SHORT
                            ).show()
                            is ProductDetailsViewModel.Event.SizesClickEvent ->
                                sizeBottomSheet.show(
                                    childFragmentManager,
                                    SizeBottomSheetFragment::class.simpleName
                                )
                            is ProductDetailsViewModel.Event.ColorsClickEvent ->
                                colorBottomSheet.show(
                                    childFragmentManager,
                                    ColorBottomSheetFragment::class.simpleName
                                )
                            is ProductDetailsViewModel.Event.ReviewsClickEvent -> {
                                val bundle = bundleOf(
                                    "itemCategory" to it.item.category,
                                    "itemTitle" to it.item.title
                                )
                                navigate(
                                    R.id.action_productDetailsFragment_to_productReviewsFragment,
                                    bundle
                                )
                            }
                            is ProductDetailsViewModel.Event.CartSuccessEvent -> Toast.makeText(
                                context, getString(R.string.cart_success), Toast.LENGTH_SHORT
                            ).show()
                            is ProductDetailsViewModel.Event.CartFailEvent -> Toast.makeText(
                                context, getString(R.string.cart_fail), Toast.LENGTH_SHORT
                            ).show()
                            is ProductDetailsViewModel.Event.AddToCartEvent -> {
                                if (!colorBottomSheet.isAdded) {
                                    if (binding.buttonColor.text == "Color") {
                                        colorBottomSheet.show(
                                            childFragmentManager,
                                            ColorBottomSheetFragment::class.simpleName
                                        )
                                    }
                                }

                                if (!sizeBottomSheet.isAdded) {
                                    if (binding.buttonSize.text == "Size") {
                                        sizeBottomSheet.show(
                                            childFragmentManager,
                                            SizeBottomSheetFragment::class.simpleName
                                        )
                                    }
                                }

                                if (binding.buttonSize.text != "Size" && binding.buttonColor.text != "Color") {
                                    viewModel.addToCart()
                                }
                            }
                            is ProductDetailsViewModel.Event.SizesSheetDismiss -> sizeBottomSheet.dismiss()
                            is ProductDetailsViewModel.Event.SizesSheetFavDismiss -> sizeBottomSheetFav.dismiss()
                            is ProductDetailsViewModel.Event.FavoriteClickEvent -> {
                                viewModel.favoriteClick(it.item)

                                favoriteButton = (it.view as MaterialButton)
                                favoriteButton.isChecked = false

                                if (viewModel.chosenColorSmall.get() == "Color" && !it.item.isFavorite) {
                                    colorBottomSheetFav.show(
                                        childFragmentManager,
                                        ColorBottomSheetFavFragment::class.simpleName
                                    )
                                }
                                if (viewModel.chosenSizeSmall.get() == "Size" && !it.item.isFavorite) {
                                    sizeBottomSheetFav.show(
                                        childFragmentManager,
                                        SizeBottomSheetFavFragment::class.simpleName
                                    )
                                }
                            }
                            is ProductDetailsViewModel.Event.ColorsSheetDismiss -> colorBottomSheet.dismiss()
                            is ProductDetailsViewModel.Event.ColorsSheetFavDismiss -> {
                                viewModel.chosenColorSmall.set("Color")
                                viewModel.chosenSizeSmall.set("Size")
                                colorBottomSheetFav.dismiss()
                            }
                            is ProductDetailsViewModel.Event.AddedToFavorites -> favoriteButton.isChecked = true
                            else -> {}
                        }
                    }
                }
            }
        }

        binding.vm = viewModel

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }
}
