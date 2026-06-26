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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.luisvertiz.nutriscan.R
import com.luisvertiz.nutriscan.component.ErrorDialog
import com.luisvertiz.nutriscan.navigation.main.MainNavigationRoute
import com.luisvertiz.nutriscan.ui.theme.PrimaryGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = hiltViewModel(),
    mainNavController: NavHostController,
) {
    val uiState: UiState by loginViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        loginViewModel.uiEffect.collect { effect ->
            when(effect) {
                is UiEffect.GoToRegister -> {
                    mainNavController.navigate(MainNavigationRoute.Register)
                }
                is UiEffect.GoToDashboard -> {
                    mainNavController.navigate(MainNavigationRoute.Dashboard) {
                        popUpTo(MainNavigationRoute.Login) { inclusive = true }
                    }
                }
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

                Spacer(modifier = Modifier.height(8.dp))

                EmailTextField(
                    email = uiState.email,
                    onEmailValueChange = { email ->
                        loginViewModel.setEmail(email)
                        loginViewModel.validateInputs()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordTextField(
                    password = uiState.password,
                    isPasswordVisible = uiState.isPasswordVisible,
                    onPasswordValueChange = { password ->
                        loginViewModel.setPassword(password)
                        loginViewModel.validateInputs()
                    },
                    onPasswordVisibilityClick = { loginViewModel.togglePasswordVisibility() }
                )

                Spacer(modifier = Modifier.height(32.dp))

                LoginButton(
                    isLoading = uiState.isLoading,
                    isEnabledLoginButton = uiState.isEnabledLoginButton,
                    onLoginClick = { loginViewModel.login() }
                )

                Spacer(modifier = Modifier.height(24.dp))

                RegisterSection(
                    onRegisterClick = { loginViewModel.goToRegister() }
                )
            }
        }
    )

    uiState.idErrorMessage?.let { idErrorMessage ->
        ErrorDialog(
            message = stringResource(idErrorMessage),
            buttonText = stringResource(R.string.error_dialog_button_text),
            onDismiss = { loginViewModel.dismissErrorDialog() },
        )
    }
}

@Composable
private fun EmailTextField(
    email: String,
    onEmailValueChange: (email: String) -> Unit,
) {
    OutlinedTextField(
        value = email,
        onValueChange = { email -> onEmailValueChange(email) },
        label = { Text(stringResource(R.string.label_email)) },
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
private fun PasswordTextField(
    password: String,
    isPasswordVisible: Boolean,
    onPasswordValueChange: (password: String) -> Unit,
    onPasswordVisibilityClick: () -> Unit,
) {
    OutlinedTextField(
        value = password,
        onValueChange = { password -> onPasswordValueChange(password) },
        label = { Text(stringResource(R.string.label_password)) },
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
private fun LoginButton(
    isLoading: Boolean,
    isEnabledLoginButton: Boolean,
    onLoginClick: () -> Unit,
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
                text = stringResource(R.string.login_button_text),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }
    }
}

@Composable
private fun RegisterSection(
    onRegisterClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.text_no_account),
            fontSize = 14.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = stringResource(R.string.text_register),
            fontSize = 14.sp,
            color = PrimaryGreen,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { onRegisterClick() }
        )
    }
}