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
            is ProfileScreenEvent.NavigatedOut -> navigateSomewhere(event)
            ProfileScreenEvent.TriedAgain -> tryAgainBtnClicked()
            is ProfileScreenEvent.LoginChangeRequestAccepted -> obtainLoginChangingRequestAccepted(event)
            is ProfileScreenEvent.LoginChangeRequestCancelled -> obtainLoginChangeRequestCancelled(event)
            is ProfileScreenEvent.LoginChangeRequested -> obtainLoginChangeRequested(event)
            is ProfileScreenEvent.LoginChanged -> obtainLoginChanged(event)
        }
    }

    private fun obtainLoginChanged(event: ProfileScreenEvent.LoginChanged) {
        _uiState.value = event.info
    }

    private fun obtainLoginChangeRequested(event: ProfileScreenEvent.LoginChangeRequested) {
        _uiState.value = ProfileScreenState.EditingLogin(event.info.profileInfo.login,
            event.info.profileInfo)
    }

    private fun obtainLoginChangeRequestCancelled(
        event: ProfileScreenEvent.LoginChangeRequestCancelled
    ) {
        _uiState.value = ProfileScreenState.ShowingProfile(event.info.profileInfo)
    }

    private fun obtainLoginChangingRequestAccepted(
        event: ProfileScreenEvent.LoginChangeRequestAccepted
    ) {
        _uiState.value = ProfileScreenState.Loading
        viewModelScope.launch {
            val res = profileRepository.updateUserLogin(event.info.newLogin)
            if (res) getProfileInfo() else _uiState.postValue(ProfileScreenState.Error)
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

    private fun navigateSomewhere(event: ProfileScreenEvent.NavigatedOut) {
        event.obtainEvent()
        _uiState.value = ProfileScreenState.Loading
    }

    private fun tryAgainBtnClicked() {
        _uiState.value = ProfileScreenState.Loading
    }

}
