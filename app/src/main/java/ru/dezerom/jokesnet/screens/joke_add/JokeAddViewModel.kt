package ru.dezerom.jokesnet.screens.joke_add

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import ru.dezerom.jokesnet.R
import ru.dezerom.jokesnet.repositories.JokesRepository
import javax.inject.Inject

@HiltViewModel
class JokeAddViewModel @Inject constructor(
    private val jokesRepository: JokesRepository,
    @ApplicationContext context: Context
): ViewModel() {

    private val successToastText = context.getString(R.string.joke_added_successfully_sting)
    private val failToastText = context.getString(R.string.joke_notAdded_string)

    private val _uiState: MutableLiveData<JokeAddScreenState> = MutableLiveData(JokeAddScreenState.AddingJokeState(""))

    /**
     * Represents the current state of the screen
     */
    val uiState: LiveData<JokeAddScreenState> = _uiState

    /**
     * Obtains given event. May change [uiState]
     */
    fun obtainEvent(event: JokeAddScreenEvent) {
        when (event) {
            is JokeAddScreenEvent.JokeTextChangedEvent -> obtainJokeTextChangedEvent(event)
            is JokeAddScreenEvent.JokeAddedEvent -> obtainJokeAddedEvent(event)
            JokeAddScreenEvent.TryAgainEvent -> obtainTryAgainEvent()
        }
    }

    private fun obtainJokeTextChangedEvent(event: JokeAddScreenEvent.JokeTextChangedEvent) {
        _uiState.value = JokeAddScreenState.AddingJokeState(event.newText)
    }

    private fun obtainJokeAddedEvent(event: JokeAddScreenEvent.JokeAddedEvent) {
        val text = (uiState.value as? JokeAddScreenState.AddingJokeState)?.jokeText ?: ""
        _uiState.value = JokeAddScreenState.LoadingState
        viewModelScope.launch {
            val state = if (jokesRepository.addJoke(text)) {
                event.toast.setText(successToastText)
                JokeAddScreenState.AddingJokeState("")
            } else {
                event.toast.setText(failToastText)
                JokeAddScreenState.ErrorState(text)
            }
            _uiState.value = state
            event.obtainEvent()
        }
    }

    private fun obtainTryAgainEvent() {
        val text = (uiState.value as? JokeAddScreenState.ErrorState)?.jokeText ?: ""
        _uiState.value = JokeAddScreenState.AddingJokeState(text)
    }

}
