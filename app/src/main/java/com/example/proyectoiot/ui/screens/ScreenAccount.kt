package com.example.proyectoiot.ui.screens

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.UpdateAttributesHandler
import com.amazonaws.services.cognitoidentityprovider.model.AttributeType
import com.example.proyectoiot.AccountSettings
import com.example.proyectoiot.R
import com.example.proyectoiot.navigation.RoutesStart
import com.example.proyectoiot.ui.theme.Green
import com.example.proyectoiot.ui.theme.LightBlue
import com.example.proyectoiot.ui.theme.LightPink
import com.example.proyectoiot.ui.theme.Orange

@Composable
fun ScreenAccount(navControllerMain: NavController, navController: NavController, accountSettings: AccountSettings) {
    val FontKaushan = FontFamily(Font(R.font.kaushan_script_regular))
    val FontMontserrat = FontFamily(Font(R.font.montserrat_wght))
    val context = LocalContext.current

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var editedUsername by remember { mutableStateOf("") }
    var editedEmail by remember { mutableStateOf("") }

    var newPassword by remember { mutableStateOf("") }
    var editable by remember { mutableStateOf(false) }

    var getDetailsHandler: GetDetailsHandler = object : GetDetailsHandler {
        override fun onSuccess(attributes: CognitoUserDetails?) {
            val attributesMap = attributes?.attributes?.attributes
            username = attributesMap?.get("preferred_username") ?: ""
            email = attributesMap?.get("email") ?: ""
        }
        override fun onFailure(exception: Exception) { }
    }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }

    var updateAttributesHandler: UpdateAttributesHandler = object : UpdateAttributesHandler {
        override fun onSuccess(attributesVerificationList: MutableList<CognitoUserCodeDeliveryDetails>?) {
            Toast.makeText(context, "Cambios guardados", Toast.LENGTH_SHORT).show()
        }
        override fun onFailure(exception: java.lang.Exception?) {
            Toast.makeText(context, "Datos inválidos", Toast.LENGTH_SHORT).show()
        }
    }

    var genericHandler: GenericHandler = object : GenericHandler {
        override fun onSuccess() {
            Toast.makeText(context, "Contraseña guardada", Toast.LENGTH_SHORT).show()
        }
        override fun onFailure(exception: java.lang.Exception?) {
            Toast.makeText(context, "Contraseña inválida", Toast.LENGTH_SHORT).show()
        }
    }

    accountSettings.getUserDetails(getDetailsHandler)
    LaunchedEffect(username, email) {
        editedUsername = username
        editedEmail = email
    }

    Box() {
        if (showChangePasswordDialog) {
            AlertDialog(
                onDismissRequest = { showChangePasswordDialog = false },
                title = {
                    Text(
                        text = "Cambiar contraseña",
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontMontserrat,
                    ) },
                text = {
                    Column(
                        modifier = Modifier.padding(start = 5.dp)
                    ) {
                        Text(
                            text ="Nueva contraseña",
                            fontSize = 15.sp,
                            fontFamily = FontMontserrat,

                            )
                        Spacer(modifier = Modifier.height(height = 15.dp))

                        TextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Password
                            ),
                            singleLine = true
                        )
                    }

                },
                confirmButton = {
                    TextButton(
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Green,
                            contentColor = Color.White
                        ),
                        onClick = {
                            accountSettings.changePassword(newPassword, genericHandler)
                            showChangePasswordDialog = false
                        }
                    ) {
                        Text(text = "Aceptar")
                    }
                },
                dismissButton = {
                    TextButton(
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Red,
                            contentColor = Color.White
                        ),
                        onClick = { showChangePasswordDialog = false }
                    ) {
                        Text(text = "Cancelar")
                    }
                }
            )
        }

        if (showConfirmationDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmationDialog = false },
                title = { Text(
                    text = "Confirmar cambios",
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontMontserrat,)
                        },
                text = { Text(
                    text = "¿Estás seguro de que deseas guardar los cambios?" ,
                    fontSize = 15.sp,
                    fontFamily = FontMontserrat,
                ) },
                confirmButton = {
                    TextButton(
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Green,
                            contentColor = Color.White
                        ),
                        onClick = {
                            showConfirmationDialog = false
                            accountSettings.updateDetails(editedUsername, editedEmail, updateAttributesHandler)
                            editable = false // Cambiar editable a false después de guardar
                        }
                    ) {
                        Text(text = "Aceptar")
                    }
                },
                dismissButton = {
                    TextButton(
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Red,
                            contentColor = Color.White
                        ),
                        onClick = {
                            showConfirmationDialog = false
                            editable = false
                        }
                    ) {
                        Text(text = "Cancelar")
                    }
                }
            )
        }

        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp),
        ) {
            Spacer(modifier = Modifier.height(height = 30.dp))

            Box(
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = "Cuenta",
                    fontSize = 32.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontKaushan,
                    modifier = Modifier.align(Alignment.TopStart)
                )

                Button(
                    onClick = {
                        accountSettings.signOut()
                        navControllerMain.navigate(RoutesStart.UserLogin.route) { popUpTo(RoutesStart.UserLogin.route) { inclusive = true }}
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent,
                    ),
                    elevation = ButtonDefaults.elevation(0.dp, 0.dp),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .height(50.dp)
                        .width(120.dp)
                        .background(
                            color = Color.Red,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "SALIR",
                            fontSize = 15.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                        )
                        Icon(
                            modifier = Modifier
                                .padding(start = 10.dp),
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Edit Icon",
                            tint = Color.White,
                        )
                    }
                }
            }
            Column(
                    modifier = Modifier.fillMaxWidth()
                ){
                    Spacer(modifier = Modifier.height(height = 20.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.ic_account),
                        contentDescription = "Background Image",
                        modifier = Modifier
                            .size(170.dp)
                            .align(Alignment.CenterHorizontally),
                        tint = Color.Black,
                    )

                    Spacer(modifier = Modifier.height(height = 10.dp))

                    Text(
                        text = "Nombre de usuario",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontMontserrat,
                        color = Color.White,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black)
                            .padding(horizontal = 12.dp, vertical = 3.dp)
                    )

                    Spacer(modifier = Modifier.height(height = 5.dp))

                    TextField(
                        value = editedUsername,
                        onValueChange = { editedUsername = it },
                        enabled = editable ,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.None,
                            imeAction = ImeAction.Next,
                        ),
                        textStyle = TextStyle(fontSize = 18.sp),
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.Black,
                            backgroundColor = Color.White,
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(height = 10.dp))

                    Text(
                        text = "Correo eletronico",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontMontserrat,
                        color = Color.White,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black)
                            .padding(horizontal = 12.dp, vertical = 3.dp)
                    )

                    Spacer(modifier = Modifier.height(height = 5.dp))

                    TextField(
                        value = editedEmail,
                        onValueChange = { editedEmail = it },
                        enabled = editable,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Email,
                            capitalization = KeyboardCapitalization.None,
                            imeAction = ImeAction.Next,
                        ),
                        textStyle = TextStyle(fontSize = 18.sp),
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.Black,
                            backgroundColor = Color.White,
                        ),
                        modifier = Modifier.fillMaxWidth()

                    )

                    Spacer(modifier = Modifier.height(height = 20.dp))

                    Button(
                        onClick = {
                            if (editable) {
                                showConfirmationDialog = true
                            } else {
                                editable = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Transparent,
                        ),
                        elevation = ButtonDefaults.elevation(0.dp, 0.dp),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .height(50.dp)
                            .width(200.dp)
                            .background(
                                color = if (editable) Green else Orange,
                                shape = RoundedCornerShape(16.dp)
                            )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = if(editable) "GUARDAR" else "MODIFICAR",
                                fontSize = 15.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                            )
                            Icon(
                                modifier = Modifier
                                    .padding(start = 10.dp),
                                imageVector = if (editable) Icons.Default.CheckCircle else Icons.Default.Edit,
                                contentDescription = "Edit Icon",
                                tint = Color.White,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(height = 10.dp))

                    Button(
                        onClick = {
                            showChangePasswordDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Transparent,
                        ),
                        elevation = ButtonDefaults.elevation(0.dp, 0.dp),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .height(50.dp)
                            .width(200.dp)
                            .background(
                                color = Color.Black,
                                shape = RoundedCornerShape(16.dp)
                            )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "CONTRASEÑA",
                                fontSize = 15.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                            )
                            Icon(
                                modifier = Modifier
                                    .padding(start = 10.dp),
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Edit Icon",
                                tint = Color.White,
                            )
                        }
                    }

                }

        }
    }
}