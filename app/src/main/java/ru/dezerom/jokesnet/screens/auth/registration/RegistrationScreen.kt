package ru.dezerom.jokesnet.screens.auth.registration

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.dezerom.jokesnet.R
import ru.dezerom.jokesnet.screens.FirstLevelDestinations
import ru.dezerom.jokesnet.screens.widgets.DoNotKnowWTFTheErrorIs
import ru.dezerom.jokesnet.screens.widgets.FullWidthButton
import ru.dezerom.jokesnet.screens.widgets.FullWidthTextField
import ru.dezerom.jokesnet.screens.widgets.Loading

@Composable
fun RegistrationScreen(
    navController: NavController,
    viewModel: RegistrationViewModel = viewModel()
) {
    val state by viewModel.uiState.observeAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        when (state) {
            is RegistrationState.AccountExists -> AccountExistsScreen(navController)
            is RegistrationState.CreatingCredentials -> CreatingCredentialsScreen(
                viewModel = viewModel,
                state = state as RegistrationState.CreatingCredentials,
                navController
            )
            RegistrationState.Success -> SuccessScreen(navController)
            RegistrationState.Loading -> Loading()
            is RegistrationState.UnknownError, null -> ErrorScreen(
                viewModel = viewModel,
                state = state as RegistrationState.UnknownError
            )
            is RegistrationState.MalformedEmail -> MalformedEmailNotification(
                viewModel = viewModel,
                state = state as RegistrationState.MalformedEmail
            )
            is RegistrationState.WeakPassword -> WeakPasswordNotification(
                viewModel = viewModel,
                state = state as RegistrationState.WeakPassword
            )
        }
    }
}

@Composable
fun CreatingCredentialsScreen(
    viewModel: RegistrationViewModel,
    state: RegistrationState.CreatingCredentials,
    navController: NavController
) {
    //email text filed
    FullWidthTextField(
        value = state.email,
        onValueChange = { viewModel.obtainEvent(RegistrationScreenEvent.EmailChangedEvent(it)) },
        labelText = stringResource(R.string.email_string),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
    //login text field
    FullWidthTextField(
        value = state.login,
        onValueChange = { viewModel.obtainEvent(RegistrationScreenEvent.LoginChangedEvent(it)) },
        labelText = stringResource(R.string.login_string)
    )
    //password text field
    FullWidthTextField(
        value = state.pass,
        onValueChange = { viewModel.obtainEvent(RegistrationScreenEvent.PasswordChangedEvent(it)) },
        labelText = stringResource(R.string.password_string),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
    FullWidthButton(
        onClick = { viewModel.obtainEvent(RegistrationScreenEvent.CreateNewUserEvent(state)) },
        text = stringResource(R.string.sign_in_string)
    )
    FullWidthButton(
        onClick = { navController.navigate(FirstLevelDestinations.LOGIN.route()) },
        text = stringResource(R.string.cancel_string)
    )
}

@Composable
fun SuccessScreen(navController: NavController) {
    Image(
        painter = painterResource(
            id = R.drawable.ic_baseline_check_24
        ),
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
fun AccountExistsScreen(navController: NavController) {
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

@Composable
fun ErrorScreen(viewModel: RegistrationViewModel, state: RegistrationState.UnknownError) {
    DoNotKnowWTFTheErrorIs()
    FullWidthButton(
        onClick = {
            viewModel.obtainEvent(
                RegistrationScreenEvent.TryAgainWhenUnknownErrorEvent(
                    state.credentials
                )
            )
        },
        text = stringResource(id = R.string.try_again_string)
    )
}

@Composable
private fun MalformedEmailNotification(
    viewModel: RegistrationViewModel,
    state: RegistrationState.MalformedEmail
) {
    val toast = Toast.makeText(
        LocalContext.current,
        stringResource(R.string.wrong_email_string), Toast.LENGTH_LONG
    )
    val event = RegistrationScreenEvent.MalformedEmailEvent(state.credentials) { toast.show() }
    LaunchedEffect(key1 = Unit, block = {
        viewModel.obtainEvent(event)
    })
}

@Composable
private fun WeakPasswordNotification(
    viewModel: RegistrationViewModel,
    state: RegistrationState.WeakPassword
) {
    val toast = Toast.makeText(
        LocalContext.current,
        stringResource(R.string.weak_password_string),
        Toast.LENGTH_LONG
    )
    val event = RegistrationScreenEvent.WeakPasswordEvent(state.credentials) { toast.show() }
    LaunchedEffect(key1 = Unit, block = {
        viewModel.obtainEvent(event)
    })
}
