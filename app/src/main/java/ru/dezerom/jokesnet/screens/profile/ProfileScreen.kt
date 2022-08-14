package ru.dezerom.jokesnet.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.dezerom.jokesnet.R
import ru.dezerom.jokesnet.screens.FirstLevelDestinations
import ru.dezerom.jokesnet.screens.widgets.DoNotKnowWTFTheErrorIs
import ru.dezerom.jokesnet.screens.widgets.FullWidthButton
import ru.dezerom.jokesnet.screens.widgets.Loading as LoadingWidget

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel
) {
    val state by viewModel.uiState.observeAsState()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 48.dp)
    ) {
        when (state) {
            ProfileScreenState.Error, null -> Error(navController, viewModel)
            ProfileScreenState.Loading -> Loading(viewModel)
            is ProfileScreenState.ShowingProfile ->
                ShowingProfileScreen(state = state as ProfileScreenState.ShowingProfile)
        }
    }
}

@Composable
private fun Error(
    navController: NavController,
    viewModel: ProfileViewModel
) {
    val navToLogin = ProfileScreenEvent.NavigatedOut {
        navController.navigate(FirstLevelDestinations.LOGIN.route())
    }
    DoNotKnowWTFTheErrorIs()
    FullWidthButton(
        onClick = { viewModel.obtainEvent(ProfileScreenEvent.TriedAgain) },
        text = stringResource(id = R.string.try_again_string)
    )
    FullWidthButton(
        onClick = { viewModel.obtainEvent(navToLogin) },
        text = stringResource(R.string.log_in_string)
    )
}

@Composable
private fun ShowingProfileScreen(state: ProfileScreenState.ShowingProfile) {
    ProfileInfoRow(
        headerText = stringResource(id = R.string.email_string) + ":",
        infoString = state.profileInfo.email)
    ProfileInfoRow(
        headerText = stringResource(id = R.string.login_string) + ":",
        infoString = state.profileInfo.login
    )
    ProfileInfoRow(
        headerText = stringResource(id = R.string.jokes_added_string) + ":",
        infoString = state.profileInfo.jokesAdded.toString()
    )
}

@Composable
private fun ProfileInfoRow(headerText: String, infoString: String) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Header(text = headerText)
        Info(text = infoString)
    }
}

@Composable
private fun Header(text: String) {
    Text(
        text = text,
        Modifier
            .fillMaxWidth(0.35F)
            .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 8.dp)
    )
}

@Composable
private fun Info(text: String) {
    Text(
        text = text,
        Modifier.padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 16.dp)
    )
}

@Composable
private fun Loading(viewModel: ProfileViewModel) {
    LaunchedEffect(key1 = Unit) {
        val event = ProfileScreenEvent.ProfileInfoQueried
        viewModel.obtainEvent(event)
    }
    LoadingWidget()
}
