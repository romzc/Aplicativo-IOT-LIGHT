package com.example.proyectoiot.navigation

sealed class RoutesStart (
    val route: String,
    val title: String? = null,
    val icon: Int? = null,
    ) {
    object UserLogin: RoutesStart("login")
    object UserRegister: RoutesStart("register")
    object MainScreen: RoutesStart("main")


}