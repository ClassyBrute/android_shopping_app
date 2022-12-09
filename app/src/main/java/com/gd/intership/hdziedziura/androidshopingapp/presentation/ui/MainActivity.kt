package com.gd.intership.hdziedziura.androidshopingapp.presentation.ui

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.databinding.ActivityMainBinding
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ext.viewModels
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.MainActivityViewModel
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun androidInjector(): AndroidInjector<Any> {
        return fragmentInjector
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        lifecycle.addObserver(viewModel)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.shopFragment,
                R.id.bagFragment,
                R.id.favoritesFragment
            )
        )
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        binding.bottomNavigation.setOnItemReselectedListener {
            when (it) {
                binding.bottomNavigation.menu[0] -> {
                    if (navController.currentDestination?.id == R.id.shopProductListFragment)
                        navController.popBackStack()
                }
                binding.bottomNavigation.menu[1] -> {
                    if (navController.currentDestination?.id == R.id.shopProductListFragment)
                        navController.popBackStack()
                }
            }
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    fun setupToolbar(toolbarTitle: String, showToolbar: Boolean, toolbarIcon: Int) {
        binding.toolbar.apply {
            visibility = if (showToolbar) {
                View.VISIBLE
            } else {
                View.GONE
            }
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_new_24)
            binding.toolbarIcon.setImageResource(toolbarIcon)
            binding.toolbarTitle.text = toolbarTitle
            title = null
        }
    }

    fun setupBottomNav(showBottomNav: Boolean) {
        binding.bottomNavigation.visibility = if (showBottomNav) {
            View.VISIBLE
        } else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }
}
