package com.example.proyectoiot.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyectoiot.MQTTmanager
import com.example.proyectoiot.R
import com.example.proyectoiot.ui.theme.Green
import com.example.proyectoiot.ui.theme.LightYellow
import org.json.JSONObject


@Composable
fun ScreenOptions(navController: NavController, mqttManager: MQTTmanager) {
    val FontKaushan = FontFamily(Font(R.font.kaushan_script_regular))
    val FontMontserrat = FontFamily(Font(R.font.montserrat_wght))
    var clickedLight by remember { mutableStateOf(false) }
    var clickedSystem by remember { mutableStateOf(false) }
    var intensity by remember { mutableStateOf(0.5f) }

    Box(){
        if(clickedLight){
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 320.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_light),
                    contentDescription = "Background Image",
                    modifier = Modifier
                        .size(3000.dp)
                        .graphicsLayer(alpha = intensity),
                    tint = Color.Unspecified
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp),

            ) {
            Spacer(modifier = Modifier.height(height = 30.dp))

            Text(
                text = "Opciones de luz",
                fontSize = 32.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = FontKaushan,
            )
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp),
        ) {
                IconButton(
                    onClick = {
                        clickedLight = !clickedLight

                    }) {

                    val tint = if (!clickedLight) Color.Black else LightYellow
                    val alpha = if (!clickedLight) 0.7f else 0.9f

                    Icon(
                        painter = painterResource(id = R.drawable.ic_light_on_off),
                        contentDescription = "Bulb Image",
                        modifier = Modifier
                            .width(300.dp)
                            .graphicsLayer(alpha = alpha),
                        tint = tint
                    )
                }
                Spacer(modifier = Modifier.height(height = 40.dp))


            var ColorL = if(clickedLight) Color.Black else Color.Red
            var ColorR = if(!clickedLight) Color.Black else Color.Green

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .height(50.dp)
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(colors = listOf(ColorL, Color.Black, Color.Black, Color.Black, ColorR)),
                        shape = RoundedCornerShape(16.dp)
                    )
            )
            {
                Row(
                    verticalAlignment = Alignment.CenterVertically,

                    ) {

                    Text(
                        text = "OFF ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if(!clickedLight) Color.White else Color.Gray,
                        fontFamily = FontMontserrat,
                    )

                    Switch(
                        checked = clickedLight,
                        onCheckedChange = { newCheckedState ->
                            clickedLight = newCheckedState
                                          },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Green,
                            checkedTrackColor = Color.Green,
                            uncheckedThumbColor = Color.Red,
                            uncheckedTrackColor = Color.Red
                        ),
                        )

                    Text(
                        text = "ON  ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if(clickedLight) Color.White else Color.Gray,
                        fontFamily = FontMontserrat,
                    )
                }
            }
            Spacer(modifier = Modifier.height(height = 10.dp))

            var ColorS = if(intensity<0.25f) Color.DarkGray
            else if(intensity<0.5f) Color.Gray
            else if(intensity<0.75f) Color.LightGray
            else Color.White;

            Box (
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .height(70.dp)
                    .fillMaxWidth()
                    .background(
                        color = Color.Black,
                        shape = RoundedCornerShape(16.dp)
                    )
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "INTENSIDAD",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = FontMontserrat,
                    )

                    Slider(
                        value = intensity,
                        onValueChange = { newIntensity ->
                            intensity = newIntensity
                        },

                        colors = SliderDefaults.colors(
                            thumbColor = Color.White,
                            activeTrackColor = ColorS,
                            inactiveTrackColor = Color.Gray
                        ),
                        modifier = Modifier
                            .height(30.dp)
                            .width(220.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(height = 10.dp))

            Row(
            ){
                Button(
                    onClick = {
                        clickedSystem = !clickedSystem
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent,
                    ),
                    elevation = ButtonDefaults.elevation(0.dp, 0.dp),

                    modifier = Modifier
                        .height(50.dp)
                        .width(140.dp)
                        .background(
                            color = Color.Black,
                            shape = RoundedCornerShape(16.dp)
                        )
                ){
                    Text(
                        text = if (clickedSystem) "AUTO" else "MANUAL",
                        fontSize = 15.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    )
                    Icon(
                        modifier = Modifier
                            .padding(start = 10.dp),
                        painter = painterResource(id = if (clickedSystem) R.drawable.ic_light_on else R.drawable.ic_light_off),
                        contentDescription = "Edit Icon",
                        tint = if (clickedSystem) Color.Green else Color.Red,
                    )
                }
                Spacer(modifier = Modifier.width(width = 10.dp))

                Button(
                    onClick = {
                        val jsonObject = JSONObject()
                        jsonObject.put("action", clickedLight)
                        jsonObject.put("intensity", intensity)
                        jsonObject.put("system", clickedSystem)
                        val jsonMessage = jsonObject.toString()
                        mqttManager.publish(jsonMessage)
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent,
                    ),
                    elevation = ButtonDefaults.elevation(0.dp, 0.dp),

                    modifier = Modifier
                        .height(50.dp)
                        .width(120.dp)
                        .background(
                            color = Green,
                            shape = RoundedCornerShape(16.dp)
                        )
                ){
                    Text(
                        text = "ENVIAR",
                        fontSize = 15.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    )
                    Icon(
                        modifier = Modifier
                            .padding(start = 10.dp),
                        imageVector = Icons.Default.Send,
                        contentDescription = "Edit Icon",
                        tint = Color.White,
                    )
                }
            }
        }
    }

}