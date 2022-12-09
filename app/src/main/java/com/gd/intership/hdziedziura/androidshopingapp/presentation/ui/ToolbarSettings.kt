package com.gd.intership.hdziedziura.androidshopingapp.presentation.ui

interface ToolbarSettings {
    val showToolbar: Boolean
        get() = false
    val showBottomNavBar: Boolean
        get() = false
    val toolbarTitle: String
        get() = ""
    val toolbarIcon: Int
        get() = 0
}
