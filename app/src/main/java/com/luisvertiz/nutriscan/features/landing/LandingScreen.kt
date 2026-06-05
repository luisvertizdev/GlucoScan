package com.luisvertiz.nutriscan.features.landing

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.luisvertiz.nutriscan.navigation.bottombar.BottomBarNavigationHost

@Composable
fun LandingScreen(
    mainNavController: NavHostController
) {
    val bottomBarNavController: NavHostController = rememberNavController()

    Scaffold(
        bottomBar = {
            LandingBottomBar(
                bottomBarNavController = bottomBarNavController
            )
        },
        content = { contentPadding ->
            BottomBarNavigationHost(
                mainNavController = mainNavController,
                bottomBarNavController = bottomBarNavController,
                modifier = Modifier.padding(contentPadding)
            )
        }
    )
}
