package ru.dezerom.jokesnet.screens.login

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.dezerom.jokesnet.R

@Composable
fun Login(viewModel: LoginViewModel = viewModel()) {
    val state by viewModel.uiState.observeAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        when (state) {
            LoginState.CheckingCredentials -> Loading()
            is LoginState.WaitingCredentials -> CredentialInput(
                viewModel,
                state as LoginState.WaitingCredentials
            )
            LoginState.WrongCredentials, null -> Error(viewModel)
        }
    }
}

@Composable
fun CredentialInput(
    viewModel: LoginViewModel,
    state: LoginState.WaitingCredentials
) {
    TextField(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        value = state.login,
        onValueChange = viewModel.loginChanged,
        label = { Text(stringResource(R.string.login_string)) }
    )
    TextField(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        value = state.pass,
        onValueChange = viewModel.passChanged,
        label = { Text(text = stringResource(R.string.password_string)) }
    )
    Button(onClick = viewModel.loginBtnClicked) {
        Text(text = stringResource(R.string.log_in_string))
    }
}

@Composable
fun Error(viewModel: LoginViewModel) {
    Image(
        modifier = Modifier.fillMaxSize(0.5F),
        painter = painterResource(id = R.drawable.ic_baseline_back_hand_24),
        contentDescription = "X-like cross")
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = stringResource(R.string.wrong_creds_joke),
    )
    Text(text = stringResource(R.string.wrong_credentials_string))
    Spacer(modifier = Modifier.height(16.dp))
    Button(
        onClick = viewModel.tryAgainBtnClicked,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(text = stringResource(R.string.try_again_string))
    }
}

@Composable
fun Loading() {
    CircularProgressIndicator(
        modifier = Modifier.fillMaxSize(0.5F)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = stringResource(R.string.loading_joke),
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Preview
@Composable
fun LoginPreview() {
    Login()
}
