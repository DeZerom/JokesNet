package ru.dezerom.jokesnet.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {

    private val _uiState = MutableLiveData<LoginState>(LoginState.WaitingCredentials("", ""))

    /**
     * Current ui state
     */
    val uiState: LiveData<LoginState> = _uiState

    /**
     * Listener for login text field
     */
    val loginChanged = { new: String ->
        val state = getWaitingForCredentialsState()
        if (state.login != new) _uiState.value = state.copy(login = new)
    }

    /**
     * Listener for password text field
     */
    val passChanged = { new: String ->
        val state = getWaitingForCredentialsState()
        if (state.pass != new) _uiState.value = state.copy(pass = new)
    }

    private fun getWaitingForCredentialsState(): LoginState.WaitingCredentials {
        val state = uiState.value ?:
            throw IllegalStateException("Impossible exception thrown. Null uiState")
        if (state !is LoginState.WaitingCredentials)
            throw IllegalStateException("Got $state but expected instance of WaitingCredentials")

        return state
    }

}
