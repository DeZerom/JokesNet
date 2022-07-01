package ru.dezerom.jokesnet.screens.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.dezerom.jokesnet.repositories.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
): ViewModel() {

    private val _uiState = MutableLiveData<ProfileScreenState>(ProfileScreenState.Loading)
    val uiState: LiveData<ProfileScreenState> = _uiState

    /**
     * Obtains given event. May change [uiState]
     */
    fun obtainEvent(event: ProfileScreenEvent) {
        when (event) {
            ProfileScreenEvent.ProfileInfoQueried -> getProfileInfo()
            is ProfileScreenEvent.NavigateOut -> navigateSomewhere(event)
            ProfileScreenEvent.TryAgain -> tryAgainBtnClicked()
        }
    }

    private fun getProfileInfo() {
        viewModelScope.launch {
            val info = profileRepository.getProfileInfo()
            val state = info?.let { ProfileScreenState.ShowingProfile(it) }
                ?: ProfileScreenState.Error
            _uiState.postValue(state)
        }
    }

    private fun navigateSomewhere(event: ProfileScreenEvent.NavigateOut) {
        event.obtainEvent()
        _uiState.value = ProfileScreenState.Loading
    }

    private fun tryAgainBtnClicked() {
        _uiState.value = ProfileScreenState.Loading
    }

}
