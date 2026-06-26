package com.luisvertiz.nutriscan.features.foodresult

import android.graphics.BitmapFactory
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.luisvertiz.nutriscan.R
import com.luisvertiz.nutriscan.model.FoodAnalysisModel
import com.luisvertiz.nutriscan.ui.theme.PrimaryGreen
import com.luisvertiz.nutriscan.ui.theme.PrimaryOrange
import com.luisvertiz.nutriscan.ui.theme.PrimaryRed
import com.luisvertiz.nutriscan.ui.theme.TextPrimary
import com.luisvertiz.nutriscan.ui.theme.TextSecondary
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodResultScreen(
    mainNavController: NavHostController,
    foodAnalysis: FoodAnalysisModel,
    imagePath: String,
    foodResultViewModel: FoodResultViewModel = hiltViewModel(),
) {

    val uiState by foodResultViewModel.uiState.collectAsStateWithLifecycle()

    val bitmap = remember(imagePath) {
        val file = File(imagePath)
        if (file.exists()) {
            BitmapFactory.decodeFile(imagePath)
        } else {
            null
        }
    }

    LaunchedEffect(foodAnalysis, imagePath) {
        foodResultViewModel.loadUiData(
            foodAnalysis = foodAnalysis,
            imagePath = imagePath,
        )
    }

    Scaffold(
        topBar = {
            FoodResultTopBar(
                onBackClick = {  mainNavController.popBackStack() }
            )
        },
        containerColor = Color.White,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.LightGray)
                ) {
                    bitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = uiState.foodAnalysis.foodName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    NutritionItem(
                        label = stringResource(R.string.label_calories),
                        value = stringResource(R.string.unit_kcal, uiState.foodAnalysis.calories)
                    )
                    NutritionItem(
                        label = stringResource(R.string.label_carbohydrates),
                        value = stringResource(R.string.unit_grams, uiState.foodAnalysis.carbs)
                    )
                    NutritionItem(
                        label = stringResource(R.string.label_proteins),
                        value = stringResource(R.string.unit_grams, uiState.foodAnalysis.protein)
                    )
                    NutritionItem(
                        label = stringResource(R.string.label_fats),
                        value = stringResource(R.string.unit_grams, uiState.foodAnalysis.fat)
                    )
                    NutritionItem(
                        label = stringResource(R.string.label_fiber),
                        value = stringResource(R.string.unit_grams, uiState.foodAnalysis.fiber)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            val impactColor = when (uiState.foodAnalysis.glycemicImpact.lowercase()) {
                "alto" -> PrimaryRed
                "medio" -> PrimaryOrange
                else -> PrimaryGreen
            }
            val impactBgColor = impactColor.copy(alpha = 0.1f)

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = impactBgColor)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.label_glycemic_impact),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = null,
                            tint = impactColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = uiState.foodAnalysis.glycemicImpact,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = impactColor
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Puede elevar significativamente tu glucosa.",
                        fontSize = 14.sp,
                        color = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.label_recommendation),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = uiState.foodAnalysis.recommendation,
                fontSize = 14.sp,
                color = TextSecondary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text(
                    text = stringResource(R.string.button_save_food),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodResultTopBar(
    onBackClick: () -> Boolean,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.food_result_screen_title),
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
fun NutritionItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}