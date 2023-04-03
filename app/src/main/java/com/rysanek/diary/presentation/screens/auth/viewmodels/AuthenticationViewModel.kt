package com.rysanek.diary.presentation.screens.auth.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rysanek.diary.utils.Constants.APP_ID
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthenticationViewModel : ViewModel() {

    var authenticated = mutableStateOf(false)
        private set

    var loadingState = mutableStateOf(false)
        private set

    fun setLoading(loading: Boolean) {
        loadingState.value = loading
    }

    fun signInWithMongoAtlas(
        tokenId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {

        viewModelScope.launch(IO) {
            runCatching {
                App.create(APP_ID).login(Credentials.jwt(tokenId)).loggedIn
            }.onSuccess { success ->

                if (success) {
                    withContext(Main) {
                        onSuccess()
                        delay(800)
                        authenticated.value = true
                    }
                } else {
                    withContext(Main) {
                        onError(Exception("User is not logged in."))
                    }
                }

            }.onFailure { e ->
                withContext(Main) {
                    onError(Exception(e.message))
                }
            }

        }

    }

}