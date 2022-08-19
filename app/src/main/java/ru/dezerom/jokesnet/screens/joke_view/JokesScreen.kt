package ru.dezerom.jokesnet.screens.joke_view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.dezerom.jokesnet.R
import ru.dezerom.jokesnet.screens.widgets.FullWidthButton
import ru.dezerom.jokesnet.screens.widgets.TextWithPadding
import ru.dezerom.jokesnet.screens.widgets.Error as ErrorWidget
import ru.dezerom.jokesnet.screens.widgets.Loading as LoadingWidget

@Composable
fun JokesScreen(
    viewModel: JokeScreenViewModel
) {
    val state by viewModel.uiState.observeAsState()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        when (state) {
            JokeScreenState.NoJokeProvidedYet -> NoJokeProvidedYetScreen(viewModel = viewModel)
            is JokeScreenState.PresentingJokeState -> PresentingJoke(
                viewModel = viewModel,
                state = state as JokeScreenState.PresentingJokeState
            )
            JokeScreenState.ErrorState, null -> ErrorScreen(viewModel = viewModel)
            JokeScreenState.LoadingState -> LoadingScreen()
        }
    }
}

@Composable
private fun NoJokeProvidedYetScreen(viewModel: JokeScreenViewModel) {
    TextWithPadding(text = stringResource(id = R.string.no_joke_provided_yet_string))
    Buttons(viewModel = viewModel)
}

@Composable
private fun PresentingJoke(
    viewModel: JokeScreenViewModel,
    state: JokeScreenState.PresentingJokeState
) {
    JokeView(state)
    Buttons(viewModel)
}

@Composable
private fun JokeView(
    state: JokeScreenState.PresentingJokeState
) {
    val joke = state.joke
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = joke.creator,
            color = Color.Gray,
            fontSize = 12.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
    TextWithPadding(text = joke.text, fontSize = 18.sp)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        TextWithPadding(text = joke.views.toString(), fontSize = 12.sp)
    }
}

@Composable
private fun Buttons(viewModel: JokeScreenViewModel) {
    FullWidthButton(
        onClick = { viewModel.obtainEvent(JokeScreenEvent.RandomJokeRequested) },
        text = stringResource(R.string.random_joke_string)
    )
    FullWidthButton(
        onClick = { viewModel.obtainEvent(JokeScreenEvent.NewJokeRequested) },
        text = stringResource(R.string.something_new_string)
    )
}

@Composable
private fun ErrorScreen(viewModel: JokeScreenViewModel) {
    ErrorWidget(
        text = stringResource(R.string.joke_hasnt_been_downloaded_string),
        advice = stringResource(id = R.string.try_again_string)
    )
    Buttons(viewModel = viewModel)
}

@Composable
private fun LoadingScreen() {
    LoadingWidget()
}
