package com.example.proyectoiot.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.example.proyectoiot.AccountSettings
import com.example.proyectoiot.R
import com.example.proyectoiot.navigation.RoutesStart
import com.example.proyectoiot.ui.theme.LightBlue
import com.example.proyectoiot.ui.theme.LightPink
import kotlinx.coroutines.launch


@Composable
fun ScreenUserLogin(navController: NavController, accountSettings: AccountSettings) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passView by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }


    val FontKaushan = FontFamily(Font(R.font.kaushan_script_regular))
    val FontMontserrat = FontFamily(Font(R.font.montserrat_wght))
    var isEmailSelected by remember { mutableStateOf(false) }
    var isPasswordSelected by remember { mutableStateOf(false) }
    var authenticationHandler: AuthenticationHandler = object : AuthenticationHandler {
        override fun authenticationChallenge(continuation: ChallengeContinuation) {  }
        override fun onSuccess(userSession: CognitoUserSession, newDevice: CognitoDevice?) {
            navController.navigate(RoutesStart.MainScreen.route)
        }
        override fun onFailure(exception: Exception) {
            showError = true
            isLoading = false
        }
        override fun getAuthenticationDetails( authenticationContinuation: AuthenticationContinuation?, userId: String?) {
            val authenticationDetails = AuthenticationDetails(email, password, null)
            authenticationContinuation!!.setAuthenticationDetails(authenticationDetails)
            authenticationContinuation!!.continueTask()
        }
        override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {   }
    }

    Box(

    ) {
        Column(
            verticalArrangement = Arrangement.Top,
        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 45.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.lg_oval_black),
                    contentDescription = "Background Image",
                    modifier = Modifier.width(300.dp),
                    tint = Color.Black
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.lg_light_bulb),
                        contentDescription = "Light Icon",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(100.dp)
                    )
                    Column(

                    ) {
                        Text(
                            text = "Ligth",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            fontSize = 35.sp,
                            fontFamily = FontKaushan,
                        )
                        Text(
                            text = "Control",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            fontSize = 35.sp,
                            fontFamily = FontKaushan,
                        )
                    }
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp),
            ) {
            Spacer(modifier = Modifier.height(height = 40.dp))

            Text(
                text = "Inicio de sesión",
                fontSize = 32.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = FontKaushan,
            )

            Spacer(modifier = Modifier.height(height = 8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = LightPink,
                    unfocusedIndicatorColor = LightBlue,
                    backgroundColor = Color.White,
                    cursorColor = LightPink,
                ),

                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    capitalization = KeyboardCapitalization.None,
                    imeAction = ImeAction.Next,
                ),
                label = {
                    Text(
                        text = "Correo electrónico",
                        color = if (isEmailSelected || !email.isEmpty()) Color.White else LightBlue,
                        fontSize = 17.sp,
                        fontFamily = FontMontserrat,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isEmailSelected) LightPink else if (!email.isEmpty() && !isEmailSelected) LightBlue else Color.Transparent)
                            .padding(horizontal = 4.dp)
                        )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        isEmailSelected = it.isFocused
                    },

                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email Icon",
                        tint = if (isEmailSelected) LightPink else LightBlue
                    )
                }
            )

            Spacer(modifier = Modifier.height(height = 10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = LightPink,
                    unfocusedIndicatorColor = LightBlue,
                    backgroundColor = Color.White,
                    cursorColor = LightPink,
                ),


                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    capitalization = KeyboardCapitalization.None,
                    imeAction = ImeAction.Next,
                ),
                label = { Text(
                    text = "Contraseña",
                    color = if (isPasswordSelected || !password.isEmpty()) Color.White else LightBlue,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontMontserrat,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isPasswordSelected) LightPink else if (!password.isEmpty() && !isPasswordSelected) LightBlue else Color.Transparent)
                        .padding(horizontal = 4.dp)
                    )
                        },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        isPasswordSelected = it.isFocused
                    },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = if (passView) R.drawable.ic_password_off else R.drawable.ic_password_eye),
                        contentDescription = "Eye Icon",
                        tint = if (isPasswordSelected) LightPink else LightBlue,
                        modifier = Modifier.clickable { passView = !passView },
                    )
                },
                visualTransformation = if (passView) VisualTransformation.None else PasswordVisualTransformation(),
            )
            Spacer(modifier = Modifier.height(height = 30.dp))

            Button(
                onClick = {
                    if (checkValues(email, password)) {
                        accountSettings.userLogin(email, password, authenticationHandler)
                        isLoading = true
                    } else {
                        showError = true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                ),
                elevation = ButtonDefaults.elevation(0.dp, 0.dp),
                modifier = Modifier
                    .align(Alignment.End)
                    .height(50.dp)
                    .width(180.dp)
                    .background(
                        color = Color.Black,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "INGRESAR",
                        fontSize = 15.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    )
                    Icon(
                        modifier = Modifier
                            .padding(start = 10.dp),
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Arrow Icon",
                        tint = Color.White,
                    )
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp),

        ) {
            if (showError) {
                Text(
                    text = "*Por favor, revise sus credenciales*",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold

                )
                Spacer(modifier = Modifier.height(height = 80.dp))
            }
            Row() {
                Text(
                    text = "¿No tienes una cuenta?",
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Registrate",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 17.sp,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable { navController.navigate(RoutesStart.UserRegister.route) },
                )
            }
        }
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(enabled = false, onClick = { }),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color.White
                )

            }
        }
    }
}

fun checkValues(email: String, password: String): Boolean {
    if (email.isEmpty() || password.isEmpty()) {
        return false
    }
    return true
}
