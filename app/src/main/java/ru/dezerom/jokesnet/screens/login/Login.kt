package ru.dezerom.jokesnet.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Login(viewModel: LoginViewModel = viewModel()) {
    val state by viewModel.uiState.observeAsState()

    when(state) {
        LoginState.CheckingCredentials -> TODO()
        is LoginState.WaitingCredentials -> CredentialInput(viewModel,
            state as LoginState.WaitingCredentials)
        LoginState.WrongCredentials -> TODO()
        null -> TODO()
    }
}

@Composable
fun CredentialInput(
    viewModel: LoginViewModel,
    state: LoginState.WaitingCredentials
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(text = "Login")
        TextField(value = state.login, onValueChange = viewModel.loginChanged)
        Text(text = "Password")
        TextField(value = state.pass, onValueChange = viewModel.passChanged)
    }
}

@Composable
fun Error() {

}

@Composable
fun Loading() {

}

@Preview
@Composable
fun LoginPreview() {
    Login()
}
