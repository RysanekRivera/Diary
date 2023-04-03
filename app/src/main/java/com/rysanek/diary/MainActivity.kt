package com.rysanek.diary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.rysanek.diary.navigation.Screen
import com.rysanek.diary.navigation.SetupNavGraph
import com.rysanek.diary.ui.theme.DiaryTheme
import com.rysanek.diary.utils.Constants.APP_ID
import io.realm.kotlin.mongodb.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        super.onCreate(savedInstanceState)

        setContent {
            DiaryTheme {
                val navController = rememberNavController()
                SetupNavGraph(startDestination = provideStartDestination(), navController = navController)
            }
        }
    }

    private fun provideStartDestination(): String {
        val user = App.create(APP_ID).currentUser
        return if (user != null && user.loggedIn) Screen.Home.route
        else Screen.Authentication.route

    }
}
