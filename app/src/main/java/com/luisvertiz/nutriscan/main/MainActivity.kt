package com.luisvertiz.nutriscan.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.luisvertiz.nutriscan.navigation.main.NavigationHost
import com.luisvertiz.nutriscan.ui.theme.NutriScanTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen: SplashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition {
            mainViewModel.isLoading.value
        }

        setContent {
            val startDestination by mainViewModel.startDestination.collectAsState()
            NutriScanTheme {
                val mainNavController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { contentPadding ->
                        NavigationHost(
                            mainNavController = mainNavController,
                            modifier = Modifier.padding(contentPadding),
                            startDestination = startDestination
                        )
                    }
                )
            }
        }
    }
}