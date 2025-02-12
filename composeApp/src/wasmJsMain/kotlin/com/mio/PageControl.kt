package com.mio

import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow

object PageControl {
    var page = MutableStateFlow("#home")
    lateinit var navController: NavHostController

    fun navigateTo(route: String) = navController.navigate(route)
}