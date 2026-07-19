package com.luisvertiz.nutriscan.features.home

import android.Manifest
import android.content.Context
import android.content.res.Resources
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.luisvertiz.nutriscan.R
import com.luisvertiz.nutriscan.component.ErrorDialog
import com.luisvertiz.nutriscan.navigation.main.MainNavigationRoute
import com.luisvertiz.nutriscan.ui.theme.PrimaryBlue
import com.luisvertiz.nutriscan.ui.theme.PrimaryGray
import com.luisvertiz.nutriscan.ui.theme.PrimaryGreen
import com.luisvertiz.nutriscan.ui.theme.TextPrimary
import com.luisvertiz.nutriscan.ui.theme.TextSecondary
import com.luisvertiz.nutriscan.util.goToSettings
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    rootNavController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState: UiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val context: Context = LocalContext.current
    val resources: Resources = LocalResources.current
    val scope: CoroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            homeViewModel.goToFoodCamera()
        } else {
            scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = resources.getString(R.string.camera_permission_denied_message),
                    actionLabel = resources.getString(R.string.camera_permission_go_to_settings_action),
                    duration = SnackbarDuration.Long
                )
                if (result == SnackbarResult.ActionPerformed) { 
                    context.goToSettings()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.uiEffect.collect { effect ->
            when(effect) {
                is UiEffect.GoToNutritionGoal -> {
                    rootNavController.navigate(MainNavigationRoute.NutritionGoal)
                }
                is UiEffect.GoToFoodCamera -> {
                    rootNavController.navigate(MainNavigationRoute.FoodCamera)
                }
            }
        }
    }

    Scaffold(
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(contentPadding)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp),
                    ) {
                        HeaderSection(fullName = uiState.user.fullName)

                        Spacer(modifier = Modifier.height(24.dp))

                        if (uiState.user.nutritionGoal == null) {
                            RegisterNutritionGoalSection(
                                onRegisterNutritionGoalClick = {
                                    homeViewModel.goToNutritionGoal()
                                }
                            )
                        } else {
                            TodaySummaryHeader(
                                todayDate = uiState.todayDate,
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            DailyNutritionSummary(
                                goalCarbs = uiState.user.nutritionGoal?.nutritionResult?.dailyCarbsGr ?: 0,
                                consumedCarbs = uiState.totalConsumedCarbs,
                                onSeeDetailsClick = {
                                    rootNavController.navigate(MainNavigationRoute.NutritionDetails)
                                }
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            ScanFoodButton(
                                onScanFoodClick = {
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                },
                            )
                        }
                    }
                }
            }
        }
    )

    uiState.idErrorMessage?.let { idErrorMessage ->
        ErrorDialog(
            message = stringResource(idErrorMessage),
            buttonText = stringResource(R.string.error_dialog_button_text),
            onDismiss = { homeViewModel.dismissErrorDialog() },
        )
    }
}

@Composable
fun ScanFoodButton(
    onScanFoodClick: () -> Unit,
) {
    Button(
        onClick = { onScanFoodClick() },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryGreen
        ),
    ) {
        Icon(
            imageVector = Icons.Rounded.PhotoCamera,
            contentDescription = null,
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = stringResource(R.string.scan_food_button_text),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp),
        )
    }
}

@Composable
private fun TodaySummaryHeader(
    todayDate: String,
) {
    Column {
        Text(
            text = stringResource(R.string.today_summary_header_title),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = TextSecondary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = todayDate,
                fontSize = 14.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun DailyNutritionSummary(
    goalCarbs: Int,
    consumedCarbs: Int,
    onSeeDetailsClick: () -> Unit,
) {
    val remainingCarbs = (goalCarbs - consumedCarbs).coerceAtLeast(0)
    val percentage = if (goalCarbs > 0) consumedCarbs * 100 / goalCarbs else 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFFF1F8E9), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = stringResource(R.string.label_carbohydrates),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = stringResource(R.string.daily_nutrition_summary_goal, goalCarbs),
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center,
            ) {
                PieChart(
                    modifier = Modifier.size(150.dp),
                    data = listOf(
                        Pie(
                            label = stringResource(R.string.legend_consumed),
                            data = consumedCarbs.toDouble(),
                            color = PrimaryGreen,
                        ),
                        Pie(
                            label = stringResource(R.string.legend_remaining),
                            data = remainingCarbs.toDouble(),
                            color = PrimaryGray,
                        )
                    ),
                    onPieClick = {},
                    style = Pie.Style.Stroke(width = 20.dp),
                )

                Text(
                    text = "$percentage%",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen,
                    modifier = Modifier.padding(top = 36.dp),
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.legend_consumed),
                        fontSize = 14.sp,
                        color = PrimaryGreen,
                    )
                    Text(
                        text = stringResource(R.string.unit_grams, consumedCarbs),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen,
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.legend_remaining),
                        fontSize = 14.sp,
                        color = PrimaryGray,
                    )

                    Text(
                        text = stringResource(R.string.unit_grams, remainingCarbs),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGray,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                modifier = Modifier.align(Alignment.End),
                onClick = { onSeeDetailsClick() }
            ) {
                Text(
                    text = stringResource(R.string.daily_nutrition_summary_see_details_button_text),
                    color = PrimaryBlue,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    tint = PrimaryBlue,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun HeaderSection(fullName: String) {
    Text(
        text = stringResource(R.string.home_greeting, fullName),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
}

@Composable
private fun RegisterNutritionGoalSection(
    onRegisterNutritionGoalClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_nutrition_goal),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.goal_section_title),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.goal_section_description),
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(16.dp))

            RegisterNutritionGoalButton(
                onRegisterNutritionGoalClick = { onRegisterNutritionGoalClick() }
            )
        }
    }
}

@Composable
fun RegisterNutritionGoalButton(
    onRegisterNutritionGoalClick: () -> Unit,
) {
    Button(
        onClick = { onRegisterNutritionGoalClick() },
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryGreen
        )
    ) {
        Text(
            text = stringResource(R.string.goal_section_button_text),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp),
        )
    }
}