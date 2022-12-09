package com.gd.intership.hdziedziura.androidshopingapp.presentation.ext

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.navigate(destination: Int, args: Bundle? = null) {
    findNavController().navigate(destination, args)
}
