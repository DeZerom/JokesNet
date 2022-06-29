package ru.dezerom.jokesnet.screens.auth.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.dezerom.jokesnet.R
import ru.dezerom.jokesnet.screens.Screens
import ru.dezerom.jokesnet.screens.auth.Loading
import ru.dezerom.jokesnet.screens.widgets.FullWidthButton
import ru.dezerom.jokesnet.screens.widgets.FullWidthTextField

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
            LoginState.CheckingCredentials -> Loading()
            is LoginState.WaitingCredentials -> CredentialInput(
                viewModel,
                state as LoginState.WaitingCredentials,
                navController
            )
            LoginState.Success -> navController.navigate(Screens.PROFILE.route())
            LoginState.CheckingToken -> CheckingToken()
            LoginState.WrongCredentials, null -> Error(viewModel)
        }
    }
}

@Composable
fun CredentialInput(
    viewModel: LoginViewModel,
    state: LoginState.WaitingCredentials,
    navController: NavController
) {
    //login text field
    FullWidthTextField(
        value = state.login,
        onValueChange = viewModel.loginChanged,
        labelText = stringResource(id = R.string.login_string)
    )
    //password text field
    FullWidthTextField(
        value = state.pass,
        onValueChange = viewModel.passChanged,
        labelText = stringResource(id = R.string.password_string),
        visualTransformation = PasswordVisualTransformation()
    )
    FullWidthButton(
        onClick = viewModel.loginBtnClicked,
        text = stringResource(id = R.string.log_in_string)
    )
    FullWidthButton(
        onClick = { navController.navigate(Screens.REGISTRATION.route()) },
        text = stringResource(R.string.sign_in_string))
}

@Composable
fun Error(viewModel: LoginViewModel) {
    Image(
        modifier = Modifier.fillMaxSize(0.5F),
        painter = painterResource(id = R.drawable.ic_baseline_back_hand_24),
        contentDescription = "Hand"
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = stringResource(R.string.wrong_creds_joke),
    )
    Text(text = stringResource(R.string.wrong_credentials_string))
    Spacer(modifier = Modifier.height(16.dp))
    FullWidthButton(
        onClick = viewModel.tryAgainBtnClicked,
        text = stringResource(R.string.try_again_string))
}

@Composable
fun CheckingToken() {
    CircularProgressIndicator(
        modifier = Modifier.fillMaxSize(0.5F)
    )
    Text(text = stringResource(R.string.checkingToken_joke))
}

@Preview
@Composable
fun LoginPreview() {
    Login(navController = rememberNavController(), viewModel())
}
