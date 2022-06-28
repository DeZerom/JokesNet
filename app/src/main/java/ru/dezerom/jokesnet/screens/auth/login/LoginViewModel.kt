package ru.dezerom.jokesnet.screens.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.dezerom.jokesnet.repositories.AuthenticationRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepo: AuthenticationRepository
): ViewModel() {

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

    /**
     * Log in btn listener
     */
    val loginBtnClicked = {
        val state = getWaitingForCredentialsState()
        _uiState.value = LoginState.CheckingCredentials

        checkCredentials(state)
    }

    /**
     * Try again btn listener
     */
    val tryAgainBtnClicked = {
        _uiState.value = LoginState.WaitingCredentials("", "")
    }

    private fun getWaitingForCredentialsState(): LoginState.WaitingCredentials {
        val state = uiState.value ?:
            throw IllegalStateException("Impossible exception thrown. Null uiState")
        if (state !is LoginState.WaitingCredentials)
            throw IllegalStateException("Got $state but expected instance of WaitingCredentials")

        return state
    }

    private fun checkCredentials(state: LoginState.WaitingCredentials) {
        viewModelScope.launch {
            val isSuccess = authRepo.checkCredentials(state.login, state.pass)

            if (isSuccess) _uiState.postValue(LoginState.WaitingCredentials("good", "boy"))
            else _uiState.postValue(LoginState.WrongCredentials)
        }
    }

}
