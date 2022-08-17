package ru.dezerom.jokesnet.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.dezerom.jokesnet.R
import ru.dezerom.jokesnet.screens.FirstLevelDestinations
import ru.dezerom.jokesnet.screens.widgets.DoNotKnowWTFTheErrorIs
import ru.dezerom.jokesnet.screens.widgets.FullWidthButton
import ru.dezerom.jokesnet.screens.widgets.FullWidthTextField
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
                ShowingProfileScreen(state = state as ProfileScreenState.ShowingProfile, viewModel)
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
private fun ShowingProfileScreen(
    state: ProfileScreenState.ShowingProfile,
    viewModel: ProfileViewModel
) {
    ProfileInfoRow(
        headerText = stringResource(id = R.string.email_string) + ":",
        infoString = state.profileInfo.email
    )
    ProfileInfoRowWithEditableInfoPart(
        headerText = stringResource(id = R.string.login_string) + ":",
        infoString = state.profileInfo.login,
        onEditBtnClick = { viewModel.obtainEvent(ProfileScreenEvent.LoginChangeRequested(state)) }
    )
    ProfileInfoRow(
        headerText = stringResource(id = R.string.jokes_added_string) + ":",
        infoString = state.profileInfo.jokesAdded.toString()
    )

    LoginChangingDialog(state = state, viewModel = viewModel)
}

@Composable
private fun LoginChangingDialog(
    state: ProfileScreenState.ShowingProfile,
    viewModel: ProfileViewModel
) {
    if (state is ProfileScreenState.EditingLogin) {
        AlertDialog(
            onDismissRequest = {
                viewModel.obtainEvent(
                    ProfileScreenEvent.LoginChangeRequestCancelled(state)
                )
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Button(onClick = {
                        viewModel.obtainEvent(
                            ProfileScreenEvent.LoginChangeRequestCancelled(
                                state
                            )
                        )
                    }) {
                        Text(text = stringResource(id = R.string.cancel_string))
                    }
                    Button(onClick = {
                        viewModel.obtainEvent(
                            ProfileScreenEvent.LoginChangeRequestAccepted(
                                state
                            )
                        )
                    }) {
                        Text(text = stringResource(id = R.string.ok_string))
                    }
                }
            },
            title = { Text(text = stringResource(R.string.login_changing_title)) },
            text = {
                FullWidthTextField(
                    value = state.newLogin,
                    onValueChange = {
                        val newState = ProfileScreenState.EditingLogin(it, state.profileInfo)
                        viewModel.obtainEvent(ProfileScreenEvent.LoginChanged(newState))
                    },
                    labelText = stringResource(R.string.new_login_string)
                )
            }
        )
    }
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
private fun ProfileInfoRowWithEditableInfoPart(
    headerText: String,
    infoString: String,
    onEditBtnClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Header(text = headerText)
        EditableInfo(text = infoString, onEditBtnClick = onEditBtnClick)
    }
}

@Composable
private fun Header(text: String) {
    Text(
        text = text,
        Modifier
            .fillMaxWidth(0.35F)
            .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 8.dp),
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
private fun EditableInfo(text: String, onEditBtnClick: () -> Unit) {
    TextField(
        value = text,
        onValueChange = {},
        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 16.dp),
        trailingIcon = {
            IconButton(onClick = onEditBtnClick) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit")
            }
        },
        readOnly = true,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
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
