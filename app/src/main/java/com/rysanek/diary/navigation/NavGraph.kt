package com.rysanek.diary.navigation

import android.util.Log
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rysanek.diary.presentation.components.DisplayAlertDialog
import com.rysanek.diary.presentation.screens.auth.AuthenticationScreen
import com.rysanek.diary.presentation.screens.auth.viewmodels.AuthenticationViewModel
import com.rysanek.diary.presentation.screens.home.HomeScreen
import com.rysanek.diary.utils.Constants.APP_ID
import com.rysanek.diary.utils.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SetupNavGraph(
    startDestination:
    String, navController: NavHostController
) {
    
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        
        authenticationRoute(
            navigateToHome = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Authentication.route) { inclusive = true }
                }
            }
        )
        
        homeRoute(
            navigateToWrite = {
                navController.navigate(Screen.Write.route)
            },
            navigateToAuth = {
                navController.navigate(Screen.Authentication.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                }
            }
        )
        
        writeRoute()
        
    }
    
}

fun NavGraphBuilder.authenticationRoute(navigateToHome: () -> Unit) {
    
    composable(route = Screen.Authentication.route) {
        val viewModel: AuthenticationViewModel = viewModel()
        val authenticated by viewModel.authenticated
        val loadingState by viewModel.loadingState
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        
        AuthenticationScreen(
            authenticated = authenticated,
            loadingState = loadingState,
            oneTapSignInState = oneTapState,
            messageBarState = messageBarState,
            navigateToHome = navigateToHome,
            onTokenIdReceived = { tokenId ->
                viewModel.signInWithMongoAtlas(
                    tokenId,
                    onSuccess = {
                        messageBarState.addSuccess("Successfully Authenticated")
                        viewModel.setLoading(false)
                    },
                    onError = { e ->
                        messageBarState.addError(Exception(e.message))
                        viewModel.setLoading(false)
                    }
                )
                
            },
            onErrorReceived = { message ->
                messageBarState.addError(Exception(message))
                viewModel.setLoading(false)
            },
            onButtonClicked = {
                oneTapState.open()
                viewModel.setLoading(true)
            }
        )
    }
    
}

fun NavGraphBuilder.homeRoute(
    navigateToWrite: () -> Unit,
    navigateToAuth: () -> Unit
) {
    
    composable(route = Screen.Home.route) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var signOutDialogOpened by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        
        HomeScreen(
            drawerState = drawerState,
            onMenuClicked = {
                scope.launch {
                    drawerState.open()
                }
            },
            onSignOutClicked = {
                signOutDialogOpened = true
            },
            navigateToWrite = navigateToWrite
        )
        
        DisplayAlertDialog(
            title = "Sign Out",
            message = "Are you sure that you want to Sign Out from your Google Account?",
            dialogOpened = signOutDialogOpened,
            onCloseDialog = {
                signOutDialogOpened = false
            },
            onYesClicked = {
                scope.launch(IO) {
                    runCatching {
                        App.create(APP_ID).currentUser
                    }.onSuccess { user ->
                        user?.let {
                            it.logOut()
                            withContext(Main){
                                navigateToAuth()
                            }
                        }
                    }.onFailure {
                        Log.d("HOME", "Error logging out:\n${it.message}")
                    }
                }
            }
        )
    }
    
}

fun NavGraphBuilder.writeRoute() {
    
    composable(
        route = Screen.Write.route,
        arguments = listOf(
            navArgument(
                name = WRITE_SCREEN_ARGUMENT_KEY,
            ) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) {
    
    
    }
    
}