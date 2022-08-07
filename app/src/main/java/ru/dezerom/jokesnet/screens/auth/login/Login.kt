package ru.dezerom.jokesnet.screens.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.dezerom.jokesnet.R
import ru.dezerom.jokesnet.screens.FirstLevelDestinations
import ru.dezerom.jokesnet.screens.widgets.*

@Composable
fun Login(
    navController: NavController,
    viewModel: LoginViewModel
) {
    val state by viewModel.uiState.observeAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        when (state) {
            LoginScreenState.CheckingCredentials -> Loading()
            is LoginScreenState.WaitingCredentials -> CredentialInput(
                viewModel,
                state as LoginScreenState.WaitingCredentials,
                navController
            )
            LoginScreenState.Success -> SuccessAction(
                viewModel = viewModel,
                navController = navController
            )
            LoginScreenState.CheckingToken -> CheckingToken()
            is LoginScreenState.WrongCredentials -> WrongCredentialsScreen(
                viewModel,
                state as LoginScreenState.WrongCredentials
            )
            is LoginScreenState.UnknownErrorState, null -> UnknownErrorScreen(
                viewModel = viewModel,
                state = state as LoginScreenState.UnknownErrorState
            )
        }
    }
}

@Composable
private fun CredentialInput(
    viewModel: LoginViewModel,
    state: LoginScreenState.WaitingCredentials,
    navController: NavController
) {
    //login text field
    FullWidthTextField(
        value = state.email,
        onValueChange = { viewModel.obtainEvent(LoginScreenEvent.EmailChanged(it)) },
        labelText = stringResource(id = R.string.email_string)
    )
    //password text field
    FullWidthTextField(
        value = state.pass,
        onValueChange = { viewModel.obtainEvent(LoginScreenEvent.PasswordChanged(it)) },
        labelText = stringResource(id = R.string.password_string),
        visualTransformation = PasswordVisualTransformation()
    )
    FullWidthButton(
        onClick = { viewModel.obtainEvent(LoginScreenEvent.LoginBtnClicked(state)) },
        text = stringResource(id = R.string.log_in_string)
    )
    FullWidthButton(
        onClick = {
            viewModel.obtainEvent(LoginScreenEvent.RegistrationBtnClicked {
                navController.navigate(FirstLevelDestinations.REGISTRATION.route())
            })
        },
        text = stringResource(R.string.sign_in_string)
    )
}

@Composable
private fun WrongCredentialsScreen(
    viewModel: LoginViewModel,
    state: LoginScreenState.WrongCredentials
) {
    Error(
        text = stringResource(id = R.string.wrong_creds_joke),
        advice = stringResource(id = R.string.wrong_credentials_string)
    )
    FullWidthButton(
        onClick = {
            viewModel.obtainEvent(
                LoginScreenEvent.TryAgainBtnClickedWhenWrongCredentials(state.credentials)
            )
        },
        text = stringResource(R.string.try_again_string)
    )
}

@Composable
private fun CheckingToken() {
    CircularProgressIndicator(
        modifier = Modifier.fillMaxSize(0.5F)
    )
    Text(text = stringResource(R.string.checkingToken_joke))
}

@Composable
private fun UnknownErrorScreen(
    viewModel: LoginViewModel,
    state: LoginScreenState.UnknownErrorState
) {
    DoNotKnowWTFTheErrorIs()
    FullWidthButton(
        onClick = {
            viewModel.obtainEvent(
                LoginScreenEvent.TryAgainBtnClickedWhenUnknownError(state.credentials)
            )
        },
        text = stringResource(id = R.string.try_again_string)
    )
}

@Composable
private fun SuccessAction(
    viewModel: LoginViewModel,
    navController: NavController
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.obtainEvent(LoginScreenEvent.SuccessLoginEvent {
            navController.navigate(FirstLevelDestinations.NESTED_SCREENS.route())
        })
    }
}

@Preview
@Composable
private fun LoginPreview() {
    Login(navController = rememberNavController(), viewModel())
}
