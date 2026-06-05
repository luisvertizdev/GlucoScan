package com.luisvertiz.nutriscan.features.nutritiongoal

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.material.icons.filled.CalendarToday
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.luisvertiz.nutriscan.model.ActivityLevelModel
import com.luisvertiz.nutriscan.model.GenderModel
import com.luisvertiz.nutriscan.model.GoalModel
import com.luisvertiz.nutriscan.navigation.main.MainNavigationRoute
import com.luisvertiz.nutriscan.ui.theme.PrimaryGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionGoalScreen(
    modifier: Modifier = Modifier,
    nutritionGoalViewModel: NutritionGoalViewModel = hiltViewModel(),
    mainNavController: NavHostController,
) {
    val birthDate: String by nutritionGoalViewModel.birthDate.collectAsState()
    val gender: GenderModel? by nutritionGoalViewModel.gender.collectAsState()
    val activityLevel: ActivityLevelModel? by nutritionGoalViewModel.activityLevel.collectAsState()
    val mainGoalModel: GoalModel? by nutritionGoalViewModel.mainGoalModel.collectAsState()
    val weight: String by nutritionGoalViewModel.weight.collectAsState()
    val height: String by nutritionGoalViewModel.height.collectAsState()
    val isEnabledCalculateGoalButton: Boolean by nutritionGoalViewModel.isEnabledCalculateGoalButton.collectAsState()
    val goToNutritionResult: Boolean by nutritionGoalViewModel.goToNutritionResult.collectAsState()
    val isLoading: Boolean by nutritionGoalViewModel.isLoading.collectAsState()
    val errorMessage: String? by nutritionGoalViewModel.errorMessage.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }
    var expandedGender by remember { mutableStateOf(false) }
    var expandedActivityLevel by remember { mutableStateOf(false) }
    var expandedMainGoal by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()

    LaunchedEffect(goToNutritionResult) {
        if (goToNutritionResult) {
            mainNavController.navigate(MainNavigationRoute.NutritionResult) {
                popUpTo(MainNavigationRoute.Login) { inclusive = true }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = Color.White,
        topBar = {
            NutritionGoalTopBar(
                onBackClick = {  mainNavController.popBackStack() }
            )
        },
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

                val interactionSource = remember { MutableInteractionSource() }
                val isPressed by interactionSource.collectIsPressedAsState()

                if (isPressed) {
                    showDatePicker = true
                }

                OutlinedTextField(
                    value = birthDate,
                    onValueChange = { birthDate -> nutritionGoalViewModel.setBirthDate(birthDate) },
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
                                    nutritionGoalViewModel.setBirthDate(date)
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
                        value = gender?.description ?: "",
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
                        GenderModel.entries.forEach { genderModel ->
                            DropdownMenuItem(
                                text = { Text(text = genderModel.description) },
                                onClick = {
                                    nutritionGoalViewModel.setGender(genderModel)
                                    expandedGender = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight -> nutritionGoalViewModel.setWeight(weight) },
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
                    value = height,
                    onValueChange = { height -> nutritionGoalViewModel.setHeight(height) },
                    label = { Text("Estatura (cm)") },
                    placeholder = { Text("Ej. 175") },
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
                        value = activityLevel?.description ?: "",
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
                        ActivityLevelModel.entries.forEach { levelActivityModel ->
                            DropdownMenuItem(
                                text = { Text(text = levelActivityModel.description) },
                                onClick = {
                                    nutritionGoalViewModel.setActivityLevel(levelActivityModel)
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
                        value = mainGoalModel?.description ?: "",
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
                        GoalModel.entries.forEach { mainGoalModel ->
                            DropdownMenuItem(
                                text = { Text(text = mainGoalModel.description) },
                                onClick = {
                                    nutritionGoalViewModel.setMainGoal(mainGoalModel)
                                    expandedMainGoal = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                CalculateGoalButton(
                    isLoading = isLoading,
                    isEnabledCalculateGoalButton = isEnabledCalculateGoalButton,
                    onCalculateGoalClick = { nutritionGoalViewModel.calculateNutritionGoal() }
                )

                Spacer(modifier = Modifier.height(24.dp))

            }
        }
    )

    if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = {
                nutritionGoalViewModel.dismissError()
            },
            text = {
                Text(text = errorMessage.orEmpty())
            },
            confirmButton = {
                Button(
                    onClick = {
                        nutritionGoalViewModel.dismissError()
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

@Composable
fun CalculateGoalButton(
    isLoading: Boolean,
    isEnabledCalculateGoalButton: Boolean,
    onCalculateGoalClick: () -> Unit
) {
    Button(
        onClick = { onCalculateGoalClick() },
        modifier = Modifier.fillMaxWidth(),
        enabled = isEnabledCalculateGoalButton,
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
                text = "Calcular objetivo",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionGoalTopBar(
    onBackClick: () -> Boolean
) {
    TopAppBar(
        title = {
            Text(
                text = "Registrar objetivo nutricional",
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
