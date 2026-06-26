package com.luisvertiz.nutriscan.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.luisvertiz.nutriscan.R
import com.luisvertiz.nutriscan.component.ErrorDialog
import com.luisvertiz.nutriscan.navigation.main.MainNavigationRoute
import com.luisvertiz.nutriscan.ui.theme.PrimaryGreen
import com.luisvertiz.nutriscan.ui.theme.TextPrimary
import com.luisvertiz.nutriscan.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    rootNavController: NavHostController,
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by profileViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        profileViewModel.uiEffect.collect { effect ->
            when (effect) {
                is UiEffect.GoToNutritionGoal -> {
                    rootNavController.navigate(MainNavigationRoute.NutritionGoal)
                }
                is UiEffect.GoToLogin -> {
                    rootNavController.navigate(MainNavigationRoute.Login) {
                        popUpTo(0)
                    }
                }
            }
        }
    }

    Scaffold(
        containerColor = Color.White,
        content = { contentPadding ->
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                if (uiState.isScreenLoading) {
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
                        ProfilePictureSection()

                        Spacer(modifier = Modifier.height(16.dp))

                        PersonalInfoSection(uiState)

                        Spacer(modifier = Modifier.height(16.dp))

                        UpdateNutritionProfileButton(
                            onUpdateNutritionProfileClick = { profileViewModel.goToNutritionGoal() },
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        LogoutButton(
                            isLoading = uiState.isLogoutButtonLoading,
                            onLogoutClick = { profileViewModel.logout() },
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
            onDismiss = { profileViewModel.dismissErrorDialog() },
        )
    }
}

@Composable
private fun UpdateNutritionProfileButton(
    onUpdateNutritionProfileClick: () -> Unit,
) {
    Button(
        onClick = { onUpdateNutritionProfileClick() },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryGreen
        )
    ) {
        Text(
            text = stringResource(R.string.profile_update_nutrition_goal_button_text),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp),
        )
    }
}

@Composable
private fun LogoutButton(
    isLoading: Boolean,
    onLogoutClick: () -> Unit,
) {
    OutlinedButton(
        onClick = { onLogoutClick() },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(36.dp),
                trackColor = Color.White,
            )
        } else {
            Text(
                text = stringResource(R.string.profile_logout_button_text),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }
    }
}

@Composable
fun ProfilePictureSection() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFFE5E7EB)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun PersonalInfoSection(uiState: UiState) {
    Column(modifier = Modifier.fillMaxWidth()) {

        val nutritionGoal = uiState.user.nutritionGoal

        ProfileInfoItem(
            label = stringResource(R.string.label_full_name),
            value = uiState.user.fullName
        )

        Spacer(modifier = Modifier.height(4.dp))
        
        ProfileInfoItem(
            label = stringResource(R.string.label_gender),
            value = nutritionGoal?.gender?.description.orEmpty()
        )

        Spacer(modifier = Modifier.height(4.dp))

        ProfileInfoItem(
            label = stringResource(R.string.label_birthday),
            value = nutritionGoal?.birthDate.orEmpty()
        )

        Spacer(modifier = Modifier.height(4.dp))

        ProfileInfoItem(
            label = stringResource(R.string.label_activity_level),
            value = nutritionGoal?.activityLevel?.description.orEmpty()
        )

        Spacer(modifier = Modifier.height(4.dp))

        ProfileInfoItem(
            label = stringResource(R.string.label_diabetes_type),
            value = nutritionGoal?.diabetesTypeModel?.description.orEmpty()
        )

        ProfileInfoItem(
            label = stringResource(R.string.label_weight_kg),
            value = stringResource(R.string.profile_kg_value, nutritionGoal?.weightKg?.toInt() ?: 0)
        )

        ProfileInfoItem(
            label = stringResource(R.string.label_height_cm),
            value = stringResource(R.string.profile_cm_value, nutritionGoal?.heightCm ?: 0)
        )
    }
}

@Composable
fun ProfileInfoItem(label: String, value: String) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = TextSecondary,
        )
    }
}