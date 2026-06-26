package com.luisvertiz.nutriscan.features.nutritionresult

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.luisvertiz.nutriscan.R
import com.luisvertiz.nutriscan.component.ErrorDialog
import com.luisvertiz.nutriscan.model.NutritionResultModel
import com.luisvertiz.nutriscan.ui.theme.PrimaryGreen
import com.luisvertiz.nutriscan.ui.theme.PrimaryOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionResultScreen(
    modifier: Modifier = Modifier,
    nutritionResultViewModel: NutritionResultViewModel = hiltViewModel(),
    mainNavController: NavHostController,
) {
    val uiState: UiState by nutritionResultViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        nutritionResultViewModel.uiEffect.collect { effect ->
            when(effect) {
                is UiEffect.GoBack -> {
                    mainNavController.popBackStack()
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = Color.White,
        content = { contentPadding ->
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(contentPadding)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        HeaderSection()

                        Spacer(modifier = Modifier.height(24.dp))

                        DailyGoalSection(nutritionResult = uiState.nutritionResult)

                        Spacer(modifier = Modifier.height(24.dp))

                        NutritionResultButton(
                            onNutritionResultClick = {
                                nutritionResultViewModel.goBack()
                                mainNavController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    )

    uiState.idErrorMessage?.let { idErrorMessage ->
        ErrorDialog(
            message = stringResource(idErrorMessage),
            buttonText = stringResource(R.string.error_dialog_button_text),
            onDismiss = { nutritionResultViewModel.dismissErrorDialog() },
        )
    }
}

@Composable
private fun HeaderSection() {
    Image(
        painter = painterResource(R.drawable.ic_nutrition_result_success),
        contentDescription = null
    )

    Text(
        text = "¡Listo!",
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )

    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = "Este es tu objetivo diario personalizado",
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        color = Color.Gray,
    )
}

@Composable
private fun DailyGoalSection(nutritionResult: NutritionResultModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Plan diario para el control de glucosa",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            MacronutrientSection(
                icon = Icons.Default.LocalFireDepartment,
                iconColor = PrimaryGreen,
                value = nutritionResult.dailyCalories.toString(),
                unit = "kcal",
                label = "Calorías recomendadas"
            )

            Spacer(modifier = Modifier.height(16.dp))

            MacronutrientSection(
                icon = Icons.Default.Grain,
                iconColor = PrimaryOrange,
                value = nutritionResult.dailyCarbsGr.toString(),
                unit = "g",
                label = "Carbohidratos recomendados"
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun MacronutrientSection(
    icon: ImageVector,
    iconColor: Color,
    value: String,
    unit: String,
    label: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(iconColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = iconColor
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = unit,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = iconColor,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun NutritionResultButton(
    onNutritionResultClick: () -> Unit
) {
    Button(
        onClick = { onNutritionResultClick() },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryGreen
        )
    ) {
        Text(
            text = "Entendido",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp),
        )
    }
}