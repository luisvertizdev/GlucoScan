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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.luisvertiz.nutriscan.navigation.main.MainNavigationRoute
import com.luisvertiz.nutriscan.ui.theme.PrimaryGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = hiltViewModel(),
    mainNavController: NavHostController,
) {
    val email: String by loginViewModel.email.collectAsState()
    val password: String by loginViewModel.password.collectAsState()
    val isEnabledLoginButton: Boolean by loginViewModel.isEnabledLoginButton.collectAsState()
    val goToLanding: Boolean by loginViewModel.goToLanding.collectAsState()
    val isLoading: Boolean by loginViewModel.isLoading.collectAsState()
    val errorMessage: String? by loginViewModel.errorMessage.collectAsState()
    val isPasswordVisible: Boolean by loginViewModel.isPasswordVisible.collectAsState()

    LaunchedEffect(goToLanding) {
        if (goToLanding) {
            mainNavController.navigate(MainNavigationRoute.Landing) {
                popUpTo(MainNavigationRoute.Login) { inclusive = true }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = Color.White,
        content = { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_banner),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.height(24.dp))

                EmailTextField(
                    email = email,
                    onEmailValueChange = { email ->
                        loginViewModel.setEmail(email)
                        loginViewModel.validateInputs()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordTextField(
                    password = password,
                    isPasswordVisible = isPasswordVisible,
                    onPasswordValueChange = { password ->
                        loginViewModel.setPassword(password)
                        loginViewModel.validateInputs()
                    },
                    onPasswordVisibilityClick = { loginViewModel.togglePasswordVisibility() }
                )

                Spacer(modifier = Modifier.height(32.dp))

                LoginButton(
                    isLoading = isLoading,
                    isEnabledLoginButton = isEnabledLoginButton,
                    onLoginClick = { loginViewModel.login() }
                )

                Spacer(modifier = Modifier.height(24.dp))

                RegisterSection(
                    onRegisterClick = { mainNavController.navigate(MainNavigationRoute.Register) }
                )
            }
        }
    )

    if (errorMessage != null) {
        ErrorDialog(
            errorMessage = errorMessage.orEmpty(),
            onDismiss = { loginViewModel.dismissErrorDialog() }
        )
    }
}

@Composable
fun EmailTextField(
    email: String,
    onEmailValueChange: (email: String) -> Unit
) {
    OutlinedTextField(
        value = email,
        onValueChange = { email -> onEmailValueChange(email) },
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
}

@Composable
fun PasswordTextField(
    password: String,
    isPasswordVisible: Boolean,
    onPasswordValueChange: (password: String) -> Unit,
    onPasswordVisibilityClick: () -> Unit
) {
    OutlinedTextField(
        value = password,
        onValueChange = { password -> onPasswordValueChange(password) },
        label = { Text("Contraseña") },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            val image = if (isPasswordVisible) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }
            IconButton(onClick = { onPasswordVisibilityClick() }
            ) {
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
}

@Composable
fun LoginButton(
    isLoading: Boolean,
    isEnabledLoginButton: Boolean,
    onLoginClick: () -> Unit
) {
    Button(
        onClick = { onLoginClick() },
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
                text = "Iniciar sesión",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }
    }
}

@Composable
fun RegisterSection(
    onRegisterClick: () -> Unit
) {
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
            modifier = Modifier.clickable { onRegisterClick() }
        )
    }
}

@Composable
fun ErrorDialog(
    errorMessage: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        text = {
            Text(text = errorMessage)
        },
        confirmButton = {
            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen
                )
            ) {
                Text("Entendido")
            }
        }
    )
}