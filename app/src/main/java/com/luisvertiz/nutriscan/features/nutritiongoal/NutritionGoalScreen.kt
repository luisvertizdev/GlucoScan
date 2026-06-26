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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.luisvertiz.nutriscan.R
import com.luisvertiz.nutriscan.component.ErrorDialog
import com.luisvertiz.nutriscan.model.ActivityLevelModel
import com.luisvertiz.nutriscan.model.GenderModel
import com.luisvertiz.nutriscan.model.DiabetesTypeModel
import com.luisvertiz.nutriscan.navigation.main.MainNavigationRoute
import com.luisvertiz.nutriscan.ui.theme.PrimaryGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionGoalScreen(
    modifier: Modifier = Modifier,
    nutritionGoalViewModel: NutritionGoalViewModel = hiltViewModel(),
    mainNavController: NavHostController,
) {
    val datePickerState = rememberDatePickerState()
    val uiState: UiState by nutritionGoalViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        nutritionGoalViewModel.uiEffect.collect { effect ->
            when(effect) {
                is UiEffect.GoBack -> {
                    mainNavController.popBackStack()
                }
                is UiEffect.GoToNutritionResult -> {
                    mainNavController.navigate(MainNavigationRoute.NutritionResult) {
                        popUpTo(MainNavigationRoute.NutritionGoal) { inclusive = true }
                    }
                }
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
                    nutritionGoalViewModel.showDatePicker(true)
                }

                OutlinedTextField(
                    value = uiState.birthDate,
                    onValueChange = { birthDate -> nutritionGoalViewModel.setBirthDate(birthDate) },
                    label = { Text(stringResource(R.string.label_birthday)) },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    interactionSource = interactionSource,
                    placeholder = { Text(stringResource(R.string.placeholder_birthday)) },
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryGreen,
                        focusedLabelColor = PrimaryGreen,
                        cursorColor = PrimaryGreen
                    )
                )

                if (uiState.showDatePicker) {
                    DatePickerDialog(
                        onDismissRequest = { nutritionGoalViewModel.showDatePicker(false) },
                        confirmButton = {
                            TextButton(onClick = {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    nutritionGoalViewModel.setBirthDate(millis)
                                }
                                nutritionGoalViewModel.showDatePicker(false)
                            }) {
                                Text(stringResource(R.string.date_picker_confirm_button_text), color = PrimaryGreen)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { nutritionGoalViewModel.showDatePicker(false) }) {
                                Text(stringResource(R.string.date_picker_dismiss_button_text), color = PrimaryGreen)
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                ExposedDropdownMenuBox(
                    expanded = uiState.isExpandedGenderDropdown,
                    onExpandedChange = { nutritionGoalViewModel.toggleGenderDropdown() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = uiState.gender?.description.orEmpty(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.label_gender)) },
                        placeholder = { Text(stringResource(R.string.placeholder_select)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.isExpandedGenderDropdown) },
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
                        expanded = uiState.isExpandedGenderDropdown,
                        onDismissRequest = { nutritionGoalViewModel.toggleGenderDropdown() }
                    ) {
                        GenderModel.entries.forEach { genderModel ->
                            DropdownMenuItem(
                                text = { Text(text = genderModel.description) },
                                onClick = {
                                    nutritionGoalViewModel.setGender(genderModel)
                                    nutritionGoalViewModel.toggleGenderDropdown()
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.weightKg,
                    onValueChange = { weight -> nutritionGoalViewModel.setWeightKg(weight) },
                    label = { Text(stringResource(R.string.label_weight_kg)) },
                    placeholder = { Text(stringResource(R.string.placeholder_weight_kg)) },
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
                    value = uiState.heightCm,
                    onValueChange = { height -> nutritionGoalViewModel.setHeightCm(height) },
                    label = { Text(stringResource(R.string.label_height_cm)) },
                    placeholder = { Text(stringResource(R.string.placeholder_height_cm)) },
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
                    expanded = uiState.isExpandedActivityLevelDropdown,
                    onExpandedChange = { nutritionGoalViewModel.toggleActivityLevelDropdown() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = uiState.activityLevel?.description.orEmpty(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.label_activity_level)) },
                        placeholder = { Text(stringResource(R.string.placeholder_select)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.isExpandedActivityLevelDropdown) },
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
                        expanded = uiState.isExpandedActivityLevelDropdown,
                        onDismissRequest = { nutritionGoalViewModel.toggleActivityLevelDropdown() }
                    ) {
                        ActivityLevelModel.entries.forEach { levelActivityModel ->
                            DropdownMenuItem(
                                text = { Text(text = levelActivityModel.description) },
                                onClick = {
                                    nutritionGoalViewModel.setActivityLevel(levelActivityModel)
                                    nutritionGoalViewModel.toggleActivityLevelDropdown()
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                ExposedDropdownMenuBox(
                    expanded = uiState.isExpandedDiabetesTypeDropdown,
                    onExpandedChange = { nutritionGoalViewModel.toggleDiabetesTypeDropdown() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = uiState.diabetesType?.description.orEmpty(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.label_diabetes_type)) },
                        placeholder = { Text(stringResource(R.string.placeholder_select)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.isExpandedDiabetesTypeDropdown) },
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
                        expanded = uiState.isExpandedDiabetesTypeDropdown,
                        onDismissRequest = { nutritionGoalViewModel.toggleDiabetesTypeDropdown() }
                    ) {
                        DiabetesTypeModel.entries.forEach { diabetesType ->
                            DropdownMenuItem(
                                text = { Text(text = diabetesType.description) },
                                onClick = {
                                    nutritionGoalViewModel.setDiabetesType(diabetesType)
                                    nutritionGoalViewModel.toggleDiabetesTypeDropdown()
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                CalculateGoalButton(
                    isLoading = uiState.isLoading,
                    isEnabledCalculateGoalButton = uiState.isEnabledCalculateGoalButton,
                    onCalculateGoalClick = { nutritionGoalViewModel.calculateNutritionGoal() }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    )

    uiState.idErrorMessage?.let { idErrorMessage ->
        ErrorDialog(
            message = stringResource(idErrorMessage),
            buttonText = stringResource(R.string.error_dialog_button_text),
            onDismiss = { nutritionGoalViewModel.dismissErrorDialog() },
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
                text = stringResource(R.string.nutrition_goal_button_text),
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
                text = stringResource(R.string.nutrition_goal_screen_title),
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