package com.luisvertiz.nutriscan.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.luisvertiz.nutriscan.R
import com.luisvertiz.nutriscan.features.login.ErrorDialog
import com.luisvertiz.nutriscan.model.UserModel
import com.luisvertiz.nutriscan.navigation.main.MainNavigationRoute
import com.luisvertiz.nutriscan.ui.theme.PrimaryGreen
import com.luisvertiz.nutriscan.ui.theme.TextPrimary
import com.luisvertiz.nutriscan.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    rootNavController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val user: UserModel by homeViewModel.user.collectAsState()
    val errorMessage: String? by homeViewModel.errorMessage.collectAsState()

    Scaffold(
        containerColor = Color.White,
        content = { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {

                HeaderSection(fullName = user.fullName)

                Spacer(modifier = Modifier.height(24.dp))

                if (user.nutritionGoal == null) {
                    RegisterNutritionGoalSection(
                        onRegisterNutritionGoalClick = {
                            rootNavController.navigate(MainNavigationRoute.NutritionGoal)
                        }
                    )
                }
            }
        }
    )

    if (errorMessage != null) {
        ErrorDialog(
            errorMessage = errorMessage.orEmpty(),
            onDismiss = { homeViewModel.dismissErrorDialog() }
        )
    }
}

@Composable
fun HeaderSection(fullName: String) {
    Text(
        text = "¡Hola, ${fullName}! 👋",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
}

@Composable
fun RegisterNutritionGoalSection(
    onRegisterNutritionGoalClick: () -> Unit
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
                text = "Aún no tienes un objetivo nutricional",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Define tu objetivo diario y comienza a alcanzar tus metas de nutrición.",
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
    onRegisterNutritionGoalClick: () -> Unit
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
            text = "Registrar objetivo",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp),
        )
    }
}
