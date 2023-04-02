package com.rysanek.diary.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rysanek.diary.presentation.screens.auth.AuthenticationScreen
import com.rysanek.diary.presentation.screens.auth.viewmodels.AuthenticationViewModel
import com.rysanek.diary.utils.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState

@Composable
fun SetupNavGraph(
    startDestination:
    String, navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {

        authenticationRoute()
        homeRoute()
        writeRoute()

    }

}

fun NavGraphBuilder.authenticationRoute() {

    composable(route = Screen.Authentication.route) {
        val viewModel: AuthenticationViewModel = viewModel()
        val loadingState by viewModel.loadingState
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()

        AuthenticationScreen(
            loadingState = loadingState,
            oneTapSignInState = oneTapState,
            messageBarState = messageBarState,
            onTokenIdReceived = { tokenId ->
                viewModel.signInWithMongoAtlas(
                    tokenId,
                    onSuccess = { success ->
                        if (success) {
                            messageBarState.addSuccess("Successfully Authenticated")
                            viewModel.setLoading(false)
                        }
                    },
                    onError = { e ->
                        messageBarState.addError(Exception(e.message))
                        viewModel.setLoading(false)
                    }
                )

            },
            onErrorReceived = { message ->
                messageBarState.addError(Exception(message))
            },
            onButtonClicked = {
                oneTapState.open()
                viewModel.setLoading(true)
            }
        )
    }

}

fun NavGraphBuilder.homeRoute() {

    composable(route = Screen.Home.route) {



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