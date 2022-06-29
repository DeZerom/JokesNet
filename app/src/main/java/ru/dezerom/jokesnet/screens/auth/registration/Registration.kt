package ru.dezerom.jokesnet.screens.auth.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.dezerom.jokesnet.R
import ru.dezerom.jokesnet.screens.FirstLevelDestinations
import ru.dezerom.jokesnet.screens.auth.Loading
import ru.dezerom.jokesnet.screens.widgets.FullWidthButton
import ru.dezerom.jokesnet.screens.widgets.FullWidthTextField

@Composable
fun Registration(
    navController: NavController,
    viewModel: RegistrationViewModel = viewModel()
) {
    val state by viewModel.uiState.observeAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        when(state) {
            is RegistrationState.AccountExists -> AccountExists(navController)
            is RegistrationState.CreatingCredentials -> CreatingCredentials(
                viewModel = viewModel,
                state = state as RegistrationState.CreatingCredentials,
                navController
            )
            RegistrationState.Success -> Success(navController)
            RegistrationState.Loading -> Loading()
            null -> TODO()
        }
    }
}

@Composable
fun CreatingCredentials(
    viewModel: RegistrationViewModel,
    state: RegistrationState.CreatingCredentials,
    navController: NavController
) {
    //login text field
    FullWidthTextField(
        value = state.login,
        onValueChange = viewModel.loginChanged,
        labelText = stringResource(R.string.login_string))
    //password text field
    FullWidthTextField(
        value = state.pass,
        onValueChange = viewModel.passChanged,
        labelText = stringResource(R.string.password_string),
        visualTransformation = PasswordVisualTransformation()
    )
    FullWidthButton(onClick = viewModel.signIn, text = stringResource(R.string.sign_in_string))
    FullWidthButton(
        onClick = { navController.navigate(FirstLevelDestinations.LOGIN.route()) },
        text = stringResource(R.string.cancel_string)
    )
}

@Composable
fun Success(navController: NavController) {
    Image(painter = painterResource(
        id = R.drawable.ic_baseline_check_24),
        contentDescription = "Check",
        modifier = Modifier.fillMaxSize(0.5F)
    )
    Text(text = stringResource(R.string.successfull_signingIn_string))
    Spacer(modifier = Modifier.height(16.dp))
    FullWidthButton(
        onClick = { navController.navigate(FirstLevelDestinations.LOGIN.route()) },
        text = stringResource(R.string.log_in_string)
    )
}

@Composable
fun AccountExists(navController: NavController) {
    Image(
        painter = painterResource(id = R.drawable.ic_baseline_back_hand_24),
        contentDescription = "Hand",
        modifier = Modifier.fillMaxSize(0.5F)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = stringResource(R.string.account_exists_string))
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = stringResource(R.string.have_to_login))
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = stringResource(R.string.dude_string))
    Spacer(modifier = Modifier.height(16.dp))
    Button(onClick = { navController.navigate(FirstLevelDestinations.LOGIN.route()) }) {
        Text(text = stringResource(R.string.log_in_string))
    }
}
