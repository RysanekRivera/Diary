package com.rysanek.diary.presentation.screens.auth.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rysanek.diary.utils.Constants.APP_ID
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.GoogleAuthType
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class AuthenticationViewModel : ViewModel() {

    var loadingState = mutableStateOf(false)
        private set

    fun setLoading(loading: Boolean) {
        loadingState.value = loading
    }

    fun signInWithMongoAtlas(
        tokenId: String,
        onSuccess: (Boolean) -> Unit,
        onError: (Exception) -> Unit
    ) {

        viewModelScope.launch(IO) {
            runCatching {
                App.create(APP_ID).login(Credentials.google(tokenId, GoogleAuthType.ID_TOKEN)).loggedIn
            }.onSuccess { result ->
                onSuccess(result)
            }.onFailure { e ->
                onError(Exception(e.message))
            }

        }

    }

}