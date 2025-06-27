package com.example.thecodecup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.thecodecup.ui.screens.SplashScreen
import com.example.thecodecup.ui.theme.TheCodeCupTheme
import androidx.compose.foundation.layout.padding

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheCodeCupTheme {
                var showSplash by remember { mutableStateOf(true) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (showSplash) {
                        SplashScreen(
                            onNavigateToHome = {
                                showSplash = false
                            }
                        )
                    } else {
                        Greeting(
                            name = "Android",
                            modifier = androidx.compose.ui.Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}

@androidx.compose.runtime.Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    androidx.compose.material3.Text(
        text = "Hello $name!",
        modifier = modifier
    )
}