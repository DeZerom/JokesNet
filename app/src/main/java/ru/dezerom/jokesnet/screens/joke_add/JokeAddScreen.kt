package ru.dezerom.jokesnet.screens.joke_add

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.dezerom.jokesnet.R
import ru.dezerom.jokesnet.screens.widgets.FullWidthButton
import ru.dezerom.jokesnet.screens.widgets.Error as ErrorWidget
import ru.dezerom.jokesnet.screens.widgets.Loading as LoadingWidget

@Composable
fun AddJokeScreen(
    viewModel: JokeAddViewModel,
) {
    val state by viewModel.uiState.observeAsState()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        when (state) {
            is JokeAddScreenState.AddingJokeState ->
                AddingJokeScreen(state as JokeAddScreenState.AddingJokeState, viewModel)
            is JokeAddScreenState.ErrorState, null -> ErrorScreen(viewModel)
            JokeAddScreenState.LoadingState -> LoadingScreen()
        }
    }
}

@Composable
fun AddingJokeScreen(
    state: JokeAddScreenState.AddingJokeState,
    viewModel: JokeAddViewModel
) {
    TextField(
        value = state.jokeText,
        onValueChange = { viewModel.obtainEvent(JokeAddScreenEvent.JokeTextChangedEvent(it)) },
        singleLine = false,
        maxLines = 10,
        label = { Text(text = stringResource(R.string.text_of_a_joke_string)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )

    //view model will provide a text for a toast later
    val event = JokeAddScreenEvent
        .JokeAddedEvent(Toast.makeText(LocalContext.current, "", Toast.LENGTH_SHORT))
    FullWidthButton(
        onClick = { viewModel.obtainEvent(event) },
        text = stringResource(R.string.add_joke_string)
    )
}

@Composable
fun ErrorScreen(viewModel: JokeAddViewModel) {
    ErrorWidget(
        text = stringResource(R.string.joke_notAdded_string),
        advice = stringResource(R.string.joke_notAdded_advice) + " " +
                stringResource(R.string.and_string) + " ... " +
                stringResource(R.string.profileScreen_error_advise)
    )
    FullWidthButton(
        onClick = { viewModel.obtainEvent(JokeAddScreenEvent.TryAgainEvent) },
        text = stringResource(R.string.try_again_string)
    )
}

@Composable
fun LoadingScreen() {
    LoadingWidget()
}
