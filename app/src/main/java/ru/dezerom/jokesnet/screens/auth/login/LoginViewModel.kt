package ru.dezerom.jokesnet.screens.auth.login

import android.util.Log
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
) : ViewModel() {

    private val _uiState = MutableLiveData<LoginScreenState>(LoginScreenState.CheckingToken)

    /**
     * Current ui state
     */
    val uiState: LiveData<LoginScreenState> = _uiState

    init {
        val state = if (authRepo.isLoggedIn) LoginScreenState.Success
            else LoginScreenState.WaitingCredentials("", "")
        _uiState.value = state
    }

    /**
     * Obtains given [event]. May change [uiState] value.
     */
    fun obtainEvent(event: LoginScreenEvent) {
        when (event) {
            is LoginScreenEvent.EmailChanged -> obtainEmailChangedEvent(event)
            is LoginScreenEvent.PasswordChanged -> obtainPasswordChangedEvent(event)
            is LoginScreenEvent.RegistrationBtnClicked -> obtainRegistrationBtnClickedEvent(event)
            is LoginScreenEvent.LoginBtnClicked -> obtainLoginBtnClicked(event)
            is LoginScreenEvent.SuccessLoginEvent -> obtainSuccessLoginEvent(event)
            is LoginScreenEvent.TryAgainBtnClickedWhenWrongCredentials ->
                obtainTryAgainButtonsClick(event.credentials)
            is LoginScreenEvent.TryAgainBtnClickedWhenUnknownError ->
                obtainTryAgainButtonsClick(event.credentials)
        }
    }

    private fun obtainTryAgainButtonsClick(credentials: LoginScreenState.WaitingCredentials) {
        _uiState.value = LoginScreenState.WaitingCredentials(credentials.email, credentials.pass)
    }

    private fun obtainSuccessLoginEvent(event: LoginScreenEvent.SuccessLoginEvent) {
        event.obtainEvent()
    }

    private fun obtainLoginBtnClicked(event: LoginScreenEvent.LoginBtnClicked) {
        val credentials = event.credentials
        _uiState.value = LoginScreenState.CheckingCredentials
        viewModelScope.launch {
            val res = authRepo.performLogin(credentials.email, credentials.pass)
            Log.e("LoginViewModel", "${res.isSuccessful} ${res.reason}")
            val state = if (res.isSuccessful) {
                LoginScreenState.Success
            } else {
                with(AuthenticationRepository.SignInStatus) {
                    when (res.reason) {
                        WRONG_PASSWORD, ACCOUNT_DOES_NOT_EXISTS ->
                            LoginScreenState.WrongCredentials(credentials)
                        else -> LoginScreenState.UnknownErrorState(credentials)
                    }
                }
            }
            Log.e("LoginViewModel", "$state")
            _uiState.postValue(state)
        }
    }

    private fun obtainRegistrationBtnClickedEvent(event: LoginScreenEvent.RegistrationBtnClicked) {
        event.obtainEvent()
    }

    private fun obtainPasswordChangedEvent(event: LoginScreenEvent.PasswordChanged) {
        val state = getWaitingCredentialsState()
        _uiState.value = state.copy(pass = event.newPassword)
    }

    private fun obtainEmailChangedEvent(event: LoginScreenEvent.EmailChanged) {
        val state = getWaitingCredentialsState()
        _uiState.value = state.copy(email = event.newEmail)
    }

    private fun getWaitingCredentialsState(): LoginScreenState.WaitingCredentials {
        return uiState.value as? LoginScreenState.WaitingCredentials
            ?: LoginScreenState.WaitingCredentials("", "")
    }

}
