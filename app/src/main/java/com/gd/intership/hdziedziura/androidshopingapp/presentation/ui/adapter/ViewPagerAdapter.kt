package com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.ShopFragment
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.ShopKidsFragment
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.ShopMenFragment
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.ShopWomenFragment

class ViewPagerAdapter(
    parentFragment: ShopFragment,
    private var totalCount: Int
) : FragmentStateAdapter(parentFragment) {
    override fun getItemCount(): Int = totalCount
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ShopWomenFragment()
            1 -> ShopMenFragment()
            2 -> ShopKidsFragment()
            else -> ShopWomenFragment()
        }
    }
}
