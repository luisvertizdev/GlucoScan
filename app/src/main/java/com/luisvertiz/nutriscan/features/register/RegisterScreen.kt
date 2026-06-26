package com.luisvertiz.nutriscan.features.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
fun RegisterScreen(
    modifier: Modifier = Modifier,
    registerViewModel: RegisterViewModel = hiltViewModel(),
    mainNavController: NavHostController,
) {
    val uiState: UiState by registerViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        registerViewModel.uiEffect.collect { effect ->
            when(effect) {
                is UiEffect.GoBack -> {
                    mainNavController.popBackStack()
                }
                is UiEffect.GoToLogin -> {
                    mainNavController.navigate(MainNavigationRoute.Login) {
                        popUpTo(MainNavigationRoute.Register) { inclusive = true }
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = Color.White,
        topBar = {
            RegisterTopBar(
                onBackClick = { registerViewModel.goBack() }
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
                Image(
                    painter = painterResource(R.drawable.ic_banner),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.height(8.dp))

                FullNameTextField(
                    fullName = uiState.fullName,
                    onFullNameValueChange = { fullName ->
                        registerViewModel.setFullName(fullName)
                        registerViewModel.validateInputs()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                EmailTextField(
                    email = uiState.email,
                    onEmailValueChange = { email ->
                        registerViewModel.setEmail(email)
                        registerViewModel.validateInputs()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordTextField(
                    password = uiState.password,
                    isPasswordVisible = uiState.isPasswordVisible,
                    onPasswordValueChange = { password ->
                        registerViewModel.setPassword(password)
                        registerViewModel.validateInputs()
                    },
                    onPasswordVisibilityClick = { registerViewModel.togglePasswordVisibility() }
                )

                Spacer(modifier = Modifier.height(16.dp))

                ConfirmPasswordTextField(
                    confirmPassword = uiState.confirmPassword,
                    isConfirmPasswordVisible = uiState.isConfirmPasswordVisible,
                    onConfirmPasswordValueChange = { confirmPassword ->
                        registerViewModel.setConfirmPassword(confirmPassword)
                        registerViewModel.validateInputs()
                    },
                    onConfirmPasswordVisibilityClick = { registerViewModel.toggleConfirmPasswordVisibility() }
                )

                Spacer(modifier = Modifier.height(32.dp))

                RegisterButton(
                    isLoading = uiState.isLoading,
                    isEnabledRegisterButton = uiState.isEnabledRegisterButton,
                    onRegisterClick = { registerViewModel.register() }
                )
            }
        }
    )

    uiState.idErrorMessage?.let { idErrorMessage ->
        ErrorDialog(
            message = stringResource(idErrorMessage),
            buttonText = stringResource(R.string.error_dialog_button_text),
            onDismiss = { registerViewModel.dismissErrorDialog() },
        )
    }

    uiState.idSuccessRegisterMessage?.let { idSuccessRegisterMessage ->
        SuccessRegisterDialog(
            successRegisterMessage = stringResource(idSuccessRegisterMessage),
            onDismiss = { registerViewModel.dismissSuccessRegisterDialog() },
            onGoToLogin = {
                registerViewModel.dismissSuccessRegisterDialog()
                registerViewModel.goToLogin()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegisterTopBar(
    onBackClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.register_screen_title),
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
private fun FullNameTextField(
    fullName: String,
    onFullNameValueChange: (fullName: String) -> Unit,
) {
    OutlinedTextField(
        value = fullName,
        onValueChange = { fullName -> onFullNameValueChange(fullName) },
        label = { Text(stringResource(R.string.label_full_name)) },
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
            IconButton(
                onClick = { onPasswordVisibilityClick() }
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
private fun ConfirmPasswordTextField(
    confirmPassword: String,
    isConfirmPasswordVisible: Boolean,
    onConfirmPasswordValueChange: (confirmPassword: String) -> Unit,
    onConfirmPasswordVisibilityClick: () -> Unit,
) {
    OutlinedTextField(
        value = confirmPassword,
        onValueChange = { confirmPassword -> onConfirmPasswordValueChange(confirmPassword) },
        label = { Text(stringResource(R.string.label_confirm_password)) },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (isConfirmPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            val image = if (isConfirmPasswordVisible) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }
            IconButton(
                onClick = { onConfirmPasswordVisibilityClick() }
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
private fun RegisterButton(
    isLoading: Boolean,
    isEnabledRegisterButton: Boolean,
    onRegisterClick: () -> Unit,
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
                text = stringResource(R.string.register_button_text),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }
    }
}

@Composable
private fun SuccessRegisterDialog(
    successRegisterMessage: String,
    onDismiss: () -> Unit,
    onGoToLogin: () -> Unit,
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
                Text(stringResource(R.string.go_to_login))
            }
        }
    )
}