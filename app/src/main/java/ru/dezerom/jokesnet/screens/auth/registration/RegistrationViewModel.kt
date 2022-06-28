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
        MutableLiveData<RegistrationState>(RegistrationState.CreatingCredentials("", ""))

    /**
     * Current ui state
     */
    val uiState: LiveData<RegistrationState> = _uiState

    /**
     * Listener for login text field
     */
    val loginChanged = { new: String ->
        val state = getCreatingCredentialsState()
        if (state.login != new) _uiState.value = state.copy(login = new)
    }

    /**
     * Listener for password text field
     */
    val passChanged = { new: String ->
        val state = getCreatingCredentialsState()
        if (state.pass != new) _uiState.value = state.copy(pass = new)
    }

    val signIn = {
        val state = getCreatingCredentialsState()
        _uiState.value = RegistrationState.Loading

        signInNewUser(state)
    }

    private fun getCreatingCredentialsState(): RegistrationState.CreatingCredentials {
        val state = uiState.value ?: throw IllegalStateException("Impossible exception thrown. Null uiState")

        if (state !is RegistrationState.CreatingCredentials)
            throw IllegalStateException("Got $state but expected instance of CreatingCredentials")

        return state
    }

    private fun signInNewUser(state: RegistrationState.CreatingCredentials) {
        viewModelScope.launch {
            val isSuccess = authRepo.signInNewUser(state.login, state.pass)

            if (isSuccess) _uiState.postValue(RegistrationState.Success)
            else _uiState.postValue(RegistrationState.AccountExists)
        }
    }

}