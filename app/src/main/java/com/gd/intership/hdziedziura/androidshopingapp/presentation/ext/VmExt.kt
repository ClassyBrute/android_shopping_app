package com.gd.intership.hdziedziura.androidshopingapp.presentation.ext

import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.BaseFragment
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.MainActivity

@MainThread
inline fun <reified VM : ViewModel> BaseFragment.viewModels() =
    viewModels<VM>(
        ownerProducer = { this },
        factoryProducer = { this.viewModelFactory }
    )

@MainThread
inline fun <reified VM : ViewModel> BaseFragment.parentFragmentViewModels() =
    viewModels<VM>(
        ownerProducer = { this.requireParentFragment() },
        factoryProducer = { this.viewModelFactory }
    )

@MainThread
inline fun <reified VM : ViewModel> DialogFragment.parentFragmentViewModels() =
    viewModels<VM>(
        ownerProducer = { this.requireParentFragment() }
    )

@MainThread
inline fun <reified VM : ViewModel> BaseFragment.activityViewModels() =
    activityViewModels<VM>(factoryProducer = { this.viewModelFactory })

@MainThread
inline fun <reified VM : ViewModel> MainActivity.viewModels() =
    viewModels<VM>(factoryProducer = { this.viewModelFactory })
