package com.example.proyectoiot.navigation

import com.example.proyectoiot.R

sealed class RoutesNavBar (
    val route: String,
    val title: String,
    val icon: Int,
) {
    object Home: RoutesStart("home", "Home", R.drawable.baseline_home_24)
    object Options: RoutesStart("options","Opciones", R.drawable.baseline_window_24)
    object ListRegisters: RoutesStart("list_registers", "Registros", R.drawable.baseline_playlist_add_check_24)
    object Account: RoutesStart("account", "Cuenta", R.drawable.baseline_person_24)
}