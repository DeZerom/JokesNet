package ru.dezerom.jokesnet.screens.joke_view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.dezerom.jokesnet.repositories.JokesRepository
import javax.inject.Inject

@HiltViewModel
class JokeScreenViewModel @Inject constructor(
    private val jokeRepo: JokesRepository
): ViewModel() {
    private val _uiState: MutableLiveData<JokeScreenState> =
        MutableLiveData(JokeScreenState.NoJokeProvidedYet)

    /**
     * Current ui state
     */
    val uiState: LiveData<JokeScreenState> = _uiState

    /**
     * Obtains the given [event]. May change [uiState]
     */
    fun obtainEvent(event: JokeScreenEvent) {
        when (event) {
            JokeScreenEvent.NewJokeRequested -> obtainJokeRequestedEvent(1)
            JokeScreenEvent.RandomJokeRequested -> obtainJokeRequestedEvent(2)
        }
    }

    /**
     * @param type 1 for new joke, 2 for random joke
     */
    private fun obtainJokeRequestedEvent(type: Int) {
        _uiState.value = JokeScreenState.LoadingState
        viewModelScope.launch {
            val joke = if (type == 1) jokeRepo.getNewJoke() else jokeRepo.getRandomJoke()
            val newState = joke?.let { JokeScreenState.PresentingJokeState(it) }
                ?: run { JokeScreenState.ErrorState }
            _uiState.postValue(newState)
        }
    }
}