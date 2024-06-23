package com.example.proyectoiot.ui.composables

import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.proyectoiot.navigation.RoutesStart
import com.example.proyectoiot.ui.theme.GreenBlue
import com.example.proyectoiot.ui.theme.LightPink

@Composable
fun BottomNavigationBar(
    navController: NavController,
    items: List<RoutesStart>
){
    val currentRoute = CurrentRoute(navController)
    BottomNavigation(
        backgroundColor = Color.Black,

    ) {
        items.forEach { screen ->
            BottomNavigationItem(
                unselectedContentColor = Color.White,
                selectedContentColor = LightPink,
                icon = {
                    Icon(
                        painter = painterResource(screen.icon!!),
                        contentDescription = "Eye Icon",
                    )
                },
                label = { Text(screen.title!!)},
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route){
                        popUpTo(navController.graph.findStartDestination().id){
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}


@Composable
private fun CurrentRoute(navController: NavController):String?{
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route

}

