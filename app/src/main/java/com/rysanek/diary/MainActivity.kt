package com.rysanek.diary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.rysanek.diary.navigation.Screen
import com.rysanek.diary.navigation.SetupNavGraph
import com.rysanek.diary.presentation.screens.auth.AuthenticationScreen
import com.rysanek.diary.ui.theme.DiaryTheme
import dagger.hilt.android.AndroidEntryPoint

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            DiaryTheme {
                val navController = rememberNavController()
                SetupNavGraph(startDestination = Screen.Authentication.route, navController = navController)
            }
        }
    }
}
