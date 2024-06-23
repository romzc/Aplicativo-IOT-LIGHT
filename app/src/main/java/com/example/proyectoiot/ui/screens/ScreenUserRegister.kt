package com.example.proyectoiot.ui.screens

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.example.proyectoiot.AccountSettings
import com.example.proyectoiot.R
import com.example.proyectoiot.navigation.RoutesNavBar
import com.example.proyectoiot.navigation.RoutesStart
import com.example.proyectoiot.ui.theme.LightBlue
import com.example.proyectoiot.ui.theme.LightPink

@Composable
fun ScreenUserRegister(navController: NavHostController, accountSettings: AccountSettings) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confPass by remember { mutableStateOf("") }
    var passView by remember { mutableStateOf(false) }
    var confView by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    var isUserSelected by remember { mutableStateOf(false) }
    var isEmailSelected by remember { mutableStateOf(false) }
    var isPassSelected by remember { mutableStateOf(false) }
    var isConfSelected by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var signUpHandler: SignUpHandler = object : SignUpHandler {
        override fun onSuccess(cognitoUser: CognitoUser, userConfirmed: Boolean, cognitoUserCodeDeliveryDetails: CognitoUserCodeDeliveryDetails) {
            navController.navigate(RoutesStart.UserLogin.route)
            Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
            isLoading = false
        }

        override fun onFailure(exception: Exception) {
            showError = true
            isLoading = false
        }
    }


    val FontKaushan = FontFamily(Font(R.font.kaushan_script_regular))
    val FontMontserrat = FontFamily(Font(R.font.montserrat_wght))

    Box(
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp),
            ) {
            Text(
                text = "Crear cuenta",
                fontSize = 32.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = FontKaushan,
            )
            Spacer(modifier = Modifier.height(height = 8.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = LightPink,
                    unfocusedIndicatorColor = LightBlue,
                    backgroundColor = Color.White,
                    cursorColor = LightPink,
                ),

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
                label = {
                    Text(
                        text = "Nombre de usuario",
                        color = if (isUserSelected || !username.isEmpty()) Color.White else LightBlue,
                        fontSize = 17.sp,
                        fontFamily = FontMontserrat,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isUserSelected) LightPink else if (!username.isEmpty() && !isUserSelected) LightBlue else Color.Transparent,)
                            .padding(horizontal = 4.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isUserSelected = it.isFocused },

                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Email Icon",
                        tint = if (isUserSelected) LightPink else LightBlue
                    )
                }
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

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
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
                            .background(if (isEmailSelected) LightPink else if (!email.isEmpty() && !isEmailSelected) LightBlue else Color.Transparent,)
                            .padding(horizontal = 4.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isEmailSelected = it.isFocused },

                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email Icon",
                        tint = if (isEmailSelected) LightPink else LightBlue
                    )
                }
            )
            Spacer(modifier = Modifier.height(height = 8.dp))
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
                    color = if (isPassSelected || !password.isEmpty()) Color.White else LightBlue,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontMontserrat,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isPassSelected) LightPink else if (!password.isEmpty() && !isPassSelected) LightBlue else Color.Transparent,)
                        .padding(horizontal = 4.dp)
                )
                },
                modifier = Modifier.fillMaxWidth().onFocusChanged { isPassSelected = it.isFocused },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = if (passView) R.drawable.ic_password_off else R.drawable.ic_password_eye),
                        contentDescription = "Eye Icon",
                        tint = if (isPassSelected) LightPink else LightBlue,
                        modifier = Modifier.clickable { passView = !passView },
                    )
                },
                visualTransformation = if (passView) VisualTransformation.None else PasswordVisualTransformation(),
            )
            Spacer(modifier = Modifier.height(height = 8.dp))
            OutlinedTextField(
                value = confPass,
                onValueChange = { confPass = it },
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
                    text = "Confirmar contraseña",
                    color = if (isConfSelected || !confPass.isEmpty()) Color.White else LightBlue,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontMontserrat,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isConfSelected) LightPink else if (!confPass.isEmpty() && !isConfSelected) LightBlue else Color.Transparent,)
                        .padding(horizontal = 4.dp)
                )
                },
                modifier = Modifier.fillMaxWidth().onFocusChanged { isConfSelected = it.isFocused },
                trailingIcon = {Icon(
                        painter = painterResource(id = if (confView) R.drawable.ic_password_off else R.drawable.ic_password_eye),
                        contentDescription = "Eye Icon",
                        tint = if (isConfSelected) LightPink else LightBlue,
                        modifier = Modifier.clickable { confView = !confView },
                    )
                },
                visualTransformation = if (confView) VisualTransformation.None else PasswordVisualTransformation(),
            )

            Spacer(modifier = Modifier.height(height = 30.dp))
            Button(
                onClick = {

                    if (checkValues(username, password, confPass, email)) {
                        accountSettings.addAttribute("preferred_username", username)
                        accountSettings.signUpInBackground(email, password, signUpHandler)
                        isLoading = true
                    } else {
                        showError = true
                    }

                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent,),
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
                        text = "CREAR",
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
            Spacer(modifier = Modifier.height(height = 100.dp))
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
                    text = "*Por favor, complete todos",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold

                )
                Text(
                    text = "los campos correctamente*",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(height = 60.dp))
            }

            Row() {

                Text(
                    text = "¿Ya tienes una cuenta?",
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Ingresa",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 17.sp,
                    modifier = Modifier.padding(start = 8.dp)
                        .clickable { navController.navigate(RoutesStart.UserLogin.route) { popUpTo(RoutesStart.UserLogin.route) { inclusive = true }}
                        },
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

fun checkValues(username: String, password: String,  confPass: String, email: String): Boolean {
    if (username.isEmpty() || password.isEmpty() || confPass.isEmpty() || email.isEmpty()) {
        return false
    }
    if (password != confPass) {
        return false
    }
    return true
}
