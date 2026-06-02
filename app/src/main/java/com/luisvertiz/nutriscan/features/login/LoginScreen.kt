package com.luisvertiz.nutriscan.features.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.luisvertiz.nutriscan.R
import com.luisvertiz.nutriscan.navigation.NavigationRoute
import com.luisvertiz.nutriscan.ui.theme.PrimaryGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val email: String by loginViewModel.email.collectAsState()
    val password: String by loginViewModel.password.collectAsState()
    val isEnabledLoginButton: Boolean by loginViewModel.isEnabledLoginButton.collectAsState()
    val goToHome: Boolean by loginViewModel.goToHome.collectAsState()
    val isLoading: Boolean by loginViewModel.isLoading.collectAsState()
    val errorMessage: String by loginViewModel.errorMessage.collectAsState()

    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (goToHome) {
            navController.navigate(NavigationRoute.Home) {
                popUpTo(NavigationRoute.Login) { inclusive = true }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = Color.White,
        content = { contentPadding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_banner),
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email ->
                        loginViewModel.setEmail(email)
                        loginViewModel.validateInputs()
                    },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryGreen,
                        focusedLabelColor = PrimaryGreen,
                        cursorColor = PrimaryGreen
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password ->
                        loginViewModel.setPassword(password)
                        loginViewModel.validateInputs()
                    },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = null)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryGreen,
                        focusedLabelColor = PrimaryGreen,
                        cursorColor = PrimaryGreen
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        loginViewModel.login()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isEnabledLoginButton,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(36.dp),
                            trackColor = Color.White,
                        )
                    } else {
                        Text(
                            text = "Iniciar Sesión",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿No tienes una cuenta? ",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.width(4.dp))
                    
                    Text(
                        text = "Regístrate",
                        fontSize = 14.sp,
                        color = PrimaryGreen,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {  }
                    )
                }
            }
        }
    )

    if (errorMessage.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = {
                loginViewModel.dismissError()
            },
            text = {
                Text(text = errorMessage)
            },
            confirmButton = {
                Button(
                    onClick = {
                        loginViewModel.dismissError()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen
                    )
                ) {
                    Text("Entendido")
                }
            }
        )
    }
}