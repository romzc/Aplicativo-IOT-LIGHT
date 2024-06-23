package com.example.proyectoiot.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyectoiot.R
import com.example.proyectoiot.ui.composables.BarChart
import com.example.proyectoiot.ui.graphics.GraphicViewData


@Composable
fun ScreenHome(navController: NavController, viewModelGraphic: GraphicViewData) {
    val FontKaushan = FontFamily(Font(R.font.kaushan_script_regular))
    val FontMontserrat = FontFamily(Font(R.font.montserrat_wght))

    val graphicData by rememberUpdatedState(viewModelGraphic.graphicData)
    viewModelGraphic.fetchDataFromApi()
    Box() {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp),
        ) {
            Spacer(modifier = Modifier.height(height = 30.dp))

            Text(
                text = "Home",
                fontSize = 32.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = FontKaushan,
            )

            var showDescription by remember { mutableStateOf(true) }

            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(height = 20.dp))
                Text(
                    text = "Diagrama de porcentaje",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontMontserrat,
                    color = Color.White,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black)
                        .padding(horizontal = 12.dp, vertical = 3.dp)
                )
                Spacer(modifier = Modifier.height(height = 10.dp))
                BarChart(
                    graphicData.value!!,
                    modifier = Modifier.fillMaxWidth().height(300.dp),
                    showDescription = showDescription
                )

                Spacer(modifier = Modifier.height(height = 200.dp))

            }
        }
    }
}
