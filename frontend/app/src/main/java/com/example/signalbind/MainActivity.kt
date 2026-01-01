package com.example.signalbind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.signalbind.ui.screens.HomeScreen
import com.example.signalbind.ui.screens.KycFormScreen
import com.example.signalbind.ui.screens.KycResultScreen
import com.example.signalbind.ui.screens.ResultScreen
import com.example.signalbind.ui.theme.SignalbindTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SignalbindTheme {
                SignalBindApp()
            }
        }
    }
}

@Composable
fun SignalBindApp() {
    val navController = rememberNavController()
    
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen(
                    onNavigateToResult = {
                        navController.navigate("result")
                    },
                    onNavigateToKyc = {
                        navController.navigate("kyc-form")
                    }
                )
            }
            
            composable("result") {
                ResultScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
            
            composable("kyc-form") {
                KycFormScreen(
                    onNavigateToResult = {
                        navController.navigate("kyc-result")
                    }
                )
            }
            
            composable("kyc-result") {
                KycResultScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}