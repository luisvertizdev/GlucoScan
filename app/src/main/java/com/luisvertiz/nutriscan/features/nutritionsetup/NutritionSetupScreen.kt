package com.luisvertiz.nutriscan.features.nutritionsetup

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionSetupScreen(
    modifier: Modifier = Modifier,
    nutritionSetupViewModel: NutritionSetupViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val email: String by nutritionSetupViewModel.email.collectAsState()
    val password: String by nutritionSetupViewModel.password.collectAsState()
    val birthDate: String by nutritionSetupViewModel.birthDate.collectAsState()
    val gender: String by nutritionSetupViewModel.gender.collectAsState()
    val activityLevel: String by nutritionSetupViewModel.activityLevel.collectAsState()
    val mainGoal: String by nutritionSetupViewModel.mainGoal.collectAsState()
    val isEnabledLoginButton: Boolean by nutritionSetupViewModel.isEnabledLoginButton.collectAsState()
    val goToHome: Boolean by nutritionSetupViewModel.goToHome.collectAsState()
    val isLoading: Boolean by nutritionSetupViewModel.isLoading.collectAsState()
    val errorMessage: String by nutritionSetupViewModel.errorMessage.collectAsState()

    var passwordVisible by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var expandedGender by remember { mutableStateOf(false) }
    var expandedActivityLevel by remember { mutableStateOf(false) }
    var expandedMainGoal by remember { mutableStateOf(false) }

    val genderOptions = listOf("Masculino", "Femenino")
    val activityLevelOptions = listOf("Sedentario", "Moderado", "Activo")
    val mainGoalOptions = listOf("Bajar grasa", "Mantener peso", "Ganar masa muscular")

    val datePickerState = rememberDatePickerState()

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
                    .verticalScroll(rememberScrollState())
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
                        nutritionSetupViewModel.setEmail(email)
                        nutritionSetupViewModel.validateInputs()
                    },
                    label = { Text("Nombre completo") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryGreen,
                        focusedLabelColor = PrimaryGreen,
                        cursorColor = PrimaryGreen
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                val interactionSource = remember { MutableInteractionSource() }
                val isPressed by interactionSource.collectIsPressedAsState()

                if (isPressed) {
                    showDatePicker = true
                }

                OutlinedTextField(
                    value = birthDate,
                    onValueChange = { },
                    label = { Text("Fecha de nacimiento") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    interactionSource = interactionSource,
                    placeholder = { Text("DD / MM / AAAA") },
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryGreen,
                        focusedLabelColor = PrimaryGreen,
                        cursorColor = PrimaryGreen
                    )
                )

                if (showDatePicker) {
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            TextButton(onClick = {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    val sdf = SimpleDateFormat("dd / MM / yyyy", Locale.getDefault())
                                    sdf.timeZone = TimeZone.getTimeZone("UTC")
                                    val date = sdf.format(Date(millis))
                                    nutritionSetupViewModel.setBirthDate(date)
                                }
                                showDatePicker = false
                            }) {
                                Text("Aceptar", color = PrimaryGreen)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDatePicker = false }) {
                                Text("Cancelar", color = PrimaryGreen)
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedGender,
                    onExpandedChange = { expandedGender = !expandedGender },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = gender,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Sexo") },
                        placeholder = { Text("Seleccionar") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGender) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryGreen,
                            focusedLabelColor = PrimaryGreen,
                            cursorColor = PrimaryGreen
                        ),
                        modifier = Modifier
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedGender,
                        onDismissRequest = { expandedGender = false }
                    ) {
                        genderOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(text = option) },
                                onClick = {
                                    nutritionSetupViewModel.setGender(option)
                                    expandedGender = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email ->
                        nutritionSetupViewModel.setEmail(email)
                        nutritionSetupViewModel.validateInputs()
                    },
                    label = { Text("Peso actual (kg)") },
                    placeholder = { Text("Ej. 70") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryGreen,
                        focusedLabelColor = PrimaryGreen,
                        cursorColor = PrimaryGreen
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email ->
                        nutritionSetupViewModel.setEmail(email)
                        nutritionSetupViewModel.validateInputs()
                    },
                    label = { Text("Estatura (cm)") },
                    placeholder = { Text("Ej. 1.75") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryGreen,
                        focusedLabelColor = PrimaryGreen,
                        cursorColor = PrimaryGreen
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedActivityLevel,
                    onExpandedChange = { expandedActivityLevel = !expandedActivityLevel },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = activityLevel,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Nivel de actividad") },
                        placeholder = { Text("Seleccionar") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedActivityLevel) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryGreen,
                            focusedLabelColor = PrimaryGreen,
                            cursorColor = PrimaryGreen
                        ),
                        modifier = Modifier
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedActivityLevel,
                        onDismissRequest = { expandedActivityLevel = false }
                    ) {
                        activityLevelOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(text = option) },
                                onClick = {
                                    nutritionSetupViewModel.setActivityLevel(option)
                                    expandedActivityLevel = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedMainGoal,
                    onExpandedChange = { expandedMainGoal = !expandedMainGoal },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = mainGoal,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Objetivo principal") },
                        placeholder = { Text("Seleccionar") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMainGoal) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryGreen,
                            focusedLabelColor = PrimaryGreen,
                            cursorColor = PrimaryGreen
                        ),
                        modifier = Modifier
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedMainGoal,
                        onDismissRequest = { expandedMainGoal = false }
                    ) {
                        mainGoalOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(text = option) },
                                onClick = {
                                    nutritionSetupViewModel.setMainGoal(option)
                                    expandedMainGoal = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        nutritionSetupViewModel.login()
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
                            text = "Calcular mi objetivo",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

            }
        }
    )

    if (errorMessage.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = {
                nutritionSetupViewModel.dismissError()
            },
            text = {
                Text(text = errorMessage)
            },
            confirmButton = {
                Button(
                    onClick = {
                        nutritionSetupViewModel.dismissError()
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