package ru.dezerom.jokesnet.screens.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.dezerom.jokesnet.R
import ru.dezerom.jokesnet.screens.FirstLevelDestinations
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
fun Error(
    navController: NavController,
    viewModel: ProfileViewModel
) {
    val navToLogin = ProfileScreenEvent.NavigateOut {
        navController.navigate(FirstLevelDestinations.LOGIN.route())
    }
    Image(
        painter = painterResource(id = R.drawable.ic_baseline_add_task_24),
        contentDescription = "Check in the circle",
        modifier = Modifier
            .fillMaxSize(0.5F)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
    Text(
        text = stringResource(R.string.profileScreen_error_string),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
    Text(
        text = stringResource(R.string.profileScreen_error_advise),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
    FullWidthButton(
        onClick = { viewModel.obtainEvent(ProfileScreenEvent.TryAgain) },
        text = stringResource(id = R.string.try_again_string)
    )
    FullWidthButton(
        onClick = { viewModel.obtainEvent(navToLogin) },
        text = stringResource(R.string.log_in_string)
    )
}

@Composable
fun ShowingProfileScreen(state: ProfileScreenState.ShowingProfile) {
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
fun ProfileInfoRow(headerText: String, infoString: String) {
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
fun Header(text: String) {
    Text(
        text = text,
        Modifier
            .fillMaxWidth(0.35F)
            .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 8.dp)
    )
}

@Composable
fun Info(text: String) {
    Text(
        text = text,
        Modifier.padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 16.dp)
    )
}

@Composable
fun Loading(viewModel: ProfileViewModel) {
    LaunchedEffect(key1 = Unit) {
        val event = ProfileScreenEvent.ProfileInfoQueried
        viewModel.obtainEvent(event)
    }
    LoadingWidget()
}
