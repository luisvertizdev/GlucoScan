package com.luisvertiz.nutriscan.features.register

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.luisvertiz.nutriscan.R
import com.luisvertiz.nutriscan.navigation.NavigationRoute
import com.luisvertiz.nutriscan.ui.theme.PrimaryGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    registerViewModel: RegisterViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val fullName: String by registerViewModel.fullName.collectAsState()
    val email: String by registerViewModel.email.collectAsState()
    val password: String by registerViewModel.password.collectAsState()
    val confirmPassword: String by registerViewModel.confirmPassword.collectAsState()
    val isEnabledRegisterButton: Boolean by registerViewModel.isEnabledRegisterButton.collectAsState()
    val isLoading: Boolean by registerViewModel.isLoading.collectAsState()
    val errorMessage: String? by registerViewModel.errorMessage.collectAsState()
    val successRegisterMessage: String? by registerViewModel.successRegisterMessage.collectAsState()

    BackHandler {
        navController.popBackStack()
    }

    Scaffold(
        modifier = modifier,
        containerColor = Color.White,
        topBar = {
            RegisterTopBar(
                onBackClick = {  navController.popBackStack() }
            )
        },
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
                BannerImage()

                Spacer(modifier = Modifier.height(24.dp))

                FullNameTextField(
                    fullName = fullName,
                    onFullNameValueChange = { fullName ->
                        registerViewModel.setFullName(fullName)
                        registerViewModel.validateInputs()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                EmailTextField(
                    email = email,
                    onEmailValueChange = { email ->
                        registerViewModel.setEmail(email)
                        registerViewModel.validateInputs()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordTextField(
                    password = password,
                    onPasswordValueChange = { password ->
                        registerViewModel.setPassword(password)
                        registerViewModel.validateInputs()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                ConfirmPasswordTextField(
                    confirmPassword = confirmPassword,
                    onConfirmPasswordValueChange = { confirmPassword ->
                        registerViewModel.setConfirmPassword(confirmPassword)
                        registerViewModel.validateInputs()
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))

                RegisterButton(
                    isLoading = isLoading,
                    isEnabledRegisterButton = isEnabledRegisterButton,
                    onRegisterClick = { registerViewModel.register() }
                )
            }
        }
    )

    if (errorMessage != null) {
        ErrorDialog(
            errorMessage = errorMessage.orEmpty(),
            onDismiss = { registerViewModel.dismissErrorDialog() }
        )
    }

    if (successRegisterMessage != null) {
        SuccessRegisterDialog(
            successRegisterMessage = successRegisterMessage.orEmpty(),
            onDismiss = { registerViewModel.dismissSuccessRegisterDialog() },
            onGoToLogin = {
                registerViewModel.dismissSuccessRegisterDialog()
                navController.navigate(NavigationRoute.Login) {
                    popUpTo(NavigationRoute.Register) { inclusive = true }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterTopBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Crear cuenta",
                fontSize = 18.sp
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { onBackClick() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}

@Composable
fun FullNameTextField(
    fullName: String,
    onFullNameValueChange: (fullName: String) -> Unit) {
    OutlinedTextField(
        value = fullName,
        onValueChange = { fullName -> onFullNameValueChange(fullName) },
        label = { Text("Nombres completos") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryGreen,
            focusedLabelColor = PrimaryGreen,
            cursorColor = PrimaryGreen
        )
    )
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
    onPasswordValueChange: (password: String) -> Unit,
) {
    OutlinedTextField(
        value = password,
        onValueChange = { password -> onPasswordValueChange(password) },
        label = { Text("Contraseña") },
        modifier = Modifier.fillMaxWidth(),
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
fun ConfirmPasswordTextField(
    confirmPassword: String,
    onConfirmPasswordValueChange: (confirmPassword: String) -> Unit,
) {
    OutlinedTextField(
        value = confirmPassword,
        onValueChange = { confirmPassword -> onConfirmPasswordValueChange(confirmPassword) },
        label = { Text("Confirmar contraseña") },
        modifier = Modifier.fillMaxWidth(),
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
fun BannerImage() {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.ic_banner),
            contentDescription = null
        )
    }
}

@Composable
fun RegisterButton(
    isLoading: Boolean,
    isEnabledRegisterButton: Boolean,
    onRegisterClick: () -> Unit
) {
    Button(
        onClick = { onRegisterClick() },
        modifier = Modifier.fillMaxWidth(),
        enabled = isEnabledRegisterButton,
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
                text = "Registrarse",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }
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

@Composable
fun SuccessRegisterDialog(
    successRegisterMessage: String,
    onDismiss: () -> Unit,
    onGoToLogin: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        text = {
            Text(text = successRegisterMessage)
        },
        confirmButton = {
            Button(
                onClick = { onGoToLogin() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen
                )
            ) {
                Text("Ir a iniciar sesión")
            }
        }
    )
}