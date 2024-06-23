package com.example.proyectoiot

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectoiot.navigation.RoutesStart
import com.example.proyectoiot.ui.graphics.GraphicViewData
import com.example.proyectoiot.ui.pagging.PagingViewData
import com.example.proyectoiot.ui.screens.ScreenMain
import com.example.proyectoiot.ui.screens.ScreenUserLogin
import com.example.proyectoiot.ui.screens.ScreenUserRegister
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setBackground()
            StartNavigationHost()
        }
    }
}

@Composable
fun setBackground(){
    Box(
        modifier = Modifier.fillMaxSize(),
        Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(R.drawable.bg_iot),
            contentDescription = "Background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun StartNavigationHost(
){
    val navController = rememberNavController()
    val viewModelPaging = hiltViewModel<PagingViewData>()
    val viewModelGraphic = hiltViewModel<GraphicViewData>()


    val context = LocalContext.current
    val accountSettings = AccountSettings(context)
    accountSettings.connectCognito()

    NavHost(navController,
        startDestination = RoutesStart.UserLogin.route
    ){
        composable(RoutesStart.UserLogin.route){
            ScreenUserLogin(navController, accountSettings)
        }
        composable(RoutesStart.UserRegister.route){
            ScreenUserRegister(navController, accountSettings)
        }
        composable(RoutesStart.MainScreen.route){
            ScreenMain(navController, viewModelGraphic, viewModelPaging, accountSettings)
        }
    }
}

