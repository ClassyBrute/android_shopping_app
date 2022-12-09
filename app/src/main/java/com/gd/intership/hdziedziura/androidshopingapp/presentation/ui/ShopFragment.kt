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
import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.databinding.FragmentShopBinding
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ext.navigate
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ext.viewModels
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.adapter.ViewPagerAdapter
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.shop.ShopViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class ShopFragment : BaseFragment() {

    private val viewModel: ShopViewModel by viewModels()
    private lateinit var binding: FragmentShopBinding
    override val showToolbar: Boolean
        get() = true
    override val showBottomNavBar: Boolean
        get() = true
    override val toolbarTitle: String
        get() = "Categories"
    override val toolbarIcon: Int
        get() = R.drawable.ic_baseline_search_24

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_shop, container, false
        )

        lifecycle.addObserver(viewModel)

        val adapter = ViewPagerAdapter(this, 3)
        binding.pager2.adapter = adapter
        TabLayoutMediator(binding.tabs, binding.pager2) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.women)
                1 -> tab.text = getString(R.string.men)
                2 -> tab.text = getString(R.string.kids)
            }
        }.attach()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiEvents.collect {
                        when (it) {
                            is ShopViewModel.Event.FetchingErrorEvent -> Toast.makeText(
                                context, getString(R.string.error_fetching), Toast.LENGTH_SHORT
                            ).show()
                            is ShopViewModel.Event.BannerClickEvent -> Toast.makeText(
                                context, getString(R.string.banner_click), Toast.LENGTH_SHORT
                            ).show()
                            is ShopViewModel.Event.CategoryClickEvent -> {
                                val bundle = bundleOf(
                                    "itemCategory" to it.item.category,
                                    "itemTitle" to it.item.title
                                )
                                navigate(R.id.action_shopFragment_to_shopProductListFragment, bundle)
                            }
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
