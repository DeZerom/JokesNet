package ru.dezerom.jokesnet.screens.auth.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.dezerom.jokesnet.repositories.AuthenticationRepository
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authRepo: AuthenticationRepository
): ViewModel() {

    private val _uiState =
        MutableLiveData<RegistrationState>(RegistrationState.CreatingCredentials("", "", ""))

    /**
     * Current ui state
     */
    val uiState: LiveData<RegistrationState> = _uiState

    /**
     * Obtains the given [event]. May change [uiState] value
     */
    fun obtainEvent(event: RegistrationScreenEvent) {
        when(event) {
            is RegistrationScreenEvent.CreateNewUserEvent -> obtainCreateNewUserEvent(event)
            is RegistrationScreenEvent.TryAgainWhenUnknownErrorEvent -> obtainTryAgainWhenUnknownErrorEvent(event)
            is RegistrationScreenEvent.MalformedEmailEvent -> obtainMalformedEmailEvent(event)
            is RegistrationScreenEvent.WeakPasswordEvent -> obtainWeakPasswordEvent(event)
            is RegistrationScreenEvent.EmailChangedEvent -> obtainEmailChangedEvent(event)
            is RegistrationScreenEvent.LoginChangedEvent -> obtainLoginChangedEvent(event)
            is RegistrationScreenEvent.PasswordChangedEvent -> obtainPasswordChangedEvent(event)
        }
    }

    private fun obtainCreateNewUserEvent(event: RegistrationScreenEvent.CreateNewUserEvent) {
        _uiState.value = RegistrationState.Loading
        val credentials = event.credentials
        viewModelScope.launch {
            val regStatus = authRepo.createNewUser(credentials.email, credentials.pass)

            val newState = if (regStatus.isSuccessful) RegistrationState.Success
            else {
                with(AuthenticationRepository.RegistrationStatus) {
                    val errorState = when(regStatus.reason) {
                        ACCOUNT_EXISTS -> RegistrationState.AccountExists
                        WEAK_PASSWORD -> RegistrationState.WeakPassword(credentials)
                        MALFORMED_EMAIL -> RegistrationState.MalformedEmail(credentials)
                        else -> RegistrationState.UnknownError(credentials)
                    }
                    errorState
                }
            }
            _uiState.postValue(newState)
        }
    }

    private fun obtainTryAgainWhenUnknownErrorEvent(event: RegistrationScreenEvent.TryAgainWhenUnknownErrorEvent) {
        _uiState.value = event.credentials
    }

    private fun obtainMalformedEmailEvent(event: RegistrationScreenEvent.MalformedEmailEvent) {
        _uiState.value = event.credentials
        event.obtainEvent()
    }

    private fun obtainWeakPasswordEvent(event: RegistrationScreenEvent.WeakPasswordEvent) {
        _uiState.value = event.credentials
        event.obtainEvent()
    }

    private fun obtainEmailChangedEvent(event: RegistrationScreenEvent.EmailChangedEvent) {
        val state = getCreatingCredentialsState()
        _uiState.value = state.copy(email = event.newEmail)
    }

    private fun obtainLoginChangedEvent(event: RegistrationScreenEvent.LoginChangedEvent) {
        val state = getCreatingCredentialsState()
        _uiState.value = state.copy(login = event.newLogin)
    }

    private fun obtainPasswordChangedEvent(event: RegistrationScreenEvent.PasswordChangedEvent) {
        val state = getCreatingCredentialsState()
        _uiState.value = state.copy(pass = event.newPassword)
    }

    private fun getCreatingCredentialsState(): RegistrationState.CreatingCredentials {
        return  uiState.value as? RegistrationState.CreatingCredentials
                ?: RegistrationState.CreatingCredentials("", "", "")
    }

}