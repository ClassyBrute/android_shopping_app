package com.gd.intership.hdziedziura.androidshopingapp.presentation.ui

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.databinding.FragmentBagBinding
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ext.navigate
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ext.viewModels
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.bag.BagViewModel
import kotlinx.coroutines.launch

class BagFragment : BaseFragment() {

    private val viewModel: BagViewModel by viewModels()
    private lateinit var binding: FragmentBagBinding
    override val showToolbar: Boolean
        get() = true
    override val showBottomNavBar: Boolean
        get() = true
    override val toolbarIcon: Int
        get() = R.drawable.ic_baseline_search_24

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_bag, container, false
        )

        lifecycle.addObserver(viewModel)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiEvents.collect {
                        when (it) {
                            is BagViewModel.Event.FetchingErrorEvent -> Toast.makeText(
                                context, getString(R.string.error_fetching), Toast.LENGTH_SHORT
                            ).show()
                            is BagViewModel.Event.ProductClickEvent -> {
                                val bundle = bundleOf(
                                    "itemCategory" to it.item.category,
                                    "itemTitle" to it.item.title
                                )
                                navigate(R.id.action_bagFragment_to_productDetailsFragment, bundle)
                            }
                            is BagViewModel.Event.IncreaseEvent ->
                                if (it.item.count.get() != 99) viewModel.increaseCount(it.item)
                            is BagViewModel.Event.DecreaseEvent ->
                                if (it.item.count.get() != 0) viewModel.decreaseCount(it.item)
                            is BagViewModel.Event.MoreEvent -> {
                                val popup = PopupMenu(
                                    context,
                                    it.view,
                                    Gravity.END,
                                    0,
                                    R.style.PopupStyle
                                )
                                if (it.item.isFavorite) {
                                    popup.menuInflater.inflate(R.menu.cart_menu_remove, popup.menu)
                                } else popup.menuInflater.inflate(R.menu.cart_menu_add, popup.menu)

                                popup.setOnMenuItemClickListener { menuItem: MenuItem ->
                                    when (menuItem.title) {
                                        getString(R.string.add_to_favorites) -> viewModel.favoritesAdd(it.item)
                                        getString(R.string.remove_from_favorites) -> viewModel.favoritesRemove(it.item)
                                        getString(R.string.delete_from_the_list) -> viewModel.deleteFromDb(it.item)
                                    }
                                    true
                                }

                                popup.show()
                            }
                            is BagViewModel.Event.DeleteSuccessEvent ->
                                println("Successfully deleted item from Db")
                            is BagViewModel.Event.DeleteFailEvent -> Toast.makeText(
                                context, "Error deleting item from Db", Toast.LENGTH_SHORT
                            ).show()
                            is BagViewModel.Event.ChangeAmountSuccessEvent ->
                                println("Successfully changed amount")
                            is BagViewModel.Event.ChangeAmountErrorEvent -> Toast.makeText(
                                context, "Error changing amount", Toast.LENGTH_SHORT
                            ).show()
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
