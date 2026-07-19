package com.luisvertiz.nutriscan.features.nutritiondetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.luisvertiz.nutriscan.R
import com.luisvertiz.nutriscan.ui.theme.*
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie
import androidx.compose.ui.tooling.preview.Preview
import com.luisvertiz.nutriscan.model.GlycemicImpactModel
import com.luisvertiz.nutriscan.util.NutritionConstants.GLYCEMIC_IMPACT_HIGH_LABEL
import com.luisvertiz.nutriscan.util.NutritionConstants.GLYCEMIC_IMPACT_LOW_LABEL
import com.luisvertiz.nutriscan.util.NutritionConstants.GLYCEMIC_IMPACT_MEDIUM_LABEL
import com.luisvertiz.nutriscan.util.NutritionConstants.GLYCEMIC_IMPACT_MIN_LABEL
import com.luisvertiz.nutriscan.util.NutritionConstants.HIGH_IMPACT_PROGRESS
import com.luisvertiz.nutriscan.util.NutritionConstants.LOW_IMPACT_PROGRESS
import com.luisvertiz.nutriscan.util.NutritionConstants.MEDIUM_IMPACT_PROGRESS
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionDetailsScreen(
    mainNavController: NavHostController,
    viewModel: NutritionDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NutritionDetailsContent(
        uiState = uiState,
        onBackClick = { mainNavController.popBackStack() }
    )
}

// Renamed string resource to nutrition_details_title to resolve potential R class sync issues
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionDetailsContent(
    uiState: UiState,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.nutrition_details_title),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                val dateStr =
                    SimpleDateFormat("dd 'de' MMMM, yyyy", Locale.getDefault()).format(Date())
                DateAndSummaryHeader(dateStr = dateStr)

                Spacer(modifier = Modifier.height(24.dp))

                NutritionCard(
                    title = stringResource(R.string.label_calories),
                    description = stringResource(R.string.label_energy_consumed),
                    icon = Icons.Default.Eco,
                    iconBackgroundColor = LightGreen,
                    iconColor = PrimaryGreen,
                    consumed = uiState.totalCalories,
                    target = uiState.nutritionGoal?.dailyCalories?.toInt() ?: 0,
                    unit = "kcal",
                    progressColor = PrimaryGreen
                )

                Spacer(modifier = Modifier.height(16.dp))

                NutritionCard(
                    title = stringResource(R.string.label_carbohydrates),
                    description = stringResource(R.string.label_carbs_glucose_impact),
                    icon = Icons.Default.Grain,
                    iconBackgroundColor = LightBlue,
                    iconColor = PrimaryBlue,
                    consumed = uiState.totalCarbs,
                    target = uiState.nutritionGoal?.dailyCarbsGr ?: 0,
                    unit = "g",
                    progressColor = PrimaryBlue
                )

                Spacer(modifier = Modifier.height(16.dp))

                GlycemicImpactCard(
                    averageImpact = uiState.averageGlycemicImpact
                )
            }
        }
    }
}

@Composable
fun DateAndSummaryHeader(dateStr: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.CalendarMonth,
            contentDescription = null,
            tint = PrimaryGray,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = dateStr,
            fontSize = 16.sp,
            color = PrimaryGray
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = stringResource(R.string.nutrition_details_summary),
        fontSize = 14.sp,
        color = TextSecondary
    )
}

@Composable
fun NutritionCard(
    title: String,
    description: String,
    icon: ImageVector,
    iconBackgroundColor: Color,
    iconColor: Color,
    consumed: Int,
    target: Int,
    unit: String,
    progressColor: Color
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(iconBackgroundColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = iconColor)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = description, fontSize = 12.sp, color = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.label_consumed),
                        fontSize = 12.sp,
                        color = iconColor,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "$consumed $unit",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = iconColor
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.label_target),
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = "$target $unit",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalProgressBarWithLabel(
                consumed = consumed,
                target = target,
                unit = unit,
                progressColor = progressColor,
            )
        }
    }
}

@Composable
fun HorizontalProgressBarWithLabel(
    consumed: Int,
    target: Int,
    unit: String,
    progressColor: Color
) {
    val progress = if (target > 0) (consumed.toFloat() / target).coerceIn(0f, 1f) else 0f

    Column(modifier = Modifier.fillMaxWidth()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .background(LightGray, RoundedCornerShape(6.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .background(progressColor, RoundedCornerShape(6.dp))
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = stringResource(R.string.unit_minimum), fontSize = 10.sp, color = TextSecondary)
            Text(text = "$target $unit", fontSize = 10.sp, color = TextSecondary)
        }
    }
}

@Composable
fun GlycemicImpactCard(
    averageImpact: String
) {
    val impactColor: Color = when (averageImpact) {
        GlycemicImpactModel.LOW.description -> PrimaryGreen
        GlycemicImpactModel.MEDIUM.description -> PrimaryOrange
        GlycemicImpactModel.HIGH.description -> PrimaryRed
        else -> PrimaryGreen
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(LightPurple, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = null,
                        tint = PrimaryPurple
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = stringResource(R.string.label_glycemic_impact),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.label_glycemic_impact_glucose),
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.label_average_impact),
                        fontSize = 10.sp,
                        color = impactColor,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = averageImpact,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = impactColor
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Row(
                    modifier = Modifier.weight(1.5f),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ImpactIndicator(
                        color = PrimaryGreen,
                        label = stringResource(R.string.label_low)
                    )
                    ImpactIndicator(
                        color = PrimaryOrange,
                        label = stringResource(R.string.label_medium)
                    )
                    ImpactIndicator(
                        color = PrimaryRed,
                        label = stringResource(R.string.label_high)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            GlycemicImpactProgressBar(averageImpact = averageImpact)
        }
    }
}

@Composable
fun ImpactIndicator(color: Color, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier
            .size(8.dp)
            .background(color, CircleShape))
        Text(text = label, fontSize = 10.sp, color = TextSecondary)
    }
}

@Composable
fun GlycemicImpactProgressBar(averageImpact: String) {
    val progress: Float = when (averageImpact) {
        GlycemicImpactModel.LOW.description -> LOW_IMPACT_PROGRESS
        GlycemicImpactModel.MEDIUM.description -> MEDIUM_IMPACT_PROGRESS
        GlycemicImpactModel.HIGH.description -> HIGH_IMPACT_PROGRESS
        else -> LOW_IMPACT_PROGRESS
    }

    val impactColor = when (averageImpact) {
        GlycemicImpactModel.LOW.description -> PrimaryGreen
        GlycemicImpactModel.MEDIUM.description -> PrimaryOrange
        GlycemicImpactModel.HIGH.description -> PrimaryRed
        else -> PrimaryGreen
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .background(LightGray, RoundedCornerShape(6.dp))
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            if (progress == LOW_IMPACT_PROGRESS) impactColor else Color.Transparent,
                            RoundedCornerShape(topStart = 6.dp, bottomStart = 6.dp)
                        )
                )
                Box(modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(Color.White))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            if (progress == MEDIUM_IMPACT_PROGRESS) impactColor else Color.Transparent
                        )
                )
                Box(modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(Color.White))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            if (progress >= HIGH_IMPACT_PROGRESS) impactColor else Color.Transparent,
                            RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp)
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = GLYCEMIC_IMPACT_MIN_LABEL, fontSize = 10.sp, color = TextSecondary)
            Text(text = GLYCEMIC_IMPACT_LOW_LABEL, fontSize = 10.sp, color = TextSecondary)
            Text(text = GLYCEMIC_IMPACT_MEDIUM_LABEL, fontSize = 10.sp, color = TextSecondary)
            Text(text = GLYCEMIC_IMPACT_HIGH_LABEL, fontSize = 10.sp, color = TextSecondary)
        }
    }
}