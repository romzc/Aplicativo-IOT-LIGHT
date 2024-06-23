package com.example.proyectoiot.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.proyectoiot.AccountSettings
import com.example.proyectoiot.MQTTmanager
import com.example.proyectoiot.R
import com.example.proyectoiot.navigation.RoutesNavBar.*
import com.example.proyectoiot.ui.composables.BottomNavigationBar
import com.example.proyectoiot.ui.graphics.GraphicViewData
import com.example.proyectoiot.ui.pagging.PagingViewData
import com.example.proyectoiot.ui.theme.DarkPurple
import com.example.proyectoiot.ui.theme.LightBlue
import com.example.proyectoiot.ui.theme.LightCyan
import com.example.proyectoiot.ui.theme.LightPink
import com.example.proyectoiot.ui.theme.LightPurple
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.UUID


@Composable
fun createMqttManager(): MQTTmanager {
    val mqttServerUri = "ssl://a2toihkqmg7dsb-ats.iot.us-east-2.amazonaws.com:8883"
    val clientId = UUID.randomUUID()
    val topic = "topic_test"
    val qos = 0
    val applicationContext = LocalContext.current
    return MQTTmanager(mqttServerUri, clientId, topic, qos, applicationContext)
}

@Composable
fun BarNavigationHost(navControllerMain: NavController, mqtt: MQTTmanager, navController: NavHostController, viewModelGraphic: GraphicViewData,viewModelPaging: PagingViewData, accountSettings: AccountSettings){

    NavHost(navController = navController,
        startDestination = Home.route
    ){
        composable(Home.route){
            ScreenHome(navController, viewModelGraphic)
        }
        composable(Options.route){
            ScreenOptions(navController, mqtt)
        }
        composable(ListRegisters.route){
            ScreenListRegisters(navController, viewModelPaging)
        }
        composable(Account.route){
            ScreenAccount(navControllerMain, navController, accountSettings)
        }
    }

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScreenMain(navControllerMain: NavController, viewModelGraphic: GraphicViewData, viewModelPaging: PagingViewData, accountSettings: AccountSettings) {
    val FontKaushan = FontFamily(Font(R.font.kaushan_script_regular))
    val navController = rememberNavController()
    val navigationItems = listOf(
        Home,
        Options,
        Account,
        ListRegisters
    )

    var isLoading by remember { mutableStateOf(true) }
    val mqtt: MQTTmanager = createMqttManager()
    val responseSubcribe = remember { mutableStateOf("") }
    LaunchedEffect(mqtt) {
        withContext(Dispatchers.IO) {
            mqtt.connect(responseSubcribe)
        }
        isLoading = false
    }

    if(isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.verticalGradient(colors = listOf(LightCyan, LightPurple)))
                .clickable(enabled = false, onClick = { }),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color.White
                )

                Text(
                    text = "Cargando",
                    fontSize = 30.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontKaushan,
                )
            }
        }

    } else {

        Scaffold(
            bottomBar = { BottomNavigationBar(navController = navController, items = navigationItems) }
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(R.drawable.bg_iot),
                    contentDescription = "Background",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )

                BarNavigationHost(navControllerMain, mqtt, navController, viewModelGraphic, viewModelPaging, accountSettings)
            }
        }
    }


}


